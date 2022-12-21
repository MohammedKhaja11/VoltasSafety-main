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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClosedActionItemActivityForEmployee extends BaseActivity {
    EditText countEdit;
    TextView cancel, submit, stutas, remarksCount, create_title;
    ProgressDialog progressDialog;
    String ObservationId;
    int ObservationItemId, ActionId;
    String strType = "";
    private LinearLayout actionAttachLayout;
    private ImageView addImage;
    int PICK_IMAGE_MULTIPLE = 1;
    int CAMERA_REQUEST_ACTION = 5;
    List<String> imagesEncodedList;
    File file;
    String imageEncoded;
    ArrayList<String> mArrayUri;
    String base64Image;
    String encImage;
    List<ObservationItemsDetailsModel> ObservationItemsDetailsModelModelslist = new ArrayList<>();
    ObservationItemsDetailsModel ObservationItemsDetailsModelModel;
    ActionRemarksModel actionRemarksModel;
    ObservationClosingModel observationClosingModel;
    private int position;
    private int size;
    private GridView gv;
    List<Integer> arrayList;
    List<ObservationAttachmentModel> linkarraylist;
    ObservationHeader observationHeader;
    private Bundle bundle;
    ObservationAttachmentModel observationAttachmentModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.action_items_status_dialog);
            countEdit = findViewById(R.id.countEdit);
            cancel = findViewById(R.id.cancel);
            submit = findViewById(R.id.submit);
            stutas = findViewById(R.id.status);
            remarksCount = findViewById(R.id.countedTwo);
            create_title = findViewById(R.id.create_title);
            actionAttachLayout = findViewById(R.id.actionAttachLayout);
            addImage = findViewById(R.id.addButton);
            gv = findViewById(R.id.gv);
            bundle = getIntent().getExtras();
            if (bundle != null) {
//                if (bundle.getString("Flag").equalsIgnoreCase("ObservationItem")) {
                if (bundle.getString("Flag").equalsIgnoreCase("Observation")) {

                    ObservationId = bundle.getString("ObservationId");
                    strType = bundle.getString("StrName");
                    create_title.setText("OBSERVATION CLOSE");
                } else {
                    ObservationItemId = bundle.getInt("ObservationItemId");
                    ActionId = bundle.getInt("ActionId");
                    position = bundle.getInt("position");
                    size = bundle.getInt("size");
                    observationHeader = (ObservationHeader) bundle.get("Header");

                    create_title.setText("ACTION CLOSE");
                    actionAttachLayout.setVisibility(View.VISIBLE);

                }
            }


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            addImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder myAlertDialog = new androidx.appcompat.app.AlertDialog.Builder(
                            ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                    myAlertDialog.setTitle("Upload Document");
//                        myAlertDialog.setMessage("How do you want to set your picture?");
                    myAlertDialog.setPositiveButton("Upload Image / Video",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                    intent.setType("*/*");
                                    startActivityForResult(intent, PICK_IMAGE_MULTIPLE);

                                }
                            });
                    myAlertDialog.setNegativeButton("Camera",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                    startActivityForResult(intent, CAMERA_REQUEST_ACTION);

                                }
                            });
                    myAlertDialog.show();

                }


            });
            //fonts
            countEdit.setTypeface(Utilities.fontRegular(getApplicationContext()));
            cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            submit.setTypeface(Utilities.fontRegular(getApplicationContext()));
            stutas.setTypeface(Utilities.fontRegular(getApplicationContext()));
            countEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    remarksCount.setText(String.valueOf(charSequence.length()) + "/" + 500);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
