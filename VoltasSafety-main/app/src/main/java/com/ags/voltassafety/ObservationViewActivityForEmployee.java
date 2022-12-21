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

import com.ags.voltassafety.adapters.ObservationViewAdapter;
import com.ags.voltassafety.adapters.ObservationViewAdapterForEmployee;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObservationViewActivityForEmployee extends BaseActivity {

    RecyclerView observation_recycler_view;
    public FloatingActionButton fab;
    LinearLayout layoutOne;
    public static AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    List<ObservationHeader> observationDataArrayList;
    int count = 0;
    ObservationViewAdapterForEmployee customAdapter;
    private EditText searchView;
    Button tvOpen, tvAssigned, tvClosed, tvAll;
    ImageView search, logOut, back;
    Toolbar toolbar;
    String strName = "";
    private String strType = "", strSubType = "";

    SharedPreferences sharedPreferences;
    String emailId;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_observation_view);
            searchView = (EditText) findViewById(R.id.searchView);
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
            customAdapter = new ObservationViewAdapterForEmployee(ObservationViewActivityForEmployee.this, observationDataArrayList);
            observation_recycler_view.setAdapter(customAdapter);
            logOut.setVisibility(View.GONE);

            TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setTypeface(Utilities.fontBold(ObservationViewActivityForEmployee.this));

            sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            emailId = sharedPreferences.getString("EmailId", null);

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
            }


            customAdapter.setOnItemClickListener(new ObservationViewAdapterForEmployee.onItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int id = view.getId();
                    if (id == R.id.location) {
                        if (observationDataArrayList.get(position).getLatitude() != null) {
                            Intent intent = new Intent(ObservationViewActivityForEmployee.this, GoogleMapActivity.class);
                            Bundle bd = new Bundle();
                            bd.putSerializable("Observation", observationDataArrayList.get(position));

                            bd.putSerializable("FLAG", "Observation");
                            intent.putExtras(bd);
                            startActivity(intent);

                        } else {
                            Utilities.showAlertDialog("Location not found", ObservationViewActivityForEmployee.this);
                        }

                    } else {

                        Intent observationdetails = new Intent(ObservationViewActivityForEmployee.this, ObservationDetailsActivityForEmployee.class);
                        observationdetails.putExtra("observationId", observationDataArrayList.get(position).getObservationId());
                        observationdetails.putExtra("Status", observationDataArrayList.get(position).getStatus());
                        observationdetails.putExtra("ObservationType", observationDataArrayList.get(position).getTypeOfObservation());
                        observationdetails.putExtra("SubType", observationDataArrayList.get(position).getReason());
//                        observationdetails.putExtra("ObsId", observationDataArrayList.get(position).getObservationId());
//                        observationdetails.putExtra("Status", observationDataArrayList.get(position).getStatus());

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
//            observation_recycler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View view, int scrollX, int i1, int i2, int i3) {
//
//
//                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//                }
//            });
            searchView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //layoutOne.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String search = searchView.getText().toString().trim();
                    if (search.isEmpty()) {
                        search("");
                    } else {

                        tvOpen.setBackgroundResource(R.drawable.open_unclick);
                        tvAll.setBackgroundResource(R.drawable.all_unclick);
                        tvAssigned.setBackgroundResource(R.drawable.assign_unclick);
                        tvClosed.setBackgroundResource(R.drawable.close_unclick);
                        tvAll.setTextColor(getResources().getColor(R.color.DashBoardTextColor));
                        tvOpen.setTextColor(getResources().getColor(R.color.open_text));
                        tvAssigned.setTextColor(getResources().getColor(R.color.assigned_text));
                        tvClosed.setTextColor(getResources().getColor(R.color.close_text));

                        search(search);
                    }
                }
            });

            tvOpen.setTypeface(Utilities.fontRegular(ObservationViewActivityForEmployee.this));
            tvAssigned.setTypeface(Utilities.fontRegular(ObservationViewActivityForEmployee.this));
            tvClosed.setTypeface(Utilities.fontRegular(ObservationViewActivityForEmployee.this));
            tvAll.setTypeface(Utilities.fontRegular(ObservationViewActivityForEmployee.this));


            tvAll.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        getObservationViewList();
                        //layoutOne.setVisibility(View.GONE);
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                        tvAll.setBackgroundResource(R.drawable.all_click);

                        tvAll.setTextColor(getResources().getColor(R.color.white));
                        tvOpen.setTextColor(getResources().getColor(R.color.open_text));
                        tvAssigned.setTextColor(getResources().getColor(R.color.assigned_text));
                        tvClosed.setTextColor(getResources().getColor(R.color.close_text));

                        tvOpen.setBackgroundResource(R.drawable.open_unclick);
                        tvAssigned.setBackgroundResource(R.drawable.assign_unclick);
                        tvClosed.setBackgroundResource(R.drawable.close_unclick);

                    }

                    return false;
                }
            });
            tvOpen.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        search("Open");
                        tvOpen.setBackgroundResource(R.drawable.open_click);


                        tvAll.setTextColor(getResources().getColor(R.color.DashBoardTextColor));
                        tvOpen.setTextColor(getResources().getColor(R.color.white));
                        tvAssigned.setTextColor(getResources().getColor(R.color.assigned_text));
                        tvClosed.setTextColor(getResources().getColor(R.color.close_text));

                        tvAll.setBackgroundResource(R.drawable.all_unclick);
                        tvAssigned.setBackgroundResource(R.drawable.assign_unclick);
                        tvClosed.setBackgroundResource(R.drawable.close_unclick);

                    }

                    return false;

                }
            });

            tvAssigned.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        search("Assigned");
                        tvAssigned.setBackgroundResource(R.drawable.assign_click);

                        tvAll.setTextColor(getResources().getColor(R.color.DashBoardTextColor));
                        tvOpen.setTextColor(getResources().getColor(R.color.open_text));
                        tvAssigned.setTextColor(getResources().getColor(R.color.white));
                        tvClosed.setTextColor(getResources().getColor(R.color.close_text));

                        tvAll.setBackgroundResource(R.drawable.all_unclick);
                        tvOpen.setBackgroundResource(R.drawable.open_unclick);
                        tvClosed.setBackgroundResource(R.drawable.close_unclick);

                    }

                    return false;
                }
            });

            tvClosed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        search("Closed");
                        tvClosed.setBackgroundResource(R.drawable.close_click);

                        tvAll.setTextColor(getResources().getColor(R.color.DashBoardTextColor));
                        tvOpen.setTextColor(getResources().getColor(R.color.open_text));
                        tvAssigned.setTextColor(getResources().getColor(R.color.assigned_text));
                        tvClosed.setTextColor(getResources().getColor(R.color.white));

                        tvAll.setBackgroundResource(R.drawable.all_unclick);
                        tvOpen.setBackgroundResource(R.drawable.open_unclick);
                        tvAssigned.setBackgroundResource(R.drawable.assign_unclick);

                        ;
                    }

                    return false;
                }
            });


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent home = new Intent(ObservationViewActivityForEmployee.this, HomeActivityForEmployee.class);
                    home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(home);
