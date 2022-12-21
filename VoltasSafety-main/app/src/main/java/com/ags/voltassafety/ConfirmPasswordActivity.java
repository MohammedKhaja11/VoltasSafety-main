package com.ags.voltassafety;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ags.voltassafety.model.ConfirmPassword;
import com.ags.voltassafety.model.ConfirmPasswordResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPasswordActivity extends BaseActivity {
    TextView information_text;
    EditText edit_Otp, edit_new_password, edit_cofirm_password;
    TextInputLayout inputlayout_new_password, inputlayout_cofirm_password, inputlayout_otp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.confirm_new_password);
            information_text = findViewById(R.id.information_text);
            edit_Otp = findViewById(R.id.edit_Otp);
            edit_new_password = findViewById(R.id.edit_new_password);
            edit_cofirm_password = findViewById(R.id.edit_cofirm_password);
            inputlayout_otp = findViewById(R.id.inputlayout_otp);
            inputlayout_new_password = findViewById(R.id.inputlayout_new_password);
            inputlayout_cofirm_password = findViewById(R.id.inputlayout_cofirm_password);

            information_text.setText("OTP Sent To Your Registered  " + getIntent().getStringExtra("Email") + " / " + getIntent().getStringExtra("mobilenumber"));

            //fonts
            information_text.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_Otp.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_new_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_cofirm_password.setTypeface(Utilities.fontRegular(getApplicationContext()));


            findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validateOtp()) {
                        if (Utilities.isConnectingToInternet(ConfirmPasswordActivity.this)) {
                            confirmPassword();
                        } else {
                            showAlert("Please Check Your Internet Connection");
                        }
                    }
                }
            });

            findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.getMessage();
            Log.d("Confirm Password", "" + e.getMessage());
        }
    }

    private void confirmPassword() {
        try {
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            byte[] encodedData = Base64.encode(edit_new_password.getText().toString().trim().getBytes(), Base64.NO_WRAP);
            //String passwordstring = new String(encodedData);
            ConfirmPassword objNewPwd = new ConfirmPassword();
            objNewPwd.setUserId(getIntent().getStringExtra("UserId"));
            objNewPwd.setOTP(edit_Otp.getText().toString().trim());
            objNewPwd.setNewPassword(edit_new_password.getText().toString().trim());
            Call<ConfirmPasswordResponse> call = apiInterface.setResetPassword(objNewPwd);
            showProgressDialog(ConfirmPasswordActivity.this);
            call.enqueue(new Callback<ConfirmPasswordResponse>() {
                public void onResponse(Call<ConfirmPasswordResponse> call, Response<ConfirmPasswordResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getResult() != null) {
                            ConfirmPasswordResponse comfirm_password_response = response.body();
                            Log.d("comfirm_password", response.body() + "");
                            if (response.body() != null) {
                                if (comfirm_password_response.getSuccess()) {

                                    dismissProgress();
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfirmPasswordActivity.this, R.style.MyDialogTheme);
                                    //alertDialog.setTitle("Information");
                                    alertDialog.setMessage("Password changed successfully");
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            Intent loginactivity = new Intent(ConfirmPasswordActivity.this, LoginActivity.class);
                                            loginactivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(loginactivity);
                                        }
                                    });
                                    alertDialog.show();


                                } else {
                                    dismissProgress();
                                    showAlert(comfirm_password_response.getErrors() + "");
                                    //showToast(loginResponse.getErrors() + "");
                                }
                            }
                        } else {
                            dismissProgress();
                            Utilities.showAlertDialog("" + response.body().getErrors()[0], ConfirmPasswordActivity.this);
                        }
                    } else {
                        dismissProgress();
                        Log.d("Error", "" + response.code());
                        Utilities.showAlertDialog("Error " + response.message(), ConfirmPasswordActivity.this);
                    }
                }

                public void onFailure(Call<ConfirmPasswordResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
            Log.d("Confirm Password", "" + e.getMessage());
        }
    }

    private boolean validateOtp() {
        try {
            if (edit_Otp.getText().toString().length() == 0) {
                inputlayout_otp.setError(getString(R.string.enter_otp));
                edit_Otp.requestFocus();
                return false;
            }
            if (edit_new_password.getText().toString().length() == 0) {
                inputlayout_new_password.setError(getString(R.string.enter_password));
                edit_new_password.requestFocus();
                return false;
            }
            if (!edit_cofirm_password.getText().toString().equals(edit_new_password.getText().toString())) {
                inputlayout_cofirm_password.setError(getString(R.string.incorrect_password));
                edit_cofirm_password.requestFocus();
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Confirm Password", "" + e.getMessage());
        }
        return true;
    }
}

