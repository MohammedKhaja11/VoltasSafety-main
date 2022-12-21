package com.ags.voltassafety;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.LoginCredentials;
import com.ags.voltassafety.model.LoginResponse;
import com.ags.voltassafety.model.ManPowerResponse;
import com.ags.voltassafety.model.RegisterInputModel;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.model.UserProfile;
import com.ags.voltassafety.model.UserProfileResponse;
import com.ags.voltassafety.model.UserZoneResponse;
import com.ags.voltassafety.model.Verticleresponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRegisterationActivity extends BaseActivity {

    EditText mETPFNumber,mETEmpName,mETProjectName,mETEmail,mETMobileNo,mETProjectInchargeName,mETProjectInchargeEmail,mETProjectInchargeMobileNo;
    TextView mTVVertical,mTVZone,mTVBranch,mCancel,mSubmit,mTitle;
    Spinner mSPVertical,mSPZone,mSPBranch;
    ArrayList<String> zonesArrayList,verticleArrayList;
    ArrayList<String> zonesArrayListId;
    ArrayList<String> branchArrayList;
    ArrayList<String> branchArrayListId;
    ArrayAdapter zoneAdapter, branchArrayAdapter,verticalAdapter;
    private String zoneID,branchID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
                setContentView(R.layout.activity_mis_register);
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                RegisterInputModel obInputModel = new RegisterInputModel();
                SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                mETPFNumber = findViewById(R.id.edit_pfnumber);
                mETEmpName = findViewById(R.id.edit_empName);
                mETProjectName = findViewById(R.id.edit_projectname);
                mETEmail = findViewById(R.id.edit_emailid);
                mETMobileNo = findViewById(R.id.edit_mobileno);
                mTVVertical = findViewById(R.id.textVertical);
                mSPVertical = findViewById(R.id.spinnerVertical);
                mTVZone = findViewById(R.id.textZone);
                mSPZone = findViewById(R.id.spinnerZone);
                mTVBranch = findViewById(R.id.textBranch);
                mSPBranch = findViewById(R.id.spinnerBranch);
                mETProjectInchargeName = findViewById(R.id.edit_projectInchargeName);
                mETProjectInchargeEmail = findViewById(R.id.edit_ProjectInchargeemail);
                mETProjectInchargeMobileNo = findViewById(R.id.edit_ProjectInchargemobileno);
                mCancel = findViewById(R.id.cancel);
                mSubmit = findViewById(R.id.submit);
                mTitle = findViewById(R.id.create_title);

                mTitle.setTypeface(Utilities.fontBold(getApplicationContext()));
                mCancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
                mSubmit.setTypeface(Utilities.fontRegular(getApplicationContext()));
                mETPFNumber.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETEmpName.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETProjectName.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETPFNumber.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETEmail.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETMobileNo.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mTVVertical.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mTVZone.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mTVBranch.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETProjectInchargeName.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETProjectInchargeEmail.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));
                mETProjectInchargeMobileNo.setTypeface(Utilities.fontRegular(EmployeeRegisterationActivity.this));

            if(Utilities.isConnectingToInternet(EmployeeRegisterationActivity.this)){

                getUserData();
            }
            else{
                showAlert("Please Check Your Internet Connection");
            }

            //mETPFNumber.setText(sharedPreferences.getString("LoginUserID", null));
            //mETEmpName.setText(sharedPreferences.getString("userName", null));

               mSPZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Zone *")) {
                        zoneID = "";
                        mSPBranch.setAdapter(null);
                    } else {

                        zoneID = zonesArrayListId.get(i);
                        getBranches();
                    }
                    //obInputModel.setGuestZone(adapterView.getSelectedItem().toString());
                    obInputModel.setZoneId(zoneID);


                    Log.d("zoneId", zoneID + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mSPBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Branch *")) {
                        branchID = "";
                    } else {
                        branchID = branchArrayListId.get(i);

                    }
                    //obInputModel.setGuestBranch(adapterView.getSelectedItem().toString());
                    obInputModel.setBranchId(branchID);
