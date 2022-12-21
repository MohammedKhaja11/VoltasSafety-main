package com.ags.voltassafety;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.ReportsAdapter;
import com.ags.voltassafety.adapters.ReportsBranchAdapter;
import com.ags.voltassafety.adapters.ReportsUserAdapter;
import com.ags.voltassafety.model.BranchLevelUserResult;
import com.ags.voltassafety.model.BranchObservation;
import com.ags.voltassafety.model.BranchObservationstatus;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchObservationStatusResponse;
import com.ags.voltassafety.model.HazardBranchResponse;
import com.ags.voltassafety.model.ObservationReportDetailsInput;
import com.ags.voltassafety.model.ObservationReportDetailsResponse;
import com.ags.voltassafety.model.ObservationReportDetailsResult;
import com.ags.voltassafety.model.ObservationReportInput;
import com.ags.voltassafety.model.ObservationReportResponse;
import com.ags.voltassafety.model.ObservationReportSummaryResponse;
import com.ags.voltassafety.model.ObservationReportSummaryResult;
import com.ags.voltassafety.model.ObservationReportSummaryStatus;
import com.ags.voltassafety.model.ObservationStatusReportResult;
import com.ags.voltassafety.model.Observationsstatus;
import com.ags.voltassafety.model.Result;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.model.UserBranches;
import com.ags.voltassafety.model.UserZoneResponse;
import com.ags.voltassafety.model.UserZones;
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

public class HazardReport extends BaseActivity {

    EditText edit_from_date, edit_to_date, edit_branch, editZone;
    private int year;
    private int month;
    private int day;
    ReportsBranchAdapter ReportsBranchAdapter;
    ReportsUserAdapter ReportsUserAdapter;

//    List<UserZones> userslist = new ArrayList<>();

    List<BranchObservation> hazardBranchResultList;
    public static CheckBox chk_report;
    ArrayList<String> selectedBranchList = new ArrayList<>();
    ArrayList<String> selectedUserList = new ArrayList<>();
    ProgressDialog progressDialog;
    Button get_report;
    ObservationReportInput observationReportInput;
    // private AlertDialog.Builder mAlertDialog;
    ImageView back;
    private TextView tv_title;
    UserBranches userBranches;

    HazardBranchModel hazardBranchModelrequest;
    UserBranchResponse hazardBranchResponse;


    List<UserZones> zonesList;
    List<UserBranches> branchesList;
    StringBuilder stringBuilderZone;
    StringBuilder stringBuilderZonename;
    StringBuilder stringBuilderbranchId, stringBuilderbranch;
    ObservationReportDetailsInput observationReportDetailsInput;
    ObservationReportDetailsResponse observationReportDetailsResponse;
    List<ObservationReportDetailsResult> resultList;
    List<Observationsstatus> piceChartResultList;

    String[] destZoneArray, destBranchArray;

    ObservationStatusReportResult observationReportDetailsResult;
    ArrayList<Observationsstatus> branchObservationstatuses;
    Result observationStatusReportResult;
    String strReportName;
    private TextView tv_from_date, tv_to_date, tvZone, tvBranch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_report);
        edit_from_date = findViewById(R.id.edit_from_date);
        edit_to_date = findViewById(R.id.edit_to_date);
        edit_branch = findViewById(R.id.edit_branch);
        editZone = findViewById(R.id.editZone);
        get_report = findViewById(R.id.get_report);
        back = findViewById(R.id.back);
        tv_title = findViewById(R.id.toolbar_title);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        tvZone = findViewById(R.id.tvZone);
        tvBranch = findViewById(R.id.tvBranch);
