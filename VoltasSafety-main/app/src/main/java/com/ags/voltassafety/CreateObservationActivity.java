package com.ags.voltassafety;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.BranchAdapter;
import com.ags.voltassafety.adapters.ProjectManagerAdapter;
import com.ags.voltassafety.adapters.SiteEnginnerAdapter;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.Customer;
import com.ags.voltassafety.model.CustomerResponse;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.SiteEngineerName;
import com.ags.voltassafety.model.SiteEngineerResult;
import com.ags.voltassafety.model.SiteEnginnerResponse;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.model.UserZoneResponse;
import com.ags.voltassafety.model.Verticleresponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.SimpleDividerItemDecoration;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.LinearLayoutManager.*;

public class CreateObservationActivity extends BaseActivity implements BranchAdapter.OnItemClicked {

    EditText edit_incident_date, edit_incident_time, edit_sap_number, edit_site_name, edit_address,
            edit_pf_number, edit_concern_pf_number, edit_site_engineer_email, edit_customer_name, edit_site_engineer_phone, targetDate, edit_other_responsible_persons;
    AutoCompleteTextView edit_site_engineer_name, edit_concern_engineer_name;
    private String phone, email;
    RecyclerView branch_recyclerview;
    private Dialog dialog;
    private SearchView searchView;
    String selecteddate = null;
    TextInputLayout inputlayout_customer_name, inputlayout_sap_number, inputlayout_incident_date, inputlayout_incident_time;
    TextView create_title, concern_engineer_information_text, cancel, next, customer_information_textview, incident_information_text, site_engineer_information_text;

    ArrayList<String> customernamearraylist;
    ArrayList<Customer> getCustomernamearraylist = new ArrayList<Customer>();
    ArrayList<String> customeridarraylist;
    List<SiteEngineerResult> siteEngineerResultArrayList;
    List<String> otherResponsiblePerList;
    private ImageView sendMail, callPhone;
    ObservationHeader observationHeader;
    private String address, custName, sapId, siteName;
    Intent intent;
    String[] parts;
    String part1, part2;
    ProjectManagerAdapter managerAdapter;
    private boolean searchFlag = true;
    private String strSelectedEngName = "";
    private Intent dashboardintent;
    BranchAdapter branchAdapter;
    ArrayList<String> zonesArrayList;
    ArrayList<String> zonesArrayListId;
    ArrayList<String> branchArrayList;
    ArrayList<String> branchArrayListId;
    private Spinner spinnerZone, spinnerBranch;
    ProgressDialog progressDialog;
    String zoneID, branchID, zone, branch;
    String GuestuserName, GuestuserEmail, GuestuserNumber, LoginUserID;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayAdapter zoneAdapter, branchArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_create_observation);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            initialisation();
            setFonts();

            final Calendar calendar = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String formattedDate = df.format(calendar.getTime());

            intent = getIntent();
            if (intent.getExtras() != null) {
                observationHeader = (ObservationHeader) intent.getExtras().getSerializable("HeaderObject");
            }
            sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            if (sharedPreferences != null) {

//                GuestuserName = sharedPreferences.getString("Bearertoken", null);
//                GuestuserEmail = sharedPreferences.getString("GuestuserEmail", null);
//                GuestuserNumber = sharedPreferences.getString("GuestuserNumber", null);
                LoginUserID = sharedPreferences.getString("LoginUserID", null);
                Log.d("LoginUserID", sharedPreferences.getString("LoginUserID", null));

               /* if (sharedPreferences.getString("LoginUserID", null).equalsIgnoreCase("Guest")) {
                    site_engineer_information_text.setVisibility(View.GONE);
                    edit_site_engineer_name.setVisibility(View.GONE);
                    edit_pf_number.setVisibility(View.GONE);
                    edit_site_engineer_email.setVisibility(View.GONE);
                    edit_site_engineer_phone.setVisibility(View.GONE);
                    targetDate.setVisibility(View.GONE);
                } else {
                    site_engineer_information_text.setVisibility(View.VISIBLE);
                    edit_site_engineer_name.setVisibility(View.VISIBLE);
                    edit_pf_number.setVisibility(View.VISIBLE);
                    edit_site_engineer_email.setVisibility(View.VISIBLE);
                    edit_site_engineer_phone.setVisibility(View.VISIBLE);
                    targetDate.setVisibility(View.VISIBLE);
                }*/
            }


            if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident")) {
                Log.d("Near miss>>>", observationHeader.getTypeOfObservation());

            } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
                Log.d("Hazaard>>>>", observationHeader.getTypeOfObservation());
            }


            create_title.setText(observationHeader.getReason().toUpperCase());
            getZones();

            spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Zone *")) {
                        zoneID = "";
                        spinnerBranch.setAdapter(null);
                    } else {

                        zoneID = zonesArrayListId.get(i);
                        getBranches();

                    }
                    observationHeader.setGuestZone(adapterView.getSelectedItem().toString());
                    observationHeader.setGuestZoneId(zoneID);


                    Log.d("zoneId", zoneID + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Branch *")) {
                        branchID = "";
                    } else {
                        branchID = branchArrayListId.get(i);

                    }
                    observationHeader.setGuestBranch(adapterView.getSelectedItem().toString());
                    observationHeader.setGuestBranchId(branchID);
