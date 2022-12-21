package com.ags.voltassafety;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ags.voltassafety.model.BranchObservationstatus;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchObservationStatusResponse;
import com.ags.voltassafety.model.ObservationReportDetailsInput;
import com.ags.voltassafety.model.ObservationReportSummaryResult;
import com.ags.voltassafety.model.ObservationStatusReportResult;
import com.ags.voltassafety.model.Observationsstatus;
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
import com.github.mikephil.charting.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HazardObservationReport extends AppCompatActivity {


    ArrayList<String> namesAL = new ArrayList<>();
    ArrayList<Integer> valuesAL = new ArrayList<>();
    TextView toolbar_title;

    PieChart piechart;
    ImageView back;
    private AlertDialog.Builder mAlertDialog;
    ArrayList<BranchObservationstatus> branchObservationstatuses;
    HazardBranchObservationStatusResponse hazardBranchObservationStatusResponse;
    HazardBranchModel hazardBranchModel;
    int total = 0;
    List<Observationsstatus> resultList;
    List<ObservationReportSummaryResult> observationReportSummaryList;
    ObservationStatusReportResult observationReportDetailsResult;
    ObservationReportDetailsInput observationReportDetailsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_observation_report);

        piechart = findViewById(R.id.piechart);
        back = findViewById(R.id.back);
        toolbar_title = findViewById(R.id.toolbar_title);

        if (getIntent().getExtras().getString("Title").equalsIgnoreCase("Hazard Report")) {
            toolbar_title.setText("Hazard Report");
        } else if (getIntent().getExtras().getString("Title").equalsIgnoreCase("Near Miss Report")) {
            toolbar_title.setText("Near Miss Report");
        } else {
            toolbar_title.setText("Accident Report");
        }
        toolbar_title.setTypeface(Utilities.fontBold(HazardObservationReport.this));

        resultList = ((ArrayList<Observationsstatus>) getIntent().getSerializableExtra("reportsResultStatus"));
        observationReportDetailsInput = (ObservationReportDetailsInput) getIntent().getSerializableExtra("observationReportDetailsInput");
        ;
        observationReportDetailsResult = (ObservationStatusReportResult) getIntent().getSerializableExtra("reportsResultCount");
//        observationReportSummaryList = ((ArrayList<ObservationReportSummaryResult>) getIntent().getSerializableExtra("reportsCountResult"));

//        Log.d("Hazard", observationReportSummaryList.toString());
       /* for (int i = 0; i < observationReportSummaryList.size(); i++) {
            if (observationReportSummaryList.get(i).getName().equalsIgnoreCase("Hazard")) {
                namesAL.add(observationReportSummaryList.get(i).getName());
                valuesAL.add(observationReportSummaryList.get(i).getOpen());
                total = total + observationReportSummaryList.get(i).getAll();
            }
        }*/

        for (int i = 0; i < resultList.size(); i++) {
//            if (resultList.get(i).getObservationsStatus().get(i).getCount()) {
            namesAL.add(resultList.get(i).getName() + "");
            valuesAL.add(resultList.get(i).getCount());
//            total = total + observationReportSummaryList.get(i).getAll();
            total = observationReportDetailsResult.getResult().getTotalCount();
//            }
        }
     /*   for (int i = 0; i < observationReportSummaryList.size(); i++) {
//            if (observationReportSummaryList.getResult().getBranchObservationStatus().get(i).getName().equalsIgnoreCase("Assigned")) {
            namesAL.add(observationReportSummaryList.get(i).getName());
            valuesAL.add(observationReportSummaryList.get(i).getOpen());
            total = total + observationReportSummaryList.get(i).getAll();
//            }
        }
        for (int i = 0; i < observationReportSummaryList.size(); i++) {
//            if (observationReportSummaryList.getResult().getBranchObservationStatus().get(i).getName().equalsIgnoreCase("Closed")) {
            namesAL.add(observationReportSummaryList.get(i).getName());
            valuesAL.add(observationReportSummaryList.get(i).getOpen());
            total = total + observationReportSummaryList.get(i).getAll();
//            }
        }*/


//        hazardBranchModel = (HazardBranchModel) getIntent().getSerializableExtra("observationbranchstatusrequest");

//        for (int i = 0; i < branchObservationstatuses.size(); i++) {
//            namesAL.add(branchObservationstatuses.get(i).getName());
//            valuesAL.add(branchObservationstatuses.get(i).getCount());
//        }

       /* for (int i = 0; i < hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().size(); i++) {
            if (hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getName().equalsIgnoreCase("Open")) {
                namesAL.add(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getName());
                valuesAL.add(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getCount());
                total = total + hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getCount();
            }
        }
        for (int i = 0; i < hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().size(); i++) {
            if (hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getName().equalsIgnoreCase("Assigned")) {
                namesAL.add(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getName());
                valuesAL.add(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getCount());
                total = total + hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getCount();
            }
        }
        for (int i = 0; i < hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().size(); i++) {
            if (hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getName().equalsIgnoreCase("Closed")) {
                namesAL.add(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getName());
                valuesAL.add(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getCount());
                total = total + hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus().get(i).getCount();
            }
        }*/
        piechart.setCenterText("Total " + total);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        piechart.setUsePercentValues(true);
        piechart.setDescription("");

        piechart.setExtraOffsets(5, 10, 5, 5);
        piechart.setDragDecelerationFrictionCoef(0.5f);

        //Utils.init(HazardObservationReport.this.getResources());

        piechart.setDrawSlicesUnderHole(true);
        piechart.setDrawCenterText(true);
