package com.ags.voltassafety;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.ags.voltassafety.adapters.SlidingImageAdapter;
import com.ags.voltassafety.model.EmailLoginOTPVerificationResponse;
import com.ags.voltassafety.model.EmailSignInResponse;
import com.ags.voltassafety.model.EmailandOtpVerificationRequest;
import com.ags.voltassafety.model.EmainSignInRequest;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationResponse;
import com.ags.voltassafety.model.SendOtpResponse;
import com.ags.voltassafety.model.VersionUpdateModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LandingPageActivity extends BaseActivity {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private LinearLayout voltasEmployee, guestUser, employee, nearmissDefnation, hazardDefnation, incidentDefnation;
    private FrameLayout button;
    EditText editUserName, editUserEmail, editUserNumber;
    private static ViewPager viewPager;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "[0-9]{10}";
    private static final Integer[] images = {R.mipmap.ic_land_hazard, R.mipmap.ic_land_nearmiss, R.mipmap.ic_land_incident, R.mipmap.ic_land_hazard, R.mipmap.ic_land_hazard, 0};
    private String[] def = {"What Is Hazard? \n\nAny situation or Activity potential for injury and / or Property damage", "What Is Near Miss? \n\nA Near Miss is an unplanned event that did not result in injury, illness, or damage – but had the potential to do so.", "What Is Accident? \n\nAn accident is an incident which has given rise to injury, ill health or fatality and /or property damage", "What is Unsafe Act? \n\nAny personal activity may lead to injury or property damage or both", "What is Unsafe Condition? \n\nAny situation place supportive for human injury or property damage", ""};
    private String[] name = {"Hazard", "Near Miss", "Accident", "Unsafe Act", "Unsafe Condition", ""};
    private ArrayList<Integer> imageArray = new ArrayList<Integer>();
    private ArrayList<String> defnationArray = new ArrayList<String>();
    private ArrayList<String> nameArray = new ArrayList<String>();
    //    CircleIndicator indicator;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.
    CircleIndicator indicator;
    private TextView text, textOne, textTwo, textThree, videoText, employee_text/*, textFour, textFive, textSix, unsafeConditionText, unsafeActText*/;
    private SeekBar seekBar;
    private VideoView video_view;
    private Handler mHandler = new Handler();
    private ImageView close;
    String versionCodeOne;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText editEmail, edit_otp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        voltasEmployee = findViewById(R.id.voltasEmployee);
        videoText = findViewById(R.id.video);


        sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
        guestUser = findViewById(R.id.guestUser);
        employee = findViewById(R.id.employee);
        init();
        SpannableString content = new SpannableString("Awareness video : Precautions to be taken for Corona virus COVID-19");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        videoText.setText(content);
        videoText.setTypeface(Utilities.fontBold(LandingPageActivity.this));

        MediaController mediaController = new MediaController(this, true);

        videoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog videodialog = new Dialog(LandingPageActivity.this, R.style.MyDialogTheme);
                videodialog.setContentView(R.layout.video_covid_layout);
                videodialog.show();
                video_view = videodialog.findViewById(R.id.video_view);
                seekBar = (SeekBar) videodialog.findViewById(R.id.seekBar);
                close = videodialog.findViewById(R.id.close);
                text = videodialog.findViewById(R.id.video);
                text.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                /*mediaController.setAnchorView(video_view);
                video_view.setMediaController(mediaController);
                mediaController.setVisibility(View.VISIBLE);*/

                videodialog.setCancelable(false);
                video_view.requestFocus();
                video_view.setVideoPath("https://vserve.voltasworld.com/advent/CoronaSafetyInstruction.mp4");
                video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaController.show();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        videodialog.cancel();
                    }
                });
//                video_view.setOnPreparedListener(new MediaPlayer.OnCompletionListener() {
//
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mediaController.show();
//
//                    }
//                });
                seekBar.setProgress(0);
                seekBar.setMax(100);

//                video_view.setZOrderMediaOverlay(true);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean fromTouch) {

                        if (fromTouch) {
                            video_view.seekTo(i);
                        }