//                    getBranches();

                    Log.d("branchID", branchID + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            if (observationHeader.getReason().equalsIgnoreCase("accident")) {
                create_title.setText("ACCIDENT");
            } else {
                create_title.setText(observationHeader.getReason().toUpperCase());
            }


            if (observationHeader.getObservationId() != null) {
                parts = formattedDate.split(" ");
                part1 = parts[0]; // 004
                part2 = parts[1];
                targetDate.setText(part1);

                observationHeader = (ObservationHeader) intent.getExtras().getSerializable("HeaderObject");
                next.setText("Submit");
                address = observationHeader.getAddress();
                custName = observationHeader.getCustomerName();
                sapId = observationHeader.getSapCustomerId();
                siteName = observationHeader.getSiteName();
                zone = observationHeader.getGuestZone();
                branch = observationHeader.getGuestBranch();
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(observationHeader.getDateOfIncidence());
                } catch (ParseException e) {
                    e.getMessage();
                }

                edit_customer_name.setText(custName);
                edit_sap_number.setText(sapId);
                edit_site_name.setText(siteName);
                edit_address.setText(address);
                edit_incident_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
                edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(date));

              /*  zonesArrayList = new ArrayList<>();
                zonesArrayList.add(observationHeader.getGuestZone());
                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, zonesArrayList);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerZone.setAdapter(adp1);*/
             /*   String[] arraySpinner = new String[]{
                        zone
                };
//                Spinner s = (Spinner) findViewById(R.id.Spinner01);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateObservationActivity.this, android.R.layout.simple_list_item_1, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinnerZone.setAdapter(adapter);*/
                // spinnerZone.setTag(observationHeader.getGuestZone());

//                edit_customer_name.setEnabled(false);
                edit_sap_number.setEnabled(false);
                edit_site_name.setEnabled(false);
//                edit_address.setEnabled(false);
                edit_incident_date.setEnabled(false);
                edit_incident_time.setEnabled(false);
                spinnerZone.setEnabled(false);
                spinnerBranch.setEnabled(false);

            } else {

                //observationHeader = new ObservationHeader();
                parts = formattedDate.split(" ");
                part1 = parts[0]; // 004
                part2 = parts[1];
                Log.d("CurrrentDate", formattedDate);
                edit_incident_date.setText(part1);
                edit_incident_time.setText(part2);
                targetDate.setText(part1);
            }
            sendMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    email = edit_site_engineer_email.getText().toString();
                    if (email.equals("")) {
                        Utilities.showAlertDialog("No email to perform", CreateObservationActivity.this);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "" + email, null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                    }
                }
            });

            callPhone.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {

                    phone = edit_site_engineer_phone.getText().toString();
                    if (!phone.equals("")) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phone));
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);
                    } else {
                        Utilities.showAlertDialog("No phone number to perform", CreateObservationActivity.this);
                    }
                }
            });


            //getProjectEnginnerName(strKey);

            edit_incident_date.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final Dialog calenderdialog = new Dialog(CreateObservationActivity.this, R.style.MyDialogTheme);
                    calenderdialog.setContentView(R.layout.activity_incidentdate);
                    calenderdialog.setCanceledOnTouchOutside(false);
                    calenderdialog.show();
                    CalendarView calendarView;
                    calendarView = calenderdialog.findViewById(R.id.calendarView);
                    TextView cancel = calenderdialog.findViewById(R.id.cancel);
                    TextView done = calenderdialog.findViewById(R.id.done);
                    calendarView.setMaxDate(new Date().getTime());
                    selecteddate = null;
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            selecteddate = null;
                            int m = month + 1;
                            String currentmonth;
                            String currentday;
                            if (m < 10) {
                                currentmonth = "0" + m + "";
                            } else {
                                currentmonth = m + "";
                            }
                            if (dayOfMonth < 10) {
                                currentday = "0" + dayOfMonth;
                            } else {
                                currentday = dayOfMonth + "";
                            }
                            selecteddate = currentday + "-" + currentmonth + "-" + year;
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            calenderdialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (selecteddate != null) {

                                edit_incident_date.setText(selecteddate);

                            } else {
                                edit_incident_date.setText(part1);
                            }
                            Date selectedDate = null;
                            try {
                                selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(edit_incident_date.getText().toString() + " " + new SimpleDateFormat("HH:mm").format(new Date()) + ":00");
                            } catch (ParseException p) {
                                p.getMessage();
                            }

                            if (selectedDate.equals(new Date()) || selectedDate.before(new Date())) {
                                edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
                            } else {
                                edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                            }
                            calenderdialog.dismiss();
                        }
                    });
                }
            });

            edit_incident_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog timepickerdialog = new Dialog(CreateObservationActivity.this, R.style.MyDialogTheme);
                    timepickerdialog.setContentView(R.layout.timepicker_dialog);
                    timepickerdialog.setCanceledOnTouchOutside(false);
                    timepickerdialog.show();
                    final TimePicker timePicker = timepickerdialog.findViewById(R.id.timepicker_view);
                    TextView cancel = timepickerdialog.findViewById(R.id.cancel);
                    TextView done = timepickerdialog.findViewById(R.id.done);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timepickerdialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int hour, minute;
                            String am_pm;
                            if (Build.VERSION.SDK_INT >= 23) {
                                hour = timePicker.getHour();
                                minute = timePicker.getMinute();
                            } else {
                                hour = timePicker.getCurrentHour();
                                minute = timePicker.getCurrentMinute();
                            }

                            Date selectedDate = null;
                            try {
                                selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(edit_incident_date.getText().toString() + " " + hour + ":" + minute + ":00");
                            } catch (ParseException p) {
                                p.getMessage();
                            }

                            if (selectedDate.equals(new Date()) || selectedDate.before(new Date())) {
                                edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
                            } else {
                                edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                            }
