package com.ags.voltassafety;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.HazardBrachAdapter;
import com.ags.voltassafety.model.BranchObservation;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchResponse;

import java.util.ArrayList;

public class HazardBanchReport extends AppCompatActivity {

    RecyclerView branch_recyclerview;
    ArrayList<BranchObservation> branches;
    HazardBrachAdapter hazardBrachAdapter;
    EditText filter_edit;
    TextView total_count;
    HazardBranchResponse hazardBranchResponse;
    ImageView back;
    HazardBranchModel hazardBranchModelrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_banch_report);
        branch_recyclerview = findViewById(R.id.branch_recyclerview);
        filter_edit = findViewById(R.id.filter_edit);
        total_count = findViewById(R.id.total_count);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        total_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        branches = new ArrayList<>();
        branches = (ArrayList<BranchObservation>) getIntent().getSerializableExtra("reportsResult");


        hazardBranchResponse = (HazardBranchResponse) getIntent().getSerializableExtra("reportResponse");
        total_count.setText("" + hazardBranchResponse.getResult().getTotalCount());

        hazardBranchModelrequest = (HazardBranchModel) getIntent().getSerializableExtra("reportRequest");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        branch_recyclerview.setLayoutManager(linearLayoutManager);

        hazardBrachAdapter = new HazardBrachAdapter(HazardBanchReport.this, branches,hazardBranchModelrequest);
        branch_recyclerview.setAdapter(hazardBrachAdapter);

        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hazardBrachAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