//                        Toast.makeText(getApplicationContext(), "seekbar progress: " + i, Toast.LENGTH_SHORT).show();
//                        video_view.seekTo(i);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
//                        mHandler.removeCallbacks(updateTimeTask);
                        onStartTouch(seekBar);
//                        video_view.seekTo(seekBar);
//                        Toast.makeText(getApplicationContext(), "seekbar Toch: ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        onStopTouch(seekBar);
//                        Toast.makeText(getApplicationContext(), "seekbar Stop: ", Toast.LENGTH_SHORT).show();
                    }
                });

                video_view.start();
            }
        });
//        nearmissDefnation = findViewById(R.id.nearmissDefnation);
//        hazardDefnation = findViewById(R.id.hazardDefnation);
//        incidentDefnation = findViewById(R.id.incidentDefnation);


        /*=====================Normal View Texts=========================*/

        textOne = findViewById(R.id.textOne);
        textTwo = findViewById(R.id.textTwo);
        textThree = findViewById(R.id.textThree);
        employee_text = findViewById(R.id.employee_text);
//        textFour = findViewById(R.id.textFour);
//        textFive = findViewById(R.id.textFive);
//        textSix = findViewById(R.id.textSix);
//        unsafeConditionText = findViewById(R.id.unsafeConditionText);
//        unsafeActText = findViewById(R.id.unsafeActText);
//        textSix.setTypeface(Utilities.fontBold(LandingPageActivity.this));
//        textFive.setTypeface(Utilities.fontBold(LandingPageActivity.this));
//        textFour.setTypeface(Utilities.fontBold(LandingPageActivity.this));
        textThree.setTypeface(Utilities.fontBold(LandingPageActivity.this));
        textTwo.setTypeface(Utilities.fontBold(LandingPageActivity.this));
        textOne.setTypeface(Utilities.fontBold(LandingPageActivity.this));
        employee_text.setTypeface(Utilities.fontBold(LandingPageActivity.this));