//                    getBranches();

                    Log.d("branchID", branchID + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

                    try{
                        if(validate()) {
                            obInputModel.setType("MIS");
                            obInputModel.setPfno(mETPFNumber.getText().toString().trim());
                            obInputModel.setEmpName(mETEmpName.getText().toString().trim());
                            obInputModel.setProjectName(mETProjectName.getText().toString().trim());
                            obInputModel.setMobileNo(mETMobileNo.getText().toString().trim());
                            obInputModel.setEmailID(mETEmail.getText().toString().trim());
                            obInputModel.setVerticalId(mSPVertical.getSelectedItem().toString().trim());
                            //obInputModel.setZoneId(mSPZone.getSelectedItem().toString().trim());
                            //obInputModel.setBranchId(mSPBranch.getSelectedItem().toString().trim());
                            obInputModel.setProjectInchargeName(mETProjectInchargeName.getText().toString().trim());
                            obInputModel.setProjectInchargeEmail(mETProjectInchargeEmail.getText().toString().trim());
                            obInputModel.setProjectInchargeMobileNo(mETProjectInchargeMobileNo.getText().toString().trim());

                            if (Utilities.isConnectingToInternet(EmployeeRegisterationActivity.this)) {

                                setRegisterData(obInputModel);
                            } else {
                                showAlert("Please Check Your Internet Connection");
                            }
                        }
                    }catch (Exception e){
                        e.getMessage();
                    }

                }
            });
        }catch (Exception e){
            e.getMessage();
        }
    }

    private void setRegisterData(RegisterInputModel inputModel) {
        try {
            //CreateResponse registerResponse;
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Call<CreateResponse> call = apiInterface.createRegistration("Bearer "+sharedPreferences.getString("Bearertoken", null),inputModel);
            showProgressDialog(EmployeeRegisterationActivity.this);
            call.enqueue(new Callback<CreateResponse>() {
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
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
                                CreateResponse registerResponse = response.body();

                                Intent intent = new Intent(EmployeeRegisterationActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //intent.putExtra("Name", "HAZARD REPORTING");
                                startActivity(intent);
                            }
                            else{
                                Utilities.showAlertDialog(response.body().getErrors()[0],EmployeeRegisterationActivity.this);
                            }

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), EmployeeRegisterationActivity.this);
                    }
                }

                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    Log.d("LoginResponse", t.getMessage() + "");
                }
            });
//            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
    }

    private void getUserData() {
        try {
            //CreateResponse registerResponse;
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Call<UserProfileResponse> call = apiInterface.getUserProfile("Bearer "+sharedPreferences.getString("Bearertoken", null));
            showProgressDialog(EmployeeRegisterationActivity.this);
            call.enqueue(new Callback<UserProfileResponse>() {
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    if (response.isSuccessful()) {
                        try {
                                Thread.sleep(400);
                                // close it after response
                                dismissProgress();
                                if(response.body().getResult() != null){
                                    UserProfile userProfile = response.body().getResult();
                                    mETPFNumber.setText(userProfile.getEmployeeCode());
                                    mETEmpName.setText(userProfile.getUserName());
                                    mETEmail.setText(userProfile.geteMail());
                                    mETMobileNo.setText(userProfile.getPhoneNo());
                                }
                            getVerticle();

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), EmployeeRegisterationActivity.this);
                    }
                }

                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    Log.d("LoginResponse", t.getMessage() + "");
                }
            });
