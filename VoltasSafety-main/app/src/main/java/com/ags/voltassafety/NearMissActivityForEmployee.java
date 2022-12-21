package com.ags.voltassafety;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ags.voltassafety.adapters.NearMissAdapter;
import com.ags.voltassafety.adapters.NearMissAdapterForEmployee;
import com.ags.voltassafety.model.AttachmentUpload;
import com.ags.voltassafety.model.AttachmentUploadModel;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.EntityResponse;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.FileResponse;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.model.Verticleresponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.FileUtils;
import com.ags.voltassafety.utility.GPSTracker;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ags.voltassafety.HazardActivity.ADAPTER_CAMERA_REQUEST;


public class NearMissActivityForEmployee extends BaseActivity {

    TextView create_title, cancel, submit;
    ImageView addButton, add_tasks;
    int PICK_IMAGE_MULTIPLE = 1;
    List<String> imagesEncodedList;
    String imageEncoded;
    ArrayList<String> mArrayUri;
    String base64Image;
    File file;
    ListView hazard_item_recycler_view;
    NearMissAdapterForEmployee hazardAdapter;
    List<ObservationItemsDetailsModel> ObservationItemsDetailsModelModelslist = new ArrayList<>();
    ObservationItemsDetailsModel ObservationItemsDetailsModelModel;
    ArrayList<EntityResult> entityResponseArrayList;
    ObservationHeader observationHeader;
    int itemposition;
    ProgressDialog progressDialog;
    ArrayList<String> verticleResponseResultArrayList;
    GPSTracker gpsTracker;
    LinearLayout images_view1;
    String strobservatonId = null;
    public static String strStatus;
    TextView listoftask_header, listof_taskes_tag;
    ArrayList<String> filePaths = new ArrayList<String>();
    ArrayList<AttachmentUploadModel> attachFile = new ArrayList<>();
    private int CAMERA_REQUEST = 0;
    public android.app.AlertDialog.Builder alertDialog;
    String encImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_hazard_);
//            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            create_title = findViewById(R.id.create_title);
            create_title.setText("NEAR MISS");
//            if (observationHeader.getReason().equalsIgnoreCase("Near Miss")) {
//
//            } else {
//                create_title.setText("HAZARD OBSERVATION");
//            }
            hazard_item_recycler_view = findViewById(R.id.hazard_item_recycler_view);


            cancel = findViewById(R.id.cancel);
            submit = findViewById(R.id.submit);
            images_view1 = findViewById(R.id.images_view1);
            listoftask_header = findViewById(R.id.listoftask_header);
            listof_taskes_tag = findViewById(R.id.listof_taskes_tag);


            create_title.setTypeface(Utilities.fontBold(getApplicationContext()));
            cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            submit.setTypeface(Utilities.fontRegular(getApplicationContext()));
            listof_taskes_tag.setTypeface(Utilities.fontRegular(getApplicationContext()));
            listoftask_header.setTypeface(Utilities.fontBold(getApplicationContext()));


            addButton = findViewById(R.id.addButton);
            add_tasks = findViewById(R.id.add_tasks);


            //SharedPreferences sharedPreferences = this.getSharedPreferences("ObservationStatus", Context.MODE_PRIVATE);

            if (Utilities.isConnectingToInternet(NearMissActivityForEmployee.this)) {

                getVerticle();
            }
            final Intent intent = getIntent();

            observationHeader = (ObservationHeader) intent.getExtras().getSerializable("HeaderObject");
            if (observationHeader.getObservationId() != null) {

            } else {
                ObservationItemsDetailsModelModel = new ObservationItemsDetailsModel();
                ObservationItemsDetailsModelModel.setStatus("Open");

                ObservationItemsDetailsModelModelslist.add(ObservationItemsDetailsModelModel);
            }

            if (observationHeader.getObservationId() != null) {
                hazard_item_recycler_view.setStackFromBottom(true);
                if (observationHeader.getStatus().equalsIgnoreCase("Open")) {
                    ObservationItemsDetailsModelModelslist.addAll(observationHeader.getObservationItemsDetailsModels());
                    strStatus = observationHeader.getStatus();
                } else {

                    int itemposition = intent.getExtras().getInt("position");
                    ObservationItemsDetailsModelModelslist.add(observationHeader.getObservationItemsDetailsModels().get(itemposition));
                    strobservatonId = observationHeader.getObservationId();
                    strStatus = observationHeader.getStatus();
                }
            } else {
                int value = intent.getExtras().getInt("NearMiss");
//                if (value == 1) {
//                    create_title.setText("NEAR MISS");
//                } else {
//
//                }
            }
            Log.d("observationheader", observationHeader + "");
            if (observationHeader.getObservationId() != null) {
//                hazard_item_recycler_view.setStackFromBottom(true);
                if (observationHeader.getStatus().equalsIgnoreCase("Assigned")) {
                    listoftask_header.setPadding(0, 60, 0, 0);
                    listof_taskes_tag.setPadding(0, 35, 0, 0);
                    add_tasks.setVisibility(View.GONE);
                } else {
                    add_tasks.setVisibility(View.VISIBLE);
                }
            }
