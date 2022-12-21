package com.ags.voltassafety;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ags.voltassafety.model.ForgorPasswordResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends BaseActivity {

    Button ok_button, cancel_button;
    EditText employee_id_edittext;
    TextView information_text;

    TextInputLayout inputlayout_your_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.dummy_forgot_layout);
            information_text = findViewById(R.id.information_text);
            employee_id_edittext = findViewById(R.id.edit_your_name);
            ok_button = findViewById(R.id.ok_button);
            cancel_button = findViewById(R.id.cancel_button);
            //employee_id_edittext.setText("");
            inputlayout_your_name = findViewById(R.id.inputlayout_your_name);
            employee_id_edittext.setTypeface(Utilities.fontRegular(getApplicationContext()));
            information_text.setTypeface(Utilities.fontRegular(getApplicationContext()));

            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (employee_id_edittext.getText().toString().length() > 0) {


                            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                            Call<ForgorPasswordResponse> call = apiInterface.getForgotPassword(employee_id_edittext.getText().toString());

                            //Log.d("ForgotRequest", String.valueOf(apiInterface.forgot_password_service(employee_id_edittext.getText().toString())));

                            showProgressDialog(ForgotActivity.this);
                            call.enqueue(new Callback<ForgorPasswordResponse>() {
                                public void onResponse(Call<ForgorPasswordResponse> call, Response<ForgorPasswordResponse> response) {
                                    int statusCode = response.code();
                                    if (response.isSuccessful()) {
                                        if (response.body().getResult() != null) {
                                            ForgorPasswordResponse forgotResponse = response.body();
                                            Log.d("LoginResponse", forgotResponse.getSuccess() + "");
                                            if (forgotResponse.getResult() != null) {
                                                if (forgotResponse.getSuccess()) {
                                                    dismissProgress();

                                                    Intent confir_password_intent = new Intent(ForgotActivity.this, ConfirmPasswordActivity.class);
                                                    confir_password_intent.putExtra("UserId", employee_id_edittext.getText().toString());
                                                    confir_password_intent.putExtra("Email", forgotResponse.getResult().getUserEmail());
                                                    confir_password_intent.putExtra("mobilenumber", forgotResponse.getResult().getUserPhone());
                                                    startActivity(confir_password_intent);


                                                } else {
                                                    dismissProgress();
                                                    showAlert(forgotResponse.getErrors() + "");
                                                    //showToast(loginResponse.getErrors() + "");
                                                }
                                            } else {
                                                dismissProgress();
                                                //Log.d("Error", "" + response.message());
                                                Utilities.showAlertDialog("NetWork Error ", ForgotActivity.this);
                                            }
                                        } else {
                                            dismissProgress();
                                            //Log.d("Error", "" + response.message());
                                            Utilities.showAlertDialog("" + response.body().getErrors()[0], ForgotActivity.this);
                                        }

                                    } else {
                                        dismissProgress();
                                        Log.d("Error", "" + response.message());
                                        Utilities.showAlertDialog("Error " + response.message(), ForgotActivity.this);
                                    }
                                }

                                public void onFailure(Call<ForgorPasswordResponse> call, Throwable t) {
                                    Log.d("LoginResponse", t.getMessage() + "");
                                    dismissProgress();
                                }

                            });

                        } else {
                            isValid();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            });

            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent confir_password_intent = new Intent(ForgotActivity.this, LoginActivity.class);
                  //  confir_password_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(confir_password_intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public boolean isValid() {

        if (employee_id_edittext.getText().toString().trim().equalsIgnoreCase("")) {
            employee_id_edittext.requestFocus();
            employee_id_edittext.setError("User name can not be blank");
            return false;
        }

        return true;
    }
}