//                    finish();
                }
            });
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog = new AlertDialog.Builder(ObservationViewActivityForEmployee.this, R.style.MyDialogTheme);
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Do you want to Logout?");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginPage = new Intent(ObservationViewActivityForEmployee.this, LoginActivity.class);
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
            if (Utilities.isConnectingToInternet(this)) {
                getObservationViewList();
            } else {
                Utilities.showAlertDialog("Please Check Your Internet Connection", ObservationViewActivityForEmployee.this);
            }


            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent dashBoard = new Intent(ObservationViewActivityForEmployee.this, CreateObservationActivityForEmployee.class);
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

//            searchView.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    if (charSequence.length() > 0) {
//                        layoutOne.setVisibility(View.VISIBLE);
//                    } else {
//                        layoutOne.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//
//            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
        Intent intent = new Intent(ObservationViewActivityForEmployee.this, HomeActivityForEmployee.class);
        Log.i("Hello", "This is Coomon Log");
        startActivity(intent);
        return;
    }

    private void search(final String serchWord) {
        try {

            final SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationResponse> call = apiInterface.searchObservations("Bearer " + sharedPreferences.getString("Bearertoken", null), serchWord, strType, strSubType);
            progressDialog = new ProgressDialog(ObservationViewActivityForEmployee.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<ObservationResponse>() {
                @Override
                public void onResponse(Call<ObservationResponse> call, Response<ObservationResponse> response) {
                    if (response.isSuccessful()) {

                        try {
                            observationDataArrayList.clear();
                            if (response.body().getResult() != null) {

                                List<ObservationHeader> list = response.body().getResult();
                                observationDataArrayList.addAll(list);


                                Log.d("SearchResult", "" + response.toString());
                                if (observationDataArrayList.size() > 0) {
                                    customAdapter = new ObservationViewAdapterForEmployee(ObservationViewActivityForEmployee.this, observationDataArrayList);
                                    Collections.sort(observationDataArrayList, ObservationHeader.dateComparator);
                                    observation_recycler_view.setAdapter(customAdapter);
                                    Thread.sleep(100);
                                    progressDialog.dismiss();
                                } else {
                                    showAlert("No items found");
                                    progressDialog.dismiss();
                                    searchView.setText("");
                                    observation_recycler_view.setAdapter(customAdapter);
                                    // finish();
                                }
                            } else {
                                progressDialog.dismiss();
                                searchView.setText("");
                                observation_recycler_view.setAdapter(customAdapter);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }

                @Override
                public void onFailure(Call<ObservationResponse> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.getMessage();
            progressDialog.dismiss();
        } finally {

        }
    }

    private void getObservationViewList() {
        try {
            observationDataArrayList.clear();
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ObservationResponse> call = apiInterface.getObservationbyEmailID("Bearer " + sharedPreferences.getString("Bearertoken", null),emailId,strSubType);
            showProgressDialog(ObservationViewActivityForEmployee.this);

            call.enqueue(new Callback<ObservationResponse>() {
                public void onResponse(Call<ObservationResponse> call, Response<ObservationResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            ObservationResponse loginResponse = response.body();
                            Log.d("observationlistview", loginResponse.getSuccess() + "");
                            if (loginResponse.getSuccess().equals(true)) {
                                String status = null;
//                                int position = 0;
//                                for (int i = 0; i < response.body().getResult().size(); i++) {
//                                    status=response.body().getResult().get(i).getStatus();
//
//                                }
//                                if(status.equalsIgnoreCase("Delete")){
//
//                                    observationDataArrayList.remove(response.body().getResult().get(position));
//                                }else{
                                observationDataArrayList.addAll(response.body().getResult());
                                //   }

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

                            alertDialog = new AlertDialog.Builder(ObservationViewActivityForEmployee.this, R.style.MyDialogTheme);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("No data to display");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ObservationViewActivityForEmployee.this, ObservationViewActivityForEmployee.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();

                        }
                    } else {
                        dismissProgress();
                        Utilities.showAlertDialog("No data to display", ObservationViewActivityForEmployee.this);
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
