package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.voltassafety.adapters.ObservationDetailReportViewAdapter;
import com.ags.voltassafety.adapters.ObservationDetailsAdapter;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationDetailsResponse;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

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

public class ObservationDetailReportView extends BaseActivity implements ObservationDetailsAdapter.OnItemClicked {
    EditText edit_incident_date, edit_incident_time, edit_sap_number, edit_site_name, edit_other_responsible_persons, edit_address, edit_concern_engineer_name, edit_concern_pf_number, editZone, editBranch, editObservationId, edit_pf_number, edit_site_engineer_email, edit_customer_name, edit_site_engineer_phone, edit_site_engineer_name, edit_email_id, edit_phone_number, employee_name, employee_id, employee_email_id, employee_phone_number;
    RecyclerView observation_items_recyclerview;
    public TextView concern_engineer_information_text,obsr_id,observation_header_text;
    ObservationDetailReportViewAdapter observationDetailsAdapter;
    List<ObservationItemsDetailsModel> observationItemsList;
    List<ObservationAttachmentModel> observationAttachmentsList;
    TextView toolbar_title, customer_information_textview, incident_information_text, employee_information_text, site_engineer_information_text, observation_header;
    Button edit_button, statusButton, closeObservation;
    ImageView back;
    int itemposition;
    ObservationHeader observationHeader = null;
    private ImageView editDetails;
    String status, observationId, alertType, Zone, branch,observationType,subType;
    public ImageView item_header_edit;
    public ProgressDialog progressDialog;
    public TextInputLayout typeLayout;
    StringBuilder otherEmails;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_observation_detail_report_view);
            initialisation();
            setFonts();
