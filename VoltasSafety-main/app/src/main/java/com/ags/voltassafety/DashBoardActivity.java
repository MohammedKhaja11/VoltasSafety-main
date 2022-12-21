package com.ags.voltassafety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;


import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.snackbar.Snackbar;

public class DashBoardActivity extends Activity {

    public TextView cancel, next;
    TextView header, hazardText, incidentText;
    ObservationHeader observationHeader;
    private RelativeLayout hazard, incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);

            initialisation();
            setFonts();

            hazard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    observationHeader.setTypeOfObservation("Hazard");
                    observationHeader.setReason("Hazard");
//                    hazard.setBackgroundColor(ContextCompat.getColor(DashBoardActivity.this, R.color.holoheme));
//                    incident.setBackgroundColor(ContextCompat.getColor(DashBoardActivity.this, R.color.white));
                    Intent intent = new Intent(DashBoardActivity.this, HazardActivity.class);
                    //observationHeader.setTypeOfObservation("Hazard");
                    Bundle b = new Bundle();
                    b.putSerializable("HeaderObject", observationHeader);
                    intent.putExtras(b);
                    startActivity(intent);
                   /* if (observationHeader.getTypeOfObservation() != null && observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
                        Intent intent = new Intent(DashBoardActivity.this, HazardActivity.class);
                        //observationHeader.setTypeOfObservation("Hazard");
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", observationHeader);
                        intent.putExtras(b);
                        startActivity(intent);

                    }*/
//                    else if (observationHeader.getTypeOfObservation() != null && observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident")) {
////
//                        Intent intent1 = new Intent(DashBoardActivity.this, IncidentTypeActivity.class);
//                        //observationHeader.setTypeOfObservation("Incident");
//                        Bundle b = new Bundle();
//                        b.putSerializable("HeaderObject", observationHeader);
//                        intent1.putExtras(b);
//                        startActivity(intent1);
//
//                    } else {
//                        Snackbar.make(view, "Please select observation type", Snackbar.LENGTH_LONG).show();
//                    }

                }
            });
            incident.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    observationHeader.setTypeOfObservation("Incident");
                    Intent intent1 = new Intent(DashBoardActivity.this, IncidentTypeActivity.class);
                    //observationHeader.setTypeOfObservation("Incident");
                    Bundle b = new Bundle();
                    b.putSerializable("HeaderObject", observationHeader);
                    intent1.putExtras(b);
                    startActivity(intent1);
                    //observationHeader.setTypeOfObservation("Incident");
//                    hazard.setBackgroundColor(ContextCompat.getColor(DashBoardActivity.this, R.color.white));
//                    incident.setBackgroundColor(ContextCompat.getColor(DashBoardActivity.this, R.color.holoheme));
                }
            });

            Intent intent = getIntent();
            observationHeader = (ObservationHeader) intent.getExtras().getSerializable("HeaderObject");


            //fonts

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  if (observationHeader.getTypeOfObservation() != null && observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
                        Intent intent = new Intent(DashBoardActivity.this, HazardActivity.class);
                        //observationHeader.setTypeOfObservation("Hazard");
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", observationHeader);
                        intent.putExtras(b);
                        startActivity(intent);

                    } else if (observationHeader.getTypeOfObservation() != null && observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident")) {
//
                        Intent intent1 = new Intent(DashBoardActivity.this, IncidentTypeActivity.class);
                        //observationHeader.setTypeOfObservation("Incident");
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", observationHeader);
                        intent1.putExtras(b);
                        startActivity(intent1);

                    } else {
                        Snackbar.make(view, "Please select observation type", Snackbar.LENGTH_LONG).show();
                    }*/
                }

            });

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setFonts() {
        try {
            header.setTypeface(Utilities.fontBold(getApplicationContext()));
            next.setTypeface(Utilities.fontRegular(getApplicationContext()));
            cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            hazardText.setTypeface(Utilities.fontBold(getApplicationContext()));
            incidentText.setTypeface(Utilities.fontBold(getApplicationContext()));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void initialisation() {
        try {
            cancel = (TextView) findViewById(R.id.cancel);
            next = (TextView) findViewById(R.id.next);
            hazardText = (TextView) findViewById(R.id.hazardText);
            incidentText = (TextView) findViewById(R.id.incidentText);
            header = findViewById(R.id.header);
            hazard = findViewById(R.id.hazard);
            incident = findViewById(R.id.incident);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}