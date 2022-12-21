package com.ags.voltassafety;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.ObservationDetailsAdapter;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationDetailsResponse;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObservationDetailsActivity extends BaseActivity implements ObservationDetailsAdapter.OnItemClicked {

    EditText edit_incident_date, edit_incident_time, edit_sap_number, edit_site_name, edit_address,
            edit_pf_number, edit_concern_pf_number, edit_site_engineer_email, edit_customer_name, edit_site_engineer_phone, edit_site_engineer_name, edit_email_id, edit_other_responsible_persons, edit_phone_number, employee_name, employee_id, employee_email_id, employee_phone_number;
    RecyclerView observation_items_recyclerview;
    ImageView delete_observation;
    public TextView concern_engineer_information_text,obsr_id;
    ObservationDetailsAdapter observationDetailsAdapter;
    List<ObservationItemsDetailsModel> observationItemsList;
    List<ObservationAttachmentModel> observationAttachmentsList;
    AlertDialog.Builder mAlertDialog;
    Button edit_button, statusButton, closeObservation;
    ImageView back;
    int itemposition;
    StringBuilder otherEmails;
    ObservationHeader observationHeader = null;
    private ImageView editDetails;
    String status, observationId, obsId, observationType, subType;
    public ImageView item_header_edit;
    public EditText obsrId, edit_branch, edit_zone;
    public ProgressDialog progressDialog;
    private TextView toolbar_title, incident_information_text, observation_header_text, customer_information_textview, employee_information_text,
            site_engineer_information_text;
    public TextInputLayout typeLayout;
    EditText edit_concern_engineer_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_observation_details);
            initialisation();
            setFonts();
            toolbar_title = findViewById(R.id.toolbar_title);
            toolbar_title.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            incident_information_text.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            customer_information_textview.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            employee_information_text.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            site_engineer_information_text.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            concern_engineer_information_text.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            observation_header_text.setTypeface(Utilities.fontBold(ObservationDetailsActivity.this));
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                status = bundle.getString("Status");
//                obsId = bundle.getString("ObsId");
                observationId = bundle.getString("observationId");
                observationType = bundle.getString("ObservationType");
                subType = bundle.getString("SubType");
            }
            if (observationType.equalsIgnoreCase("Hazard")) {
                typeLayout.setHint("Hazard ID");
                incident_information_text.setText("HAZARD INFORMATION");
                observation_header_text.setText("HAZARD DETAILS");
                //obsr_id.setText("Hazard ID: "+observationId);
            }
            if (subType.equalsIgnoreCase("Accident")) {
                typeLayout.setHint("Accident ID");
                incident_information_text.setText("ACCIDENT INFORMATION");
                observation_header_text.setText("ACCIDENT DETAILS");
                //obsr_id.setText("Accident ID: "+observationId);
            }
            if (subType.equalsIgnoreCase("Near Miss")) {
                typeLayout.setHint("NearMiss ID");
                incident_information_text.setText("NEAR MISS INFORMATION");
                observation_header_text.setText("NEAR MISS DETAILS");
                //obsr_id.setText("NearMiss ID: "+observationId);
            }
            obsr_id.setVisibility(View.GONE);
            statusButton.setText(status);
            obsrId.setText(observationId);

