package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.ags.voltassafety.model.BranchObservation;
import com.ags.voltassafety.model.EscalationResponse;
import com.ags.voltassafety.model.EscalationResult;
import com.ags.voltassafety.model.ObservationReportInput;

import java.util.ArrayList;
import java.util.List;

public class EscalationReportList extends AppCompatActivity {

    RecyclerView escalation_report_recycler;
    EscalationResponse escalationResponse;
    List<EscalationResult> escalationResultList;
    EscalationResult escalationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalation_report_list);

        //escalationResult = (EscalationResult) getIntent().getExtras().getSerializable("reportsResult");

        escalation_report_recycler = findViewById(R.id.escalation_report_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        escalation_report_recycler.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        escalationResultList = new ArrayList<>();

        escalationResultList = (List<EscalationResult>) getIntent().getExtras().getSerializable("reportsResult");

        EscalationListAdapter customAdapter = new EscalationListAdapter(EscalationReportList.this, escalationResultList);
        escalation_report_recycler.setAdapter(customAdapter); // set the Adapter to RecyclerView

    }
}
