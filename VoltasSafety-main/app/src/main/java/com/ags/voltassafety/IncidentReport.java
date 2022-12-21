package com.ags.voltassafety;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.IncidentTypeAdapter;
import com.ags.voltassafety.adapters.ReportsBranchAdapter;
import com.ags.voltassafety.adapters.ReportsUserAdapter;
import com.ags.voltassafety.model.BranchLevelUserResponse;
import com.ags.voltassafety.model.BranchLevelUserResult;
import com.ags.voltassafety.model.BranchObservation;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchResponse;
import com.ags.voltassafety.model.IncidentTypeModel;
import com.ags.voltassafety.model.ObservationReportDetailsInput;
import com.ags.voltassafety.model.ObservationReportDetailsResponse;
import com.ags.voltassafety.model.ObservationReportDetailsResult;
import com.ags.voltassafety.model.ObservationReportInput;
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

public class IncidentReport extends BaseActivity {
    EditText edit_from_date, edit_to_date, edit_branch, edit_user, edit_incident_type, editZone;
    private int year;
    private int month;
    private int day;
    com.ags.voltassafety.adapters.ReportsBranchAdapter ReportsBranchAdapter;
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
    //    private AlertDialog.Builder mAlertDialog;
    ImageView back;
    private TextView tv_title;
    UserBranches userBranches;

    HazardBranchModel hazardBranchModelrequest;
    HazardBranchResponse hazardBranchResponse;
    IncidentTypeModel incidentTypeModel;

    List<ObservationReportDetailsResult> resultList;
    ObservationReportDetailsInput observationReportDetailsInput;
    ObservationReportDetailsResponse observationReportDetailsResponse;
    StringBuilder stringBuilderZone;
    StringBuilder stringBuilderZonename;
    StringBuilder stringBuilderbranchId, stringBuilderbranch;
    List<UserZones> zonesList;

    String[] destZoneArray, destBranchArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_report);

        edit_from_date = findViewById(R.id.edit_from_date);
        edit_to_date = findViewById(R.id.edit_to_date);
        edit_branch = findViewById(R.id.edit_branch);
        editZone = findViewById(R.id.editZone);
//        edit_user = findViewById(R.id.edit_user);
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

        if (Utilities.isConnectingToInternet(IncidentReport.this)) {
            getZones();

        } else {
            showAlert("Please Check Your Internet Connection");
        }


        get_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getObservationReportDetails();
