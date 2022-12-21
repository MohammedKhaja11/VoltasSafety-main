package com.ags.voltassafety;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.voltassafety.model.ChangePassword;
import com.ags.voltassafety.model.ChangePasswordModel;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailRequestPopupActivity extends BaseActivity {
    private EditText edit_oldPassword, edit_new_password, edit_cofirm_password, edit_userId;
    private TextInputLayout inputlayout_oldPassword, inputlayout_new_password, inputlayout_cofirm_password;
    private ImageView back;
    private int domineId = 1;
    private String strFlag;
    private TextView information_text;
//    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strFlag = getIntent().getExtras().getString("strFlag");

        if (strFlag.equalsIgnoreCase("EmployeeNotExisted")) {
            setContentView(R.layout.activity_user_not_exist);
            information_text = findViewById(R.id.information_text);
            edit_userId = findViewById(R.id.et_empcode);
            edit_oldPassword = findViewById(R.id.et_empname);
            edit_new_password = findViewById(R.id.et_empemail);
            edit_cofirm_password = findViewById(R.id.et_reporting_email);
            inputlayout_oldPassword = findViewById(R.id.inputlayout_oldPassword);
            inputlayout_new_password = findViewById(R.id.inputlayout_new_password);
            inputlayout_cofirm_password = findViewById(R.id.inputlayout_cofirm_password);
            back = findViewById(R.id.back);
            edit_oldPassword.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_new_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_cofirm_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            information_text.setTypeface(Utilities.fontRegular(getApplicationContext()));
            information_text.setText("Emp code does not exist, please contact your reporting manager and get the details added");
            //inputlayout_new_password.setVisibility(View.INVISIBLE);
        } else {
            setContentView(R.layout.activity_user_pwd_reset);
            information_text = findViewById(R.id.information_text);
            edit_userId = findViewById(R.id.et_empcode);
            edit_oldPassword = findViewById(R.id.et_empname);
            edit_new_password = findViewById(R.id.et_empemail);
            edit_cofirm_password = findViewById(R.id.et_reporting_email);
            inputlayout_oldPassword = findViewById(R.id.inputlayout_oldPassword);
            inputlayout_new_password = findViewById(R.id.inputlayout_new_password);
            inputlayout_cofirm_password = findViewById(R.id.inputlayout_cofirm_password);
            back = findViewById(R.id.back);
            edit_oldPassword.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_new_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_cofirm_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
            information_text.setTypeface(Utilities.fontRegular(getApplicationContext()));
            edit_userId.setText(getIntent().getExtras().getString("EmpCode"));
            edit_oldPassword.setText(getIntent().getExtras().getString("EmpName"));
            edit_new_password.setText(getIntent().getExtras().getString("EmpEmail"));
            information_text.setText("Invalid password or Password expired, please contact your reporting manager to reset your password");
            //inputlayout_new_password.setVisibility(View.VISIBLE);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(EmailRequestPopupActivity.this, LoginActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }

        });
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(EmailRequestPopupActivity.this, LoginActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }
        });
        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strFlag.equalsIgnoreCase("EmployeeNotExisted")) {
                    if (validateOtp()) {
                        if (Utilities.isValidEmail(edit_cofirm_password.getText().toString().trim())) {
                            if (Utilities.isConnectingToInternet(EmailRequestPopupActivity.this)) {

                                setEmailRequestForUser(edit_userId.getText().toString().trim(), edit_oldPassword.getText().toString().trim(), edit_cofirm_password.getText().toString().trim());
                            } else {
                                showAlert("Please Check Your Internet Connection");
                            }
                        } else {
                            showAlert("Please enter valid email id");
                        }
                    }
                } else {
                    if (validate()) {
                        if (Utilities.isValidEmail(edit_new_password.getText().toString().trim())) {
                            if (Utilities.isValidEmail(edit_cofirm_password.getText().toString().trim())) {
                                setEmailRequestForPwd(edit_userId.getText().toString().trim(), edit_oldPassword.getText().toString().trim(),edit_new_password.getText().toString().trim(),edit_cofirm_password.getText().toString().trim());
                            }else {
                                showAlert("Please enter valid email id");
                            }
                        }else {
                            showAlert("Please enter valid email id");
                        }
                    }
                }
            }
        });
    /*    edit_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() == 0) {
                    inputlayout_new_password.setPasswordVisibilityToggleEnabled(false);
                } else {
                    inputlayout_new_password.setPasswordVisibilityToggleEnabled(true);
                }

            }
        });*/
    }

    private void setEmailRequestForUser(String empCode, String name, String reportingEmail) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<CreateResponse> call = apiInterface.setEmailRequestForUserCreate(reportingEmail, empCode, name);
            showProgressDialog(EmailRequestPopupActivity.this);
            call.enqueue(new Callback<CreateResponse>() {
                @Override
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                    if (response.isSuccessful()) {
                        dismissProgress();
                        if (response.body().getResult() != null) {
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EmailRequestPopupActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Email sent successfully")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent1 = new Intent(EmailRequestPopupActivity.this, LoginActivity.class);
                                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent1);
                                        }
                                    }).create().show();

                        } else {
                            dismissProgress();
                            showAlert("" + response.body().getErrors()[0]);
                        }

                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }

                }

                @Override
                public void onFailure(Call<CreateResponse> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.getMessage();
            Utilities.showAlertDialog(e.getMessage(), EmailRequestPopupActivity.this);

        }
    }


    private void setEmailRequestForPwd(String empCode, String name, String email,String reportingEmail) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<CreateResponse> call = apiInterface.setEmailRequestForPwdReset(reportingEmail,email,empCode,name);
            showProgressDialog(EmailRequestPopupActivity.this);
            call.enqueue(new Callback<CreateResponse>() {
                @Override
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                    if (response.isSuccessful()) {
                        dismissProgress();
                        if (response.body().getResult() != null) {
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EmailRequestPopupActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Email sent successfully")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent1 = new Intent(EmailRequestPopupActivity.this, LoginActivity.class);
                                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent1);
                                        }
                                    }).create().show();

                        } else {
                            dismissProgress();
                            showAlert("" + response.body().getErrors()[0]);
                        }

                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }

                }

                @Override
                public void onFailure(Call<CreateResponse> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.getMessage();
            Utilities.showAlertDialog(e.getMessage(), EmailRequestPopupActivity.this);

        }
    }
    private boolean validateOtp() {
        try {
            if (edit_userId.getText().toString().length() == 0) {
                edit_userId.setError("Please Enter Employee Code");
                edit_userId.requestFocus();
                return false;
            }
            if (edit_oldPassword.getText().toString().length() == 0) {
                edit_oldPassword.setError("Please Enter Employee Name");
                edit_oldPassword.requestFocus();
                return false;
            }
            if (edit_cofirm_password.getText().toString().length() == 0) {
                edit_cofirm_password.setError("Please Enter Reporting Manager Email");
                edit_cofirm_password.requestFocus();
                return false;
            }

        } catch (Exception e) {
            e.getMessage();
            Log.d("Confirm Password", "" + e.getMessage());
        }
        return true;
    }

    private boolean validate() {
        try {
            if (edit_userId.getText().toString().length() == 0) {
                edit_userId.setError("Please Enter Employee Code");
                edit_userId.requestFocus();
                return false;
            }
            if (edit_oldPassword.getText().toString().length() == 0) {
                edit_oldPassword.setError("Please Enter Employee Name");
                edit_oldPassword.requestFocus();
                return false;
            }
            if (edit_new_password.getText().toString().length() == 0) {
                edit_new_password.setError("Please Enter Employee Email");
                edit_new_password.requestFocus();
                return false;
            }
            if (edit_cofirm_password.getText().toString().length() == 0) {
                edit_cofirm_password.setError("Please Enter Reporting Manager Email");
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