//            getObservationDetails();
            toolbar_title = findViewById(R.id.toolbar_title);
            toolbar_title.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            incident_information_text.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            customer_information_textview.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            employee_information_text.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            site_engineer_information_text.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            concern_engineer_information_text.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            observation_header_text.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
            //SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
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
                obsr_id.setText("Hazard ID: "+observationId);
            }
            if (subType.equalsIgnoreCase("Accident")) {
                typeLayout.setHint("Accident ID");
                incident_information_text.setText("ACCIDENT INFORMATION");
                observation_header_text.setText("ACCIDENT DETAILS");
                obsr_id.setText("Accident ID: "+observationId);
            }
            if (subType.equalsIgnoreCase("Near Miss")) {
                typeLayout.setHint("NearMiss ID");
                incident_information_text.setText("NEAR MISS INFORMATION");
                observation_header_text.setText("NEAR MISS DETAILS");
                obsr_id.setText("NearMiss ID: "+observationId);
            }

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
    }

    private void getObservationDetails() {
        try {
            if (Utilities.isConnectingToInternet(getApplicationContext())) {
                SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                Call<ObservationDetailsResponse> call = apiInterface.getObservationByID("Bearer " + sharedPreferences.getString("Bearertoken", null), getIntent().getStringExtra("observationId"));
                progressDialog = new ProgressDialog(ObservationDetailReportView.this, R.style.MyDialogTheme);
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
//                        dismissProgress();
                                progressDialog.dismiss();
                                ObservationDetailsResponse observationDetailsResponse = response.body();

                                Log.d("observatondetailsresp", response.body().getResult() + "");
                                if (observationDetailsResponse.getSuccess()) {
                                    observationHeader = response.body().getResult();
                                    Log.d("observationgetresult", observationDetailsResponse.getResult() + "");
                                    observationItemsList.addAll(observationDetailsResponse.getResult().getObservationItemsDetailsModels());
                                    for (int i = 0; i < observationItemsList.size(); i++) {
                                        observationAttachmentsList.addAll(observationItemsList.get(i).getObservationAttachmentModels());
                                    }
                                    statusButton.setText(status);
                                    edit_customer_name.setText(response.body().getResult().getCustomerName());
                                    edit_sap_number.setText(response.body().getResult().getSapCustomerId());
                                    edit_site_name.setText(response.body().getResult().getSiteName());
                                    edit_address.setText(response.body().getResult().getAddress());
                                    edit_incident_date.setText(response.body().getResult().getDateOfIncidence());
                                    edit_site_engineer_name.setText(response.body().getResult().getProjectManagerName());
                                    edit_concern_engineer_name.setText(response.body().getResult().getConcernEngineerOrSupervisor());
                                    //edit_pf_number.setText(response.body().getResult().getProjectManagerId());
                                    edit_pf_number.setText(response.body().getResult().getProjectManagerId());
                                    edit_email_id.setText(response.body().getResult().getManagerEmail());
                                    edit_phone_number.setText(response.body().getResult().getManagerPhoneNumber());

                                    employee_name.setText(response.body().getResult().getUserName());
                                    employee_id.setText(response.body().getResult().getUserEmployeeId());
                                    employee_email_id.setText(response.body().getResult().getUserEmail());
                                    employee_phone_number.setText(response.body().getResult().getUserPhoneNumber());
                                    editZone.setText(response.body().getResult().getGuestZone());
                                    editBranch.setText(response.body().getResult().getGuestBranch());
                                    editObservationId.setText(response.body().getResult().getObservationId());
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
                                    if (response.body().getResult().getReason().equalsIgnoreCase("Hazard")) {
                                        toolbar_title.setText(response.body().getResult().getReason() + " Report("+observationId+")");
                                        toolbar_title.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
                                    } else if (response.body().getResult().getReason().equalsIgnoreCase("Near Miss")) {
                                        toolbar_title.setText(response.body().getResult().getReason() + " Report("+observationId+")");
                                        toolbar_title.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
                                    } else {
                                        toolbar_title.setText(response.body().getResult().getReason() + " Report("+observationId+")");
                                        toolbar_title.setTypeface(Utilities.fontBold(ObservationDetailReportView.this));
                                    }


                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//                            String formattedDate = df.format(calendar.getTime());

                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(observationHeader.getDateOfIncidence());
                                    } catch (ParseException e) {
                                        e.getMessage();
                                    }
                                    edit_incident_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
                                    edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(date));

                                    observationDetailsAdapter = new ObservationDetailReportViewAdapter(ObservationDetailReportView.this, observationItemsList, observationAttachmentsList, ObservationDetailReportView.this, observationHeader);
                                    observation_items_recyclerview.setAdapter(observationDetailsAdapter);
                                    observationDetailsAdapter.notifyDataSetChanged();
//                                    if (observationItemsList != null && observationItemsList.size() > 0 && !status.equalsIgnoreCase("Closed") && (sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000001") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000002") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000003") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000004") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000005") || sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000006"))) {
//                                        if (getValidateObservation(observationItemsList)) {
//                                            closeObservation.setVisibility(View.VISIBLE);
//                                        } else {
//                                            closeObservation.setVisibility(View.GONE);
//                                        }
//                                    }

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
                Utilities.showAlertDialog("Please check your internet connection", ObservationDetailReportView.this);
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
                itemeditintent = new Intent(ObservationDetailReportView.this, IncidentActivity.class);
            } else {
                itemeditintent = new Intent(ObservationDetailReportView.this, HazardActivity.class);
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
            edit_other_responsible_persons = findViewById(R.id.edit_other_responsible_persons);
            obsr_id = findViewById(R.id.textview_obsr_id);
            typeLayout = (TextInputLayout) findViewById(R.id.typeLayout);
            edit_customer_name = findViewById(R.id.edit_customer_name);
            edit_site_engineer_name = findViewById(R.id.edit_site_engineer_name);
            edit_incident_date = findViewById(R.id.edit_incident_date);
            edit_incident_time = findViewById(R.id.edit_incident_time);
            edit_sap_number = findViewById(R.id.edit_sap_number);
            edit_site_name = findViewById(R.id.edit_site_name);
            edit_address = findViewById(R.id.edit_address);
            item_header_edit = findViewById(R.id.item_header_edit);
            edit_email_id = findViewById(R.id.edit_email_id);
            edit_phone_number = findViewById(R.id.edit_phone_number);
//            toolbar_title = findViewById(R.id.toolbar_title);
            observation_header_text = findViewById(R.id.observation_header);
            edit_pf_number = findViewById(R.id.edit_pf_number);
            edit_site_engineer_email = findViewById(R.id.edit_site_engineer_email);
            edit_site_engineer_phone = findViewById(R.id.edit_site_engineer_phone);
            toolbar_title = findViewById(R.id.toolbar_title);


            employee_name = findViewById(R.id.employee_name);
            employee_id = findViewById(R.id.employee_id);
            employee_email_id = findViewById(R.id.employee_email_id);
            employee_phone_number = findViewById(R.id.employee_phone_number);


            observation_items_recyclerview = findViewById(R.id.observation_items_recyclerview);
            editDetails = (ImageView) findViewById(R.id.edit);
            back = findViewById(R.id.back);
            edit_button = findViewById(R.id.edit_button);
            statusButton = findViewById(R.id.statusButton);
            closeObservation = findViewById(R.id.closeObservation);

            customer_information_textview = findViewById(R.id.customer_information_textview);
            incident_information_text = findViewById(R.id.incident_information_text);
            employee_information_text = findViewById(R.id.employee_information_text);
            site_engineer_information_text = findViewById(R.id.site_engineer_information_text);
            observation_header = findViewById(R.id.observation_header);
            concern_engineer_information_text = findViewById(R.id.concern_engineer_information_text);
            edit_concern_engineer_name = findViewById(R.id.edit_concern_engineer_name);
            edit_concern_pf_number = findViewById(R.id.edit_concern_pf_number);

            editZone = findViewById(R.id.edit_zone);
            editBranch = findViewById(R.id.edit_branch);
            editObservationId = findViewById(R.id.obsrId);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setFonts() {
        try {
            edit_other_responsible_persons.setTypeface(Utilities.fontBold(getApplicationContext()));
            obsr_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            customer_information_textview.setTypeface(Utilities.fontBold(getApplicationContext()));
            incident_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            site_engineer_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            concern_engineer_information_text.setTypeface(Utilities.fontBold(getApplicationContext()));
            observation_header.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_customer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_email_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_phone_number.setTypeface(Utilities.fontBold(getApplicationContext()));

            employee_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_email_id.setTypeface(Utilities.fontBold(getApplicationContext()));
            employee_phone_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_concern_engineer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_concern_engineer_name.setFocusable(false);
            edit_concern_pf_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_incident_date.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_incident_time.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_sap_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_site_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_address.setTypeface(Utilities.fontBold(getApplicationContext()));
            editZone.setTypeface(Utilities.fontBold(getApplicationContext()));
            editBranch.setTypeface(Utilities.fontBold(getApplicationContext()));
            editObservationId.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_site_engineer_name.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_pf_number.setTypeface(Utilities.fontBold(getApplicationContext()));
            edit_site_engineer_email.setTypeface(Utilities.fontBold(getApplicationContext()));

//            toolbar_title.setTypeface(Utilities.fontBold(getApplicationContext()));


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

//    @Override
//    public void onBackPressed() {
//        // do nothing.
//        Intent intent = new Intent(ObservationDetailReportView.this, ObservationViewActivity.class);
//        Log.i("Hello", "This is Coomon Log");
//        startActivity(intent);
//        return;
//    }

}