//            else {
//                hazard_item_recycler_view.setStackFromBottom(false);
//            }

            add_tasks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        alertDialog = new android.app.AlertDialog.Builder(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
                        alertDialog.setMessage("Do you want to add Item Person?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ObservationItemsDetailsModelModel = new ObservationItemsDetailsModel();
                                ObservationItemsDetailsModelModel.setStatus("Open");

                                ObservationItemsDetailsModelModelslist.add(ObservationItemsDetailsModelModel);
                                int i = ObservationItemsDetailsModelModelslist.indexOf(ObservationItemsDetailsModelModel);

                                Handler handler = new Handler();
                                final int finalI = i;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hazard_item_recycler_view.smoothScrollToPosition(i);

                                    }
                                }, 100);
                                hazard_item_recycler_view.setSelection(i);
                                hazard_item_recycler_view.clearFocus();
                                hazardAdapter.notifyDataSetChanged();


                            }
                        });

                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.show();


                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            mArrayUri = new ArrayList<>();

            gpsTracker = new GPSTracker(getApplicationContext());

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (observationHeader.getObservationItemsDetailsModels() != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("If you want to go back data will Deleted")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();

                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                        } else if (ObservationItemsDetailsModelModelslist != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("If you want to go back data will Deleted")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();

                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                        } else {
                            finish();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {

                        if (observationHeader.getObservationId() != null && observationHeader.getStatus().equalsIgnoreCase("Open")) {
                            for (ObservationItemsDetailsModel obj : ObservationItemsDetailsModelModelslist) {
                                obj.setObservationId(observationHeader.getObservationId());
                                obj.setTypeOfObservation(observationHeader.getTypeOfObservation());
                                if (obj.getId() != null) {

                                } else {
                                    obj.setState("Create");
                                }
                            }
                        }
                        observationHeader.setObservationItemsDetailsModels(ObservationItemsDetailsModelModelslist);
                        if (observationHeader.getObservationId() != null && observationHeader.getStatus().equalsIgnoreCase("Assigned")) {

                            if (ObservationItemsDetailsModelModelslist.get(0).getItemsActionDetailsModels() != null && ObservationItemsDetailsModelModelslist.get(0).getItemsActionDetailsModels().size() > 0) {
                                if (Utilities.isConnectingToInternet(NearMissActivityForEmployee.this)) {

                                    if (observationHeader.getObservationId() != null) {
                                        updateObservation();
                                    }
                                } else {
                                    Utilities.showAlertDialog("Please check your internet connection", NearMissActivityForEmployee.this);
                                }
                            } else {
                                Utilities.showAlertDialog("Please add atleast one action taken", NearMissActivityForEmployee.this);
                            }
                        } else {

                            if (ObservationItemsDetailsModelModelslist != null && ObservationItemsDetailsModelModelslist.size() > 0) {
                                if (Utilities.isConnectingToInternet(NearMissActivityForEmployee.this)) {
                                    if (validateItems(ObservationItemsDetailsModelModelslist)) {
                                        if (observationHeader.getObservationId() != null) {
                                            updateObservation();
                                        } else {

                                            createObservation();
                                        }
                                    } else {
                                        Utilities.showAlertDialog("Please add mandatory fields", NearMissActivityForEmployee.this);
                                    }
                                } else {
                                    Utilities.showAlertDialog("Please check your internet connection", NearMissActivityForEmployee.this);
                                }

                            } else {
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
                                builder.setTitle(getResources().getString(R.string.information))
                                        .setMessage("Please add atleast one item")
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
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            });

        } catch (Exception e) {
            e.getMessage();
        }

    }

    private boolean validateItems(List<ObservationItemsDetailsModel> list) {
        boolean flag = true;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getVertical().equalsIgnoreCase("Select Verticle *") && !list.get(i).getHazardType().equalsIgnoreCase("Select Near Miss Type *") && !list.get(i).getRisk().equalsIgnoreCase("Select Risk Type *") && !list.get(i).getCategory().equalsIgnoreCase("Select Category *")) {

                } else {
                    flag = false;
                    Handler handler = new Handler();
                    final int finalI = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hazard_item_recycler_view.smoothScrollToPosition(finalI);
                        }
                    }, 100);
                    hazard_item_recycler_view.setSelection(finalI);
                    hazard_item_recycler_view.clearFocus();
                    break;
                }
            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return flag;
    }

    private void getVerticle() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<Verticleresponse> call = apiInterface.getVerticleData("Bearer " + sharedPreferences.getString("Bearertoken", null));
            progressDialog = new ProgressDialog(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<Verticleresponse>() {
                public void onResponse(Call<Verticleresponse> call, Response<Verticleresponse> response) {
                    verticleResponseResultArrayList = new ArrayList<>();
                    verticleResponseResultArrayList.add("Select Verticle *");
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Verticleresponse loginResponse = response.body();
                        Log.d("verticlerespons", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {

                            for (int i = 0; i < loginResponse.getResult().size(); i++) {
                                verticleResponseResultArrayList.add(loginResponse.getResult().get(i).getClientName());
                            }
                            if (Utilities.isConnectingToInternet(NearMissActivityForEmployee.this)) {
                                entityMapValues();
                            }

                        } else {
                            showAlert(loginResponse.getErrors() + "");
                        }
                    } else {
                        progressDialog.dismiss();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<Verticleresponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();

        }
    }

    private void entityMapValues() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<EntityResponse> call = apiInterface.getEntityMapData("Bearer " + sharedPreferences.getString("Bearertoken", null), "observation");
            showProgressDialog(NearMissActivityForEmployee.this);
            call.enqueue(new Callback<EntityResponse>() {
                public void onResponse(Call<EntityResponse> call, Response<EntityResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        EntityResponse loginResponse = response.body();
                        Log.d("LoginResponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            dismissProgress();

                            // ObservationType observationType = new ObservationType();
                            entityResponseArrayList = new ArrayList<>();

                            entityResponseArrayList.addAll(response.body().getResult());
                            Collections.sort(entityResponseArrayList, EntityResult.valueComparator);
                            // hazardAdapter = new NearMissAdapter(NearMissActivity.this, ObservationItemsDetailsModelModelslist, entityResponseArrayList, verticleResponseResultArrayList,observationHeader.getObservationId());
                            //add on done on 17-03-2-2021 we are send target date of closer to adapter
                            hazardAdapter = new NearMissAdapterForEmployee(NearMissActivityForEmployee.this, ObservationItemsDetailsModelModelslist, entityResponseArrayList, verticleResponseResultArrayList, observationHeader.getObservationId(), observationHeader.getTargetDateOfClosure());
                            hazard_item_recycler_view.setAdapter(hazardAdapter);


                        } else {
                            dismissProgress();
                            showAlert(loginResponse.getErrors() + "");
                            //showToast(loginResponse.getErrors() + "");
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<EntityResponse> call, Throwable t) {
                    dismissProgress();

                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
        }
        hazard_item_recycler_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    long viewId = view.getId();
                    ObservationItemsDetailsModel enqRetrieve = (ObservationItemsDetailsModel) parent.getItemAtPosition(position);
                    if (viewId == R.id.addButton) {
                        itemposition = position;
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                                NearMissActivityForEmployee.this, R.style.MyDialogTheme);
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
//                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                        startActivityForResult(intent, ADAPTER_CAMERA_REQUEST);

                                    }
                                });
                        myAlertDialog.show();

//
//
//                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                        intent.setType("*/*");
//                        startActivityForResult(intent, PICK_IMAGE_MULTIPLE);

                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                //  mArrayUri = new ArrayList<String>();
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();


                    file = new File(FileUtils.getRealPath(NearMissActivityForEmployee.this, mImageUri));//create path from uri

                    final String[] split = file.getPath().split(":");//split the path.
//
//
                    File filebytes = new File(file.getPath());


                    int size = (int) filebytes.length();
                    byte[] bytes = new byte[size];
//

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


                    base64Image = Utilities.getStringFile(new File(file.getPath()));


                    long filesize = filebytes.length();
                    long filesizeInKB = filesize / 1024;
                    //long filesizeinMB = filesizeInKB / 1024;
                    double filesizeinMB = (double) filesize / (1024 * 1024);

//                    filePaths.add(file.getPath());

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

//                    AttachmentUpload model_obj = new AttachmentUpload("",file.getPath(),file.getName());


                    if (filesizeinMB <= 3) {


                        if (Utilities.isConnectingToInternet(NearMissActivityForEmployee.this)) {
                            attachmentsAdding(itemposition);
                        }

                    } else {
                        showAlert("File size is not more than 3MB");
                    }
                    Log.v("LOG_TAG", "Selected Images " + mArrayUri.size());


                } else {
//
                }
                // }
            } else if (requestCode == ADAPTER_CAMERA_REQUEST && resultCode == RESULT_OK && null != data) {
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
                    //long filesizeinMB = filesizeInKB / 1024;
                    double filesizeinMB = (double) filesize / (1024 * 1024);


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
                        if (Utilities.isConnectingToInternet(NearMissActivityForEmployee.this)) {
                            attachmentsAdding(itemposition);
                        }

                    } else {
                        showAlert("File size is not more than 3MB");
                    }
                    Log.v("LOG_TAG", "Selected Images " + mArrayUri.size());


                }

            }
        } catch (Exception e) {
            e.getMessage();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    List<Integer> arrayList;
    List<ObservationAttachmentModel> linkarraylist;

    private void attachmentsAdding(final int itemposition) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<FileResponse> call = apiInterface.uploadObservationAttachment("Bearer " + sharedPreferences.getString("Bearertoken", null), new AttachmentUpload(base64Image, file.getName()));
            showProgressDialog(NearMissActivityForEmployee.this);
            call.enqueue(new Callback<FileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        dismissProgress();
                        FileResponse loginResponse = response.body();
                        Log.d("ImageId", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            try {
                                Thread.sleep(300);
                                dismissProgress();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            arrayList = ObservationItemsDetailsModelModelslist.get(itemposition).getItemAttachments();
                            ObservationAttachmentModel observationAttachmentModel = new ObservationAttachmentModel();
                            observationAttachmentModel.setId(response.body().getResult());
                            observationAttachmentModel.setUrl(file.getPath());

                            if (arrayList != null) {

                            } else {
                                arrayList = new ArrayList<>();
                            }

                            arrayList.add(response.body().getResult());
                            ObservationItemsDetailsModelModelslist.get(itemposition).setItemAttachments(arrayList);
                            Log.d("ItemsModelslistsize", ObservationItemsDetailsModelModelslist.get(itemposition).getItemAttachments().size() + "" + " position " + itemposition);

                            linkarraylist = ObservationItemsDetailsModelModelslist.get(itemposition).getObservationAttachmentModels();

                            if (linkarraylist != null) {

                            } else {
                                linkarraylist = new ArrayList<>();
                            }
                            linkarraylist.add(observationAttachmentModel);
                            ObservationItemsDetailsModelModelslist.get(itemposition).setObservationAttachmentModels(linkarraylist);

                            hazardAdapter.notifyDataSetChanged();


                        } else {
                            dismissProgress();
                            showAlert(loginResponse.getErrors() + "");
                            //showToast(loginResponse.getErrors() + "");
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
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

    public void createObservation() {
        try {

            if (gpsTracker.canGetLocation()) {
                observationHeader.setLatitude(gpsTracker.getLatitude() + "");
                observationHeader.setLongitude(gpsTracker.getLongitude() + "");
                // observationHeader.setLocation(gpsTracker.getLocation()+"");
            }
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(NearMissActivityForEmployee.this, Locale.getDefault());

            try {

                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    observationHeader.setLocation(address + "");
                    Log.d("locationAddress", address + "");
                } else {
//                            Utilities.showAlertDialog("Address not found", HazardActivity.this);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("", "");
            }

            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Gson gson = new Gson();
            String json = gson.toJson(observationHeader);

            Log.d("jsonRequest", json);

            Call<CreateResponse> call = apiInterface.createObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationHeader);
            showProgressDialog(NearMissActivityForEmployee.this);

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {
                        CreateResponse loginResponse = response.body();
                        Log.d("observationresponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            dismissProgress();
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Thank you for Reporting.\n\nYour Observation ID "+ Html.fromHtml("<b>"+response.body().getResult()+"</b>")+ "\n\nYou will recieve a confirmation mail shortly.")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String LoginUserID = sharedPreferences.getString("LoginUserID", null);
                                            if (LoginUserID != null && LoginUserID.equalsIgnoreCase("Guest")) {
                                                Intent intent1 = new Intent(NearMissActivityForEmployee.this, LandingPageActivity.class);
                                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent1.putExtra("Name", "NEAR MISS REPORTING");

                                                startActivity(intent1);
                                            } else {
                                                Intent intent1 = new Intent(NearMissActivityForEmployee.this, ObservationViewActivityForEmployee.class);
//                                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent1.putExtra("Name", "NEAR MISS REPORTING");
                                                startActivity(intent1);
                                            }
//                                            Intent intent1 = new Intent(NearMissActivity.this, ObservationViewActivity.class);
//                                            intent1.putExtra("Name", "NEAR MISS REPORTING");
//                                            startActivity(intent1);
                                            // dialogInterface.dismiss();
                                        }
                                    }).create().show();
//
                        } else {
                            dismissProgress();
                            showAlert(loginResponse.getErrors() + "");
                            //showToast(loginResponse.getErrors() + "");
                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }

                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updateObservation() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Gson gson = new Gson();
            String json = gson.toJson(observationHeader);

            Log.d("jsonRequest", json);

            Call<CreateResponse> call = apiInterface.updateObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationHeader);
            showProgressDialog(NearMissActivityForEmployee.this);

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {
                        CreateResponse loginResponse = response.body();
                        Log.d("observationresponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            dismissProgress();
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NearMissActivityForEmployee.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Action Item has been created.")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String LoginUserID = sharedPreferences.getString("LoginUserID", null);
                                            if (LoginUserID != null && LoginUserID.equalsIgnoreCase("Guest")) {
                                                Intent intent = new Intent(NearMissActivityForEmployee.this, LandingPageActivity.class);
                                                intent = new Intent(NearMissActivityForEmployee.this, LandingPageActivity.class);
                                                intent.putExtra("Name", "NEAR MISS REPORTING");

                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(NearMissActivityForEmployee.this, ObservationViewActivity.class);
                                                intent = new Intent(NearMissActivityForEmployee.this, ObservationViewActivityForEmployee.class);
                                                intent.putExtra("Name", "NEAR MISS REPORTING");
                                                startActivity(intent);
                                            }

                                        }
                                    }).create().show();

                        } else {
                            dismissProgress();
                            showAlert(loginResponse.getErrors() + "");
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }


}
