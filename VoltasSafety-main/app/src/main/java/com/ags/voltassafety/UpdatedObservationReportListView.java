package com.ags.voltassafety;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.HazardObservationListViewAdapter;
import com.ags.voltassafety.adapters.ObservationListViewAdapter;
import com.ags.voltassafety.model.HazardStatusWiseReportResult;
import com.ags.voltassafety.model.ObservationReportDetailsInput;
import com.ags.voltassafety.model.ObservationReportDetailsResponse;
import com.ags.voltassafety.model.ObservationReportDetailsResult;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatedObservationReportListView extends BaseActivity {
    RecyclerView observation_recycler_view;
    ImageView back;
    ObservationReportDetailsInput observationReportDetailsInput;
    TextView toolbar_title;
    List<HazardStatusWiseReportResult> observationDataArrayList;

    HazardObservationListViewAdapter hazardObservationListViewAdapter;

    List<ObservationReportDetailsResult> resultList;
    ProgressDialog progressDialog;
    ObservationReportDetailsResponse observationReportDetailsResponse;
    ObservationListViewAdapter customAdapter;
    String alertType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_report_list_view);
        observation_recycler_view = findViewById(R.id.observation_recycler_view);
        toolbar_title = findViewById(R.id.toolbar_title);
        back = findViewById(R.id.back);
        alertType = getIntent().getExtras().getString("AlertType");

        if (getIntent().getExtras().getString("AlertType").equalsIgnoreCase("Near Miss Report")) {
            toolbar_title.setText("Near Miss Report");
        } else if (getIntent().getExtras().getString("AlertType").equalsIgnoreCase("Hazard Report")) {
            toolbar_title.setText("Hazard Report");
        } else {
            toolbar_title.setText("Accident Report");
        }
        toolbar_title.setTypeface(Utilities.fontBold(UpdatedObservationReportListView.this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdatedObservationReportListView.this);
        observation_recycler_view.setLayoutManager(linearLayoutManager);


        observationReportDetailsInput = (ObservationReportDetailsInput) getIntent().getSerializableExtra("observationreportInput");


        if (Utilities.isConnectingToInternet(this)) {
            getObservationReportDetails();
        } else {
            Utilities.showAlertDialog("Please Check Your Internet Connection", UpdatedObservationReportListView.this);
        }

    }

   /* private void getObservationViewList() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<HazardStatusWiseReportResponse> call = apiInterface.getBranchObservationStatusReportDetails("Bearer " + sharedPreferences.getString("Bearertoken", null), observationReportDetailsInput);
            showProgressDialog(UpdatedObservationReportListView.this);
            observationDataArrayList = new ArrayList<>();

            Gson gson = new Gson();
            String json = gson.toJson(observationReportDetailsInput);


            Log.d("FinalRequest", json);

            call.enqueue(new Callback<HazardStatusWiseReportResponse>() {
                public void onResponse(Call<HazardStatusWiseReportResponse> call, Response<HazardStatusWiseReportResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            HazardStatusWiseReportResponse loginResponse = response.body();
                            Log.d("observationlistview", loginResponse.getSuccess() + "");
                            if (loginResponse.getSuccess().equals(true)) {
                                observationDataArrayList.addAll(loginResponse.getResult());

                                Log.d("DataArrayListSize", observationDataArrayList.size() + "");
                                hazardObservationListViewAdapter = new HazardObservationListViewAdapter(UpdatedObservationReportListView.this, observationDataArrayList);
                                observation_recycler_view.setAdapter(hazardObservationListViewAdapter);

                                try {
                                    Thread.sleep(200);
                                    dismissProgress();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                dismissProgress();
                                showAlert(loginResponse.getErrors() + "");
                                //showToast(loginResponse.getErrors() + "");
                            }

                        } else {
                            dismissProgress();
//                        Utilities.showAlertDialog("No data to display", ObservationViewActivity.this);

                            alertDialog = new AlertDialog.Builder(UpdatedObservationReportListView.this, R.style.MyDialogTheme);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("No data to display");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UpdatedObservationReportListView.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();

                        }
                    } else {
                        dismissProgress();
                        Utilities.showAlertDialog("No data to display", UpdatedObservationReportListView.this);
                    }


                }

                public void onFailure(Call<HazardStatusWiseReportResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }*/


    private void getObservationReportDetails() {
        try {
//            setObservationInput();
            resultList = new ArrayList<>();
            Gson gson = new Gson();
            String json = gson.toJson(observationReportDetailsInput);
            Log.d("ReportResponse", json);

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationReportDetailsResponse> call = apiInterface.GetObservationReportDetails("Bearer " + sharedPreferences.getString("Bearertoken", null), observationReportDetailsInput);
            progressDialog = new ProgressDialog(UpdatedObservationReportListView.this, R.style.MyDialogTheme);
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
                            customAdapter = new ObservationListViewAdapter(UpdatedObservationReportListView.this, resultList, alertType);
                            observation_recycler_view.setAdapter(customAdapter);
                         /*   resultList.addAll(observationReportDetailsResponse.getResult());
                            Intent observationreportview = new Intent(UpdatedObservationReportListView.this, ObservationReportListView.class);
                            Bundle bd = new Bundle();
                            bd.putSerializable("reportsResult", (Serializable) resultList);
                            bd.putString("title", "Hazard Report");
                            observationreportview.putExtras(bd);
                            startActivity(observationreportview);*/
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
}
