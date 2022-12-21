package com.ags.voltassafety;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ags.voltassafety.adapters.ObservationListViewAdapter;

import com.ags.voltassafety.model.ObservationListViewResponse;
import com.ags.voltassafety.model.ObservationListViewResponseResult;
import com.ags.voltassafety.model.ObservationReportDetailsResult;
import com.ags.voltassafety.model.ObservationReportInput;

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

public class ObservationReportListView extends BaseActivity {

    RecyclerView observation_recycler_view;
    List<ObservationListViewResponseResult> observationDataArrayList;
    ObservationListViewAdapter customAdapter;
    public static AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    ImageView back;

    ObservationReportInput observationListViewRequest;
    List<ObservationReportDetailsResult> resultList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_report_list_view);
        observation_recycler_view = findViewById(R.id.observation_recycler_view);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = getIntent();

        resultList = (ArrayList<ObservationReportDetailsResult>) intent.getExtras().getSerializable("reportsResult");


//        String criteria = String.valueOf(intent.getExtras().getSerializable("reportType"));
//
//        observationListViewRequest = (ObservationReportInput) intent.getExtras().getSerializable("observationreportInput");
//
//        observationListViewRequest.setCriteria(String.valueOf(intent.getExtras().getSerializable("reportType")));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ObservationReportListView.this);
        observation_recycler_view.setLayoutManager(linearLayoutManager);
//        customAdapter = new ObservationListViewAdapter(ObservationReportListView.this, resultList);
//        observation_recycler_view.setAdapter(customAdapter);
//
//        if (Utilities.isConnectingToInternet(this)) {
//            getObservationViewList();
//        } else {
//            Utilities.showAlertDialog("Please Check Your Internet Connection", ObservationReportListView.this);
//        }
//
//
//        Gson gson = new Gson();
//        String json = gson.toJson(observationListViewRequest);
//
//        Log.d("ReportResponse", json);

    }

    private void getObservationViewList() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationListViewResponse> call = apiInterface.observationListviewResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), observationListViewRequest);
            showProgressDialog(ObservationReportListView.this);
            observationDataArrayList = new ArrayList<>();
            call.enqueue(new Callback<ObservationListViewResponse>() {
                public void onResponse(Call<ObservationListViewResponse> call, Response<ObservationListViewResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            ObservationListViewResponse loginResponse = response.body();
                            Log.d("observationlistview", loginResponse.getSuccess() + "");
                            if (loginResponse.getSuccess().equals(true)) {
                                observationDataArrayList.addAll(loginResponse.getResult());

                                Log.d("DataArrayListSize", observationDataArrayList.size() + "");
//                                customAdapter = new ObservationListViewAdapter(ObservationReportListView.this, observationDataArrayList);
//                                observation_recycler_view.setAdapter(customAdapter);

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

                            alertDialog = new AlertDialog.Builder(ObservationReportListView.this, R.style.MyDialogTheme);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("No data to display");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ObservationReportListView.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();

                        }
                    } else {
                        dismissProgress();
                        Utilities.showAlertDialog("No data to display", ObservationReportListView.this);
                    }


                }

                public void onFailure(Call<ObservationListViewResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
