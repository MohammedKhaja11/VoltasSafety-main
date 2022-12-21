package com.ags.voltassafety;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ags.voltassafety.model.ObservationCountResponse;
import com.ags.voltassafety.model.ObservationStatusCounts;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivityForEmployee extends AppCompatActivity {

    public RecyclerView recyclerView;
    public LinearLayout createObserve;
    ArrayList<String> name;
    ArrayList<Integer> image;
    ArrayList<Integer> color;
    ProgressDialog progressDialog;
    List<ObservationStatusCounts> observationResult = new ArrayList<ObservationStatusCounts>();
    TextView openCount, assignedCount, closedCount, userName, closeText, assignText, openText, createText, welcome, headName, header, nameUser;
    int count;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static AlertDialog.Builder alertDialog;
    private ImageView logOut;
    private LinearLayout homeObservation, homeIncident, homeNearMiss, homeReports, homeGps, homeSupport;
    //    TableRow open, assign, close;
    private Toolbar toolbar;
    private TextView homeTextHazard, homeTextNearMiss, homeTextAccident, homeTextReports, homeTextGps, homeTextSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//           /**/ setContentView(R.layout.activity_home);
            setContentView(R.layout.home_new_layout_emp);
            initialisation();
            setFonts();
            toolbar = (Toolbar) findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            // editor.clear();
            if (sharedPreferences != null) {
                userName.setText(sharedPreferences.getString("userName", null));
            }
            homeObservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Dialog dialogOne = new Dialog(HomeActivityForEmployee.this, R.style.DialogOne);
//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                    dialogOne.setContentView(R.layout.mis_choice_dialog_layout);
                    dialogOne.setCancelable(false);
                    ImageView close = dialogOne.findViewById(R.id.close);
                    TextView titleText = dialogOne.findViewById(R.id.titleText);
                    TextView questionText = dialogOne.findViewById(R.id.questionText);
                    TextView answerText = dialogOne.findViewById(R.id.answerText);
                    TextView unsafeConditionText = dialogOne.findViewById(R.id.unsafeConditionText);
                    TextView unsafeConditionAns = dialogOne.findViewById(R.id.unsafeConditionAns);
                    TextView unsafeActText = dialogOne.findViewById(R.id.unsafeActText);
                    Button creationPage = dialogOne.findViewById(R.id.creationPage);
                    TextView unsafeActAns = dialogOne.findViewById(R.id.unsafeActAns);
                    titleText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    questionText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    answerText.setTypeface(Utilities.fontRegular(HomeActivityForEmployee.this));
                    creationPage.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    unsafeConditionText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    unsafeConditionAns.setTypeface(Utilities.fontRegular(HomeActivityForEmployee.this));
                    unsafeActText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    unsafeActAns.setTypeface(Utilities.fontRegular(HomeActivityForEmployee.this));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                        }
                    });
                    creationPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                            Intent intent = new Intent(HomeActivityForEmployee.this, ObservationViewActivityForEmployee.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("Name", "HAZARD REPORTING");
                            startActivity(intent);


                        }
                    });
                    dialogOne.show();
                }
            });

            homeIncident.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialogOne = new Dialog(HomeActivityForEmployee.this, R.style.DialogOne);
//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                    dialogOne.setContentView(R.layout.accident_defnation_layout);
                    dialogOne.setCancelable(false);
                    ImageView close = dialogOne.findViewById(R.id.close);
//                    TextView titleText = dialogOne.findViewById(R.id.titleText);
                    TextView questionText = dialogOne.findViewById(R.id.questionText);
                    TextView answerText = dialogOne.findViewById(R.id.answerText);
//                    TextView unsafeConditionText = dialogOne.findViewById(R.id.unsafeConditionText);
//                    TextView unsafeConditionAns = dialogOne.findViewById(R.id.unsafeConditionAns);
//                    TextView unsafeActText = dialogOne.findViewById(R.id.unsafeActText);
                    Button creationPage = dialogOne.findViewById(R.id.creationPage);
//                    TextView unsafeActAns = dialogOne.findViewById(R.id.unsafeActAns);
//                    titleText.setTypeface(Utilities.fontBold(HomeActivity.this));
                    questionText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    answerText.setTypeface(Utilities.fontRegular(HomeActivityForEmployee.this));
                    creationPage.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