//        piechart.setCenterText("Total " + observationReportResult.getTotal());
        piechart.setCenterTextSize(16f);
        piechart.setCenterTextTypeface(Utilities.fontBold(HazardObservationReport.this));
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


        setData(namesAL, valuesAL);


        Legend l = piechart.getLegend();
        //l.setEnabled(false);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setXEntrySpace(2f);
        l.setYEntrySpace(2f);
        l.setYOffset(30f);
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(100);
        //l.setXOffset(0f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextColor(Color.WHITE);
        l.setTextSize(14f);
        l.setTypeface(Utilities.fontRegular(HazardObservationReport.this));
        l.setFormSize(8f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if (namesAL != null) {
            ArrayList<String> titles = new ArrayList<String>();
            // String[] titles = new String[names.length];
            for (int i = 0; i < namesAL.size(); i++) {
                // titles[i]= names[i] + "(" + values[i] + " cr)";
                if (namesAL.get(i).equalsIgnoreCase("Open")) {
                   // colors.add(Color.GREEN);
                    colors.add(Color.RED);
                } else if (namesAL.get(i).equalsIgnoreCase("Assigned")) {
                    colors.add(Color.parseColor("#FF8800"));
                } else {
                   // colors.add(Color.RED);
                    colors.add(Color.GREEN);
                    //colors.add(Color.GREEN);
                }

                if (valuesAL.get(i) != 0) {
                    titles.add(i, namesAL.get(i) + "(" + valuesAL.get(i) + ")");
                } else {
                    titles.add(i, namesAL.get(i) + "(" + 0.0 + ")");
                }
            }
            l.setCustom(colors, titles);
        }
        /*Legend l = piechart.getLegend();
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
        l.setTypeface(Utilities.fontRegular(HazardObservationReport.this));
        l.setFormSize(10f);*/


        piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {

                String typeName = namesAL.get(entry.getXIndex());
                Log.d("TypeNAme", typeName);
//                observationReportDetailsInput.setStatus(typeName);

                Intent reportdataviewinList = new Intent(HazardObservationReport.this, UpdatedObservationReportListView.class);

                Bundle bd = new Bundle();
                bd.putString("AlertType", getIntent().getExtras().getString("Title"));
                bd.putSerializable("reportType", (Serializable) typeName);
                observationReportDetailsInput.setAlertStatus(typeName);
                if (getIntent().getExtras().getString("Title").equalsIgnoreCase("Hazard Report")) {
                    observationReportDetailsInput.setAlertType("Hazard");
                } else if (getIntent().getExtras().getString("Title").equalsIgnoreCase("Near Miss Report")) {
                    observationReportDetailsInput.setAlertType("Near Miss");
                } else {
                    observationReportDetailsInput.setAlertType("Accident");
                }
                bd.putSerializable("observationreportInput", (Serializable) observationReportDetailsInput);
//                bd.putString("title", toolbar_title.getText().toString());
                reportdataviewinList.putExtras(bd);

                startActivity(reportdataviewinList);


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
            Utils.init(getResources());

            ArrayList<String> xVals = new ArrayList<String>();

            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int i = 0; i < names.size(); i++) {
                xVals.add(names.get(i));
                if (names.get(i).equalsIgnoreCase("Open")) {
                   // colors.add(Color.GREEN);
                    colors.add(Color.RED);
                } else if (names.get(i).equalsIgnoreCase("Assigned")) {
                    colors.add(Color.parseColor("#FF8800"));
                } else if (names.get(i).equalsIgnoreCase("Closed")) {
                   // colors.add(Color.RED);
                    colors.add(Color.GREEN);
                }
            }
            PieDataSet dataSet = new PieDataSet(yVals1, "");
            dataSet.setSliceSpace(2f);
            dataSet.setSelectionShift(5f);
            dataSet.setHighlightEnabled(true); // allow highlighting for DataSet

            // add a lot of colors

           /* ArrayList<Integer> colors = new ArrayList<Integer>();

            colors.add(Color.GREEN);
            colors.add(Color.parseColor("#FF8800"));
            colors.add(Color.RED);
*/
            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setDrawValues(true);
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Utilities.fontRegular(HazardObservationReport.this));

            piechart.setData(data);
            // undo all highlights
            //piechart.highlightValues(null);
            Highlight[] highlights = piechart.getHighlighted();
            piechart.highlightValues(highlights);

            piechart.invalidate();
            //  return data;
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

