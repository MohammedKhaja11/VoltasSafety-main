package com.ags.voltassafety;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.ags.voltassafety.adapters.ManPowerExternalAdapter;
import com.ags.voltassafety.adapters.ManPowerInternalAdapter;
import com.ags.voltassafety.model.CreateManPowerResponce;
import com.ags.voltassafety.model.CreateManPowerResult;
import com.ags.voltassafety.model.CreateManpowerInput;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.EntityResponse;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.External;
import com.ags.voltassafety.model.Internal;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateManpowerListActivity extends BaseActivity {

    EditText mETPFNumber;
    TextView mTVVertical;
    Spinner mSPZone, mSPBranch;
    ListView mLVInternalRecyclerView, mLVExternalRecyclerView;
    private TextView mCancel, mTitle, mSubmit, mTVInternalHeader, mTVExternalHeader;
    ImageView mBtnAddInternal, mBtnAddExternal;
    ArrayList<EntityResult> entityResponseArrayList;
    List<Internal> internalArrayList = new ArrayList<>();
    List<External> externalArrayList = new ArrayList<>();
    CreateManpowerInput objManpowerHeader;
    Internal objInternal;
    External objExternal;
    ManPowerInternalAdapter manPowerInternalAdapter;
    ManPowerExternalAdapter manPowerExternalAdapter;
    private android.app.AlertDialog.Builder alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_create_manpower_list);
