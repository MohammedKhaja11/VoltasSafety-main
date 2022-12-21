package com.ags.voltassafety;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ags.voltassafety.adapters.CloseActionAttachAdapter;
import com.ags.voltassafety.model.ActionRemarksModel;
import com.ags.voltassafety.model.AttachmentUpload;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.FileResponse;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationClosingModel;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.FileUtils;
import com.ags.voltassafety.utility.Utilities;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActionItemActivity extends BaseActivity {
    EditText etDescription, etActionTaken, etCPRemarks;
    TextView cancel, submit, create_title;
    ProgressDialog progressDialog;
    String ObservationId;
    int ObservationItemId, ActionId;
    String strType = "";
    ActionRemarksModel actionRemarksModel;
    ObservationClosingModel observationClosingModel;
    List<Integer> arrayList;
    List<ObservationAttachmentModel> linkarraylist;
    ObservationHeader observationHeader;
    private Bundle bundle;
    private int position, size;
    ObservationAttachmentModel observationAttachmentModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.action_items_edit_dialog);
            //countEdit = findViewById(R.id.countEdit);
            cancel = findViewById(R.id.cancel);
            submit = findViewById(R.id.submit);
            etDescription = findViewById(R.id.et_actiondescription);
            etCPRemarks = findViewById(R.id.et_cpremarks);
            etActionTaken = findViewById(R.id.et_actiontaken);
            create_title = findViewById(R.id.create_title);

            bundle = getIntent().getExtras();
            if (bundle != null) {
                ObservationItemId = bundle.getInt("ObservationItemId");
                strType = bundle.getString("StrName");
                create_title.setText("ACTION EDIT");
                ActionId = bundle.getInt("ActionId");
                position = bundle.getInt("position");
                size = bundle.getInt("size");
                observationHeader = (ObservationHeader) bundle.get("Header");
                ObservationId = observationHeader.getObservationId();
                //create_title.setText("ACTION CLOSE");
            }


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            submit.setTypeface(Utilities.fontRegular(getApplicationContext()));
            //stutas.setTypeface(Utilities.fontRegular(getApplicationContext()));


           /* etCPRemarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    etCPRemarks.setText(String.valueOf(charSequence.length()) + "/" + 500);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });*/

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        if (etDescription.getText().toString().trim() != null && etDescription.getText().toString().trim().length() > 0) {
                            if (etActionTaken.getText().toString().trim() != null && etActionTaken.getText().toString().trim().length() > 0) {
                                if (etCPRemarks.getText().toString().trim() != null && etCPRemarks.getText().toString().trim().length() > 0) {

                                    for (int i = 0; i < observationHeader.getObservationItemsDetailsModels().size(); i++) {
                                        if (observationHeader.getObservationItemsDetailsModels().get(i).getId() == ObservationItemId) {
                                            for (int j = 0; j < observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().size(); j++) {
                                                if (observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().get(j).getId() == ActionId) {

                                                    observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().get(j).setDescription(etDescription.getText().toString().trim());
                                                    observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().get(j).setAdminRemarks(etCPRemarks.getText().toString().trim());
                                                    observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().get(j).setActionName(etActionTaken.getText().toString().trim());
                                                    updateObservation();
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    Utilities.showAlertDialog("Please enter  CP remarks", EditActionItemActivity.this);
                                }
                            } else {

                                Utilities.showAlertDialog("Please enter action taken", EditActionItemActivity.this);
                            }
                        } else {
                            Utilities.showAlertDialog("Please enter description", EditActionItemActivity.this);
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


    public void updateObservation() {
        try {
            Gson gson;
            String json;
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            gson = new Gson();
            json = gson.toJson(observationHeader);
            Log.d("jsonRequest", json);

            Call<CreateResponse> call = apiInterface.updateObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationHeader);
            showProgressDialog(EditActionItemActivity.this);

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                    CreateResponse createResponse = response.body();
                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                CreateResponse loginResponse = response.body();
                                Log.d("observationresponse", response.body().getResult() + "");
                                dismissProgress();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(EditActionItemActivity.this, R.style.MyDialogTheme);
                                builder.setTitle(EditActionItemActivity.this.getResources().getString(R.string.information))
                                        .setMessage("Action item Updated Successfully")
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                        Intent intent = new Intent(ClosedActionItemActivity.this, ObservationDetailsActivity.class);
//                                        startActivity(intent);
                                                finish();
                                            }
                                        }).create().show();

                            } else {
                                dismissProgress();
                                showAlert("No Data");
                            }
                        } else {
                            dismissProgress();
                            showAlert(response.body().getErrors()[0]);
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }

                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