//
                            timepickerdialog.dismiss();
                        }
                    });
                }
            });
            edit_customer_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    observationHeader.setCustomerName(editable.toString().trim());
                }
            });
            edit_address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    observationHeader.setAddress(editable.toString().trim());
                }
            });

            edit_pf_number.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    edit_pf_number.setFocusable(true);
                    edit_pf_number.setError(null);
                }
            });
            edit_site_engineer_email.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    edit_site_engineer_email.setFocusable(true);
                    edit_site_engineer_email.setError(null);
                }
            });
            edit_site_engineer_phone.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    edit_site_engineer_phone.setFocusable(true);
                    edit_site_engineer_phone.setError(null);
                }
            });
            edit_site_engineer_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchFlag = true;
                    edit_pf_number.setText("");
                    edit_site_engineer_email.setText("");
                    edit_site_engineer_phone.setText("");
                    sendMail.setVisibility(View.GONE);
                    callPhone.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (!editable.toString().isEmpty() && editable.toString().trim().length() > 2 && !strSelectedEngName.trim().equalsIgnoreCase(editable.toString().trim()) && !editable.toString().contains("com.ags.voltassafety.model.SiteEngineerResult") && searchFlag) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getProjectEnginnerName(editable.toString().trim());
                            }
                        });
                        thread.start();

                    }
                }
            });
            edit_concern_engineer_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchFlag = true;
//                    edit_pf_number.setText("");
//                    edit_concern_engineer_name.setText("");
//                    edit_site_engineer_phone.setText("");
//                    sendMail.setVisibility(View.GONE);
//                    callPhone.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(final Editable editable) {

                    if (!editable.toString().isEmpty() && editable.toString().trim().length() > 2 && !strSelectedEngName.trim().equalsIgnoreCase(editable.toString().trim()) && !editable.toString().contains("com.ags.voltassafety.model.SiteEngineerResult") && searchFlag) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getConcernEnginnerName(editable.toString().trim());