//            Toolbar toolbar = findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
            mCancel = findViewById(R.id.cancel);
            mSubmit = findViewById(R.id.submit);
            mTitle = findViewById(R.id.create_title);
            mLVInternalRecyclerView = findViewById(R.id.internal_item_recycler_view);
            mLVExternalRecyclerView = findViewById(R.id.external_items_recycler_view);
            mTVInternalHeader = findViewById(R.id.listoftask__internal_header);
            mTVExternalHeader = findViewById(R.id.listoftask_extenal_header);

            mTVInternalHeader.setTypeface(Utilities.fontBold(getApplicationContext()));

            mTVExternalHeader.setTypeface(Utilities.fontBold(getApplicationContext()));
            mTitle.setTypeface(Utilities.fontBold(getApplicationContext()));
            mCancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            mSubmit.setTypeface(Utilities.fontRegular(getApplicationContext()));

            mBtnAddInternal = findViewById(R.id.add_tasks_internal);
            mBtnAddExternal = findViewById(R.id.add_tasks_external);
            objManpowerHeader = (CreateManpowerInput) getIntent().getExtras().getSerializable("HeaderObject");
            if (objManpowerHeader != null && objManpowerHeader.getId() != null) {

                internalArrayList.addAll(objManpowerHeader.getInternal());
                externalArrayList.addAll(objManpowerHeader.getExternal());
            } else {

                objInternal = new Internal();
                internalArrayList.add(objInternal);

                objExternal = new External();
                externalArrayList.add(objExternal);
            }

            if (Utilities.isConnectingToInternet(CreateManpowerListActivity.this)) {

                entityMapValues();
            } else {
                showAlert("Please Check Your Internet Connection");
            }

            mBtnAddExternal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        alertDialog = new android.app.AlertDialog.Builder(CreateManpowerListActivity.this, R.style.MyDialogTheme);
                        alertDialog.setMessage("Do you want to add Task?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                objExternal = new External();
                                externalArrayList.add(objExternal);
                                int i = externalArrayList.indexOf(objExternal);

                                Handler handler = new Handler();
                                final int finalI = i;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLVExternalRecyclerView.smoothScrollToPosition(finalI);
                                        mLVExternalRecyclerView.setStackFromBottom(true);
                                    }
                                }, 100);
                                mLVExternalRecyclerView.setSelection(finalI);

                                //  hazard_item_recycler_view.smoothScrollToPosition(i); // after adding item moving to that position


                                manPowerExternalAdapter.notifyDataSetChanged();

                            }
                        });

                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.show();

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            mBtnAddInternal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        alertDialog = new android.app.AlertDialog.Builder(CreateManpowerListActivity.this, R.style.MyDialogTheme);
                        alertDialog.setMessage("Do you want to add Task?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                objInternal = new Internal();
                                internalArrayList.add(objInternal);
                                int i = internalArrayList.indexOf(objInternal);

                                Handler handler = new Handler();
                                final int finalI = i;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLVInternalRecyclerView.smoothScrollToPosition(finalI);
                                        mLVInternalRecyclerView.setStackFromBottom(true);
                                    }
                                }, 100);
                                mLVInternalRecyclerView.setSelection(finalI);

                                //  hazard_item_recycler_view.smoothScrollToPosition(i); // after adding item moving to that position


                                manPowerInternalAdapter.notifyDataSetChanged();

                            }
                        });

                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.show();

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (validateInternalItems(internalArrayList) && validateExternalItems(externalArrayList))
                            if (objManpowerHeader != null && objManpowerHeader.getId() != null) {

                                objManpowerHeader.setInternal(internalArrayList);
                                objManpowerHeader.setExternal(externalArrayList);
                                if (Utilities.isConnectingToInternet(CreateManpowerListActivity.this)) {

                                    updateManPowerData(objManpowerHeader);
                                } else {
                                    showAlert("Please Check Your Internet Connection");
                                }


                            } else {
                                objManpowerHeader.setInternal(internalArrayList);
                                objManpowerHeader.setExternal(externalArrayList);
                                if (Utilities.isConnectingToInternet(CreateManpowerListActivity.this)) {

                                    createManPowerData(objManpowerHeader);
                                } else {
                                    showAlert("Please Check Your Internet Connection");
                                }
                            }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void createManPowerData(CreateManpowerInput inputModel) {
        try {
            //CreateResponse registerResponse;
            Gson gson = new Gson();
            String json = gson.toJson(inputModel);

            Log.d("<<<<>>>>>>>>>>>>", json);
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Call<CreateManPowerResponce> call = apiInterface.createManPower("Bearer " + sharedPreferences.getString("Bearertoken", null), inputModel);
            showProgressDialog(CreateManpowerListActivity.this);
            call.enqueue(new Callback<CreateManPowerResponce>() {
                public void onResponse(Call<CreateManPowerResponce> call, Response<CreateManPowerResponce> response) {
                    if (response.isSuccessful()) {
                        try {
                            dismissProgress();

                            if (response.body() != null && response.body().getSuccess()) {

                                final AlertDialog.Builder builder = new AlertDialog.Builder(CreateManpowerListActivity.this, R.style.MyDialogTheme);
                                builder.setTitle(getResources().getString(R.string.information))
                                        .setMessage("Man Power Created Successfully With Man Power Id--(" + response.body().getResult().getId() + ")")
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                Intent intent = new Intent(CreateManpowerListActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //intent.putExtra("Name", "HAZARD REPORTING");
                                                startActivity(intent);
                                            }
                                        }).create().show();
//                                CreateManPowerResponce registerResponse = response.body();


                            } else {
                                Utilities.showAlertDialog(response.body().toString(), CreateManpowerListActivity.this);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), CreateManpowerListActivity.this);
                    }
                }

                public void onFailure(Call<CreateManPowerResponce> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }
            });
