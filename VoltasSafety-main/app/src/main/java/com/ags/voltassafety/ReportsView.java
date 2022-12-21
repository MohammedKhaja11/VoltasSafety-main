package com.ags.voltassafety;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.voltassafety.adapters.ReportsBranchAdapter;
import com.ags.voltassafety.model.ObservationReportInput;
import com.ags.voltassafety.model.ObservationReportResponse;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.model.UserBranches;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsView extends BaseActivity {

    EditText edit_from_date, edit_to_date, edit_branch;
    private int year;
    private int month;
    private int day;
    ReportsBranchAdapter ReportsBranchAdapter;
    List<UserBranches> branchesList;
    public static CheckBox chk_select_all;
    ArrayList<String> selectedBranchList = new ArrayList<>();
    ProgressDialog progressDialog;
    Button get_report;
    ObservationReportInput observationReportInput;
    //    private AlertDialog.Builder mAlertDialog;
    ImageView back;
    private TextView tv_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_view);

        edit_from_date = findViewById(R.id.edit_from_date);
        edit_to_date = findViewById(R.id.edit_to_date);
        edit_branch = findViewById(R.id.edit_branch);
        //edit_zone = findViewById(R.id.edit_zone);
        get_report = findViewById(R.id.get_report);
        back = findViewById(R.id.back);
        tv_title = findViewById(R.id.toolbar_title);
        String strReportName = getIntent().getExtras().getString("ReportName");
        tv_title.setText(strReportName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_from_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edit_to_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
//        edit_branch.setTypeface(Utilities.fontRegular(getApplicationContext()));

        if (Utilities.isConnectingToInternet(ReportsView.this)) {
            getUserBranches();

        } else {
            showAlert("Please Check Your Internet Connection");
        }

        get_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strReportName.equalsIgnoreCase("Observation Report")) {
                    setObservationInput();
                    Intent observationreportview = new Intent(ReportsView.this, ObservationReportView.class);
                    Bundle bd = new Bundle();
                    bd.putSerializable("reportsInput", (Serializable) observationReportInput);
                    observationreportview.putExtras(bd);
                    startActivity(observationreportview);
                }
            }
        });


//        Calendar calReturn = Calendar.getInstance();
        Calendar calReturn = Calendar.getInstance(TimeZone.getDefault());
        calReturn.add(Calendar.DATE, -30);

        year = calReturn.get(Calendar.YEAR);
        month = calReturn.get(Calendar.MONTH);
        month = month + 1;
        day = calReturn.get(Calendar.DAY_OF_MONTH);

        edit_from_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(calReturn.getTime()));
//        edit_from_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        edit_to_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));


        edit_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String selToDate = edit_to_date.getText().toString().trim();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ReportsView.this, R.style.MyDialogTheme,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    String strSelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    try {
                                        strSelectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse(strSelectedDate));
                                    } catch (ParseException p) {
                                        p.getMessage();
                                    }
                                    Date dateFrom = Utilities.convertStringToDate(strSelectedDate);
                                    Date dateTo = Utilities.convertStringToDate(selToDate);
                                    if (dateFrom.after(dateTo)) {
                                        edit_from_date.setText(selToDate);
                                    } else {
                                        edit_from_date.setText(strSelectedDate);
                                    }
                                }
                            }, year, month, day);
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        edit_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String selFromDate = edit_from_date.getText().toString().trim();
                    final Date dateFrom = Utilities.convertStringToDate(selFromDate);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ReportsView.this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            String strSelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            try {
                                strSelectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse(strSelectedDate));
                            } catch (ParseException p) {
                                p.getMessage();
                            }
                            Date dateTo = Utilities.convertStringToDate(strSelectedDate);
                            if (dateTo.before(dateFrom)) {
                                edit_to_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                            } else {
                                edit_to_date.setText(strSelectedDate);
                            }
                        }
                    }, year, month, day);
                    datePickerDialog.getDatePicker().setMinDate(dateFrom.getTime());
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


        edit_branch.setText("All");
        edit_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(ReportsView.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReportsView.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                ReportsBranchAdapter = new ReportsBranchAdapter(ReportsView.this, branchesList);
                recyclerView.setAdapter(ReportsBranchAdapter);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        branchDialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder stringBuilder = new StringBuilder();
                        selectedBranchList.clear();
                        //  selectedBranchList.add("ORGN000001");
                        for (int i = 0; i < branchesList.size(); i++) {
                            if (branchesList.get(i).isSelected()) {
                                selectedBranchList.add(branchesList.get(i).getBranchId());

                                Log.d("selected branches:", "" + branchesList.get(i).getBranchId());

                                for (int j = 0; j < selectedBranchList.size(); j++) {
                                    if (j != selectedBranchList.size() - 1) {
                                        stringBuilder.append(",");
                                        break;
                                    }
                                }
                                stringBuilder.append(branchesList.get(i).getBranchName());
                            }
                        }

                        if (stringBuilder.length() > 0) {
                            edit_branch.setText(stringBuilder);
                        } else {
                            edit_branch.setText("All");
                        }
                        branchDialog.dismiss();
                    }
                });

                chk_select_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chk_select_all.isChecked()) {
                            for (UserBranches userBranches : branchesList) {

                                userBranches.setSelected(true);
                            }

                        } else {
                            for (UserBranches userBranches : branchesList) {

                                userBranches.setSelected(false);
                            }
                        }
                        ReportsBranchAdapter.notifyDataSetChanged();
                    }
                });
                editTextFilter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        ReportsBranchAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

    }

    private void setObservationInput() {

        Log.d("strFromDate", edit_from_date.getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'");

        Date data = null;
        try {
            data = sdf.parse(edit_from_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("strFromDate", output.format(data) + "00:00:00");
        observationReportInput = new ObservationReportInput();
        observationReportInput.setStartDate(String.valueOf(output.format(data) + "00:00:00"));
        observationReportInput.setBranchs(selectedBranchList);
        observationReportInput.setZones(new ArrayList<String>());

        try {
            data = sdf.parse(edit_to_date.getText().toString());
            observationReportInput.setEndDate(output.format(data) + "00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        observationReportInput.setCriteria("");
    }

    private void getUserBranches() {
        try {
            branchesList = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserBranchResponse> call = apiInterface.getUserBranchResponse("Bearer " + sharedPreferences.getString("Bearertoken", null));
            progressDialog = new ProgressDialog(ReportsView.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<UserBranchResponse>() {
                public void onResponse(Call<UserBranchResponse> call, Response<UserBranchResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                progressDialog.dismiss();
                                UserBranchResponse loginResponse = response.body();
                                //Log.d("branchresponse", response.body().getResult() + "");
                                branchesList.addAll(loginResponse.getResult());

                            } else {
                                progressDialog.dismiss();
                                showAlert("No Data");
                            }
                        } else {
                            progressDialog.dismiss();
                            showAlert(response.body().getErrors()[0]);
                        }
                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<UserBranchResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }
    }
}