//            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
    }
    private void getVerticle() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<Verticleresponse> call = apiInterface.getVerticleData("Bearer " + sharedPreferences.getString("Bearertoken", null));
            showProgressDialog(EmployeeRegisterationActivity.this);
            call.enqueue(new Callback<Verticleresponse>() {
                public void onResponse(Call<Verticleresponse> call, Response<Verticleresponse> response) {
                    verticleArrayList = new ArrayList<>();
                    verticleArrayList.add("Select Vertical *");
                    if (response.isSuccessful()) {
                        dismissProgress();
                        if (response.body().getResult() != null) {

                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                verticleArrayList.add(response.body().getResult().get(i).getClientName());
                            }
                            verticalAdapter = new ArrayAdapter(EmployeeRegisterationActivity.this, android.R.layout.simple_list_item_1, verticleArrayList);
                            verticalAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            mSPVertical.setAdapter(verticalAdapter);
                            getZones();

                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<Verticleresponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();

        }
    }
    private void getZones() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserZoneResponse> call = apiInterface.getGuestZoneResponse("Bearer " + sharedPreferences.getString("Bearertoken", null),sharedPreferences.getString("Domain", null));
            showProgressDialog(EmployeeRegisterationActivity.this);
            call.enqueue(new Callback<UserZoneResponse>() {
                public void onResponse(Call<UserZoneResponse> call, Response<UserZoneResponse> response) {
                    zonesArrayList = new ArrayList<>();
                    zonesArrayListId = new ArrayList<>();
                    zonesArrayListId.add("");
                    zonesArrayList.add("Select Zone *");
                    if (response.isSuccessful()) {
                       dismissProgress();
                        Log.d("", response.toString());
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            zonesArrayList.add(response.body().getResult().get(i).getZoneName());
                            zonesArrayListId.add(response.body().getResult().get(i).getZoneId());

                        }

                        zoneAdapter = new ArrayAdapter(EmployeeRegisterationActivity.this, android.R.layout.simple_list_item_1, zonesArrayList);
                        zoneAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        mSPZone.setAdapter(zoneAdapter);

                      /*  if (observationHeader.getGuestZone() != null) {
                            int position = zoneAdapter.getPosition(observationHeader.getGuestZone());
                            mSPZone.setSelection(position);
                        }*/


                    }

                }


                public void onFailure(Call<UserZoneResponse> call, Throwable t) {
                   dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            dismissProgress();

        }
    }

    private void getBranches() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserBranchResponse> call = apiInterface.getGuestBranchResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), zoneID);
            showProgressDialog(EmployeeRegisterationActivity.this);
            call.enqueue(new Callback<UserBranchResponse>() {
                public void onResponse(Call<UserBranchResponse> call, Response<UserBranchResponse> response) {
                    branchArrayList = new ArrayList<>();
                    branchArrayListId = new ArrayList<>();
                    branchArrayListId.add("");
                    branchArrayList.add("Select Branch *");
                    if (response.isSuccessful()) {
                       dismissProgress();
                        Log.d("", response.toString());
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            branchArrayList.add(response.body().getResult().get(i).getBranchName());
                            branchArrayListId.add(response.body().getResult().get(i).getBranchId());

                        }
                        branchArrayAdapter = new ArrayAdapter(EmployeeRegisterationActivity.this, android.R.layout.simple_list_item_1, branchArrayList);
                        branchArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        mSPBranch.setAdapter(branchArrayAdapter);

                       /* if (observationHeader.getGuestBranch() != null) {
                            String branch = observationHeader.getGuestBranch();
                            int position = branchArrayAdapter.getPosition(branch);
                            spinnerBranch.setSelection(position);
                        }
*/

                    } else {
                        dismissProgress();
                    }

                }


                public void onFailure(Call<UserBranchResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            dismissProgress();

        }
    }
    private boolean validate() {
        try {

            if (mETPFNumber.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PF No";
                mETPFNumber.setError(errorString);
                mETPFNumber.requestFocus();
                return false;
            }

            if (mETEmpName.getText().toString().trim().length() == 0) {

                String errorString = "Please Select Emp Name";
                mETEmpName.setError(errorString);
                mETEmpName.requestFocus();
                return false;
            }
            if (mETProjectName.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Name";
                mETProjectName.setError(errorString);
                mETProjectName.requestFocus();
                return false;
            }
            if (mETEmail.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Email id";
                mETEmail.setError(errorString);
                mETEmail.requestFocus();
                return false;
            }
            if (mETMobileNo.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Mobile No";
                mETMobileNo.setError(errorString);
                mETMobileNo.requestFocus();
                return false;
            }

            if (mSPVertical.getSelectedItem().toString().equalsIgnoreCase("Select Vertical *")) {
                Utilities.showAlertDialog("Please Select Vertical", EmployeeRegisterationActivity.this);
                return false;
            }
            if (mSPZone.getSelectedItem().toString().equalsIgnoreCase("Select Zone *")) {
                Utilities.showAlertDialog("Please Select Zone", EmployeeRegisterationActivity.this);
                return false;
            }
            if (mSPBranch.getSelectedItem().toString().equalsIgnoreCase("Select Branch *")) {
                Utilities.showAlertDialog("Please Select Branch", EmployeeRegisterationActivity.this);
                return false;
            }
            if (mETProjectInchargeName.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Incharge Name";
                mETProjectInchargeName.setError(errorString);
                mETProjectInchargeName.requestFocus();
                return false;
            }
            if (mETProjectInchargeEmail.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Incharge Email";
                mETProjectInchargeEmail.setError(errorString);
                mETProjectInchargeEmail.requestFocus();
                return false;
            }
            if (mETProjectInchargeMobileNo.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Incharge MobileNo";
                mETProjectInchargeMobileNo.setError(errorString);
                mETProjectInchargeMobileNo.requestFocus();
                return false;
            }

        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
        return true;
    }
}
