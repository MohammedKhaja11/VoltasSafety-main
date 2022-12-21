package com.ags.voltassafety;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ags.voltassafety.model.ObservationReportInput;
import com.ags.voltassafety.model.ObservationReportResponse;
import com.ags.voltassafety.model.ObservationReportResult;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObservationReportView extends BaseActivity {

    //List<ObservationReportResult> observationReportResults = new ArrayList<>();
    ObservationReportResult observationReportResult;
    ObservationReportInput observationReportInput;
    //    ArrayList<Integer> hazard= new ArrayList<Integer>();
//    ArrayList<Integer> incident= new ArrayList<>();
    private Button typeWiseReport, statusWiseReport;

    ArrayList<String> namesAL = new ArrayList<>();
    ArrayList<Integer> valuesAL = new ArrayList<>();

    PieChart piechart;
    ImageView back;
    private AlertDialog.Builder mAlertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_report_view);
        typeWiseReport = (Button) findViewById(R.id.type);
        statusWiseReport = (Button) findViewById(R.id.status);
        typeWiseReport.setTypeface(Utilities.fontBold(ObservationReportView.this));
        statusWiseReport.setTypeface(Utilities.fontBold(ObservationReportView.this));
        statusWiseReport.setBackgroundResource(R.drawable.status_bg_checked);
        final Intent intent = getIntent();

//        observationReportResult = (ObservationReportResult) intent.getExtras().getSerializable("reportsobject");
        observationReportInput = (ObservationReportInput) intent.getExtras().getSerializable("reportsInput");
        observationReportInput.setReportType("Status");
        getReport();
        piechart = findViewById(R.id.piechart);
        back = findViewById(R.id.back);

//        typeWiseReport.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    typeWiseReport.setBackgroundResource(R.drawable.type_bg_checked);
//                    statusWiseReport.setBackgroundResource(R.drawable.status_bg_unchecked);
//                    typeWiseReport.setTextColor(getResources().getColor(R.color.white));
//                    statusWiseReport.setTextColor(getResources().getColor(R.color.black));
//                    observationReportInput.setReportType("Type");
//                    getTypeReport();
//
//                }
//                return false;
//            }
//        });
//
//
//        statusWiseReport.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                typeWiseReport.setBackgroundResource(R.drawable.type_bg_unchecked);
//                statusWiseReport.setBackgroundResource(R.drawable.status_bg_checked);
//                statusWiseReport.setTextColor(getResources().getColor(R.color.white));
//                typeWiseReport.setTextColor(getResources().getColor(R.color.black));
//                observationReportInput.setReportType("Status");
//                getTypeReport();
//                return false;
//            }
//        });
        typeWiseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeWiseReport.setBackgroundResource(R.drawable.type_bg_checked);
                statusWiseReport.setBackgroundResource(R.drawable.status_bg_unchecked);
                typeWiseReport.setTextColor(getResources().getColor(R.color.white));
                statusWiseReport.setTextColor(getResources().getColor(R.color.black));
                observationReportInput.setReportType("Type");
                getReport();

            }
        });
        statusWiseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeWiseReport.setBackgroundResource(R.drawable.type_bg_unchecked);
                statusWiseReport.setBackgroundResource(R.drawable.status_bg_checked);
                statusWiseReport.setTextColor(getResources().getColor(R.color.white));
                typeWiseReport.setTextColor(getResources().getColor(R.color.black));
                observationReportInput.setReportType("Status");
                getReport();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



      /*  namesAL.add("Total");
        valuesAL.add(observationReportResult.getTotal());*/

        //Gson gson = new Gson();
        //String json = gson.toJson(observationReportResult);

        //Log.d("ReportResponse", json);


        piechart.setUsePercentValues(true);
        piechart.setDescription("");

        piechart.setExtraOffsets(5, 10, 5, 5);
        piechart.setDragDecelerationFrictionCoef(0.5f);

        Utils.init(ObservationReportView.this.getResources());

        piechart.setDrawSlicesUnderHole(true);
        piechart.setDrawCenterText(true);
