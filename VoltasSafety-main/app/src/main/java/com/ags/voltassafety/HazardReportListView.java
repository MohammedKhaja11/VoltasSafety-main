package com.ags.voltassafety;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.HazardObservationListViewAdapter;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardStatusWiseReportResponse;
import com.ags.voltassafety.model.HazardStatusWiseReportResult;
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

import static com.ags.voltassafety.ObservationReportListView.alertDialog;

public class HazardReportListView extends BaseActivity {
    RecyclerView observation_recycler_view;
    ImageView back;
    HazardBranchModel hazardBranchModelrequest;
        TextView toolbar_title;
    List<HazardStatusWiseReportResult> observationDataArrayList;

    HazardObservationListViewAdapter hazardObservationListViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_report_list_view);
        observation_recycler_view = findViewById(R.id.observation_recycler_view);
        toolbar_title = findViewById(R.id.toolbar_title);
        back = findViewById(R.id.back);



        if(getIntent().getExtras().getString("title").equalsIgnoreCase("Incident Report")){
            toolbar_title.setText("Incident Report");
        }else{
            toolbar_title.setText("Hazard Report");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HazardReportListView.this);
        observation_recycler_view.setLayoutManager(linearLayoutManager);


        hazardBranchModelrequest=(HazardBranchModel) getIntent().getSerializableExtra("observationreportInput");



        if (Utilities.isConnectingToInternet(this)) {
            getObservationViewList();
        } else {
            Utilities.showAlertDialog("Please Check Your Internet Connection", HazardReportListView.this);
        }

    }

    private void getObservationViewList() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<HazardStatusWiseReportResponse> call = apiInterface.getBranchObservationStatusReportDetails("Bearer " + sharedPreferences.getString("Bearertoken", null), hazardBranchModelrequest);
            showProgressDialog(HazardReportListView.this);
            observationDataArrayList = new ArrayList<>();

            Gson gson = new Gson();
            String json = gson.toJson(hazardBranchModelrequest);


            Log.d("FinalRequest",json);

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
                                hazardObservationListViewAdapter = new HazardObservationListViewAdapter(HazardReportListView.this, observationDataArrayList );
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

                            alertDialog = new AlertDialog.Builder(HazardReportListView.this, R.style.MyDialogTheme);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("No data to display");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(HazardReportListView.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();

                        }
                    } else {
                        dismissProgress();
                        Utilities.showAlertDialog("No data to display", HazardReportListView.this);
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
    }
}
