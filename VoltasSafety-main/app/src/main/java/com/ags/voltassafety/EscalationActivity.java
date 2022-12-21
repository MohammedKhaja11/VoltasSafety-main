package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ags.voltassafety.adapters.IncidentTypeAdapter;
import com.ags.voltassafety.adapters.ReportsBranchAdapter;
import com.ags.voltassafety.adapters.ReportsUserAdapter;
import com.ags.voltassafety.model.BranchLevelUserResult;
import com.ags.voltassafety.model.BranchObservation;
import com.ags.voltassafety.model.EscalationModel;
import com.ags.voltassafety.model.EscalationResponse;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchResponse;
import com.ags.voltassafety.model.IncidentTypeModel;
import com.ags.voltassafety.model.ObservationReportInput;
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

public class EscalationActivity extends BaseActivity {
    EditText edit_from_date, edit_to_date, edit_branch, edit_user    /*, edit_incident_type*/;
    private Spinner spinner_incident_type;
    private int year;
    private int month;
    private int day;
    ReportsBranchAdapter ReportsBranchAdapter;
    IncidentTypeAdapter incidentTypeAdapter;
    ReportsUserAdapter ReportsUserAdapter;
    List<UserBranches> branchesList;
    List<IncidentTypeModel> incidentlist;
    List<BranchLevelUserResult> userslist = new ArrayList<>();

    List<BranchObservation> hazardBranchResultList;
    public static CheckBox chk_select_all;
    ArrayList<String> selectedBranchList = new ArrayList<>();
    ArrayList<String> selectedUserList = new ArrayList<>();
    ProgressDialog progressDialog;
    Button get_report;
    ObservationReportInput observationReportInput;
    //private AlertDialog.Builder mAlertDialog;
    ImageView back;
    private TextView tv_title;
    UserBranches userBranches;

    EscalationModel escalationModel;
    EscalationResponse escalationResponse;
    IncidentTypeModel incidentTypeModel;
    String type_spinner;
    String[] type = {"Select Type", "Hazard", "Near Miss", "Accident"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalation);

        edit_from_date = findViewById(R.id.edit_from_date);
        edit_to_date = findViewById(R.id.edit_to_date);
        edit_branch = findViewById(R.id.edit_branch);
        spinner_incident_type = findViewById(R.id.spinner_incident_type);
        edit_user = findViewById(R.id.edit_user);
        get_report = findViewById(R.id.get_report);
        get_report.setTypeface(Utilities.fontBold(EscalationActivity.this));
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

        ArrayAdapter arrayAdapter = new ArrayAdapter(EscalationActivity.this, android.R.layout.simple_list_item_1, type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_incident_type.setAdapter(arrayAdapter);

        spinner_incident_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Type")) {
                    type_spinner = "";
                } else {
                    type_spinner = adapterView.getSelectedItem().toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        incidentlist = new ArrayList<>();

        incidentTypeModel = new IncidentTypeModel();
        incidentTypeModel.setType("Accident");
        incidentlist.add(incidentTypeModel);
        incidentTypeModel = new IncidentTypeModel();
        incidentTypeModel.setType("Near Miss");
        incidentlist.add(incidentTypeModel);

        edit_from_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edit_to_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
//        edit_branch.setTypeface(Utilities.fontRegular(getApplicationContext()));

        if (Utilities.isConnectingToInternet(EscalationActivity.this)) {
            //getUserBranches();

        } else {
            showAlert("Please Check Your Internet Connection");
        }

        get_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type_spinner != null && type_spinner.equalsIgnoreCase("")) {
                    showAlert("Please Select Type");
                } else {
                    getEscalationReport();
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EscalationActivity.this, R.style.MyDialogTheme,
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EscalationActivity.this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {

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
                final Dialog branchDialog = new Dialog(EscalationActivity.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EscalationActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);

//                ReportsBranchAdapter = new ReportsBranchAdapter(EscalationActivity.this, branchesList);
//                recyclerView.setAdapter(ReportsBranchAdapter);

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
                                selectedBranchList.add(branchesList.get(i).getBranchName());

                                Log.d("selected branches:", "" + branchesList.get(i).getBranchName());

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
                        // edit_user.setText("All");
                        //getBranchLevelUser(selectedBranchList);
                    }
                });