//        piechart.setCenterText("Total " + observationReportResult.getTotal());
        piechart.setCenterTextSize(16f);
        piechart.setCenterTextTypeface(Utilities.fontBold(ObservationReportView.this));
        piechart.setRotationAngle(0);

        piechart.setRotationEnabled(false);
        piechart.setDrawSliceText(false);
        piechart.highlightValue(null);
        piechart.setDrawHoleEnabled(true);


        // enable rotation of the chart by touch
        piechart.setRotationEnabled(false);
        piechart.setDrawSliceText(false);
        //piechart.highlightValue(null);
        piechart.setClickable(true);

        // mChart.setOnChartValueSelectedListener(MajorOrderReportFragment.this);
        piechart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        Legend l = piechart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setXEntrySpace(2f);
        l.setYEntrySpace(2f);
        l.setYOffset(20f);
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(100);
        //l.setXOffset(0f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextColor(Color.WHITE);
        l.setTextSize(16f);
        l.setTypeface(Utilities.fontRegular(ObservationReportView.this));
        l.setFormSize(10f);


        piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {

                String typeName = namesAL.get(entry.getXIndex());
                Log.d("TypeNAme", typeName);
                observationReportInput.setCriteria(typeName);

                Intent reportdataviewinList = new Intent(ObservationReportView.this, ObservationReportListView.class);

                Bundle bd = new Bundle();
                bd.putSerializable("reportType", (Serializable) typeName);
                bd.putSerializable("observationreportInput", (Serializable) observationReportInput);
                reportdataviewinList.putExtras(bd);

                startActivity(reportdataviewinList);


//                Fragment fragment = null;
//                fragment = new DealersReportListFragment();
                //replacing the fragment
//                if (fragment != null) {
//
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, fragment).addToBackStack("landingPage");
//                    //ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Filter", objInputData);
//                    fragment.setArguments(bundle);
//                }
                //    Log.i("VAL SELECTED","Value: " + entry.getVal() + ", xIndex: " + entry.getXIndex()+ ", DataSet index: " + i);

                return;
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setData(ArrayList<String> names, ArrayList<Integer> values) {

        try {
            //loat mult = range;
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            // IMPORTANT: In a PieChart, no values (Entry) should have the same
            // xIndex (even if from different DataSets), since no values can be
            // drawn above each other.
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) != 0) {
                    yVals1.add(new Entry(values.get(i), i));
                } else {
                    yVals1.add(new Entry(Float.parseFloat("0.0"), i));
                }
            }
            //Utils.init(getActivity().getResources());

            ArrayList<String> xVals = new ArrayList<String>();

            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int i = 0; i < names.size(); i++) {
                xVals.add(names.get(i));
                if (names.get(i).equalsIgnoreCase("Open")) {
                    colors.add(Color.GREEN);
                } else if (names.get(i).equalsIgnoreCase("Assigned")) {
                    colors.add(Color.parseColor("#FF8800"));
                } else if (names.get(i).equalsIgnoreCase("Closed")) {
                    colors.add(Color.RED);
                }
                else if(names.get(i).equalsIgnoreCase("Hazard")){
                    colors.add(Color.GREEN);
                }
                else{
                    colors.add(Color.parseColor("#FF8800"));
                }
            }

            PieDataSet dataSet = new PieDataSet(yVals1, "");
            dataSet.setSliceSpace(2f);
            dataSet.setSelectionShift(5f);
            dataSet.setHighlightEnabled(true); // allow highlighting for DataSet

            // add a lot of colors

           /* ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);
            colors.add(Color.RED);
            colors.add(Color.parseColor("#FF8800"));
            colors.add(Color.GREEN);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);*/
            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setDrawValues(true);
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Utilities.fontRegular(ObservationReportView.this));

            piechart.setData(data);
            // undo all highlights
            //piechart.highlightValues(null);
            Highlight[] highlights = piechart.getHighlighted();
            piechart.highlightValues(highlights);

            Legend l = piechart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setXEntrySpace(2f);
            l.setYEntrySpace(2f);
            l.setYOffset(20f);
            l.setWordWrapEnabled(true);
            l.setMaxSizePercent(100);
            //l.setXOffset(0f);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            //l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setTextColor(Color.WHITE);
            l.setTextSize(14f);
            l.setTypeface(Utilities.fontRegular(ObservationReportView.this));
            l.setFormSize(8f);

            if (names != null) {
                ArrayList<String> titles = new ArrayList<String>();
                // String[] titles = new String[names.length];
                for (int i = 0; i < names.size(); i++) {
                    // titles[i]= names[i] + "(" + values[i] + " cr)";
                    if (values.get(i) != 0) {
                        titles.add(i, names.get(i) + "(" + values.get(i) + ")");
                    } else {
                        titles.add(i, names.get(i) + "(" + 0.0 + ")");
                    }
                }
                l.setComputedLabels(titles);
            }
            piechart.invalidate();
            //  return data;
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void getReport() {
        if (Utilities.isConnectingToInternet(ObservationReportView.this)) {

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Gson gson = new Gson();
            String json = gson.toJson(observationReportInput);

            Log.d("ReportRequest", json);

            Call<ObservationReportResponse> call = apiInterface.observationReportResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), observationReportInput);
            showProgressDialog(ObservationReportView.this);
            call.enqueue(new Callback<ObservationReportResponse>() {
                public void onResponse(Call<ObservationReportResponse> call, Response<ObservationReportResponse> response) {
                    if (response.isSuccessful()) {
                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                dismissProgress();
                            }
                        };

                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 1000);
                        try {
                            namesAL.clear();
                            valuesAL.clear();


                            if (response.body() != null) {
                                ObservationReportResponse loginResponse = response.body();
                                Log.d("reportresponse", loginResponse.toString());

                                if (loginResponse.getSuccess()) {
                                    if (response.body().getResult() != null) {
                                        try {
                                            observationReportResult = response.body().getResult();

                                            if (observationReportResult != null) {

                                                if (observationReportInput.getReportType().equalsIgnoreCase("Status")) {
                                                    for (int i = 0; i < observationReportResult.getObservationStatus().size(); i++) {
                                                        if(observationReportResult.getObservationStatus().get(i).getName().equalsIgnoreCase("Open")) {
                                                            namesAL.add(observationReportResult.getObservationStatus().get(i).getName());
                                                            valuesAL.add(observationReportResult.getObservationStatus().get(i).getCount());
                                                        }
                                                    }
                                                    for (int i = 0; i < observationReportResult.getObservationStatus().size(); i++) {
                                                        if(observationReportResult.getObservationStatus().get(i).getName().equalsIgnoreCase("Assigned")) {
                                                            namesAL.add(observationReportResult.getObservationStatus().get(i).getName());
                                                            valuesAL.add(observationReportResult.getObservationStatus().get(i).getCount());
                                                        }
                                                    }
                                                    for (int i = 0; i < observationReportResult.getObservationStatus().size(); i++) {
                                                        if(observationReportResult.getObservationStatus().get(i).getName().equalsIgnoreCase("Closed")) {
                                                            namesAL.add(observationReportResult.getObservationStatus().get(i).getName());
                                                            valuesAL.add(observationReportResult.getObservationStatus().get(i).getCount());
                                                        }
                                                    }

                                                    piechart.setCenterText("Total " + observationReportResult.getObservationStatusTotal());
                                                } else {
                                                    for (int i = 0; i < observationReportResult.getObservationTypes().size(); i++) {
                                                        namesAL.add(observationReportResult.getObservationTypes().get(i).getName());
                                                        valuesAL.add(observationReportResult.getObservationTypes().get(i).getCount());
                                                    }

                                                    piechart.setCenterText("Total " + observationReportResult.getObservationTypesTotal());
                                                }

                                                setData(namesAL, valuesAL);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        mAlertDialog = new AlertDialog.Builder(ObservationReportView.this, R.style.MyDialogTheme);
                                        mAlertDialog.setTitle("Information");
                                        mAlertDialog.setMessage("No data to generate report");
                                        mAlertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        mAlertDialog.show();
                                    }

                                } else {
                                    dismissProgress();
                                    showAlert(loginResponse.getErrors().get(0));
                                    //showToast(loginResponse.getErrors() + "");
                                }
                            } else {
                                dismissProgress();
                                Utilities.showAlertDialog(response.body().getErrors().get(0), ObservationReportView.this);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.message());
                        Utilities.showAlertDialog("Error " + response.message(), ObservationReportView.this);
                    }
                }

                public void onFailure(Call<ObservationReportResponse> call, Throwable t) {
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } else {
            showAlert("Please Check Your Internet Connection");
        }
    }
}

