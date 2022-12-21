package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ags.voltassafety.model.ChangePassword;
import com.ags.voltassafety.model.ChangePasswordModel;
import com.ags.voltassafety.model.ConfirmPassword;
import com.ags.voltassafety.model.ConfirmPasswordResponse;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    private EditText edit_oldPassword, edit_new_password, edit_cofirm_password, edit_userId;
    private TextInputLayout inputlayout_oldPassword, inputlayout_new_password, inputlayout_cofirm_password;
    private ImageView back;
    private int domineId = 1;
//    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edit_userId = findViewById(R.id.edit_userId);
        edit_oldPassword = findViewById(R.id.edit_oldPassword);
        edit_new_password = findViewById(R.id.edit_new_password);
        edit_cofirm_password = findViewById(R.id.edit_cofirm_password);
        inputlayout_oldPassword = findViewById(R.id.inputlayout_oldPassword);
        inputlayout_new_password = findViewById(R.id.inputlayout_new_password);
        inputlayout_cofirm_password = findViewById(R.id.inputlayout_cofirm_password);
        back = findViewById(R.id.back);

        edit_oldPassword.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edit_new_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edit_cofirm_password.setTypeface(Utilities.fontRegular(getApplicationContext()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }

        });
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }
        });
        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateOtp()) {
                    if (Utilities.isConnectingToInternet(ChangePasswordActivity.this)) {
//                        showAlert("OK");
                        changePassword(domineId, edit_userId.getText().toString().trim(), edit_oldPassword.getText().toString().trim(), edit_cofirm_password.getText().toString().trim());
                    } else {
                        showAlert("Please Check Your Internet Connection");
                    }
                }
            }
        });
        edit_new_password.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void changePassword(int domineId, String userId, String oldPassword, String newPassword) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ChangePasswordModel> call = apiInterface.changePassword("Bearer " + sharedPreferences.getString("Bearertoken", null), new ChangePassword(domineId, userId, oldPassword, newPassword));
            showProgressDialog(ChangePasswordActivity.this);
            call.enqueue(new Callback<ChangePasswordModel>() {
                @Override
                public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                    if (response.isSuccessful()) {
                        dismissProgress();
                        if (response.body().getResult() != null) {
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChangePasswordActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Password changed successfully")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent1 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
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
                public void onFailure(Call<ChangePasswordModel> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.getMessage();
            Utilities.showAlertDialog(e.getMessage(), ChangePasswordActivity.this);

        }
    }

    private boolean validateOtp() {
        try {
            if (edit_userId.getText().toString().length() == 0) {
                edit_userId.setError("Please Enter User Id");
                edit_userId.requestFocus();
                return false;
            }
            if (edit_oldPassword.getText().toString().length() == 0) {
                edit_oldPassword.setError(getString(R.string.enter_otp));
                edit_oldPassword.requestFocus();
                return false;
            }
            if (edit_new_password.getText().toString().length() == 0) {
                edit_new_password.setError(getString(R.string.enter_password));
                edit_new_password.requestFocus();
                return false;
            }
            if (edit_cofirm_password.getText().toString().length() == 0) {
                edit_cofirm_password.setError(getString(R.string.enter_conform_password));
                edit_cofirm_password.requestFocus();
                return false;
            }
            if (!edit_cofirm_password.getText().toString().equals(edit_new_password.getText().toString())) {
                edit_cofirm_password.setError(getString(R.string.incorrect_password));
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