//        HorizontalScrollView s =
//                (HorizontalScrollView) findViewById(R.id.scroller);

        strReportName = getIntent().getExtras().getString("ReportName");
        tv_title.setText(strReportName);
        tv_title.setTypeface(Utilities.fontBold(HazardReport.this));
        get_report.setTypeface(Utilities.fontRegular(HazardReport.this));
        edit_from_date.setTypeface(Utilities.fontRegular(HazardReport.this));
        tv_from_date.setTypeface(Utilities.fontRegular(HazardReport.this));
        tv_to_date.setTypeface(Utilities.fontRegular(HazardReport.this));
        tvZone.setTypeface(Utilities.fontRegular(HazardReport.this));
        tvBranch.setTypeface(Utilities.fontRegular(HazardReport.this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_from_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edit_to_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
//  edit_branch.setTypeface(Utilities.fontRegular(getApplicationContext()));
        branchesList = new ArrayList<>();
        editZone.setText("All");
        editZone.setTypeface(Utilities.fontRegular(HazardReport.this));
        edit_branch.setTypeface(Utilities.fontRegular(HazardReport.this));

        if (Utilities.isConnectingToInternet(HazardReport.this)) {
           // getHazardBranches();
            getZones();


        } else {
            showAlert("Please Check Your Internet Connection");
        }

        get_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getHazardBranches();
//                getObservationsReportSummary();
//                getObservationReportDetails();
                getPieChartData();

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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(HazardReport.this, R.style.MyDialogTheme,
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(HazardReport.this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {

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

        editZone.setText("All");

        editZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(HazardReport.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_report = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                chk_report.setTypeface(Utilities.fontBold(HazardReport.this));
                cancel.setTypeface(Utilities.fontBold(HazardReport.this));
                done.setTypeface(Utilities.fontBold(HazardReport.this));
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HazardReport.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                ReportsUserAdapter = new ReportsUserAdapter(HazardReport.this, zonesList);
                recyclerView.setAdapter(ReportsUserAdapter);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        branchDialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        stringBuilderZone = new StringBuilder();
                        stringBuilderZonename = new StringBuilder();
                        selectedUserList.clear();
                        //  selectedBranchList.add("ORGN000001");
                        for (int i = 0; i < zonesList.size(); i++) {
                            if (zonesList.get(i).isSelected()) {
                                selectedUserList.add(zonesList.get(i).getZoneId());

                                Log.d("selectedzones:", "" + zonesList.get(i).getZoneId());

                                for (int j = 0; j < selectedUserList.size(); j++) {
                                    if (j != selectedUserList.size() - 1) {
                                        stringBuilderZone.append(",");
                                        stringBuilderZonename.append(",");
                                        break;
                                    }
                                }
                                stringBuilderZone.append(zonesList.get(i).getZoneId());
                                stringBuilderZonename.append(zonesList.get(i).getZoneName());

                            }
                        }

                        if (stringBuilderZone.length() > 0) {
                            editZone.setText(stringBuilderZonename);


                        } else {
                            editZone.setText("All");
                        }
                        getHazardBranches();
                        branchDialog.dismiss();
                    }
                });

                chk_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        int tag = (int) v.getTag();
//                        Log.d("Tag", tag + "");
                        if (chk_report.isChecked()) {
                            for (UserZones userBranches : zonesList) {

                                userBranches.setSelected(true);
                            }

                        } else {
                            for (UserZones userBranches : zonesList) {

                                userBranches.setSelected(false);
//                                chk_select_all.setChecked(false);
                            }
                        }
                        ReportsUserAdapter.notifyDataSetChanged();
                    }
                });
             /*   recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                        return false;
                    }

                    @Override
                    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });*/
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


        edit_branch.setText("All");
       /* if (editZone.getText().toString().equalsIgnoreCase("All")) {
            getHazardBranches();
        }*/
        edit_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getHazardBranches();
                final Dialog branchDialog = new Dialog(HazardReport.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_report = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                chk_report.setTypeface(Utilities.fontBold(HazardReport.this));
                cancel.setTypeface(Utilities.fontBold(HazardReport.this));
                done.setTypeface(Utilities.fontBold(HazardReport.this));
//                editTextFilter.setTy
               /* TypefaceSpan typefaceSpan = new TypefaceSpan(Utilities.fontRegular(HazardReport.this));
                SpannableString spannableString = new SpannableString("Search..");

                spannableString.setSpan(typefaceSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                editTextFilter.setHint(spannableString);*/

                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HazardReport.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                ReportsBranchAdapter = new ReportsBranchAdapter(HazardReport.this, branchesList);
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

                        stringBuilderbranchId = new StringBuilder();
                        stringBuilderbranch = new StringBuilder();
                        selectedUserList.clear();
                        //  selectedBranchList.add("ORGN000001");
                        for (int i = 0; i < branchesList.size(); i++) {
                            if (branchesList.get(i).isSelected()) {
                                selectedUserList.add(branchesList.get(i).getBranchId());

                                Log.d("selected branches:", "" + branchesList.get(i).getBranchId());

                                for (int j = 0; j < selectedUserList.size(); j++) {
                                    if (j != selectedUserList.size() - 1) {
                                        stringBuilderbranchId.append(",");
                                        stringBuilderbranch.append(",");
                                        break;
                                    }
                                }
                                stringBuilderbranchId.append(branchesList.get(i).getBranchId());
                                stringBuilderbranch.append(branchesList.get(i).getBranchName());
//                                getHazardBranches();
                            }
                        }

                        if (stringBuilderbranch.length() > 0) {
                            edit_branch.setText(stringBuilderbranch);

//                            s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        } else {
                            edit_branch.setText("All");
                        }
                        branchDialog.dismiss();
                    }
                });