//        unsafeActText.setTypeface(Utilities.fontRegular(LandingPageActivity.this));
//        unsafeConditionText.setTypeface(Utilities.fontRegular(LandingPageActivity.this));

        /*===================Normal View Texts=========================*/


        guestUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                   if(Utilities.isConnectingToInternet(getApplicationContext())){

                       downloadLatestApp("GuestLogin");
                } else {
                    Utilities.showAlertDialog("Please check your internet connection", LandingPageActivity.this);
                }

                }

            }
        });
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent employeeintent = new Intent(LandingPageActivity.this,HomeActivityForEmployee.class);
//                startActivity(employeeintent);

                if (checkPermissions()) {

                    if(Utilities.isConnectingToInternet(getApplicationContext())){
                        downloadLatestApp("EmployeeLogin");

                } else {
                    Utilities.showAlertDialog("Please check your internet connection", LandingPageActivity.this);
                }


                }
            }
        });


       /* hazardDefnation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogOne = new Dialog(LandingPageActivity.this, R.style.DialogOne);
//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                dialogOne.setContentView(R.layout.hazard_defnation_layout);
                dialogOne.setCancelable(false);
                ImageView close = dialogOne.findViewById(R.id.close);
                TextView titleText = dialogOne.findViewById(R.id.titleText);
                TextView questionText = dialogOne.findViewById(R.id.questionText);
                TextView answerText = dialogOne.findViewById(R.id.answerText);
                titleText.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                questionText.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                answerText.setTypeface(Utilities.fontRegular(LandingPageActivity.this));
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogOne.dismiss();
                    }
                });
                dialogOne.show();

            }
        });*/
    /*    nearmissDefnation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogOne = new Dialog(LandingPageActivity.this, R.style.DialogOne);
//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                dialogOne.setContentView(R.layout.nearmiss_defnation_layout);
                dialogOne.setCancelable(false);
                ImageView close = dialogOne.findViewById(R.id.close);
                TextView titleText = dialogOne.findViewById(R.id.titleText);
                TextView questionText = dialogOne.findViewById(R.id.questionText);
                TextView answerText = dialogOne.findViewById(R.id.answerText);
                titleText.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                questionText.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                answerText.setTypeface(Utilities.fontRegular(LandingPageActivity.this));
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogOne.dismiss();
                    }
                });
                dialogOne.show();
//                overridePendingTransition(R.anim.animation_enter, R.anim.animation_enter);
            }
        });*/
      /*  incidentDefnation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogOne = new Dialog(LandingPageActivity.this, R.style.DialogOne);
//                dialogOne.getWindow().getDecorView().setTop(150);

//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                dialogOne.setContentView(R.layout.accident_defnation_layout);
                dialogOne.setCancelable(false);
                ImageView close = dialogOne.findViewById(R.id.close);
                TextView titleText = dialogOne.findViewById(R.id.titleText);
                TextView questionText = dialogOne.findViewById(R.id.questionText);
                TextView answerText = dialogOne.findViewById(R.id.answerText);
                titleText.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                questionText.setTypeface(Utilities.fontBold(LandingPageActivity.this));
                answerText.setTypeface(Utilities.fontRegular(LandingPageActivity.this));
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogOne.dismiss();
                    }
                });
                dialogOne.show();
            }
        });*/

        voltasEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {

                    if(Utilities.isConnectingToInternet(getApplicationContext())) {

                        downloadLatestApp("SafetyLogin");
                    }
                    else{
                        Utilities.showAlertDialog("Please check your internet connection", LandingPageActivity.this);
                    }

                }
            }
        });
    }

    private void employeeLogin() {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextInputLayout inputOTP, inputEmail;
    TextView cancel, submit, login;
    Dialog emploeedialog = new Dialog(LandingPageActivity.this, R.style.DialogOne);
                    emploeedialog.setContentView(R.layout.employee_login_popup);
    editEmail = emploeedialog.findViewById(R.id.editEmail);
    edit_otp = emploeedialog.findViewById(R.id.edit_otp);
    inputOTP = emploeedialog.findViewById(R.id.inputOTP);
    inputEmail = emploeedialog.findViewById(R.id.inputEmail);
    cancel = emploeedialog.findViewById(R.id.cancel);
    submit = emploeedialog.findViewById(R.id.submit);
    login = emploeedialog.findViewById(R.id.login);
                    emploeedialog.show();
                    emploeedialog.setCanceledOnTouchOutside(false);


                    inputEmail.setTypeface(Utilities.fontRegular(LandingPageActivity.this));
                    inputOTP.setTypeface(Utilities.fontRegular(LandingPageActivity.this));
                    edit_otp.requestFocus();


                    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (validateEmail(editEmail)) {

                // sendOTPtoEmail(editEmail.getText().toString().trim());

                //downloadLatestApp();

                try {
                    if (Utilities.isConnectingToInternet(getApplicationContext())) {
                        // SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                        ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                        Call<SendOtpResponse> call = apiInterface.getOtp(editEmail.getText().toString().trim());
                        final ProgressDialog progressDialog = new ProgressDialog(LandingPageActivity.this, R.style.MyDialogTheme);
                        progressDialog.setMessage("Please wait verifying....");
                        //progressDialog.setTitle("ProgressDialog");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        // show it
                        progressDialog.show();
                        //observationDataArrayList = new ArrayList<>();
                        call.enqueue(new Callback<SendOtpResponse>() {
                            public void onResponse(Call<SendOtpResponse> call, Response<SendOtpResponse> response) {

                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if (response.body().getSuccess()) {
                                        inputOTP.setVisibility(View.VISIBLE);
                                        login.setVisibility(View.VISIBLE);
                                        submit.setVisibility(View.GONE);
                                        inputEmail.setVisibility(View.GONE);

                                    } else {
                                        showAlert(response.body().getErrors().toString());
                                    }

                                } else {
                                    progressDialog.dismiss();
                                    Utilities.showAlertDialog("No data to display", LandingPageActivity.this);
                                }


                            }

                            public void onFailure(Call<SendOtpResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.d("LoginResponse", t.getMessage() + "");
                            }

                        });
                    } else {
                        Utilities.showAlertDialog("Please check your internet connection", LandingPageActivity.this);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


                //  Toast.makeText(LandingPageActivity.this, "email entered", Toast.LENGTH_SHORT).show();
            }


        }
    });
                    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emploeedialog.dismiss();
        }
    });
                    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (edit_otp.getText().toString().length() > 0) {

                EmailandOtpVerificationRequest emailandOtpVerificationRequest = new EmailandOtpVerificationRequest();
                emailandOtpVerificationRequest.setEmailID(editEmail.getText().toString().trim());
                emailandOtpVerificationRequest.setOtp(edit_otp.getText().toString().trim());

                try {
                    if (Utilities.isConnectingToInternet(getApplicationContext())) {
                        // SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                        ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                        Call<EmailLoginOTPVerificationResponse> call = apiInterface.getEmailandOtpVerification(emailandOtpVerificationRequest);
                        final ProgressDialog progressDialog = new ProgressDialog(LandingPageActivity.this, R.style.MyDialogTheme);
                        progressDialog.setMessage("Please wait verifying....");
                        //progressDialog.setTitle("ProgressDialog");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        // show it
                        progressDialog.show();
                        //observationDataArrayList = new ArrayList<>();
                        call.enqueue(new Callback<EmailLoginOTPVerificationResponse>() {
                            public void onResponse(Call<EmailLoginOTPVerificationResponse> call, Response<EmailLoginOTPVerificationResponse> response) {

                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if (response.body().getSuccess()) {
                                        getEmailSignIn(response.body().getResult());
                                        emploeedialog.dismiss();
                                    } else {
                                        showAlert(response.body().getErrors().toString());
                                    }

                                } else {
                                    progressDialog.dismiss();
                                    Utilities.showAlertDialog("No data to display", LandingPageActivity.this);
                                }


                            }

                            public void onFailure(Call<EmailLoginOTPVerificationResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.d("LoginResponse", t.getMessage() + "");
                            }

                        });
                    } else {
                        Utilities.showAlertDialog("Please check your internet connection", LandingPageActivity.this);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


            } else {
                showAlert("Enter OTP");
            }
        }
    });

