package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ags.voltassafety.model.LoginCredentials;
import com.ags.voltassafety.model.LoginResponse;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestUserActivity extends AppCompatActivity {
    private LinearLayout hazard, nearmiss;
    private TextView incidentText, hazardText, cancel, titleText, textOne, textTwo, textThree;
    private Button creationPage;
    private String strType = "", subType = "";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "[0-9]{10}";
    EditText editUserName, editUserEmail, editUserNumber;

    ArrayList<String> zonesArrayList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LoginResponse loginResponse;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_user);
        validateUserLogin();
        sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        hazard = findViewById(R.id.hazard);
        nearmiss = findViewById(R.id.nearmiss);
        incidentText = findViewById(R.id.nearMissText);
        hazardText = findViewById(R.id.hazardText);
        creationPage = findViewById(R.id.creationPage);
        editUserName = findViewById(R.id.editName);
        editUserEmail = findViewById(R.id.editEmail);
        editUserNumber = findViewById(R.id.editMobileNumber);
        TextInputLayout inputName = findViewById(R.id.inputName);
        TextInputLayout inputEmail = findViewById(R.id.inputEmail);
        TextInputLayout inputNumber = findViewById(R.id.inputNumber);
        inputName.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        inputNumber.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        inputEmail.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        creationPage.setTypeface(Utilities.fontBold(GuestUserActivity.this));

        cancel = findViewById(R.id.cancel);
        titleText = findViewById(R.id.titleText);
        textOne = findViewById(R.id.textOne);
        textTwo = findViewById(R.id.textTwo);
        textThree = findViewById(R.id.textThree);
        cancel.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        textOne.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        textTwo.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        textThree.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        editUserName.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
        titleText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
        hazardText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
        incidentText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
        editor.putString("Domain", "null");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        hazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                hazardDefnation.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
                if (validation(editUserName, editUserEmail, editUserNumber)) {
                    Dialog dialogOne = new Dialog(GuestUserActivity.this, R.style.DialogOne);
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
                    titleText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
                    questionText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
                    answerText.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
                    creationPage.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
                    unsafeConditionText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
                    unsafeConditionAns.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
                    unsafeActText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
                    unsafeActAns.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                        }
                    });
                    creationPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strType = "Hazard";
                            subType = "Hazard";
//                        if (validation(editUserName, editUserEmail, editUserNumber)) {

                            ObservationHeader observationHeader = new ObservationHeader();
                            observationHeader.setTypeOfObservation(strType);
                            observationHeader.setReason(subType);
                            observationHeader.setGuestName(editUserName.getText().toString().trim());
                            observationHeader.setGuestEmail(editUserEmail.getText().toString().trim());
                            observationHeader.setGuestPhone(editUserNumber.getText().toString().trim());
//                   observationHeader.setUserId("USER000001");
                            if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident")) {
                                dialogOne.dismiss();
                                Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);

                                Bundle b = new Bundle();

                                b.putSerializable("HeaderObject", observationHeader);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                            } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
//dialogOne.dismiss();
                                dialogOne.dismiss();
                                Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Bundle b = new Bundle();
//
                                b.putSerializable("HeaderObject", observationHeader);
//                        b.putString("Guest",);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                            } else {
                                Snackbar.make(view, "Please select type", Snackbar.LENGTH_LONG).show();
                            }
//                    Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);
//                    ObservationHeader observationHeader;
//                    Bundle b = new Bundle();
//                    observationHeader = new ObservationHeader();
//                    observationHeader.setTypeOfObservation(strType);
//                    observationHeader.setReason(strType);
//                    b.putSerializable("HeaderObject", observationHeader);
//                    intent.putExtras(b);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                        }
                        }
                    });
                    dialogOne.show();

//                    }
//                });
//                hazard.setBackgroundResource(R.drawable.guest_user_proceed_bg);
//                hazardText.setTextColor(Color.WHITE);
//                incidentText.setTextColor(Color.BLACK);
//                nearmiss.setBackgroundResource(R.drawable.guest_user_unclick_bg);

//                observationHeader = new ObservationHeader();
//                observationHeader.setTypeOfObservation(strType);

                }
            }
        });


        nearmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                nearmiss.setBackgroundResource(R.drawable.guest_user_proceed_bg);