//                                getProjectEnginnerName(editable.toString().trim());
                            }
                        });
                        thread.start();

                    }
                }
            });
            edit_site_engineer_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        searchFlag = false;
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                        edit_site_engineer_name.setFocusable(true);
                        edit_site_engineer_name.setError(null);

                        SiteEngineerResult objEngineerResult = (SiteEngineerResult) adapterView.getItemAtPosition(i);
                        edit_site_engineer_name.setText(objEngineerResult.getUserName());
                        strSelectedEngName = objEngineerResult.getUserName();
                        edit_pf_number.setText(objEngineerResult.getUserId());
                        if (objEngineerResult.getUserMail() != null && objEngineerResult.getUserMail().length() > 0) {
                            edit_site_engineer_email.setText(objEngineerResult.getUserMail().toLowerCase());
                            sendMail.setVisibility(View.VISIBLE);
                        }
                        if (objEngineerResult.getUserPhone() != null && objEngineerResult.getUserPhone().length() > 0) {
                            edit_site_engineer_phone.setText(objEngineerResult.getUserPhone());
                            callPhone.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            edit_concern_engineer_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        searchFlag = false;
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                        edit_concern_engineer_name.setFocusable(true);
                        edit_concern_engineer_name.setError(null);

                        SiteEngineerResult objEngineerResult = (SiteEngineerResult) adapterView.getItemAtPosition(i);
                        edit_concern_engineer_name.setText(objEngineerResult.getUserName());
//                        strSelectedEngName = objEngineerResult.getUserName();
//                        edit_pf_number.setText(objEngineerResult.getUserId());
                        if (objEngineerResult.getUserMail() != null && objEngineerResult.getUserMail().length() > 0) {
                            edit_concern_pf_number.setText(objEngineerResult.getUserMail().toLowerCase());
                            sendMail.setVisibility(View.VISIBLE);
                        }


                        observationHeader.setConcernEngineerOrSupervisor(edit_concern_engineer_name.getText().toString().trim());
                        if (edit_concern_pf_number.getText().toString().trim() != null &&
                                edit_concern_pf_number.getText().toString().trim().length() > 0) {
                            observationHeader.setConcernEngineerOrSupervisorEmail(edit_concern_pf_number.getText().toString().trim());
                        }

                       /* if (objEngineerResult.getUserPhone() != null && objEngineerResult.getUserPhone().length() > 0) {
                            edit_concern_pf_number.setText(objEngineerResult.getUserPhone());
                            callPhone.setVisibility(View.VISIBLE);
                        }
*/
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });


            findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validate()) {

                        if (edit_site_engineer_name.getText().length() > 0) {

                            if (edit_pf_number.getText().toString().trim().length() == 0 ||
                                    edit_site_engineer_email.getText().toString().trim().length() == 0 ||
                                    edit_site_engineer_phone.getText().toString().trim().length() == 0) {

                                edit_site_engineer_name.setText("");
                                showAlert("Please select valid  project manager");
                            } else {
                                otherResponsiblePerList = new ArrayList<String>();
                                if(edit_other_responsible_persons.getText().toString().trim().length() > 0){
                                    String[] otherEmails = edit_other_responsible_persons.getText().toString().trim().split(",");
                                    for(String i: otherEmails){

                                        otherResponsiblePerList.add(i);
                                    }
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'");
                                observationHeader.setProjectManagerId(edit_pf_number.getText().toString().trim());
                                observationHeader.setSapCustomerId(edit_site_engineer_phone.getText().toString().trim());
                                observationHeader.setCustomerId(edit_site_engineer_email.getText().toString().trim());
                                observationHeader.setProjectManagerName(edit_site_engineer_name.getText().toString().trim());
                                observationHeader.setConcernEngineerOrSupervisor(edit_concern_engineer_name.getText().toString().trim());
                                observationHeader.setConcernEngineerOrSupervisorEmail(edit_concern_pf_number.getText().toString().trim());
                                observationHeader.setOtherResponsiblepersons(otherResponsiblePerList);

                                Date data = null;
                                try {
                                    data = sdf.parse(targetDate.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                observationHeader.setTargetDateOfClosure(output.format(data) + "00:00:00");

                                if (Utilities.isConnectingToInternet(CreateObservationActivity.this)) {
                                    if (observationHeader.getObservationId() != null) {

                                        updateObservation();

                                    } else {
                                        if (isValid()) {

                                            try {
                                                data = sdf.parse(edit_incident_date.getText().toString());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String formattedTime = output.format(data);

                                            Log.d("Dateandtime", formattedTime + "" + edit_incident_time.getText().toString());

                                            observationHeader.setDateOfIncidence(formattedTime + "" + edit_incident_time.getText().toString());
                                            if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                                dashboardintent = new Intent(CreateObservationActivity.this, HazardActivity.class);
                                            } else if (observationHeader.getReason().equalsIgnoreCase("Accident")) {
                                                dashboardintent = new Intent(CreateObservationActivity.this, IncidentActivity.class);
                                                dashboardintent.putExtra("Accident", "Accident");
                                            } else {
                                                dashboardintent = new Intent(CreateObservationActivity.this, NearMissActivity.class);
                                            }
                                            Bundle bd = new Bundle();
                                            bd.putSerializable("HeaderObject", (Serializable) observationHeader);
                                            //bd.putBoolean("SelectValue", true);
                                            dashboardintent.putExtras(bd);
                                            startActivity(dashboardintent);
                                        }
                                    }
                                } else {
                                    Utilities.showAlertDialog("Please Check Your Internet Connection", CreateObservationActivity.this);
                                }
                            }
                        } else {

                            otherResponsiblePerList= new ArrayList<String>();
                            if(edit_other_responsible_persons.getText().toString().trim().length() > 0){
                                String[] otherEmails = edit_other_responsible_persons.getText().toString().trim().split(",");
                                for(String i: otherEmails){

                                    otherResponsiblePerList.add(i);
                                }
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'");
                            observationHeader.setProjectManagerId(edit_pf_number.getText().toString().trim());
                            observationHeader.setSapCustomerId(edit_site_engineer_phone.getText().toString().trim());
                            observationHeader.setCustomerId(edit_site_engineer_email.getText().toString().trim());
                            observationHeader.setProjectManagerName(edit_site_engineer_name.getText().toString().trim());
                            observationHeader.setConcernEngineerOrSupervisor(edit_concern_engineer_name.getText().toString().trim());
                            observationHeader.setConcernEngineerOrSupervisorEmail(edit_concern_pf_number.getText().toString().trim());
                            observationHeader.setOtherResponsiblepersons(otherResponsiblePerList);

                            Date data = null;
                            try {
                                data = sdf.parse(targetDate.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            observationHeader.setTargetDateOfClosure(output.format(data) + "00:00:00");
                            if (Utilities.isConnectingToInternet(CreateObservationActivity.this)) {
                                if (observationHeader.getObservationId() != null) {

                                    updateObservation();

                                } else {
                                    if (isValid()) {

                                        try {
                                            data = sdf.parse(edit_incident_date.getText().toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String formattedTime = output.format(data);

                                        Log.d("Dateandtime", formattedTime + "" + edit_incident_time.getText().toString());
                                        //observationHeader.setTypeOfObservation("Hazard");
                                        //observationHeader.setReason("Hazard");
                                        observationHeader.setDateOfIncidence(formattedTime + "" + edit_incident_time.getText().toString());
                                        if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                            dashboardintent = new Intent(CreateObservationActivity.this, HazardActivity.class);
                                        } else if (observationHeader.getReason().equalsIgnoreCase("Accident")) {
                                            dashboardintent = new Intent(CreateObservationActivity.this, IncidentActivity.class);
                                        } else {
                                            dashboardintent = new Intent(CreateObservationActivity.this, NearMissActivity.class);
                                        }
                                        Bundle bd = new Bundle();
                                        bd.putSerializable("HeaderObject", (Serializable) observationHeader);
                                        //bd.putBoolean("SelectValue", true);
                                        dashboardintent.putExtras(bd);
                                        startActivity(dashboardintent);
                                    }
                                }
                            } else {
                                Utilities.showAlertDialog("Please Check Your Internet Connection", CreateObservationActivity.this);
                            }
                        }
                    } else {
//                        Utilities.showAlertDialog("Please Select All Mandatory Fields", CreateObservationActivity.this);

                    }


                }
            });
            targetDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog calenderdialog = new Dialog(CreateObservationActivity.this, R.style.MyDialogTheme);

                    calenderdialog.setContentView(R.layout.activity_incidentdate);
                    calenderdialog.setCanceledOnTouchOutside(false);
                    calenderdialog.show();

                    CalendarView calendarView;
                    calendarView = calenderdialog.findViewById(R.id.calendarView);
                    TextView cancel = calenderdialog.findViewById(R.id.cancel);
                    TextView done = calenderdialog.findViewById(R.id.done);

                    calendarView.setMinDate(new Date().getTime());
                    selecteddate = null;
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                            int m = month + 1;
                            String currentmonth;
                            String currentday;
                            if (m < 10) {
                                currentmonth = "0" + m + "";
                            } else {
                                currentmonth = m + "";
                            }
                            if (dayOfMonth < 10) {
                                currentday = "0" + dayOfMonth;
                            } else {
                                currentday = dayOfMonth + "";
                            }
                            selecteddate = currentday + "-" + currentmonth + "-" + year;
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            calenderdialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (selecteddate != null) {

                                targetDate.setText(selecteddate);
                            } else {

                                targetDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                            }

                            calenderdialog.dismiss();
                        }
                    });

                }
            });
            findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void getProjectEnginnerName(String strKey) {
        try {
            if (Utilities.isConnectingToInternet(getApplicationContext())) {
                SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                Call<SiteEnginnerResponse> call = apiInterface.getSiteEngineers("Bearer " + sharedPreferences.getString("Bearertoken", null), strKey);

                call.enqueue(new Callback<SiteEnginnerResponse>() {
                    public void onResponse(Call<SiteEnginnerResponse> call, Response<SiteEnginnerResponse> response) {
                        if (response.isSuccessful()) {

                            // siteEngineerResultArrayList.clear();
                            siteEngineerResultArrayList = new ArrayList<>();
                            if (response.body().getResult() != null && response.body().getResult().size() > 0) {
                                siteEngineerResultArrayList.addAll(response.body().getResult());


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        managerAdapter = new ProjectManagerAdapter(CreateObservationActivity.this, R.layout.list_view_items, siteEngineerResultArrayList);
                                        //edit_site_engineer_name.setThreshold(1);//will start working from first character

                                        edit_site_engineer_name.setAdapter(managerAdapter);//setting the adapter data into the AutoCompleteTextView
                                        edit_site_engineer_name.setTextColor(Color.BLACK);
                                        managerAdapter.notifyDataSetChanged();
                                    }
                                });


                                if (siteEngineerResultArrayList.size() == 1 && siteEngineerResultArrayList.get(0).getUserName().equalsIgnoreCase(edit_site_engineer_name.getText().toString().trim())) {

                                } else {
                                    edit_site_engineer_name.showDropDown();
                                }
                            }

                        } else {

                            showAlert(response.message() + "");
                        }
                    }

                    public void onFailure(Call<SiteEnginnerResponse> call, Throwable t) {
                        //progressDialog.dismiss();
                        Log.d("LoginResponse", t.getMessage() + "");
                    }
                });
            } else {
                //dismissProgress();
                Utilities.showAlertDialog("Please check your internet connection", CreateObservationActivity.this);
            }
        } catch (Exception e) {
            //progressDialog.dismiss();
            e.getMessage();
        }
    }

    private void getConcernEnginnerName(String strKey) {
        try {
            if (Utilities.isConnectingToInternet(getApplicationContext())) {
                SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                Call<SiteEnginnerResponse> call = apiInterface.getSiteEngineers("Bearer " + sharedPreferences.getString("Bearertoken", null), strKey);

                call.enqueue(new Callback<SiteEnginnerResponse>() {
                    public void onResponse(Call<SiteEnginnerResponse> call, Response<SiteEnginnerResponse> response) {
                        if (response.isSuccessful()) {

                            // siteEngineerResultArrayList.clear();
                            siteEngineerResultArrayList = new ArrayList<>();
                            if (response.body().getResult() != null && response.body().getResult().size() > 0) {
                                siteEngineerResultArrayList.addAll(response.body().getResult());


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        managerAdapter = new ProjectManagerAdapter(CreateObservationActivity.this, R.layout.list_view_items, siteEngineerResultArrayList);
                                        //edit_site_engineer_name.setThreshold(1);//will start working from first character

                                        edit_concern_engineer_name.setAdapter(managerAdapter);//setting the adapter data into the AutoCompleteTextView
                                        edit_concern_engineer_name.setTextColor(Color.BLACK);
                                        managerAdapter.notifyDataSetChanged();
                                    }
                                });


                                if (siteEngineerResultArrayList.size() == 1 && siteEngineerResultArrayList.get(0).getUserName().equalsIgnoreCase(edit_concern_engineer_name.getText().toString().trim())) {

                                } else {
                                    edit_concern_engineer_name.showDropDown();
                                }
                            }

                        } else {

                            showAlert(response.message() + "");
                        }
                    }

                    public void onFailure(Call<SiteEnginnerResponse> call, Throwable t) {
                        //progressDialog.dismiss();
                        Log.d("LoginResponse", t.getMessage() + "");
                    }
                });
            } else {
                //dismissProgress();
                Utilities.showAlertDialog("Please check your internet connection", CreateObservationActivity.this);
            }
        } catch (Exception e) {
            //progressDialog.dismiss();
            e.getMessage();
        }
    }


    private void getCustomerNames() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<CustomerResponse> call = apiInterface.getUserCustomers("Bearer " + sharedPreferences.getString("Bearertoken", null));
            showProgressDialog(CreateObservationActivity.this);
            getCustomernamearraylist.clear();
            customernamearraylist = new ArrayList<>();
            customeridarraylist = new ArrayList<>();

            call.enqueue(new Callback<CustomerResponse>() {
                public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getResult() != null) {
                            dismissProgress();
                            getCustomernamearraylist.addAll(response.body().getResult());
                            dialog = new Dialog(CreateObservationActivity.this, R.style.MyDialogTheme);
                            dialog.setContentView(R.layout.recycler_branch_alert_dialog);
                            Toolbar toolbar = dialog.findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);
                            branch_recyclerview = dialog.findViewById(R.id.branch_recyclerview);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            // branch_recyclerview.setLayoutManager(linearLayoutManager);
                            CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getApplicationContext());
                            branch_recyclerview.setLayoutManager(linearLayoutManager);

                            branch_recyclerview.addItemDecoration(new SimpleDividerItemDecoration(CreateObservationActivity.this));
                            branchAdapter = new BranchAdapter(CreateObservationActivity.this, getCustomernamearraylist, CreateObservationActivity.this);
                            branch_recyclerview.getRecycledViewPool().clear();
                            branch_recyclerview.setAdapter(branchAdapter);
                           /* branch_recyclerview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (!hasFocus) {
                                        hideSoftKeyboard(v);
                                        searchView.clearFocus();
                                    }
                                }
                            });*/
                            branch_recyclerview.setOnTouchListener(new View.OnTouchListener() {

                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // branch_recyclerview.setLayoutFrozen(false);
                                    branch_recyclerview.setScrollContainer(false);
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    searchView.clearFocus();
                                    return false;
                                }
                            });
                            //branchAdapter.getFilter().filter("");

                        } else {
                            dismissProgress();
                            showAlert("No data");
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }

                public void onFailure(Call<CustomerResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean isValid() {

//        String errorString = "This field is mandatory";
//        if (edit_customer_name.getText().toString().length() == 0) {
//            edit_customer_name.setFocusable(true);
//            edit_customer_name.setFocusableInTouchMode(true);
//            edit_customer_name.setError(errorString);
//            edit_customer_name.requestFocus();
//            return false;
//        }
//
//        if (edit_sap_number.getText().toString().length() == 0) {
//            edit_sap_number.setError(getString(R.string.error_username));
//            edit_sap_number.requestFocus();
//            return false;
//        }
        if (edit_incident_date.getText().toString().length() == 0) {
            edit_incident_date.setError("This field is mandatory");
            edit_incident_date.requestFocus();
            return false;
        }
        if (edit_incident_time.getText().toString().length() == 0) {
            edit_incident_time.setError("This field is mandatory");
            edit_incident_time.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.branch_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        EditText searchEditText = (EditText) searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.holoheme));
        searchEditText.setHintTextColor(getResources().getColor(R.color.holoheme));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // branch_recyclerview.setLayoutFrozen(true);
                branch_recyclerview.setScrollContainer(true);
                branchAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // branch_recyclerview.setLayoutFrozen(true);
                branch_recyclerview.setScrollContainer(true);
                branchAdapter.getFilter().filter(query);

                return false;
            }
        });
       /* searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });*/
        return true;
    }

    @Override
    public void onItemClick(View view, Customer cust) {
        try {
            dialog.dismiss();
            //Log.d("itemPostion", id + "");
           /* for (Customer cust : getCustomernamearraylist) {
                if (cust.getCustomerId().equalsIgnoreCase(id)) {*/
            edit_customer_name.setText(cust.getName());
            edit_sap_number.setText(cust.getSapcustomerId());
            edit_site_name.setText(cust.getCustomerClassification());
            edit_address.setText(cust.getAddress());

            //  observationHeader.setCustomerId(cust.getCustomerId());
            // observationHeader.setCustomerName(cust.getName());
            //  observationHeader.setSiteName(cust.getCustomerClassification());
//            observationHeader.setAddress(cust.getAddress());
            //  observationHeader.setSapCustomerId(cust.getSapcustomerId());
          /*      }
            }*/
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void initialisation() {
        try {
            edit_customer_name = findViewById(R.id.edit_customer_name);
            edit_site_engineer_name = findViewById(R.id.edit_site_engineer_name);
            inputlayout_customer_name = findViewById(R.id.inputlayout_customer_name);
            edit_incident_date = findViewById(R.id.edit_incident_date);
            edit_incident_time = findViewById(R.id.edit_incident_time);
            edit_sap_number = findViewById(R.id.edit_sap_number);
            edit_site_name = findViewById(R.id.edit_site_name);
            edit_address = findViewById(R.id.edit_address);
            edit_pf_number = findViewById(R.id.edit_pf_number);
            edit_site_engineer_email = findViewById(R.id.edit_site_engineer_email);
            edit_site_engineer_phone = findViewById(R.id.edit_site_engineer_phone);
            edit_other_responsible_persons = findViewById(R.id.edit_other_responsible_persons);
            sendMail = (ImageView) findViewById(R.id.email);
            callPhone = (ImageView) findViewById(R.id.call);
            create_title = findViewById(R.id.create_title);
            cancel = findViewById(R.id.cancel);
            next = findViewById(R.id.next);
            customer_information_textview = findViewById(R.id.customer_information_textview);
            incident_information_text = findViewById(R.id.incident_information_text);
            site_engineer_information_text = findViewById(R.id.site_engineer_information_text);
            inputlayout_customer_name = findViewById(R.id.inputlayout_customer_name);
            inputlayout_sap_number = findViewById(R.id.inputlayout_sap_number);
            inputlayout_incident_date = findViewById(R.id.inputlayout_incident_date);
            inputlayout_incident_time = findViewById(R.id.inputlayout_incident_time);
            targetDate = findViewById(R.id.targetDate);
            spinnerZone = findViewById(R.id.spinnerZone);
            spinnerBranch = findViewById(R.id.spinnerBranch);
            concern_engineer_information_text = findViewById(R.id.concern_engineer_information_text);
            edit_concern_engineer_name = findViewById(R.id.edit_concern_engineer_name);
            edit_concern_pf_number = findViewById(R.id.edit_concern_pf_number);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void setFonts() {
        try {
            //    create_title.setTypeface(Utilities.fontblack(getApplicationContext()));
            cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            next.setTypeface(Utilities.fontRegular(getApplicationContext()));
            customer_information_textview.setTypeface(Utilities.fontBold(getApplicationContext()));
            incident_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            site_engineer_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_incident_date.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_incident_time.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_sap_number.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_site_name.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_address.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_site_engineer_name.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_pf_number.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_site_engineer_email.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_site_engineer_phone.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_customer_name.setTypeface(Utilities.fontRegular(getApplicationContext()));
            targetDate.setTypeface(Utilities.fontRegular(getApplicationContext()));
            create_title.setTypeface(Utilities.fontBold(getApplicationContext()));
            concern_engineer_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_concern_engineer_name.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_concern_pf_number.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_other_responsible_persons.setTypeface(Utilities.fontRegular(getApplicationContext()));

        } catch (Exception e) {
            e.getMessage();

        }
    }

    public void updateObservation() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Gson gson = new Gson();
            String json = gson.toJson(observationHeader);
            Log.d("jsonRequest", json);
            Call<CreateResponse> call = apiInterface.updateObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationHeader);
            showProgressDialog(CreateObservationActivity.this);

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {
                        CreateResponse loginResponse = response.body();
                        Log.d("observationresponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            dismissProgress();
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateObservationActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Thank you for Reporting.\n\nYour Observation ID "+ Html.fromHtml("<b>"+ observationHeader.getObservationId()+"</b>")+ "\n\nYou will recieve a confirmation mail shortly.")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (!LoginUserID.equalsIgnoreCase("Guest")) {
                                                intent = new Intent(CreateObservationActivity.this, ObservationViewActivity.class);
                                                if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                                    intent.putExtra("Name", "HAZARD REPORTING");
                                                } else if (observationHeader.getReason().equalsIgnoreCase("Accident")) {
                                                    intent.putExtra("Name", "INCIDENT REPORTING");
                                                } else {
                                                    intent.putExtra("Name", "NEAR MISS REPORTING");
                                                }
                                                startActivity(intent);
                                            } else {
                                                intent = new Intent(CreateObservationActivity.this, LandingPageActivity.class);
                                              /*  if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                                    intent.putExtra("Name", "HAZARD REPORTING");
                                                } else if (observationHeader.getReason().equalsIgnoreCase("Accident")) {
                                                    intent.putExtra("Name", "INCIDENT REPORTING");
                                                } else {
                                                    intent.putExtra("Name", "NEAR MISS REPORTING");
                                                }*/
                                                startActivity(intent);
                                            }

                                            // dialogInterface.dismiss();
                                        }
                                    }).create().show();