//                    Intent intent = new Intent(LandingPageActivity.this, GuestUserActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.mysplashanimation, R.anim.mysplashanimation);
}


    private void getEmailSignIn(String username) {

        EmainSignInRequest emainSignInRequest = new EmainSignInRequest();
        emainSignInRequest.setLoginName(username);

        try {
            if (Utilities.isConnectingToInternet(getApplicationContext())) {
                // SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                Call<EmailSignInResponse> call = apiInterface.getMailSignIN(emainSignInRequest);
                final ProgressDialog progressDialog = new ProgressDialog(LandingPageActivity.this, R.style.MyDialogTheme);
                progressDialog.setMessage("Please wait verifying....");
                //progressDialog.setTitle("ProgressDialog");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                // show it
                progressDialog.show();
                //observationDataArrayList = new ArrayList<>();
                call.enqueue(new Callback<EmailSignInResponse>() {
                    public void onResponse(Call<EmailSignInResponse> call, Response<EmailSignInResponse> response) {

                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            if (response.body().getSuccess()) {
                                // getEmailSignIn(response.body().getResult());

                                editor.putString("Bearertoken", response.body().getResult().getToken());
                                //editor.putString("UserId", response.body().getResult().getUserId());
                                editor.putString("EmailId", editEmail.getText().toString().trim());
                                editor.putString("userName", response.body().getResult().getUserName());
                                editor.putString("roleId", response.body().getResult().getRoles().get(0).toString());
                                if(response.body().getResult().getEmployeeCode() != null) {
                                    editor.putString("LoginUserID", response.body().getResult().getEmployeeCode());
                                }else{
                                    editor.putString("LoginUserID", "Employee");
                                }

                                editor.commit();

                                Intent employeeintent = new Intent(LandingPageActivity.this, HomeActivityForEmployee.class);
                                startActivity(employeeintent);

                                //showAlert(response.body().getResult().getEmployeeCode() + "\n" + response.body().getResult().getUserName() + "\n " + response.body().getResult().getUserId());
                            } else {
                                showAlert(response.body().getErrors().toString());
                            }

                        } else {
                            progressDialog.dismiss();
                            Utilities.showAlertDialog("No data to display", LandingPageActivity.this);
                        }


                    }

                    public void onFailure(Call<EmailSignInResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("LoginResponse", t.getMessage() + "");
                    }

                });
            } else {
                Utilities.showAlertDialog("Please check your internet connection", LandingPageActivity.this);
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void sendOTPtoEmail(String emailID) {

    }


    private boolean validateEmail(EditText editEmail) {
        if (editEmail.getText().toString().length() > 0) {
            if (!editEmail.getText().toString().matches(emailPattern)) {
                editEmail.setError("Invalid email address");
                editEmail.requestFocus();
                return false;
            }
        }
        else{
            editEmail.setError("Please enter email id");
            editEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void init() {
        for (int i = 0; i < images.length; i++) {
            imageArray.add(images[i]);
            defnationArray.add(def[i]);
            nameArray.add(name[i]);
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new SlidingImageAdapter(LandingPageActivity.this, imageArray, defnationArray, nameArray));
            Handler handler = new Handler();
            indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(viewPager);
//            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

//                    if (viewPager.getCurrentItem() < imageArray.size() - 1) {
//                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                    } else {
//                        viewPager.setCurrentItem(0);
//                    }
                    if (currentPage == imageArray.size() - 1) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);

        }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            seekBar.setProgress(video_view.getCurrentPosition());
            seekBar.setMax(video_view.getDuration());
            mHandler.postDelayed(this, 100);
        }
    };

    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch) {

    }

    public void onStartTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
    }

    public void onStopTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
        video_view.seekTo(seekbar.getProgress());
        updateProgressBar();
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

    private void downloadLatestApp(String strUserFlag) {
        try {
            showProgressDialog(LandingPageActivity.this);
            PackageInfo pInfo = null;
            try {
                pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                int versionCode = pInfo.versionCode;
                versionCodeOne = String.valueOf(versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<VersionUpdateModel> call = apiInterface.getUpdateAppVersion( versionCodeOne, "Android");
            call.enqueue(new Callback<VersionUpdateModel>() {
                @Override
                public void onResponse(Call<VersionUpdateModel> call, Response<VersionUpdateModel> response) {
                    if (response.isSuccessful()) {
                        dismissProgress();
                        if (response.body().getResult() != null) {

                            try {
                                if (!response.body().getResult().equalsIgnoreCase("NoUpdateAvailability")) {

                                    Intent intent = new Intent(LandingPageActivity.this, VersionUpdateActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {

                                    if(strUserFlag.equalsIgnoreCase("SafetyLogin")){

                                        Intent intent = new Intent(LandingPageActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.mysplashanimation, R.anim.mysplashanimation);
                                    }
                                    else if(strUserFlag.equalsIgnoreCase("EmployeeLogin")){

                                        employeeLogin();
                                    }
                                    else{

                                        Intent intent = new Intent(LandingPageActivity.this, GuestUserActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.mysplashanimation, R.anim.mysplashanimation);
                                    }
                                }
                            } catch (Exception e) {
                                dismissProgress();
                                e.getMessage();
                            }
                        } else {
                            dismissProgress();
                            Utilities.showAlertDialog(response.body().getErrors()[0], LandingPageActivity.this);
                        }

                    } else {
                        dismissProgress();
                        Utilities.showAlertDialog("" + response.message(), LandingPageActivity.this);
                    }

                }

                @Override
                public void onFailure(Call<VersionUpdateModel> call, Throwable t) {
                    dismissProgress();
                }
            });
        } catch (Exception e) {
            dismissProgress();
            e.getMessage();
        }
    }
}
