package com.ags.voltassafety;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.Editable;

import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ags.voltassafety.fcm.Config;
import com.ags.voltassafety.fcm.NotificationUtils;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.LoginCredentials;
import com.ags.voltassafety.model.LoginResponse;
import com.ags.voltassafety.model.NotificationModel;
import com.ags.voltassafety.model.NotificationResponse;
import com.ags.voltassafety.model.VersionUpdateModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    EditText edit_User_name, edit_password;
    FrameLayout login;
    private TextView version, date;
    TextView login_text_view, changePassword;
    TextInputLayout inputlayout_user_name, inputlayout_password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId;
    private String deviceId;
    LoginResponse loginResponse;
    TelephonyManager telephonyManager;
    EditText spDomain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);
            inputlayout_user_name = findViewById(R.id.inputlayout_user_name);
            inputlayout_password = findViewById(R.id.inputlayout_password);
            changePassword = findViewById(R.id.changePassword);

            date = findViewById(R.id.date);
            sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();
            //  finish();

            ArrayList<String> domainAL = new ArrayList<>();
            domainAL.add("--Select Domain--");
            domainAL.add("INDIA");
            domainAL.add("UAE");
            domainAL.add("QATAR");
            domainAL.add("OMAN");
            domainAL.add("SINGAPORE");

            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                    startActivity(intent);
                }
            });

            //regId = FirebaseInstanceId.getInstance().getToken();
            //displayFirebaseRegId();

            login_text_view = findViewById(R.id.login_text_view);
            //forgot_password = findViewById(R.id.forgot_password);
            //Edit Text
            edit_User_name = findViewById(R.id.edit_User_name);
            edit_password = findViewById(R.id.edit_password);
            spDomain = findViewById(R.id.edit_domain);
            String selectDomain = sharedPreferences.getString("Domain", null);

            spDomain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try{

                        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
                        AlertDialog alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this).setView(view).create();
                        ListView listBranch = view.findViewById(R.id.domain_list);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(LoginActivity.this, R.layout.support_simple_spinner_dropdown_item, domainAL);
                        listBranch.setAdapter(arrayAdapter);
                        listBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                spDomain.setText(domainAL.get(position));
                              String  strSelectedDomain = domainAL.get(position);
                                if(!strSelectedDomain.equalsIgnoreCase("--Select Domain--")){

                                    if(strSelectedDomain.equalsIgnoreCase("INDIA"))
                                        editor.putString("Domain", "SHQ");
                                    else
                                        editor.putString("Domain", strSelectedDomain);
                                }
                                alertDialogBuilder.dismiss();
                            }
                        });
                        alertDialogBuilder.show();


                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            });

           /* ArrayAdapter domainAdapter = new ArrayAdapter(LoginActivity.this, android.R.layout.simple_list_item_1, domainAL);
            domainAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            spDomain.setAdapter(domainAdapter);*/

           /* ArrayAdapter domainAdapter = new ArrayAdapter(LoginActivity.this, R.layout.list_view_items, R.id.lbl_name, domainAL);
            spDomain.setAdapter(domainAdapter);

            */
           if(selectDomain != null) {
                if(selectDomain.equalsIgnoreCase("SHQ")){
                    selectDomain = "INDIA";
                }
                for (int i = 0; i < domainAL.size(); i++) {
                    if (selectDomain.equalsIgnoreCase(domainAL.get(i))) {
                        spDomain.setText(domainAL.get(i));
                    }
                }
            }

       /* edit_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    inputlayout_password.setPasswordVisibilityToggleEnabled(true);
                }
                return false;
            }
        });*/
