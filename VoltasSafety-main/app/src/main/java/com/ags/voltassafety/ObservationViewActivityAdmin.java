package com.ags.voltassafety;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.adapters.ObservationViewAdapter;
import com.ags.voltassafety.adapters.ObservationViewAdapterAdmin;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObservationViewActivityAdmin extends BaseActivity {

    RecyclerView observation_recycler_view;
    public FloatingActionButton fab;
    LinearLayout layoutOne;
    public static AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    List<ObservationHeader> observationDataArrayList;
    int count = 0;
    ObservationViewAdapterAdmin customAdapter;
    private EditText searchView;
    Button tvOpen, tvAssigned, tvClosed, tvAll;
    ImageView search, logOut, back;
    Toolbar toolbar;
    String strName = "";
    private String strType = "", strSubType = "";
    LinearLayout search_layout;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_observation_view);
            searchView = (EditText) findViewById(R.id.searchView);
            search_layout = findViewById(R.id.search_layout);
            search_layout.setVisibility(View.GONE);
            toolbar = findViewById(R.id.toolbar);
            back = findViewById(R.id.back);
            layoutOne = findViewById(R.id.layoutOne);
            logOut = findViewById(R.id.logOut);
            tvAll = findViewById(R.id.tv_all);
            tvOpen = findViewById(R.id.tv_open);
            tvAssigned = findViewById(R.id.tv_assigned);
            tvClosed = findViewById(R.id.tv_closed);
            search = findViewById(R.id.search);
            observation_recycler_view = findViewById(R.id.observation_recycler_view);
            observation_recycler_view.setNestedScrollingEnabled(true);
            observationDataArrayList = new ArrayList<>();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            observation_recycler_view.setLayoutManager(linearLayoutManager);
            customAdapter = new ObservationViewAdapterAdmin(ObservationViewActivityAdmin.this, observationDataArrayList);
            observation_recycler_view.setAdapter(customAdapter);
            logOut.setVisibility(View.GONE);

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            layoutOne.setVisibility(View.GONE);
            TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setTypeface(Utilities.fontBold(ObservationViewActivityAdmin.this));
            toolbar_title.setText("PARTIAL CLOSED OBSERVATIONS");
/*
            strName = getIntent().getExtras().getString("Name");
            if (strName.equalsIgnoreCase("Hazard Reporting")) {
                strType = "Hazard";
                strSubType = "Hazard";
                toolbar_title.setText(strSubType.toUpperCase());
            } else if (strName.equalsIgnoreCase("Incident Reporting")) {
                strType = "Incident";
                strSubType = "Accident";

                toolbar_title.setText(strSubType.toUpperCase());

            } else {
                strType = "Incident";
                strSubType = "Near Miss";
                toolbar_title.setText(strSubType.toUpperCase());
            }*/

            if (Utilities.isConnectingToInternet(this)) {
                getObservationViewList();
            } else {
                Utilities.showAlertDialog("Please Check Your Internet Connection", ObservationViewActivityAdmin.this);
            }
            //getObservationViewList();
            customAdapter.setOnItemClickListener(new ObservationViewAdapterAdmin.onItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int id = view.getId();
                    if (id == R.id.location) {
                        if (observationDataArrayList.get(position).getLatitude() != null) {
                            Intent intent = new Intent(ObservationViewActivityAdmin.this, GoogleMapActivity.class);
                            Bundle bd = new Bundle();
                            bd.putSerializable("Observation", observationDataArrayList.get(position));
                            bd.putSerializable("FLAG", "Observation");
                            intent.putExtras(bd);
                            startActivity(intent);

                        } else {
                            Utilities.showAlertDialog("Location not found", ObservationViewActivityAdmin.this);
                        }

                    } else {

                        Intent observationdetails = new Intent(ObservationViewActivityAdmin.this, ObservationDetailsActivityForAdmin.class);
                        observationdetails.putExtra("observationId", observationDataArrayList.get(position).getObservationId());
                        observationdetails.putExtra("Status", observationDataArrayList.get(position).getStatus());
                        observationdetails.putExtra("ObservationType", observationDataArrayList.get(position).getTypeOfObservation());
                        observationdetails.putExtra("SubType", observationDataArrayList.get(position).getReason());
                        startActivity(observationdetails);
                    }
                }
            });

            observation_recycler_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return false;
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent home = new Intent(ObservationViewActivityAdmin.this, HomeActivity.class);
                    home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(home);
//                    finish();
                }
            });
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog = new AlertDialog.Builder(ObservationViewActivityAdmin.this, R.style.MyDialogTheme);
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Do you want to Logout?");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginPage = new Intent(ObservationViewActivityAdmin.this, LoginActivity.class);
                            loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(loginPage);
                        }
                    });
                    alertDialog.show();

                }
            });

//        toolbar.setBackgroundResource(Color.TRANSPARENT);
            toolbar.showOverflowMenu();
            setSupportActionBar(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportActionBar().setElevation(0f);

            }


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent dashBoard = new Intent(ObservationViewActivityAdmin.this, CreateObservationActivity.class);
                    ObservationHeader observationHeader;
                    Bundle b = new Bundle();
                    observationHeader = new ObservationHeader();
                    observationHeader.setTypeOfObservation(strType);
                    observationHeader.setReason(strSubType);
                    b.putSerializable("HeaderObject", observationHeader);
                    dashBoard.putExtras(b);
                    startActivity(dashBoard);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
        Intent intent = new Intent(ObservationViewActivityAdmin.this, HomeActivity.class);
        Log.i("Hello", "This is Coomon Log");
        startActivity(intent);
        return;
    }


    private void getObservationViewList() {
        try {
            observationDataArrayList.clear();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationResponse> call = apiInterface.getPartialObservations("Bearer " + sharedPreferences.getString("Bearertoken", null));
            showProgressDialog(ObservationViewActivityAdmin.this);

            call.enqueue(new Callback<ObservationResponse>() {
                public void onResponse(Call<ObservationResponse> call, Response<ObservationResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            ObservationResponse loginResponse = response.body();
                            Log.d("observationlistview", loginResponse.getSuccess() + "");
                            if (loginResponse.getSuccess().equals(true)) {
                                String status = null;
                                observationDataArrayList.addAll(response.body().getResult());

                                Log.d("ArrayListSize", observationDataArrayList.size() + "");


                                Collections.sort(observationDataArrayList, ObservationHeader.dateComparator);
                                customAdapter.notifyDataSetChanged();
                                // set the Adapter to RecyclerView
                                try {
                                    Thread.sleep(200);
                                    dismissProgress();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                dismissProgress();
                                showAlert(loginResponse.getErrors() + "");
                                //showToast(loginResponse.getErrors() + "");
                            }

                        } else {
                            dismissProgress();
//                        Utilities.showAlertDialog("No data to display", ObservationViewActivity.this);

                            alertDialog = new AlertDialog.Builder(ObservationViewActivityAdmin.this, R.style.MyDialogTheme);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("No data to display");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ObservationViewActivityAdmin.this, ObservationViewActivityAdmin.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();

                        }
                    } else {
                        dismissProgress();
                        Utilities.showAlertDialog("No data to display", ObservationViewActivityAdmin.this);
                    }


                }

                public void onFailure(Call<ObservationResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        getObservationViewList();
    }*/
}
