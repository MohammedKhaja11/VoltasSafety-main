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
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.voltassafety.adapters.ReportsAdapter;
import com.ags.voltassafety.model.ObservationReportSummaryResponse;
import com.ags.voltassafety.model.ObservationReportSummaryResult;
import com.ags.voltassafety.model.ObservationReportSummaryStatus;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsActivity extends BaseActivity {

    ArrayList<String> plans;
    RecyclerView recycler_listitem;
    ReportsAdapter reportsAdapter;
    ImageView back;
    List<ObservationReportSummaryStatus> observationReportSummaryStatusList;
    List<ObservationReportSummaryResult> observationReportSummaryList;
    ObservationReportSummaryResponse observationReportSummaryResponse;
    private TextView toolbar_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        recycler_listitem = findViewById(R.id.recycler_listitem);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Utilities.fontBold(ReportsActivity.this));
        back = findViewById(R.id.back);

        getObservationsReportSummary();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        plans = new ArrayList<>();
        // plans.add("Observation Report");
        plans.add("Hazard Report");
        plans.add("Near Miss Report");
        plans.add("Accident Report");
//        plans.add("Escalation Report");
//        plans.add("Accident Report");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_listitem.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView


    }

    private void getObservationsReportSummary() {
        try {
            observationReportSummaryStatusList = new ArrayList<>();
            observationReportSummaryList = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationReportSummaryResponse> call = apiInterface.getObservationsReportSummary("Bearer " + sharedPreferences.getString("Bearertoken", null));
            ProgressDialog progressDialog = new ProgressDialog(ReportsActivity.this, R.style.MyDialogTheme);
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

                                //observationReportSummaryStatusList.addAll(observationReportSummaryResponse.getResult().get(i).getStatusObservations());
                                for (int i = 1; i < response.body().getResult().size(); i++) {
                                    observationReportSummaryList.add(response.body().getResult().get(i));
                                }

                                //  observationReportSummaryList.addAll(response.body().getResult());

                                reportsAdapter = new ReportsAdapter(ReportsActivity.this, plans, observationReportSummaryList);
                                recycler_listitem.setAdapter(reportsAdapter);

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
    }


}