//            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
    }

    private void updateManPowerData(CreateManpowerInput inputModel) {
        try {
            //CreateResponse registerResponse;
            Gson gson = new Gson();
            String json = gson.toJson(inputModel);

            Log.d("<<<<>>>>>>>>>>>>", json);
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Call<CreateManPowerResponce> call = apiInterface.updateManPower("Bearer " + sharedPreferences.getString("Bearertoken", null), inputModel, "" + inputModel.getId());
            showProgressDialog(CreateManpowerListActivity.this);
            call.enqueue(new Callback<CreateManPowerResponce>() {
                public void onResponse(Call<CreateManPowerResponce> call, Response<CreateManPowerResponce> response) {
                    if (response.isSuccessful()) {
                        try {
                            try {
                                Thread.sleep(400);
                                // close it after response
                                dismissProgress();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (response.body() != null && response.body().getSuccess()) {
//                                CreateManPowerResponce registerResponse = response.body();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(CreateManpowerListActivity.this, R.style.MyDialogTheme);
                                builder.setTitle(getResources().getString(R.string.information))
                                        .setMessage("Man Power Updated Successfully With Man Power Id--(" + response.body().getResult().getId() + ")")
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                Intent intent = new Intent(CreateManpowerListActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //intent.putExtra("Name", "HAZARD REPORTING");
                                                startActivity(intent);
                                            }
                                        }).create().show();

                            } else {
                                Utilities.showAlertDialog(response.body().getErrors().toString(), CreateManpowerListActivity.this);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), CreateManpowerListActivity.this);
                    }
                }

                public void onFailure(Call<CreateManPowerResponce> call, Throwable t) {
                    Log.d("LoginResponse", t.getMessage() + "");
                }
            });
//            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
    }

    private void entityMapValues() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<EntityResponse> call = apiInterface.getEntityMapData("Bearer " + sharedPreferences.getString("Bearertoken", null), "MIS");
            showProgressDialog(CreateManpowerListActivity.this);
            call.enqueue(new Callback<EntityResponse>() {
                public void onResponse(Call<EntityResponse> call, Response<EntityResponse> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            EntityResponse loginResponse = response.body();
                            Log.d("LoginResponse", response.body().getResult() + "");
                            if (loginResponse.getSuccess()) {
                                dismissProgress();
                                // ObservationType observationType = new ObservationType();
                                entityResponseArrayList = new ArrayList<>();

                                entityResponseArrayList.addAll(response.body().getResult());
                                Collections.sort(entityResponseArrayList, EntityResult.valueComparator);

                                manPowerInternalAdapter = new ManPowerInternalAdapter(CreateManpowerListActivity.this, internalArrayList, entityResponseArrayList);
                                mLVInternalRecyclerView.setAdapter(manPowerInternalAdapter);

                                manPowerExternalAdapter = new ManPowerExternalAdapter(CreateManpowerListActivity.this, externalArrayList, entityResponseArrayList);
                                mLVExternalRecyclerView.setAdapter(manPowerExternalAdapter);


                            } else {
                                dismissProgress();
                                showAlert(loginResponse.getErrors() + "");
                                //showToast(loginResponse.getErrors() + "");
                            }
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<EntityResponse> call, Throwable t) {
                    dismissProgress();

                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean validateInternalItems(List<Internal> list) {
        boolean flag = true;
//        boolean flag = false;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getType().equalsIgnoreCase("Select Type *") && list.get(i).getNoOfTrainingSessions() > 0 && list.get(i).getStaff() > 0 && list.get(i).getLabour() > 0) {

                } else {
                    flag = false;
                    Handler handler = new Handler();
                    final int finalI = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLVInternalRecyclerView.smoothScrollToPosition(finalI);
                        }
                    }, 100);
                    mLVInternalRecyclerView.setSelection(finalI);

                    break;
                }

            }
            if (flag) {

            } else {
                Utilities.showAlertDialog("Please fill all the fields in item", CreateManpowerListActivity.this);
            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return flag;
    }

    private boolean validateExternalItems(List<External> list) {
        boolean flag = true;
        // boolean flag = false;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getType().equalsIgnoreCase("Select Type *") && list.get(i).getNoOfTraininsessions() > 0 && list.get(i).getStaff() > 0 && list.get(i).getLabours() > 0) {
                    flag = true;
                } else {
                    flag = false;
                    Handler handler = new Handler();
                    final int finalI = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLVInternalRecyclerView.smoothScrollToPosition(finalI);
                        }
                    }, 100);
                    mLVInternalRecyclerView.setSelection(finalI);

                    break;
                }

            }
            if (flag) {

            } else {
                Utilities.showAlertDialog("Please fill all the fields in item", CreateManpowerListActivity.this);
            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return flag;
    }

}