//                        countEdit.getText().toString();
                        if (countEdit.getText().toString().trim() != null && countEdit.getText().toString().trim().length() > 0) {
                            if (bundle.getString("Flag").equalsIgnoreCase("Observation")) {
                                observationClosingModel = new ObservationClosingModel();
                                observationClosingModel.setObservationId(ObservationId);
                                observationClosingModel.setAdminRemark(countEdit.getText().toString());
                                closeObservationItem(observationClosingModel);

                            } else {
                                for (int i = 0; i < observationHeader.getObservationItemsDetailsModels().size(); i++) {
                                    if (observationHeader.getObservationItemsDetailsModels().get(i).getId() == ObservationItemId) {
                                        for (int j = 0; j < observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().size(); j++) {
                                            if (observationHeader.getObservationItemsDetailsModels().get(i).getItemsActionDetailsModels().get(j).getId() == ActionId) {
                                                List<Integer> arrayListOne = observationHeader.getObservationItemsDetailsModels().get(i).getItemAttachments();
                                                if(arrayList.size() >0) {
                                                    arrayListOne.addAll(arrayList);
                                                    List<ObservationAttachmentModel> linkarraylistOne = observationHeader.getObservationItemsDetailsModels().get(i).getObservationAttachmentModels();
                                                    Log.d("linkarraylistOneSize", linkarraylistOne.size() + "");
                                                    linkarraylistOne.addAll(linkarraylist);
                                                    observationHeader.getObservationItemsDetailsModels().get(i).setItemAttachments(arrayListOne);
                                                    observationHeader.getObservationItemsDetailsModels().get(i).setObservationAttachmentModels(linkarraylistOne);
                                                }
                                                updateObservation();
                                            }

                                        }
                                    }


                                }

//                                closeActionItem(actionRemarksModel);
                            }


                        } else {
                            Utilities.showAlertDialog("Please enter remarks", ClosedActionItemActivityForEmployee.this);
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

    public void closeActionItem(ActionRemarksModel actionRemarksModel) {

        try {

            //remarks = remarks.replace("\n", "");

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            progressDialog = new ProgressDialog(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            Call<CreateResponse> call = apiInterface.closeActionItem("Bearer " + sharedPreferences.getString("Bearertoken", null), actionRemarksModel);
            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            CreateResponse loginResponse = response.body();
                            Log.d("observationresponse", response.body().getResult() + "");
                            if (loginResponse.getSuccess()) {
                                progressDialog.dismiss();
                                if(position==0) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                                    builder.setTitle(ClosedActionItemActivityForEmployee.this.getResources().getString(R.string.information))
                                            .setMessage("Action item Closed. Please ensure to close all the remaining Action Items.")
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
                                }
                                else if(size-position==1){

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                                    builder.setTitle(ClosedActionItemActivityForEmployee.this.getResources().getString(R.string.information))
                                            .setMessage("All Action items are Closed. Only Safety Admin can authorize the final close.")
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
                                }
                                else{
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                                    builder.setTitle(ClosedActionItemActivityForEmployee.this.getResources().getString(R.string.information))
                                            .setMessage("Action Item Closed Successfully ")
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
                                }
                            } else {
                                progressDialog.dismiss();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                                builder.setTitle(getResources().getString(R.string.information))
                                        .setMessage(response.body().getErrors()[0])
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).create().show();

                            }
                        } else {
                            progressDialog.dismiss();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage(response.body().getErrors()[0])
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create().show();

                        }
                    } else {
                        progressDialog.dismiss();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                        builder.setTitle(getResources().getString(R.string.information))
                                .setMessage(statusCode)
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                    }
                }

                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void closeObservationItem(ObservationClosingModel observationClosingModel) {
        try {
            // remark = remark.replace("\n", "");
            // observationClosingModel.setAdminRemark(remark);
            Gson gson = new Gson();
            String request = gson.toJson(observationClosingModel);
            Log.d("request", request);
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            progressDialog = new ProgressDialog(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            Call<CreateResponse> call = apiInterface.closeObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationClosingModel);
            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getResult() != null) {
                            CreateResponse loginResponse = response.body();
                            Log.d("observationresponse", response.body().getResult() + "");
                            if (loginResponse.getSuccess()) {
                                progressDialog.dismiss();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                                builder.setTitle(ClosedActionItemActivityForEmployee.this.getResources().getString(R.string.information))
                                        .setMessage("Observation Closed Successfully ")
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(ClosedActionItemActivityForEmployee.this, ObservationViewActivityForEmployee.class);
                                                if (strType.equalsIgnoreCase("Hazard")) {
                                                    intent.putExtra("Name", "HAZARD REPORTING");
                                                } else if (strType.equalsIgnoreCase("Accident")) {
                                                    intent.putExtra("Name", "INCIDENT REPORTING");
                                                } else {
                                                    intent.putExtra("Name", "NEAR MISS REPORTING");
                                                }
                                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        }).create().show();

                            } else {
                                progressDialog.dismiss();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                                builder.setTitle(getResources().getString(R.string.information))
                                        .setMessage(response.body().getErrors()[0])
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).create().show();

                            }
                        } else {
                            progressDialog.dismiss();
                           /* final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(ClosedActionItemActivity.this.getResources().getString(R.string.information))
                                    .setMessage(response.body().getErrors()[0])
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(ClosedActionItemActivity.this, ObservationViewActivity.class);
                                            if(strType.equalsIgnoreCase("Hazard")) {
                                                intent.putExtra("Name", "HAZARD REPORTING");
                                            }else if(strType.equalsIgnoreCase("Accident")){
                                                intent.putExtra("Name", "INCIDENT REPORTING");
                                            }
                                            else{
                                                intent.putExtra("Name", "NEAR MISS REPORTING");
                                            }
                                            startActivity(intent);
                                        }
                                    }).create().show();*/
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage(response.body().getErrors()[0])
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create().show();

                        }
                    } else {
                        progressDialog.dismiss();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ClosedActionItemActivityForEmployee.this, R.style.MyDialogTheme);
                        builder.setTitle(getResources().getString(R.string.information))
                                .setMessage(response.message())
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                    }
                }


                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    file = new File(FileUtils.getRealPath(ClosedActionItemActivityForEmployee.this, mImageUri));

                    final String[] split = file.getPath().split(":");

                    File filebytes = new File(file.getPath());

                    int size = (int) filebytes.length();
                    byte[] bytes = new byte[size];

                    Log.d("FileByte", bytes + "");
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    long filesize = filebytes.length();
                    long filesizeInKB = filesize / 1024;
                    //long filesizeinMB = filesizeInKB / 1024;
                    double filesizeinMB = (double) filesize / (1024 * 1024);

                    Log.d("Filesize", filesizeinMB + "");
                    Log.d("Filebytes", bytes + "");
                    Log.d("FilePAth", file.getPath());
                    Log.d("FileName", file.getName());

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();


                    if (filesizeinMB <= 3) {

                        base64Image = Utilities.getStringFile(new File(file.getPath()));


                        if (Utilities.isConnectingToInternet(ClosedActionItemActivityForEmployee.this)) {
                            attachmentsAdding(position);
                        }

                    } else {
                        showAlert("File size is not more than 3MB");
                    }
                    Log.v("LOG_TAG", "Selected Images " + mArrayUri.size());


                }
            } else if (requestCode == CAMERA_REQUEST_ACTION && resultCode == RESULT_OK && null != data) {
                if (data.getExtras().get("data") != null) {

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 50, bytes);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "image_" + timeStamp;

///DCIM/Camera/

                    file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/", imageFileName + ".png");

                    File filebytes = new File(file.getPath());

                    int size = (int) filebytes.length();
                    byte[] bytes1 = new byte[size];

                    Log.d("FileByte", bytes + "");
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                        buf.read(bytes1, 0, bytes1.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    long filesize = filebytes.length();
                    long filesizeInKB = filesize / 1024;
                    long filesizeinMB = filesizeInKB / 1024;


                    Log.d("Filesize", filesizeinMB + "");
                    Log.d("Filebytes", bytes + "");
                    Log.d("FilePAth", file.getPath());
                    Log.d("FileName", file.getName());


                    if (filesizeinMB <= 3) {


//                        final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        encodeImage(thumbnail);


                        base64Image = encImage;
                        Log.d("Fileabsolutepath", base64Image);
                        if (Utilities.isConnectingToInternet(ClosedActionItemActivityForEmployee.this)) {
                            attachmentsAdding(position);
                        }

                    } else {
                        showAlert("File size is not more than 3MB");
                    }
//                    Log.v("LOG_TAG", "Selected Images " + mArrayUri.size());


                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //    List<ObservationAttachmentModel> mArrayUri;

    private void attachmentsAdding(int position) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Log.v("LOG_TAG", "Filename " + file.getName());
            Call<FileResponse> call = apiInterface.uploadObservationAttachment("Bearer " + sharedPreferences.getString("Bearertoken", null), new AttachmentUpload(base64Image, file.getName()));
            showProgressDialog(ClosedActionItemActivityForEmployee.this);
            call.enqueue(new Callback<FileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        if (response.body().getResult() != null) {
                            FileResponse loginResponse = response.body();
                            Log.d("ImageId", response.body().getResult() + "");
                            if (loginResponse.getSuccess()) {
                                dismissProgress();

                                observationAttachmentModel = new ObservationAttachmentModel();
                                observationAttachmentModel.setId(response.body().getResult());
                                observationAttachmentModel.setUrl(file.getPath());
                                observationAttachmentModel.setName(file.getName());
                                if (arrayList != null) {

                                } else {
                                    arrayList = new ArrayList<>();
                                }
                                arrayList.add(response.body().getResult());
                                if (linkarraylist != null) {

                                } else {
                                    linkarraylist = new ArrayList<>();
                                }
                                linkarraylist.add(observationAttachmentModel);
                               /* Log.d("LOG_TAG", "Selected Images " + linkarraylist.size());
                                Gson gson = new Gson();
                                String json = gson.toJson(linkarraylist);
                                Log.d("linkarraylist", json);
*/
                                CloseActionAttachAdapter closeActionAttachAdapter = new CloseActionAttachAdapter(ClosedActionItemActivityForEmployee.this, linkarraylist,observationHeader.getObservationId());
                                gv.setAdapter(closeActionAttachAdapter);
                                gv.setVerticalSpacing(gv.getHorizontalSpacing());
                                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
                                mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);


                            } else {
                                dismissProgress();
                                showAlert(loginResponse.getErrors() + "");
                                //showToast(loginResponse.getErrors() + "");
                            }
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.body().getErrorCode() + "");
                    }
                }


                public void onFailure(Call<FileResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
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
            showProgressDialog(ClosedActionItemActivityForEmployee.this);

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
//                                if (Utilities.isConnectingToInternet(ClosedActionItemActivity.this)) {
//
////                                if (bundle.getString("Flag").equalsIgnoreCase("Observation")) {
//                                    if (bundle.getString("Flag").equalsIgnoreCase("Observation")) {
//                                        observationClosingModel = new ObservationClosingModel();
//                                        observationClosingModel.setObservationId(ObservationId);
//                                        //
//                                        observationClosingModel.setAdminRemark(countEdit.getText().toString());
//                                        closeObservationItem(observationClosingModel);
//
//                                    } else {
                                actionRemarksModel = new ActionRemarksModel();
                                actionRemarksModel.setItemId(ObservationItemId);
                                actionRemarksModel.setActionId(ActionId);
                                actionRemarksModel.setObservationId(observationHeader.getObservationId());
                                actionRemarksModel.setAdminRemark(countEdit.getText().toString().trim());
                                closeActionItem(actionRemarksModel);
//                                    }
//                                } else {
//                                    Utilities.showAlertDialog("Please check your internet connection", ClosedActionItemActivity.this);
//                                }
//                            showAlert("OK");
                                /*final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ClosedActionItemActivity.this, R.style.MyDialogTheme);
                                builder.setTitle(getResources().getString(R.string.information))
                                        .setMessage("Observation updated successfully " + observationHeader.getObservationId())
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                String LoginUserID = sharedPreferences.getString("LoginUserID", null);
//                                                if (LoginUserID.equalsIgnoreCase("Guest")) {
//                                                    intent = new Intent(HazardActivity.this, LandingPageActivity.class);
//                                                    intent.putExtra("Name", "HAZARD REPORTING");
//
//                                                    startActivity(intent);
//                                                } else {
                                            Intent    intent = new Intent(ClosedActionItemActivity.this, ObservationViewActivity.class);
                                                intent.putExtra("Name", "HAZARD REPORTING");
                                                startActivity(intent);
//                                                }
                                               *//* intent = new Intent(HazardActivity.this, ObservationViewActivity.class);
                                                //if(observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                                intent.putExtra("Name", "HAZARD REPORTING");
                                               *//**//* }else if(observationHeader.getReason().equalsIgnoreCase("Accident")){
                                                    intent.putExtra("Name", "INCIDENT REPORTING");
                                                }
                                                else{
                                                    intent.putExtra("Name", "NEAR MISS REPORTING");
                                                }*//**//*
                                                startActivity(intent);*//*
                                            }
                                        }).create().show();*/

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
