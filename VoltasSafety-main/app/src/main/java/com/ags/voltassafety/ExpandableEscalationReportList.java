package com.ags.voltassafety;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.CustomExpandableListAdapter;
import com.ags.voltassafety.adapters.EscalationUpdatedAdapter;
import com.ags.voltassafety.model.EscalationModel;
import com.ags.voltassafety.model.EscalationResponse;
import com.ags.voltassafety.model.EscalationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableEscalationReportList extends AppCompatActivity {


    ExpandableListView expandableListViewOne;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    EditText filter_edit;
    RecyclerView escalation_report_recycler;
    EscalationResponse escalationResponse;
    ExpandableListView expandableListView;
    List<EscalationResult> escalationResultList;
    EscalationResult escalationResult;
    TextView total_count;
    EscalationUpdatedAdapter hazardBrachAdapter;
    ArrayList<EscalationResult> branches;
    EscalationModel escalationModel;
    private ImageView back;
    CustomExpandableListAdapter customExpandableListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_escalation_report_list);
        filter_edit = (EditText) findViewById(R.id.filter_edit);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        escalationResultList = new ArrayList<>();

        escalationResultList = (List<EscalationResult>) getIntent().getExtras().getSerializable("reportsResult");

        escalationModel = (EscalationModel) getIntent().getSerializableExtra("reportRequest");
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        customExpandableListAdapter = new CustomExpandableListAdapter(ExpandableEscalationReportList.this, escalationResultList, escalationModel);
        expandableListView.setAdapter(customExpandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
              /*  Toast.makeText(getApplicationContext(),
                        escalationResultList.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });
        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customExpandableListAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        escalationResultList.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                int childId = v.getId();
//                if (childId == R.id.open) {
//                Log.d("ChildPosition", childPosition + " " + escalationResultList.get(groupPosition).getOpen());
//                }

                return false;
            }
        });

    }

}
