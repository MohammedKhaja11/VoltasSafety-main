package com.ags.voltassafety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.snackbar.Snackbar;

public class IncidentTypeActivity extends AppCompatActivity
{

    RelativeLayout accident, nearMiss;
    private TextView cancel, next, hazardText, incidentText, header;
    ObservationHeader observationHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_incident_type);
            initialisation();
            setFonts();

            Intent intent = getIntent();
            observationHeader = (ObservationHeader) intent.getExtras().getSerializable("HeaderObject");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            nearMiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    observationHeader.setReason("Near Miss");
//                    nearMiss.setBackgroundColor(ContextCompat.getColor(IncidentTypeActivity.this, R.color.holoheme));
//                    accident.setBackgroundColor(ContextCompat.getColor(IncidentTypeActivity.this, R.color.white));
                    Intent intent = new Intent(IncidentTypeActivity.this, NearMissActivity.class);
//                    observationHeader.setReason("Near Miss");
                    Bundle b = new Bundle();
                    b.putSerializable("HeaderObject", observationHeader);
                    intent.putExtras(b);
                    intent.putExtra("NearMiss", 1);
                    startActivity(intent);
                }
            });
            accident.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    observationHeader.setReason("Accident");
//                    nearMiss.setBackgroundColor(ContextCompat.getColor(IncidentTypeActivity.this, R.color.white));
//                    accident.setBackgroundColor(ContextCompat.getColor(IncidentTypeActivity.this, R.color.holoheme));

                    Intent intent1 = new Intent(IncidentTypeActivity.this, IncidentActivity.class);
//                    observationHeader.setTypeOfObservation("Incident");

//                    observationHeader.setReason("Accident");
                    Bundle b = new Bundle();
                    b.putSerializable("HeaderObject", observationHeader);
                    intent1.putExtras(b);
                    startActivity(intent1);
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                /*    if (observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Near Miss")) {
                        Intent intent = new Intent(IncidentTypeActivity.this, NearMissActivity.class);
//                    observationHeader.setReason("Near Miss");
                        Bundle b = new Bundle();
                        b.putSerializable("HeaderObject", observationHeader);
                        intent.putExtras(b);
                        intent.putExtra("NearMiss", 1);
                        startActivity(intent);

                    } else if (observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {
//
                        Intent intent1 = new Intent(IncidentTypeActivity.this, IncidentActivity.class);
//                    observationHeader.setTypeOfObservation("Incident");

//                    observationHeader.setReason("Accident");
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

    private void initialisation() {
        try {
            nearMiss = findViewById(R.id.nearMiss);
            accident = findViewById(R.id.accident);
            hazardText = findViewById(R.id.hazardText);
            incidentText = findViewById(R.id.incidentText);
            accident = findViewById(R.id.accident);
            cancel = (TextView) findViewById(R.id.cancel);
            next = (TextView) findViewById(R.id.next);
            header = (TextView) findViewById(R.id.header);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setFonts() {
        try {
            hazardText.setTypeface(Utilities.fontBold(getApplicationContext()));
            incidentText.setTypeface(Utilities.fontBold(getApplicationContext()));
            header.setTypeface(Utilities.fontBold(getApplicationContext()));
            cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            next.setTypeface(Utilities.fontRegular(getApplicationContext()));
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