//                incidentText.setTextColor(Color.WHITE);
//                hazardText.setTextColor(Color.BLACK);
//                hazard.setBackgroundResource(R.drawable.guest_user_unclick_bg);
                if (validation(editUserName, editUserEmail, editUserNumber)) {
                    Dialog dialogOne = new Dialog(GuestUserActivity.this, R.style.DialogOne);
//                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(LandingPageActivity.this);
                    dialogOne.setContentView(R.layout.nearmiss_defnation_layout);
                    dialogOne.setCancelable(false);
                    ImageView close = dialogOne.findViewById(R.id.close);
                    TextView titleText = dialogOne.findViewById(R.id.titleText);
                    TextView questionText = dialogOne.findViewById(R.id.questionText);
                    TextView answerText = dialogOne.findViewById(R.id.answerText);
                    TextView creationPage = dialogOne.findViewById(R.id.creationPage);
                    titleText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
                    questionText.setTypeface(Utilities.fontBold(GuestUserActivity.this));
                    answerText.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
                    creationPage.setTypeface(Utilities.fontRegular(GuestUserActivity.this));
                    creationPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strType = "Incident";
                            subType = "Near Miss";
                            ObservationHeader observationHeader = new ObservationHeader();
                            observationHeader.setTypeOfObservation(strType);
                            observationHeader.setReason(subType);
                            observationHeader.setGuestName(editUserName.getText().toString().trim());
                            observationHeader.setGuestEmail(editUserEmail.getText().toString().trim());
                            observationHeader.setGuestPhone(editUserNumber.getText().toString().trim());
//                   observationHeader.setUserId("USER000001");
                            if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident")) {
                                dialogOne.dismiss();
                                Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Bundle b = new Bundle();

                                b.putSerializable("HeaderObject", observationHeader);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                            } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
//dialogOne.dismiss();
                                dialogOne.dismiss();
                                Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Bundle b = new Bundle();
//
                                b.putSerializable("HeaderObject", observationHeader);
//                        b.putString("Guest",);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                            } else {
                                Snackbar.make(view, "Please select type", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOne.dismiss();
                        }
                    });
                    dialogOne.show();
                }

            }
        });
      /*  creationPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation(editUserName, editUserEmail, editUserNumber)) {


                    ObservationHeader observationHeader = new ObservationHeader();
                    observationHeader.setTypeOfObservation(strType);
                    observationHeader.setReason(subType);
                    observationHeader.setGuestName(editUserName.getText().toString().trim());
                    observationHeader.setGuestEmail(editUserEmail.getText().toString().trim());
                    observationHeader.setGuestPhone(editUserNumber.getText().toString().trim());
//                   observationHeader.setUserId("USER000001");
                    if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident")) {

                        Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);

                        Bundle b = new Bundle();

                        b.putSerializable("HeaderObject", observationHeader);
                        intent.putExtras(b);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
//
                        Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);

                        Bundle b = new Bundle();
//
                        b.putSerializable("HeaderObject", observationHeader);
//                        b.putString("Guest",);
                        intent.putExtras(b);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    } else {
                        Snackbar.make(view, "Please select type", Snackbar.LENGTH_LONG).show();
                    }
//                    Intent intent = new Intent(GuestUserActivity.this, CreateObservationActivity.class);
//                    ObservationHeader observationHeader;
//                    Bundle b = new Bundle();
//                    observationHeader = new ObservationHeader();
//                    observationHeader.setTypeOfObservation(strType);
//                    observationHeader.setReason(strType);
//                    b.putSerializable("HeaderObject", observationHeader);
//                    intent.putExtras(b);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }


            }
        });*/


    }

    private boolean validation(EditText editUserName, EditText editUserEmail, EditText editUserNumber) {
        if (editUserName.getText().toString().length() == 0) {

            String errorString = "Please Enter User Name";

            editUserName.setError(errorString);

            editUserName.requestFocus();
            return false;
        }
       /* if (editUserEmail.getText().toString().length() == 0) {

            String errorString = "Please Enter Email";

            editUserEmail.setError(errorString);

            editUserEmail.requestFocus();
            return false;
        }
       */
        if (editUserEmail.getText().toString().length() > 0) {
            if (!editUserEmail.getText().toString().matches(emailPattern)) {
                editUserEmail.setError("Invalid email address");
                editUserEmail.requestFocus();
                return false;
            }
        }
        if (editUserNumber.getText().toString().length() == 0) {

            String errorString = "Please Enter Phone Number";  // Your custom error message.

            editUserNumber.setError(errorString);

            editUserNumber.requestFocus();
            return false;
        }
        if (!editUserNumber.getText().toString().matches(mobilePattern)) {
            editUserNumber.setError("Invalid Phone Number");
            editUserNumber.requestFocus();
            return false;
        }
        return true;
    }

    private void validateUserLogin() {
        try {
//            if (checkPermissions()) {
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.validateUser(new LoginCredentials("USER000001", "123456", ""));
//            showProgressDialog(GuestUserActivity.this);
            call.enqueue(new Callback<LoginResponse>() {
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d("Responce", response.toString());
                        try {
                            try {
                                Thread.sleep(400);
                                // close it after response
//                                dismissProgress();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (response.body() != null) {
                                loginResponse = response.body();
//                                token = loginResponse.getResult().getToken();
                                if (loginResponse.getSuccess()) {

                                    editor.putString("Bearertoken", loginResponse.getResult().getToken());
                                    //editor.putString("UserId", "USER000001");
                                    //editor.putString("Password", "12345");
                                    editor.putString("userName", loginResponse.getResult().getUserName());
                                    editor.putString("LoginUserID", "Guest");
                                    editor.putString("roleId", loginResponse.getResult().getRoles().get(0));
                                    editor.commit();

                                }
                            }

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
//                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), GuestUserActivity.this);
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
}