//recyclerView.setOn
                chk_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chk_report.isChecked()) {
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
        observationReportDetailsInput = new ObservationReportDetailsInput();
        List<String> zones = new ArrayList<>();
        List<String> branch = new ArrayList<>();

        Log.d("strFromDate", edit_from_date.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'");

        Date data = null;
        try {
            data = sdf.parse(edit_from_date.getText().toString());
            observationReportDetailsInput.setStartDate(output.format(data) + "00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dataOne = null;
        try {
            dataOne = sdf.parse(edit_to_date.getText().toString());
            observationReportDetailsInput.setEndDate(output.format(dataOne) + "00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("strFromDate", output.format(data) + "00:00:00");
        // observationReportInput = new ObservationReportInput();

        observationReportDetailsInput.setStartDate(String.valueOf(output.format(data) + "00:00:00"));
        if (editZone.getText().toString().equalsIgnoreCase("All")) {
            zones = new ArrayList<>();
            observationReportDetailsInput.setZones(zones);
        }
        if (edit_branch.getText().toString().equalsIgnoreCase("All")) {
            branch = new ArrayList<>();
            observationReportDetailsInput.setBranchs(branch);
        } else {
            String zone = stringBuilderZone.toString();
            Log.d("builder", stringBuilderZone + "");
            destZoneArray = zone.split(",");

            String out = stringBuilderbranchId.toString();
            Log.d("builder", stringBuilderbranchId + "");
            destBranchArray = out.split(",");
//        branch.add(destBranchArray.toString() + "");

//        private ArrayList<String> species;
//    public Wetland(String name, String[] speciesArr) {
//            this.name = name;
//        zones.add(stringBuilderZone + "");
            for (int i = 0; i < destZoneArray.length; i++) {
                zones.add(destZoneArray[i]);
//            }
            }
            for (int i = 0; i < destBranchArray.length; i++) {
                branch.add(destBranchArray[i]);
//            }
            }
            observationReportDetailsInput.setZones(zones);
            observationReportDetailsInput.setBranchs(branch);
/*
//        branch.add(stringBuilderbranchId + "");
            if (editZone.getText().toString().trim().equalsIgnoreCase("All")) {
                List<String> zonesAll = new ArrayList<>();
                ob

            servationReportDetailsInput.setZones(zonesAll);
                observationReportDetailsInput.setBranchs(branch);
            } else {
                observationReportDetailsInput.setZones(zones);
                observationReportDetailsInput.setBranchs(branch);
            }
*/


        }
        if (strReportName.equalsIgnoreCase("Hazard Report")) {
            observationReportDetailsInput.setAlertType("Hazard");
        } else if (strReportName.equalsIgnoreCase("Near Miss Report")) {
            observationReportDetailsInput.setAlertType("Near Miss");
        } else {
            observationReportDetailsInput.setAlertType("Accident");
        }

        observationReportDetailsInput.setAlertEmail("");
        observationReportDetailsInput.setAlertStatus("");
    }

    private void getZones() {
        try {
            zonesList = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserZoneResponse> call = apiInterface.getZoneDetails("Bearer " + sharedPreferences.getString("Bearertoken", null));
            progressDialog = new ProgressDialog(HazardReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<UserZoneResponse>() {
                public void onResponse(Call<UserZoneResponse> call, Response<UserZoneResponse> response) {

                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        zonesList.clear();
                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
//                                progressDialog.dismiss();
                                UserZoneResponse loginResponse = response.body();
                                //Log.d("branchresponse", response.body().getResult() + "");
                                zonesList.addAll(loginResponse.getResult());

                            } else {
                                progressDialog.dismiss();
                                showAlert("No Data");
                            }
                        } else {
                            progressDialog.dismiss();
                            showAlert(response.body().toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<UserZoneResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }
    }

    private void getHazardBranches() {

        try {
            branchesList.clear();

            if (editZone.getText().toString().equalsIgnoreCase("All")) {
                destZoneArray = new String[]{};
            } else {
                String out = stringBuilderZone.toString();
                destZoneArray = out.split(",");
            }
            Gson gson = new Gson();
            String json = gson.toJson(destZoneArray);
            Log.d("ReportResponse", json);

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserBranchResponse> call = apiInterface.getBranchDetails("Bearer " + sharedPreferences.getString("Bearertoken", null), destZoneArray);


            progressDialog = new ProgressDialog(HazardReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
//            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<UserBranchResponse>() {
                public void onResponse(Call<UserBranchResponse> call, Response<UserBranchResponse> response) {

                    if (response.isSuccessful()) {
                        branchesList.clear();
                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
//                                progressDialog.dismiss();
                                UserBranchResponse loginResponse = response.body();
                                branchesList.addAll(loginResponse.getResult());

                            } else {
//                                progressDialog.dismiss();
                                showAlert("No Data");
                            }
                        } else {
//                            progressDialog.dismiss();
                            showAlert(response.body().getErrors().toString());
                        }
                    } else {
//                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<UserBranchResponse> call, Throwable t) {
//                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
//            progressDialog.dismiss();
            e.getMessage();

        }


    }

    private void getPieChartData() {
        try {
            setObservationInput();
            piceChartResultList = new ArrayList<>();
            Gson gson = new Gson();
            String json = gson.toJson(observationReportDetailsInput);
            Log.d("ReportResponse", json);

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationStatusReportResult> call = apiInterface.GetObservationStatusReport("Bearer " + sharedPreferences.getString("Bearertoken", null), observationReportDetailsInput);
            progressDialog = new ProgressDialog(HazardReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<ObservationStatusReportResult>() {
                public void onResponse(Call<ObservationStatusReportResult> call, Response<ObservationStatusReportResult> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            progressDialog.dismiss();
                            observationReportDetailsResult = response.body();
                            if (observationReportDetailsResult.getResult().getObservationsStatus() != null) {
                                piceChartResultList.addAll(observationReportDetailsResult.getResult().getObservationsStatus());
                                Intent observationreportview = new Intent(HazardReport.this, HazardObservationReport.class);
                                Bundle bd = new Bundle();
                                bd.putString("Title", strReportName);
                                bd.putSerializable("reportsResultStatus", (Serializable) piceChartResultList);
                                bd.putSerializable("reportsResultCount", (Serializable) observationReportDetailsResult);
                                bd.putSerializable("observationReportDetailsInput", (Serializable) observationReportDetailsInput);
//                            bd.putSerializable("reportResponse", (Serializable) hazardBranchResponse);
//                            bd.putSerializable("reportRequest", (Serializable) hazardBranchModelrequest);
                                observationreportview.putExtras(bd);
                                startActivity(observationreportview);
                            } else {
                                showAlert("No Data");
                            }


//                            observationReportDetailsResponse = response.body();
//                            resultList.addAll(observationReportDetailsResponse.getResult());
//                            Intent observationreportview = new Intent(HazardReport.this, ObservationReportListView.class);
//                            Bundle bd = new Bundle();
//                            bd.putSerializable("reportsResult", (Serializable) resultList);
//                            bd.putString("title", "Hazard Report");
//                            observationreportview.putExtras(bd);
//                            startActivity(observationreportview);
                        } else {
                            showAlert("No Data");
                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<ObservationStatusReportResult> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }

    }

    private void getObservationReportDetails() {
        try {
            setObservationInput();
            resultList = new ArrayList<>();
            Gson gson = new Gson();
            String json = gson.toJson(observationReportDetailsInput);
            Log.d("ReportResponse", json);

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationReportDetailsResponse> call = apiInterface.GetObservationReportDetails("Bearer " + sharedPreferences.getString("Bearertoken", null), observationReportDetailsInput);
            progressDialog = new ProgressDialog(HazardReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<ObservationReportDetailsResponse>() {
                public void onResponse(Call<ObservationReportDetailsResponse> call, Response<ObservationReportDetailsResponse> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            progressDialog.dismiss();
                            observationReportDetailsResponse = response.body();
                            resultList.addAll(observationReportDetailsResponse.getResult());
                            Intent observationreportview = new Intent(HazardReport.this, ObservationReportListView.class);
                            Bundle bd = new Bundle();
                            bd.putSerializable("reportsResult", (Serializable) resultList);
                            bd.putString("title", "Hazard Report");
                            observationreportview.putExtras(bd);
                            startActivity(observationreportview);
                        } else {
                            showAlert("No Data");
                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<ObservationReportDetailsResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }
    }

   /* private void getObservationsReportSummary() {
        try {
            observationReportSummaryStatusList = new ArrayList<>();
            observationReportSummaryList = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationReportSummaryResponse> call = apiInterface.getObservationsReportSummary("Bearer " + sharedPreferences.getString("Bearertoken", null));
            ProgressDialog progressDialog = new ProgressDialog(HazardReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<ObservationReportSummaryResponse>() {
                public void onResponse(Call<ObservationReportSummaryResponse> call, Response<ObservationReportSummaryResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                progressDialog.dismiss();
                                observationReportSummaryResponse = response.body();
                                Log.d("responcde", response.toString());

                                //observationReportSummaryStatusList.addAll(observationReportSummaryResponse.getResult().get(i).getStatusObservations());
                                for (int i = 1; i < response.body().getResult().size(); i++) {
                                    observationReportSummaryList.add(response.body().getResult().get(i));
                                }
                                Intent observationreportview = new Intent(HazardReport.this, HazardObservationReport.class);
                                Bundle bd = new Bundle();
                                bd.putSerializable("reportsResult", (Serializable) resultList);
                                bd.putSerializable("reportsCountResult", (Serializable) observationReportSummaryList);
                                bd.putString("title", "Hazard Report");
                                observationreportview.putExtras(bd);
                                startActivity(observationreportview);

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


                public void onFailure(Call<ObservationReportSummaryResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }
    }*/


}