//            getFirebaseId();

          /*  spDomain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   String strSelectedDomain = (String) parent.getItemAtPosition(position);
                    if(!strSelectedDomain.equalsIgnoreCase("--Select Domain--")){

                        if(strSelectedDomain.equalsIgnoreCase("INDIA"))
                            editor.putString("Domain", "SHQ");
                        else
                            editor.putString("Domain", strSelectedDomain);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(LoginActivity.this, ConfirmPasswordActivity.class);
//                    startActivity(intent);
                }
            });
            edit_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (editable.toString().length() == 0) {
                        inputlayout_password.setPasswordVisibilityToggleEnabled(false);
                    } else {
                        inputlayout_password.setPasswordVisibilityToggleEnabled(true);
                    }

                }
            });


            if (sharedPreferences != null) {
                edit_User_name.setText(sharedPreferences.getString("UserId", null));
                edit_password.setText(sharedPreferences.getString("Password", null));
            }


            version = (TextView) findViewById(R.id.version);
            //                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
//                String versionCode = pInfo.versionName;
            // version.setText(getResources().getString(R.string.version));

            //fonts
            edit_User_name.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            login_text_view.setTypeface(Utilities.fontBold(getApplicationContext()));
            //forgot_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            version.setTypeface(Utilities.fontRegular(getApplicationContext()));

//
            edit_User_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        edit_User_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_person_selected_icon_24dp, 0, 0, 0);

                    } else {
                        edit_User_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_person_outline_black_24dp, 0, 0, 0);
                    }
                }
            });

            edit_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        edit_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock_selected_icon_24dp, 0, 0, 0);
                    } else {
                        edit_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, 0, 0);
                    }
                }
            });

            login = findViewById(R.id.login);

            PushDownAnim.setPushDownAnimTo(login)
                    .setScale(PushDownAnim.MODE_STATIC_DP, 18)//value for push down 18
                    .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                    .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                    .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                    .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
                    .setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("HardwareIds")
                        @Override
                        public void onClick(View view) {
                            if (validateUserName()) {
                                if (Utilities.isConnectingToInternet(LoginActivity.this)) {
//                            String tkn= FirebaseInstanceId.getInstance().getInstanceId();
//                                    if (checkPermissions()) {
                                   // downloadLatestApp();
                                   // if (Utilities.isConnectingToInternet(LoginActivity.this)) {
                                        validateUserLogin();
                                    //} else {
                                       // showAlert("Please Check Your Internet Connection");
                                    //}

//                                    }
                                    try {
                                        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                        deviceId = telephonyManager.getDeviceId();
                                    } catch (SecurityException e) {
                                    }
//                           validateUserLogin();
                                } else {
                                    showAlert("Please Check Your Internet Connection");
                                }
                            }
                        }
                    });


//            login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (validateUserName()) {
//                        if (Utilities.isConnectingToInternet(LoginActivity.this)) {
////                            String tkn= FirebaseInstanceId.getInstance().getInstanceId();
//                            if (checkPermissions()) {
//                                downloadLatestApp();
//
//
//                            }
//                            try {
//                                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                                deviceId = telephonyManager.getDeviceId();
//                            } catch (SecurityException e) {
//
//                            }
//
////                           validateUserLogin();
//                        } else {
//                            showAlert("Please Check Your Internet Connection");
//                        }
//                    }
//
//                }
//            });

        /*
        forgot_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                    startActivity(intent);
                }
            });
            */

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        //  Log.d("FirebaseToken", intent.getData().toString());


                        //displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
