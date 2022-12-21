package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.voltassafety.model.BranchObservationstatus;
import com.ags.voltassafety.model.ObservationReportDetailsResult;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class DetailObservationReport extends AppCompatActivity {
    List<ObservationReportDetailsResult> resultList;
    PieChart piechart;
    ImageView back;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_observation_report);
     /*   piechart = findViewById(R.id.piechart);
        back = findViewById(R.id.back);
        toolbar_title = findViewById(R.id.toolbar_title);
        if (getIntent().getExtras().getString("title").equalsIgnoreCase("Hazard Report")) {
            toolbar_title.setText("Hazard Report");
        }
        resultList = ((ArrayList<ObservationReportDetailsResult>) getIntent().getSerializableExtra("observationbrachstatus"));

*/
    }
}