//
                        } else {
                            dismissProgress();
                            showAlert(loginResponse.getErrors()[0]);
                            //showToast(loginResponse.getErrors() + "");
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }

                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private boolean validate() {
        try {

            if (edit_customer_name.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Customer Name";

                edit_customer_name.setError(errorString);

                edit_customer_name.requestFocus();
                return false;
            }
            if (edit_address.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Address";  // Your custom error message.

                edit_address.setError(errorString);

                edit_address.requestFocus();
                return false;
            }
            if (spinnerZone.getSelectedItem().toString().equalsIgnoreCase("Select Zone *")) {
                Utilities.showAlertDialog("Please Select Zone", CreateObservationActivity.this);
                return false;
            }
            if (spinnerBranch.getSelectedItem().toString().equalsIgnoreCase("Select Branch *")) {
                Utilities.showAlertDialog("Please Select Branch", CreateObservationActivity.this);
                return false;
            }
            if (LoginUserID != null && !LoginUserID.equalsIgnoreCase("Guest")) {
                if (edit_site_engineer_name.getText().toString().trim().length() == 0) {
                    String errorString = "Please Enter Engineer Name";  // Your custom error message.

                    edit_site_engineer_name.setError(errorString);

                    edit_site_engineer_name.requestFocus();
                    return false;
                }
                if (edit_pf_number.getText().toString().trim().length() == 0) {
                    String errorString = "Please Enter Incharge ID";  // Your custom error message.

                    edit_pf_number.setError(errorString);

                    edit_pf_number.requestFocus();
                    return false;
                }
                if (edit_site_engineer_email.getText().toString().trim().length() == 0) {
                    String errorString = "Please Enter Incharge Email";  // Your custom error message.

                    edit_site_engineer_email.setError(errorString);

                    edit_site_engineer_email.requestFocus();
                    return false;
                }
                if (edit_site_engineer_phone.getText().toString().trim().length() == 0) {
                    String errorString = "Please Enter Incharge Phone Number";  // Your custom error message.

                    edit_site_engineer_phone.setError(errorString);

                    edit_site_engineer_phone.requestFocus();
                    return false;
                }
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
        return true;
    }

    private void getZones() {
        try {
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserZoneResponse> call = apiInterface.getGuestZoneResponse("Bearer " + sharedPreferences.getString("Bearertoken", null),sharedPreferences.getString("Domain", null));
            progressDialog = new ProgressDialog(CreateObservationActivity.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<UserZoneResponse>() {
                public void onResponse(Call<UserZoneResponse> call, Response<UserZoneResponse> response) {
                    zonesArrayList = new ArrayList<>();
                    zonesArrayListId = new ArrayList<>();
                    zonesArrayListId.add("");
                    zonesArrayList.add("Select Zone *");
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("", response.toString());
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            zonesArrayList.add(response.body().getResult().get(i).getZoneName());
                            zonesArrayListId.add(response.body().getResult().get(i).getZoneId());

                        }

                        zoneAdapter = new ArrayAdapter(CreateObservationActivity.this, android.R.layout.simple_list_item_1, zonesArrayList);
                        zoneAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinnerZone.setAdapter(zoneAdapter);


                        if (observationHeader.getGuestZone() != null) {
                            int position = zoneAdapter.getPosition(observationHeader.getGuestZone());
                            spinnerZone.setSelection(position);
                        }


                    }

                }


                public void onFailure(Call<UserZoneResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            progressDialog.dismiss();

        }
    }

    private void getBranches() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserBranchResponse> call = apiInterface.getGuestBranchResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), zoneID);
            progressDialog = new ProgressDialog(CreateObservationActivity.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<UserBranchResponse>() {
                public void onResponse(Call<UserBranchResponse> call, Response<UserBranchResponse> response) {
                    branchArrayList = new ArrayList<>();
                    branchArrayListId = new ArrayList<>();
                    branchArrayListId.add("");
                    branchArrayList.add("Select Branch *");
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("", response.toString());
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            branchArrayList.add(response.body().getResult().get(i).getBranchName());
                            branchArrayListId.add(response.body().getResult().get(i).getBranchId());

                        }
                        branchArrayAdapter = new ArrayAdapter(CreateObservationActivity.this, android.R.layout.simple_list_item_1, branchArrayList);
                        branchArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinnerBranch.setAdapter(branchArrayAdapter);

                        if (observationHeader.getGuestBranch() != null) {
                            String branch = observationHeader.getGuestBranch();
                            int position = branchArrayAdapter.getPosition(branch);
                            spinnerBranch.setSelection(position);
                        }


                    } else {
                        progressDialog.dismiss();
                    }

                }


                public void onFailure(Call<UserBranchResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            progressDialog.dismiss();

        }
    }
}