//                    txtMessage.setText(message);
                    }
                }
            };
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void validateUserLogin() {
        try {
//            if (checkPermissions()) {
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.validateUser(new LoginCredentials(edit_User_name.getText().toString(), edit_password.getText().toString(), ""));
            showProgressDialog(LoginActivity.this);
            call.enqueue(new Callback<LoginResponse>() {
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        try {
                            try {
                                Thread.sleep(400);
                                // close it after response
                                dismissProgress();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (response.body() != null) {
                                loginResponse = response.body();
                                Log.d("LoginResponse", loginResponse.getSuccess() + "");
                                if (loginResponse.getSuccess()) {

                                    if (loginResponse.getResult().getRoles() != null && loginResponse.getResult().getRoles().size() > 0) {

                                        editor.putString("Bearertoken", loginResponse.getResult().getToken());
                                        editor.putString("UserId", edit_User_name.getText().toString());
                                        editor.putString("Password", edit_password.getText().toString());
                                        editor.putString("userName", loginResponse.getResult().getUserName());
                                        editor.putString("roleId", loginResponse.getResult().getRoles().get(0));
                                        if(response.body().getResult().getEmployeeCode() != null) {
                                            editor.putString("LoginUserID", response.body().getResult().getEmployeeCode());
                                        }else{
                                            editor.putString("LoginUserID", "Admin");
                                        }
                                        editor.commit();
                                        //getFirebaseId();
                                        Intent observationviewintent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(observationviewintent);
                                        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                                    }
                                    else{
                                        Utilities.showAlertDialog("Role not available, please contact your administrator", LoginActivity.this);
                                    }
//                                        downloadLatestApp();
                                } else if (loginResponse.getErrors()[0].equalsIgnoreCase("EmployeeNotExisted")) {

                                    Intent intent = new Intent(LoginActivity.this, EmailRequestPopupActivity.class);
                                    intent.putExtra("strFlag", "EmployeeNotExisted");
                                    startActivity(intent);

                                } else if (loginResponse.getErrors()[0].contains("EmployeeNeedToResetPassword")) {

                                    Intent intent = new Intent(LoginActivity.this, EmailRequestPopupActivity.class);
                                    intent.putExtra("EmpCode", loginResponse.getErrors()[0].split(",")[1]);
                                    if (loginResponse.getErrors()[0].split(",").length > 2) {
                                        intent.putExtra("EmpName", loginResponse.getErrors()[0].split(",")[2]);
                                    }

                                    if (loginResponse.getErrors()[0].split(",").length > 3) {
                                        intent.putExtra("EmpEmail", loginResponse.getErrors()[0].split(",")[3]);
                                    }
                                    intent.putExtra("strFlag", "EmployeeNeedToResetPassword");
                                    startActivity(intent);
                                } else {
                                    showAlert(loginResponse.getErrors()[0]);
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), LoginActivity.this);
                    }
                }

                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("LoginResponse", t.getMessage() + "");
                }
            });
//            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
    }

    private boolean validateUserName() {
        try {

            if (edit_User_name.getText().toString().length() == 0) {

                String errorString = "Please Enter User Name";

                edit_User_name.setError(errorString);

                edit_User_name.requestFocus();
                return false;
            }
            if (edit_password.getText().toString().length() == 0) {

                String errorString = "Please Enter Password";  // Your custom error message.

                edit_password.setError(errorString);

                edit_password.requestFocus();
                return false;
            }
            if(spDomain.getText().toString().equalsIgnoreCase("--Select Domain--")){
               // setSpinnerError(spDomain, "Please select Domain");
                showAlert("Please select Domain");
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
        return true;
    }

    private void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.
        }
    }
    private boolean checkPermissions() {
        try {

            int readexternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readinternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int callPermition = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int phonereadpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//        int smspermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);


            List<String> permissionString = new ArrayList<String>();

            if (readexternal != PackageManager.PERMISSION_GRANTED) {
                permissionString.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (readinternal != PackageManager.PERMISSION_GRANTED) {
                permissionString.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (callPermition != PackageManager.PERMISSION_GRANTED) {
                permissionString.add(Manifest.permission.CALL_PHONE);
            }

            if (phonereadpermission != PackageManager.PERMISSION_GRANTED) {
                permissionString.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (location != PackageManager.PERMISSION_GRANTED) {
                permissionString.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (camerapermission != PackageManager.PERMISSION_GRANTED) {
                permissionString.add(Manifest.permission.CAMERA);
            }
//        if (smspermission != PackageManager.PERMISSION_GRANTED) {
//            permissionString.add(Manifest.permission.READ_SMS);
//        }

            if (!permissionString.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionString.toArray(new String[permissionString.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("TAG", "Permission callback called-------");
        try {
            switch (requestCode) {
                case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                    Map<String, Integer> perms = new HashMap<>();
                    // Initialize the map with both permissions
                    perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);


                    // Fill with actual results from user
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++)
                            perms.put(permissions[i], grantResults[i]);
                        // Check for both permissions
                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                            && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)
                        {

                            Log.d("TAG", "sms & location services permission granted");
                            // process the normal flow
                            //else any one or both the permissions are not granted

                        } else {
                            Log.d("TAG", "Some permissions are not granted ask again ");
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                                showDialogOK("LOCATION, CAMERA and MEDIA FILES required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        checkPermissions();
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        // proceed with logic by disabling the related features or quit the app.
                                                        break;
                                                }
                                            }
                                        });
                            }
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            else {
                                Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                        .show();
                                //proceed with logic by disabling the related features or quit the app.
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        //  FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        regId = refreshedToken;
        // Toast.makeText(getApplicationContext(),"re"+refreshedToken,Toast.LENGTH_SHORT).show();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

   /* private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String reg = pref.getString("regId", null);
        Log.d("deviceid", "Firebaseregid: " + regId);
        if (!android.text.TextUtils.isEmpty(regId)) {
            regId = reg;
            //   Toast.makeText(getApplicationContext(), "reg:" + regId, Toast.LENGTH_LONG).show();
        } else {
            regId = "1";
            // Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_LONG).show();
        }
    }*/

   /* public void getFirebaseId() {

        try {
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setDeviceId(deviceId);
            notificationModel.setDeviceToken(regId);
            notificationModel.setDeviceType("Android");
            notificationModel.setPlayerId("");
            notificationModel.setUserId("");
            notificationModel.setStatus("");

            Call<NotificationResponse> call = apiInterface.pushNotification("Bearer " + sharedPreferences.getString("Bearertoken", null), notificationModel);
            //showProgressDialog(LoginActivity.this);
            call.enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    Log.d("Responce", response.toString());
                    //dismissProgress();
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    //dismissProgress();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }*/
}