//                    unsafeConditionText.setTypeface(Utilities.fontBold(HomeActivity.this));
//                    unsafeConditionAns.setTypeface(Utilities.fontRegular(HomeActivity.this));
//                    unsafeActText.setTypeface(Utilities.fontBold(HomeActivity.this));
//                    unsafeActAns.setTypeface(Utilities.fontRegular(HomeActivity.this));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                        }
                    });
                    creationPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                            Intent intent = new Intent(HomeActivityForEmployee.this, ObservationViewActivityForEmployee.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("Name", "INCIDENT REPORTING");

                            startActivity(intent);


                        }
                    });
                    dialogOne.show();


                }
            });

            homeNearMiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Dialog dialogOne = new Dialog(HomeActivityForEmployee.this, R.style.DialogOne);
//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                    dialogOne.setContentView(R.layout.nearmiss_defnation_layout);
                    dialogOne.setCancelable(false);
                    ImageView close = dialogOne.findViewById(R.id.close);
//                    TextView titleText = dialogOne.findViewById(R.id.titleText);
                    TextView questionText = dialogOne.findViewById(R.id.questionText);
                    TextView answerText = dialogOne.findViewById(R.id.answerText);
//                    TextView unsafeConditionText = dialogOne.findViewById(R.id.unsafeConditionText);
//                    TextView unsafeConditionAns = dialogOne.findViewById(R.id.unsafeConditionAns);
//                    TextView unsafeActText = dialogOne.findViewById(R.id.unsafeActText);
                    Button creationPage = dialogOne.findViewById(R.id.creationPage);
//                    TextView unsafeActAns = dialogOne.findViewById(R.id.unsafeActAns);
//                    titleText.setTypeface(Utilities.fontBold(HomeActivity.this));
                    questionText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
                    answerText.setTypeface(Utilities.fontRegular(HomeActivityForEmployee.this));
                    creationPage.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
//                    unsafeConditionText.setTypeface(Utilities.fontBold(HomeActivity.this));
//                    unsafeConditionAns.setTypeface(Utilities.fontRegular(HomeActivity.this));
//                    unsafeActText.setTypeface(Utilities.fontBold(HomeActivity.this));
//                    unsafeActAns.setTypeface(Utilities.fontRegular(HomeActivity.this));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                        }
                    });
                    creationPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                            Intent intent = new Intent(HomeActivityForEmployee.this, ObservationViewActivityForEmployee.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("Name", "NEAR MISS REPORTING");
                            startActivity(intent);


                        }
                    });
                    dialogOne.show();


                }
            });
            /*homeReports.setVisibility(View.GONE);
            homeReports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (!sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000023")) {
                    Intent intent = new Intent(HomeActivityForEmployee.this, ReportsActivity.class);
                    startActivity(intent);
//                    }
                }
            });*/
           // homeGps.setVisibility(View.VISIBLE);
            homeGps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (!sharedPreferences.getString("roleId", null).equalsIgnoreCase("ROLE000023")) {
                    Intent intent = new Intent(HomeActivityForEmployee.this, GoogleMapActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bd = new Bundle();
                    bd.putString("FLAG", "");
                    intent.putExtras(bd);
                    startActivity(intent);
//                    }
                }
            });
            homeSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivityForEmployee.this, SupportActivityForEmployee.class);
                    startActivity(intent);
                }
            });

//            getCounts();