//                getHazardBranches();
                //    if (strReportName.equalsIgnoreCase("Observation Report")) {

                //    setObservationInput();

                // }
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(IncidentReport.this, R.style.MyDialogTheme,
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(IncidentReport.this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {

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
                final Dialog branchDialog = new Dialog(IncidentReport.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IncidentReport.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                ReportsUserAdapter = new ReportsUserAdapter(IncidentReport.this, zonesList);
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

                                Log.d("selected branches:", "" + zonesList.get(i).getZoneId());

                                for (int j = 0; j < selectedUserList.size(); j++) {
                                    if (j != selectedUserList.size() - 1) {
                                        stringBuilderZone.append(",");
                                        stringBuilderZonename.append(",");
                                        break;
                                    }
                                }
                                stringBuilderZone.append(zonesList.get(i).getZoneId());
                                stringBuilderZonename.append(zonesList.get(i).getZoneName());
                                getHazardBranches();
                            }
                        }

                        if (stringBuilderZone.length() > 0) {
                            editZone.setText(stringBuilderZonename);

                        } else {
                            editZone.setText("All");
                        }
                        branchDialog.dismiss();
                    }
                });

                chk_select_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chk_select_all.isChecked()) {
                            for (UserZones userBranches : zonesList) {

                                userBranches.setSelected(true);
                            }

                        } else {
                            for (UserZones userBranches : zonesList) {

                                userBranches.setSelected(false);
                            }
                        }
                        ReportsUserAdapter.notifyDataSetChanged();
                    }
                });
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

        edit_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(IncidentReport.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IncidentReport.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                ReportsBranchAdapter = new ReportsBranchAdapter(IncidentReport.this, branchesList);
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
                        stringBuilderbranch = new StringBuilder();
                        stringBuilderbranchId = new StringBuilder();
                        selectedBranchList.clear();
                        //  selectedBranchList.add("ORGN000001");
                        for (int i = 0; i < branchesList.size(); i++) {
                            if (branchesList.get(i).isSelected()) {
                                selectedBranchList.add(branchesList.get(i).getBranchName());

                                Log.d("selected branches:", "" + branchesList.get(i).getBranchName());

                                for (int j = 0; j < selectedBranchList.size(); j++) {
                                    if (j != selectedBranchList.size() - 1) {
                                        stringBuilderbranch.append(",");
                                        stringBuilderbranchId.append(",");
                                        break;
                                    }
                                }
                                stringBuilderbranch.append(branchesList.get(i).getBranchName());
                                stringBuilderbranchId.append(branchesList.get(i).getBranchId());
                            }
                        }

                        if (stringBuilderbranch.length() > 0) {
                            edit_branch.setText(stringBuilderbranch);

                        } else {
                            edit_branch.setText("All");
                        }
                        branchDialog.dismiss();
                        // edit_user.setText("All");
                        //getBranchLevelUser(selectedBranchList);
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

      /*  edit_incident_type.setText("All");
        edit_incident_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(IncidentReport.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IncidentReport.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                for (int i = 0; i < incidentlist.size(); i++) {
                    incidentTypeAdapter = new IncidentTypeAdapter(IncidentReport.this, incidentlist);
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
                        } else {
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
        });

        edit_user.setText("All");
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog branchDialog = new Dialog(IncidentReport.this, R.style.MyDialogTheme);
                branchDialog.setContentView(R.layout.activity_multi_check_spinner);
                chk_select_all = branchDialog.findViewById(R.id.chk_select_all);
                TextView cancel = branchDialog.findViewById(R.id.cancel);
                TextView done = branchDialog.findViewById(R.id.done);
                EditText editTextFilter = branchDialog.findViewById(R.id.search_edit_text);
                RecyclerView recyclerView = branchDialog.findViewById(R.id.recycler_view);
                branchDialog.setCanceledOnTouchOutside(false);
                branchDialog.show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IncidentReport.this);
                recyclerView.setLayoutManager(linearLayoutManager);

//                ReportsUserAdapter = new ReportsUserAdapter(IncidentReport.this, userslist);
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

                chk_select_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chk_select_all.isChecked()) {
                            for (BranchLevelUserResult userBranches : userslist) {

                                userBranches.setSelected(true);
                            }

                        } else {
                            for (BranchLevelUserResult userBranches : userslist) {

                                userBranches.setSelected(false);
                            }
                        }
                        ReportsUserAdapter.notifyDataSetChanged();
                    }
                });
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
        });*/

    }

   /* private void getHazardBranches() {
        setObservationInput();

        try {
            hazardBranchResultList = new ArrayList<>();

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<HazardBranchResponse> call = apiInterface.getBranchObservationReport("Bearer " + sharedPreferences.getString("Bearertoken", null), hazardBranchModelrequest);

            Gson gson = new Gson();
            String json = gson.toJson(hazardBranchModelrequest);

            Log.d("Reportrequest", json);


            progressDialog = new ProgressDialog(IncidentReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<HazardBranchResponse>() {
                public void onResponse(Call<HazardBranchResponse> call, Response<HazardBranchResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                progressDialog.dismiss();
                                hazardBranchResponse = response.body();


                                hazardBranchResultList.addAll(hazardBranchResponse.getResult().getBranchObservations());

                                Intent observationreportview = new Intent(IncidentReport.this, IncidentBanchReport.class);
                                Bundle bd = new Bundle();
                                bd.putSerializable("reportsResult", (Serializable) hazardBranchResultList);
                                bd.putSerializable("reportResponse", (Serializable) hazardBranchResponse);
                                bd.putSerializable("reportRequest", (Serializable) hazardBranchModelrequest);
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


                public void onFailure(Call<HazardBranchResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (
                Exception e) {
            e.getMessage();

        }


    }*/


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
        observationReportDetailsInput = new ObservationReportDetailsInput();
        observationReportDetailsInput.setStartDate(String.valueOf(output.format(data) + "00:00:00"));

        //  observationReportInput.setBranchs(selectedBranchList);


        //  observationReportInput.setZones(new ArrayList<>());

        try {
            data = sdf.parse(edit_to_date.getText().toString());
            observationReportDetailsInput.setEndDate(output.format(data) + "00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> zones = new ArrayList<>();
//        zones.add(destZoneArray.toString() + "");
        List<String> branch = new ArrayList<>();
        String out = stringBuilderbranchId.toString();
        destBranchArray = out.split(",");
//        branch.add(destBranchArray.toString() + "");

//        private ArrayList<String> species;
//    public Wetland(String name, String[] speciesArr) {
//            this.name = name;
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
        observationReportDetailsInput.setAlertType("Incident");
        observationReportDetailsInput.setAlertEmail("");
        observationReportDetailsInput.setAlertStatus("Assigned");

   /*     hazardBranchModelrequest.setBranch("");
        hazardBranchModelrequest.setType("Incident");
        if (edit_incident_type.getText().toString().equalsIgnoreCase("Accident,Near Miss")) {
            hazardBranchModelrequest.setSubType("All");
        } else {
            hazardBranchModelrequest.setSubType(edit_incident_type.getText().toString());
        }
        hazardBranchModelrequest.setStatus("");*/
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
            progressDialog = new ProgressDialog(IncidentReport.this, R.style.MyDialogTheme);
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
                            Intent observationreportview = new Intent(IncidentReport.this, ObservationReportListView.class);
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

    private void getHazardBranches() {

        try {
            branchesList = new ArrayList<>();
            String out = stringBuilderZone.toString();
            destZoneArray = out.split(",");
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserBranchResponse> call = apiInterface.getBranchDetails("Bearer " + sharedPreferences.getString("Bearertoken", null), destZoneArray);
            progressDialog = new ProgressDialog(IncidentReport.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
//            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<UserBranchResponse>() {
                public void onResponse(Call<UserBranchResponse> call, Response<UserBranchResponse> response) {

                    if (response.isSuccessful()) {
                        branchesList.clear();
                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                //  progressDialog.dismiss();
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

    private void getZones() {
        try {
            zonesList = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserZoneResponse> call = apiInterface.getZoneDetails("Bearer " + sharedPreferences.getString("Bearertoken", null));
            progressDialog = new ProgressDialog(IncidentReport.this, R.style.MyDialogTheme);
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

}