//                chk_select_all.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (chk_select_all.isChecked()) {
//                            for (UserBranches userBranches : branchesList) {
//
//                                userBranches.setSelected(true);
//                            }
//
//                        } else {
//                            for (UserBranches userBranches : branchesList) {
//
//                                userBranches.setSelected(false);
//                            }
//                        }
//                        ReportsBranchAdapter.notifyDataSetChanged();
//                    }
//                });
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

     /*   edit_incident_type.setText("All");
        edit_incident_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(EscalationActivity.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EscalationActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                for (int i = 0; i < incidentlist.size(); i++) {
                    incidentTypeAdapter = new IncidentTypeAdapter(EscalationActivity.this, incidentlist);
                    recyclerView.setAdapter(incidentTypeAdapter);
                }


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
                        for (int i = 0; i < incidentlist.size(); i++) {
                            if (incidentlist.get(i).isSelected()) {
                                selectedBranchList.add(incidentlist.get(i).getType());

                                Log.d("selected branches:", "" + incidentlist.get(i).getType());

                                for (int j = 0; j < selectedBranchList.size(); j++) {
                                    if (j != selectedBranchList.size() - 1) {
                                        stringBuilder.append(",");
                                        break;
                                    }
                                }
                                stringBuilder.append(incidentlist.get(i).getType());
                            }
                        }

                        if (stringBuilder.length() > 0) {
                            edit_incident_type.setText(stringBuilder);

                        } else if (stringBuilder.length() == 2) {
                            edit_incident_type.setText("All");
                        }else{
                            edit_incident_type.setText("All");
                        }
                        branchDialog.dismiss();
                        // edit_user.setText("All");
                        // getBranchLevelUser(selectedBranchList);
                    }
                });

                chk_select_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chk_select_all.isChecked()) {
                            for (IncidentTypeModel userBranches : incidentlist) {

                                userBranches.setSelected(true);
                            }

                        } else {
                            for (IncidentTypeModel userBranches : incidentlist) {

                                userBranches.setSelected(false);
                            }
                        }
                        incidentTypeAdapter.notifyDataSetChanged();
                    }
                });
                editTextFilter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        incidentTypeAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });*/

        edit_user.setText("All");
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(EscalationActivity.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EscalationActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);

//                ReportsUserAdapter = new ReportsUserAdapter(EscalationActivity.this, userslist);
//                recyclerView.setAdapter(ReportsUserAdapter);

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
                        selectedUserList.clear();
                        //  selectedBranchList.add("ORGN000001");
                        for (int i = 0; i < userslist.size(); i++) {
                            if (userslist.get(i).isSelected()) {
                                selectedUserList.add(userslist.get(i).getUserId());

                                Log.d("selected branches:", "" + userslist.get(i).getUserId());

                                for (int j = 0; j < selectedUserList.size(); j++) {
                                    if (j != selectedUserList.size() - 1) {
                                        stringBuilder.append(",");
                                        break;
                                    }
                                }
                                stringBuilder.append(userslist.get(i).getUserName());
                            }
                        }

                        if (stringBuilder.length() > 0) {
                            edit_user.setText(stringBuilder);

                        } else {
                            edit_user.setText("All");
                        }
                        branchDialog.dismiss();
                    }
                });

//                chk_select_all.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (chk_select_all.isChecked()) {
//                            for (BranchLevelUserResult userBranches : userslist) {
//
//                                userBranches.setSelected(true);
//                            }
//
//                        } else {
//                            for (BranchLevelUserResult userBranches : userslist) {
//
//                                userBranches.setSelected(false);
//                            }
//                        }
//                        ReportsUserAdapter.notifyDataSetChanged();
//                    }
//                });
                editTextFilter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        ReportsUserAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

    }

    private void getEscalationReport() {
        setObservationInput();

        try {
            //  escalationResponse = new ArrayList<>();

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<EscalationResponse> call = apiInterface.observationescalationResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), escalationModel);

            Gson gson = new Gson();
            String json = gson.toJson(escalationModel);

            Log.d("Reportrequest", json);


            progressDialog = new ProgressDialog(EscalationActivity.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<EscalationResponse>() {
                public void onResponse(Call<EscalationResponse> call, Response<EscalationResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                progressDialog.dismiss();
                                escalationResponse = response.body();
                                for (int i = 0; i < response.body().getResult().size(); i++) {
                                    Log.d("GetAssigned", response.body().getResult().get(i).getAssigned() + "");


                                }

                                Intent observationreportview = new Intent(EscalationActivity.this, ExpandableEscalationReportList.class);
                                Bundle bd = new Bundle();
                                bd.putSerializable("reportsResult", (Serializable) response.body().getResult());
                                bd.putSerializable("reportResponse", (Serializable) escalationResponse);
                                bd.putSerializable("reportRequest", (Serializable) escalationModel);
                                observationreportview.putExtras(bd);
                                startActivity(observationreportview);

//

                            } else {
                                progressDialog.dismiss();
                                showAlert("No Data");
                            }
                        } else {
                            progressDialog.dismiss();
                            showAlert(response.body().getErrors().toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<EscalationResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }

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
        // observationReportInput = new ObservationReportInput();
        escalationModel = new EscalationModel();
        escalationModel.setFrom(String.valueOf(output.format(data) + "00:00:00"));

        //  observationReportInput.setBranchs(selectedBranchList);


        //  observationReportInput.setZones(new ArrayList<>());

        try {
            data = sdf.parse(edit_to_date.getText().toString());
            escalationModel.setTo(output.format(data) + "00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        escalationModel.setType(type_spinner);

    }


}