//            recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
//            HomeAdapter homeAdapter = new HomeAdapter(HomeActivity.this, name, image, color);
//            recyclerView.setAdapter(homeAdapter);
            createObserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivityForEmployee.this, CreateObservationActivityForEmployee.class);
                    startActivity(intent);
                }
            });

            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog = new AlertDialog.Builder(HomeActivityForEmployee.this, R.style.MyDialogTheme);
                    //alertDialog.setTitle("Information");
                    alertDialog.setMessage("Do you want to Logout?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginPage = new Intent(HomeActivityForEmployee.this, LandingPageActivity.class);
                            startActivity(loginPage);

                            sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                        }
                    });

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

      /*  if (id == R.id.actionChange_password) {
            Intent intent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        } else*/
        if (id == R.id.actionLogOut) {
            alertDialog = new AlertDialog.Builder(HomeActivityForEmployee.this, R.style.MyDialogTheme);
            //alertDialog.setTitle("Information");
            alertDialog.setMessage("Do you want to Logout?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent loginPage = new Intent(HomeActivityForEmployee.this, LandingPageActivity.class);
                    loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginPage);
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        return;
    }

    private void getCounts() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationCountResponse> call = apiInterface.getObservationSummary("Bearer " + sharedPreferences.getString("Bearertoken", null));
            progressDialog = new ProgressDialog(HomeActivityForEmployee.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<ObservationCountResponse>() {
                @Override
                public void onResponse(Call<ObservationCountResponse> call, Response<ObservationCountResponse> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if (response.body().getResult() != null) {
                            observationResult = response.body().getResult();

                            for (int i = 0; i < response.body().getResult().size(); i++) {

                                if (response.body().getResult().get(i).getStatus().equalsIgnoreCase("Open")) {

                                    count = response.body().getResult().get(i).getCount();
                                    openCount.setText("" + count);
                                } else if (response.body().getResult().get(i).getStatus().equalsIgnoreCase("Assigned")) {

                                    count = response.body().getResult().get(i).getCount();
                                    assignedCount.setText("" + count);
                                } else if (response.body().getResult().get(i).getStatus().equalsIgnoreCase("Closed")) {

                                    count = response.body().getResult().get(i).getCount();
                                    closedCount.setText("" + count);
                                }
                            }

                        } else {

                            openCount.setText("" + 0);
                            assignedCount.setText("" + 0);
                            closedCount.setText("" + 0);
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ObservationCountResponse> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void AddItemsToRecyclerViewArrayList() {
        try {
            name = new ArrayList<>();
            name.add("OBSERVATIONS");
            name.add("REPORTS");
            name.add("GPS");
            name.add("SUPPORT");

            image = new ArrayList<>();
            image.add(R.mipmap.ic_all_observations);
            image.add(R.mipmap.ic_reports);
            image.add(R.mipmap.ic_gps_home);
            image.add(R.mipmap.ic_support);

            color = new ArrayList<>();
            color.add(R.drawable.all_observe_bg);
            color.add(R.drawable.reports_bg);
            color.add(R.drawable.notify_bg);
            color.add(R.drawable.support_bg);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void initialisation() {
        try {
            logOut = findViewById(R.id.logOut);
            homeObservation = (LinearLayout) findViewById(R.id.homeObservation);
            homeReports = (LinearLayout) findViewById(R.id.homeReports);
            homeGps = (LinearLayout) findViewById(R.id.homeGps);
            homeSupport = (LinearLayout) findViewById(R.id.homeSupport);
            homeIncident = (LinearLayout) findViewById(R.id.homeIncident);
            homeNearMiss = (LinearLayout) findViewById(R.id.homeNearmiss);


//            open = (TableRow) findViewById(R.id.Open);
//            assign = (TableRow) findViewById(R.id.assign);
//            close = (TableRow) findViewById(R.id.close);

            openCount = findViewById(R.id.openCount);
            assignedCount = findViewById(R.id.assignedCount);
            closedCount = findViewById(R.id.closedCount);

            openText = findViewById(R.id.openText);
            assignText = findViewById(R.id.assignText);
            closeText = findViewById(R.id.closeText);
//            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            createObserve = (LinearLayout) findViewById(R.id.createObservation);
            userName = (TextView) findViewById(R.id.name);
            createText = (TextView) findViewById(R.id.createText);
            welcome = (TextView) findViewById(R.id.welcome);
            headName = (TextView) findViewById(R.id.name);
            header = (TextView) findViewById(R.id.header);

            homeTextHazard = (TextView) findViewById(R.id.homeTextHazard);
            homeTextNearMiss = (TextView) findViewById(R.id.homeTextNearMiss);
            homeTextAccident = (TextView) findViewById(R.id.homeTextAccident);
            homeTextReports = (TextView) findViewById(R.id.homeTextReports);
            homeTextGps = (TextView) findViewById(R.id.homeTextGps);
            homeTextSupport = (TextView) findViewById(R.id.homeTextSupport);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setFonts() {
        try {
            openCount.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            assignedCount.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            closedCount.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            openText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            assignText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            closeText.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            welcome.setTypeface(Utilities.fontRegular(HomeActivityForEmployee.this));
            headName.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            header.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            homeTextHazard.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            homeTextNearMiss.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            homeTextAccident.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            homeTextReports.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            homeTextGps.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));
            homeTextSupport.setTypeface(Utilities.fontBold(HomeActivityForEmployee.this));

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getCounts();
    }
}