//            item_header_edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    Intent itemeditintent = new Intent(ObservationDetailsActivity.this, HazardActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable("HeaderObject", (Serializable) observationHeader);
//                    itemeditintent.putExtras(b);
//                    itemeditintent.putExtra("NearMiss", 2);
//                    startActivity(itemeditintent);
//
//                }
//            });


            item_header_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                        Intent itemeditintent = new Intent(ObservationDetailsActivity.this, HazardActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", (Serializable) observationHeader);
                        itemeditintent.putExtras(b);
                        itemeditintent.putExtra("Hazard", 2);
                        startActivity(itemeditintent);
                    } else if (observationHeader.getReason().equalsIgnoreCase("Near Miss")) {
                        Intent itemeditintent = new Intent(ObservationDetailsActivity.this, NearMissActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", (Serializable) observationHeader);
                        itemeditintent.putExtras(b);
                        // itemeditintent.putExtra("NearMiss", 1);
                        startActivity(itemeditintent);
                    }

                }
            });


            closeObservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ObservationDetailsActivity.this, ClosedActionItemActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("ObservationId", observationId);
                    intent.putExtra("Flag", "Observation");
                    intent.putExtra("StrName", observationHeader.getReason());

                    startActivity(intent);
                }
            });

            editDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (observationItemsList != null && observationItemsList.size() != 0) {
                        Intent itemeditintent = new Intent(ObservationDetailsActivity.this, CreateObservationActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", observationHeader);
                        itemeditintent.putExtras(b);
                        startActivity(itemeditintent);
                    } else {
                        showAlert("Please add at least one Item");
                    }


                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            observation_items_recyclerview.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView\

        } catch (Exception e) {
            e.getMessage();
        }

        delete_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog = new AlertDialog.Builder(ObservationDetailsActivity.this, R.style.MyDialogTheme);
                mAlertDialog.setTitle("Information");
                mAlertDialog.setMessage("Are you sure want to delete?");
                mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        observationHeader.setStatus("Delete");
                        updateObservation();
                    }

                });
                mAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mAlertDialog.show();

            }
        });
    }

    public void updateObservation() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Gson gson = new Gson();
            String json = gson.toJson(observationHeader);
            Log.d("jsonRequest", json);
            Call<CreateResponse> call = apiInterface.updateObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationHeader);
            showProgressDialog(ObservationDetailsActivity.this);

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {
                        CreateResponse loginResponse = response.body();
                        Log.d("observationresponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            dismissProgress();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ObservationDetailsActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Observation deleted successfully ")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent1 = new Intent(ObservationDetailsActivity.this, ObservationViewActivity.class);
                                            if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                                intent1.putExtra("Name", "HAZARD REPORTING");
                                            } else if (observationHeader.getReason().equalsIgnoreCase("Accident")) {
                                                intent1.putExtra("Name", "INCIDENT REPORTING");
                                            } else {
                                                intent1.putExtra("Name", "NEAR MISS REPORTING");
                                            }
                                            startActivity(intent1);
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


    private void getObservationDetails() {
        try {
            if (Utilities.isConnectingToInternet(getApplicationContext())) {
                SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                Call<ObservationDetailsResponse> call = apiInterface.getObservationByID("Bearer " + sharedPreferences.getString("Bearertoken", null), getIntent().getStringExtra("observationId"));
                progressDialog = new ProgressDialog(ObservationDetailsActivity.this, R.style.MyDialogTheme);
                progressDialog.setMessage("Please wait ....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                observationItemsList = new ArrayList<>();
                observationAttachmentsList = new ArrayList<>();
                call.enqueue(new Callback<ObservationDetailsResponse>() {
                    public void onResponse(Call<ObservationDetailsResponse> call, Response<ObservationDetailsResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getResult() != null) {
                                // dismissProgress();
                                progressDialog.dismiss();
                                ObservationDetailsResponse observationDetailsResponse = response.body();

                                Log.d("observatondetailsresp", response.body().getResult() + "");
                                if (observationDetailsResponse.getSuccess()) {
                                    observationHeader = response.body().getResult();
                                    if (observationHeader.getReason().equalsIgnoreCase("Near Miss")) {

                                        toolbar_title.setText(observationHeader.getReason().toUpperCase() + " DETAILS("+observationId+")");
                                    } else if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                        toolbar_title.setText(observationHeader.getReason().toUpperCase() + " DETAILS("+observationId+")");
                                    } else {

                                        //toolbar_title.setText(observationHeader.getTypeOfObservation().toUpperCase() + " DETAILS");
                                        toolbar_title.setText("ACCIDENT DETAILS("+observationId+")");
                                    }
                                    Log.d("observationgetresult", observationDetailsResponse.getResult() + "");
                                    observationItemsList.addAll(observationDetailsResponse.getResult().getObservationItemsDetailsModels());
                                    for (int i = 0; i < observationItemsList.size(); i++) {
                                        observationAttachmentsList.addAll(observationItemsList.get(i).getObservationAttachmentModels());
                                    }
                                    edit_customer_name.setText(response.body().getResult().getCustomerName());
                                    edit_sap_number.setText(response.body().getResult().getSapCustomerId());
                                    edit_site_name.setText(response.body().getResult().getSiteName());
                                    edit_address.setText(response.body().getResult().getAddress());
                                    edit_incident_date.setText(response.body().getResult().getDateOfIncidence());
                                    edit_site_engineer_name.setText(response.body().getResult().getProjectManagerName());
                                    edit_concern_engineer_name.setText(response.body().getResult().getConcernEngineerOrSupervisor());
                                    edit_pf_number.setText(response.body().getResult().getProjectManagerId());
                                    edit_zone.setText(response.body().getResult().getGuestZone());
                                    edit_branch.setText(response.body().getResult().getGuestBranch());
                                    edit_phone_number.setText(response.body().getResult().getManagerPhoneNumber());
                                    otherEmails = new StringBuilder();
                                    for(int i=0; i < observationHeader.getOtherResponsiblepersons().size(); i++){
                                         otherEmails.append(observationHeader.getOtherResponsiblepersons().get(i));
                                         if(i == observationHeader.getOtherResponsiblepersons().size()-1){
                                             //otherEmails.append("");
                                         }
                                         else{
                                             otherEmails.append(",");
                                         }
                                    }
                                    edit_other_responsible_persons.setText(otherEmails.toString());

                                    if (response.body().getResult().getConcernEngineerOrSupervisorEmail() != null && response.body().getResult().getConcernEngineerOrSupervisorEmail().length() > 0) {
                                        edit_concern_pf_number.setText(response.body().getResult().getConcernEngineerOrSupervisorEmail().toLowerCase());
                                    } else {
                                        edit_concern_pf_number.setText("");
                                    }
                                    if (response.body().getResult().getManagerEmail() != null && response.body().getResult().getManagerEmail().length() > 0) {
                                        edit_email_id.setText(response.body().getResult().getManagerEmail().toLowerCase());
                                    } else {
                                        edit_email_id.setText("");
                                    }
//                                    edit_phone_number.setText(response.body().getResult().getManagerPhoneNumber());
//                                    if (response.body().getResult().getUserId().equalsIgnoreCase("USER000001")) {
//                                        employee_name.setText(response.body().getResult().getGuestName());
//                                    }
//                                    {
                                    if (response.body().getResult().getUserName().equalsIgnoreCase("admin")) {
                                        employee_name.setText(response.body().getResult().getGuestName());
                                        employee_phone_number.setText(response.body().getResult().getGuestPhone());
                                        if (response.body().getResult().getGuestEmail() != null && response.body().getResult().getGuestEmail().length() > 0) {
                                            employee_email_id.setText(response.body().getResult().getGuestEmail().toLowerCase());
                                        } else {
                                            employee_email_id.setText("");
                                        }
                                    } else {
                                        employee_name.setText(response.body().getResult().getUserName());
                                        employee_phone_number.setText(response.body().getResult().getUserPhoneNumber());
                                        if (response.body().getResult().getUserEmail() != null && response.body().getResult().getUserEmail().length() > 0) {
                                            employee_email_id.setText(response.body().getResult().getUserEmail().toLowerCase());
                                        } else {
                                            employee_email_id.setText("");
                                        }
                                    }

                                    //  }
                                    employee_id.setText(response.body().getResult().getUserEmployeeId());

//                                    Calendar calendar = Calendar.getInstance();
//
//                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                    // String formattedDate = df.format(calendar.getTime());

                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(observationHeader.getDateOfIncidence());
                                    } catch (ParseException e) {
                                        e.getMessage();
                                    }
                                    edit_incident_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
                                    edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(date));

                                    observationDetailsAdapter = new ObservationDetailsAdapter(ObservationDetailsActivity.this, observationItemsList, observationAttachmentsList, ObservationDetailsActivity.this, observationHeader);
                                    observation_items_recyclerview.setAdapter(observationDetailsAdapter);
                                    observationDetailsAdapter.notifyDataSetChanged();
                                    if (status.equalsIgnoreCase("Open")) {
                                        if (sharedPreferences.getString("roleId", null) != null && sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000001") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000002") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000003") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000004") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000005") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000006")) {

                                            editDetails.setVisibility(View.VISIBLE);
                                            item_header_edit.setVisibility(View.VISIBLE);

                                        } else if (sharedPreferences.getString("LoginUserID", null).equalsIgnoreCase(employee_id.getText().toString())) {

                                            editDetails.setVisibility(View.VISIBLE);
                                        } else {
                                            editDetails.setVisibility(View.GONE);
                                            item_header_edit.setVisibility(View.GONE);
                                            closeObservation.setVisibility(View.GONE);
                                        }

                                    } else {
                                        editDetails.setVisibility(View.GONE);
                                        item_header_edit.setVisibility(View.GONE);
                                        closeObservation.setVisibility(View.GONE);
                                    }

                                    if (!status.equalsIgnoreCase("Closed")) {
                                        if (sharedPreferences.getString("roleId", null) != null && sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000001") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000002") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000003") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000004") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000005") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000006")) {
                                            delete_observation.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        delete_observation.setVisibility(View.GONE);
                                        closeObservation.setVisibility(View.GONE);
                                    }
                                    if (sharedPreferences.getString("roleId", null) != null && sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000001") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000002") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000003") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000004") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000005") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000006")) {
                                        if (response.body().getResult().getStatus() != null && !response.body().getResult().getStatus().equalsIgnoreCase("Closed")) {

                                            if (observationItemsList != null && observationItemsList.size() > 0 && !status.equalsIgnoreCase("Closed") && sharedPreferences.getString("roleId", null) != null && (sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000001") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000002") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000003") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000004") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000005") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000006"))) {
                                                if (getValidateObservation(observationItemsList)) {
                                                    closeObservation.setVisibility(View.VISIBLE);
                                                } else {
                                                    closeObservation.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                    if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason().equalsIgnoreCase("Accident") && observationHeader.getStatus().equalsIgnoreCase("open")) {
                                        item_header_edit.setVisibility(View.GONE);
                                    }

                                } else {
                                    progressDialog.dismiss();
                                    showAlert(observationDetailsResponse.getErrors() + "");
                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            showAlert(response.message() + "");
                        }

                    }

                    public void onFailure(Call<ObservationDetailsResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("LoginResponse", t.getMessage() + "");
                    }
                });
            } else {
                Utilities.showAlertDialog("Please check your internet connection", ObservationDetailsActivity.this);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean getValidateObservation(List<ObservationItemsDetailsModel> observationItemsList) {

        boolean flag = true;
        for (int i = 0; i < observationItemsList.size(); i++) {
            if (observationItemsList.get(i).getItemsActionDetailsModels() != null && observationItemsList.get(i).getItemsActionDetailsModels().size() > 0) {
                for (int j = 0; j < observationItemsList.get(i).getItemsActionDetailsModels().size(); j++) {
                    if (observationItemsList.get(i).getItemsActionDetailsModels().get(j).getStatus() != null && !observationItemsList.get(i).getItemsActionDetailsModels().get(j).getStatus().equalsIgnoreCase("Closed")) {
                        flag = false;
                        break;
//                    return false;
                    }
                }
            } else {
                flag = false;
                break;
            }

        }
        return flag;

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("ClickPosition", position + "");
//        Log.d("Reason", observationHeader.getReason());
        Intent itemeditintent;
        try {
            itemposition = position;
            if (observationHeader.getReason().equalsIgnoreCase("Accident")) {
                itemeditintent = new Intent(ObservationDetailsActivity.this, IncidentActivity.class);
            } else if (observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                itemeditintent = new Intent(ObservationDetailsActivity.this, HazardActivity.class);
            } else {
                itemeditintent = new Intent(ObservationDetailsActivity.this, NearMissActivity.class);
            }

            Bundle b = new Bundle();
            b.putSerializable("HeaderObject", (Serializable) observationHeader);
            b.putInt("position", position);
            itemeditintent.putExtras(b);
            startActivity(itemeditintent);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void initialisation() {
        try {

            edit_customer_name = findViewById(R.id.edit_customer_name);
            observation_header_text = findViewById(R.id.observation_header);
            obsrId = (EditText) findViewById(R.id.obsrId);
            typeLayout = (TextInputLayout) findViewById(R.id.typeLayout);
            edit_site_engineer_name = findViewById(R.id.edit_site_engineer_name);
            edit_incident_date = findViewById(R.id.edit_incident_date);
            edit_incident_time = findViewById(R.id.edit_incident_time);
            edit_sap_number = findViewById(R.id.edit_sap_number);
            edit_site_name = findViewById(R.id.edit_site_name);
            edit_address = findViewById(R.id.edit_address);
            item_header_edit = findViewById(R.id.item_header_edit);
            edit_email_id = findViewById(R.id.edit_email_id);
            edit_phone_number = findViewById(R.id.edit_phone_number);
            edit_other_responsible_persons = findViewById(R.id.edit_other_responsible_persons);
            edit_pf_number = findViewById(R.id.edit_pf_number);
            edit_site_engineer_email = findViewById(R.id.edit_site_engineer_email);
            edit_site_engineer_phone = findViewById(R.id.edit_site_engineer_phone);
            incident_information_text = findViewById(R.id.incident_information_text);


            employee_name = findViewById(R.id.employee_name);
            employee_id = findViewById(R.id.employee_id);
            employee_email_id = findViewById(R.id.employee_email_id);
            employee_phone_number = findViewById(R.id.employee_phone_number);
            delete_observation = findViewById(R.id.delete_observation);


            observation_items_recyclerview = findViewById(R.id.observation_items_recyclerview);
            editDetails = (ImageView) findViewById(R.id.edit);
            back = findViewById(R.id.back);
            edit_button = findViewById(R.id.edit_button);
            statusButton = findViewById(R.id.statusButton);
            closeObservation = findViewById(R.id.closeObservation);
            edit_zone = findViewById(R.id.edit_zone);
            edit_branch = findViewById(R.id.edit_branch);
            concern_engineer_information_text = findViewById(R.id.concern_engineer_information_text);
            edit_concern_engineer_name = findViewById(R.id.edit_concern_engineer_name);
            edit_concern_pf_number = findViewById(R.id.edit_concern_pf_number);
            customer_information_textview = findViewById(R.id.customer_information_textview);
            employee_information_text = findViewById(R.id.employee_information_text);
            site_engineer_information_text = findViewById(R.id.site_engineer_information_text);
            obsr_id = findViewById(R.id.textview_obsr_id);
//            observation_header = findViewById(R.id.observation_header);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setFonts() {
        try {
            obsr_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_customer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_email_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_phone_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            obsrId.setTypeface(Utilities.fontBold(getApplicationContext()));
//            obsrId.set
            employee_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_email_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_phone_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_other_responsible_persons.setTypeface(Utilities.fontBold(getApplicationContext()));

            edit_concern_engineer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_concern_pf_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_concern_engineer_name.setFocusable(false);

            edit_incident_date.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_incident_time.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_sap_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_site_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_address.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_zone.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_branch.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_site_engineer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_pf_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_site_engineer_email.setTypeface(Utilities.fontBold(getApplicationContext()));

//            edit_concern_engineer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
//            edit_concern_pf_number.setTypeface(Utilities.fontBold(getApplicationContext()));
//            edit_concern_engineer_name.setFocusable(false);
//        edit_site_engineer_phone.setTypeface(Utilities.fontRegular(getApplicationContext()));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status != null)
            getObservationDetails();
    }

    @Override
    public void onBackPressed() {
        // do nothing.
      /*  Intent intent = new Intent(ObservationDetailsActivity.this, ObservationViewActivity.class);
        Log.i("Hello", "This is Coomon Log");
        startActivity(intent);*/
        finish();
        return;
    }

}
