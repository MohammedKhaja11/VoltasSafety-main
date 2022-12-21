package com.ags.voltassafety;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ags.voltassafety.adapters.GalleryAdapter;
import com.ags.voltassafety.adapters.HazardAdapter;
import com.ags.voltassafety.adapters.IncidentAdapter;

import com.ags.voltassafety.adapters.InjuridGalleryAdapter;

import com.ags.voltassafety.adapters.ObservtionActionAdapter;
import com.ags.voltassafety.model.AttachmentUpload;
import com.ags.voltassafety.model.AttachmentUploadModel;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.EntityResponse;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.FileResponse;
import com.ags.voltassafety.model.InjuredDetailsModel;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.FileUtils;
import com.ags.voltassafety.utility.GPSTracker;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ags.voltassafety.HazardActivity.ADAPTER_CAMERA_REQUEST;
import static com.ags.voltassafety.HazardActivity.CAMERA_REQUEST;

public class IncidentActivity extends BaseActivity {

    private RadioGroup injuied_radio_group;
    private RadioButton injuried_yes, injuried_no;
    private LinearLayout injuredLayout, nonInjuredLayout, layoutOne;
    private ArrayList<String> incidentType;
    private EditText countEdit_brief_discription, edittext_direct_cause, edit_text_underlaying_cause;
    private AlertDialog.Builder alertDialog;
    private TextView create_title, cancel, riskHeader, descriptionCount, submit, riskTypeHeader, classificationHeader,riskCatHeader, textOne, textTwo, textThree, idAttachText, attachDiscript;
    private ListView incidentItemList;
    private ImageView addInjurePerson, addAttachment;
    //    private List<InjuredDetailsModel> list = new ArrayList();
    private List<InjuredDetailsModel> list = new ArrayList();
    private Spinner spinnerIncident, spinnerVertical,spinnerRisk, spinner_gender,sp_clasification;
    int PICK_IMAGE_MULTIPLE = 1;
    int ADAPTER_PICK_IMAGE_MULTIPLE = 2;
    List<String> imagesEncodedList;
    private GridView gv;
    List<ObservationItemsDetailsModel> ObservationItemsDetailsModelModelslist = new ArrayList<>();
    int itemposition;
    IncidentAdapter incidentAdapter;
    File file;
    String base64Image;
    String imageEncoded;
    ArrayList<String> mArrayUri;
    private ProgressDialog mProgressDialog;
    public static String strStatus;
    ObservationAttachmentModel observationAttachmentModel;
    List<ObservationAttachmentModel> linkarraylist;
    List<ObservationAttachmentModel> injuredLinkarraylist;
    private InjuridGalleryAdapter galleryAdapter;
    List<Integer> arrayList;
    List<Integer> injuredArray;
    ListView actions_listview;
    EditText action_description, action_remarks, action_name,et_analysis;
    TextView tv_analysis,tv_subcontractor,tv_clasificationremarks,count_text, countCorrective, countPreventive, preventiveText, correctiveText, verticalHeader;
    EditText et_subcontractor,et_clasificationremarks,countEdit_route_cause, correctiveEdit, preventiveEdit;
    ObservationHeader observationHeader;
    GPSTracker gpsTracker;
    List<EntityResult> entityResponseArrayList;
    ArrayList<String> listIncidentType,listVertical,listRiskType,classification;
    String selectedIncidentType = null;
    String selectedRiskType = null;
    String selectedVertical = null,selectedClassification = null;
    ImageView add_actions;
    String observationID = null;
    ItemsActionDetailsModel itemsActionDetailsModel;
    Object object = null;
    ArrayList<String> filePaths = new ArrayList<String>();
    ArrayList<AttachmentUploadModel> attachFile = new ArrayList<>();
    List<ItemsActionDetailsModel> observationitemsactondetailsModel = new ArrayList<>();
    LinearLayout action_layout;
    ObservtionActionAdapter observtionActionAdapter;
    ScrollView parentScroll;
    String encImage;
    private TextInputLayout inputlayout_brief_description;
    private Intent intent;
    LinearLayout correctiveLayout, preventiveLayout;
    TextInputLayout inputlayout_corrective_action, inputlayout_preventive_action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);
        initialisation();
        setFonts();
        entityMapValues();
//        SpannableStringBuilder builderDesignation = new SpannableStringBuilder();
//        SpannableString strDesignation1 = new SpannableString("Designation ");
//        strDesignation1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, strDesignation1.length(), 0);
//        builderDesignation.append(strDesignation1);
//        SpannableString strDesignation2 = new SpannableString("*");
//        strDesignation2.setSpan(new ForegroundColorSpan(Color.RED), 0, strDesignation2.length(), 0);
//        builderDesignation.append(strDesignation2);
//        countEdit_brief_discription.setHint(getResources().getString(R.string.pbs_setup_title));

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String accident = bundle.getString("Accident");
        create_title.setText(accident);
        observationHeader = (ObservationHeader) intent.getExtras().getSerializable("HeaderObject");
        if (observationHeader.getObservationId() != null) {
            create_title.setText("Accident");
        } else {
            InjuredDetailsModel injuredDetailsModel = new InjuredDetailsModel();
            list.add(injuredDetailsModel);

        }
        countEdit_brief_discription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // descriptionCount.setText(String.valueOf(charSequence.length()) + "/" + 500);

                if (charSequence.length() == 0) {
                    descriptionCount.setVisibility(View.GONE);
                } else {
                    descriptionCount.setVisibility(View.VISIBLE);
                    descriptionCount.setText(String.valueOf(charSequence.length()) + "/" + 500);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//        correctiveEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 0) {
//                    countCorrective.setVisibility(View.GONE);
//                } else {
//                    countCorrective.setVisibility(View.VISIBLE);
//                    countCorrective.setText(String.valueOf(charSequence.length()) + "/" + 500);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                observationHeader.getObservationItemsDetailsModels().get(0).setCorrectiveAction(editable.toString().trim());
//            }
//        });
//        preventiveEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 0) {
//                    countPreventive.setVisibility(View.GONE);
//                } else {
//                    countPreventive.setVisibility(View.VISIBLE);
//                    countPreventive.setText(String.valueOf(charSequence.length()) + "/" + 500);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                observationHeader.getObservationItemsDetailsModels().get(0).setPreventiveAction(editable.toString().trim());
//            }
//        });

        countEdit_brief_discription.setSelection(countEdit_brief_discription.getText().length());
        countEdit_brief_discription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (countEdit_brief_discription.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        if (observationHeader.getObservationId() != null) {
            action_layout.setVisibility(View.VISIBLE);

            action_description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    count_text.setText(String.valueOf(s.length()) + "/" + 500);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            action_remarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    count_text.setText(String.valueOf(s.length()) + "/" + 500);


                }

                @Override
                public void afterTextChanged(Editable editable) {
                    observationHeader.getObservationItemsDetailsModels().get(0).setAdminRemarks(action_remarks.getText().toString());
                }
            });

            add_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemsActionDetailsModel = new ItemsActionDetailsModel();
                    actions_listview.setVisibility(View.VISIBLE);
                    itemsActionDetailsModel.setId(0);
                    //  itemsActionDetailsModel.setObservationItemId(0);
                    itemsActionDetailsModel.setTargetDateOfClosure(observationHeader.getTargetDateOfClosure());
                    itemsActionDetailsModel.setReason(Collections.singletonList(0));
                    itemsActionDetailsModel.setDescription(action_description.getText().toString());
                    itemsActionDetailsModel.setCpRemarks(action_remarks.getText().toString().trim());

//                    itemsActionDetailsModel.setActionName(action_name.getText().toString());
                    if (action_name.getText().toString().length() > 0) {
                        actions_listview.setVisibility(View.VISIBLE);

                        itemsActionDetailsModel.setActionName(action_name.getText().toString());
                        itemsActionDetailsModel.setStatus("Open");

                        Log.d("remarks", action_remarks.getText().toString());

                        observationHeader.getObservationItemsDetailsModels().get(0).setActionsDescription(action_description.getText().toString());

                        itemsActionDetailsModel.setObservationItemId(observationHeader.getObservationItemsDetailsModels().get(0).getId());
                        observationitemsactondetailsModel.add(itemsActionDetailsModel);

                        observtionActionAdapter = new ObservtionActionAdapter(IncidentActivity.this, observationitemsactondetailsModel);
                        actions_listview.setAdapter(observtionActionAdapter);


                        action_name.setText("");
                        //observtionActionAdapter.notifyDataSetChanged();
                        observationHeader.getObservationItemsDetailsModels().get(0).setItemsActionDetailsModels(observationitemsactondetailsModel);

                        Gson gson = new Gson();
                        String json = gson.toJson(observationHeader);

                        Log.d("jsonRequest", json);

                    } else {
                        String errorString = "Please Enter User Name";

                        action_name.setError(errorString);
                        action_name.setFocusable(true);
                        action_name.setFocusableInTouchMode(true);
                        action_name.setEnabled(true);
                        action_name.requestFocus();
                    }
                }
            });

            actions_listview.setItemsCanFocus(true);


            action_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    final Handler handler;
                    handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
//                            v.requestFocus();
//                            v.requestFocusFromTouch();
                            //  parent_scroll.smoothScrollTo(0, 500);
                            handler.postDelayed(this, 50);
                        }
                    };
                    handler.postDelayed(r, 10);
                }
            });

            actions_listview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // Disallow the touch request for parent scroll on touch of child view
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });


            action_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    final Handler handler;
                    handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            //  v.requestFocus();
                            //  v.requestFocusFromTouch();
                            //       parent_scroll.smoothScrollTo(0, 500);
                            handler.postDelayed(this, 200);
                        }
                    };
                    handler.postDelayed(r, 200);
                }
            });


            action_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    final Handler handler;
                    handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            //  v.requestFocus();
                            //  v.requestFocusFromTouch();
                            //      parent_scroll.smoothScrollTo(0, 500);
                            handler.postDelayed(this, 200);
                        }
                    };
                    handler.postDelayed(r, 200);
                }
            });

        }

        listVertical = new ArrayList<>();
        listIncidentType = new ArrayList<>();
        listRiskType = new ArrayList<>();
        classification = new ArrayList<>();
        incidentItemList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });
        if (observationHeader.getObservationId() != null) {

            galleryAdapter = new InjuridGalleryAdapter(IncidentActivity.this, observationHeader.getObservationItemsDetailsModels().get(0).getObservationAttachmentModels(), observationHeader.getObservationId());
            gv.setAdapter(galleryAdapter);
            gv.setVerticalSpacing(gv.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv
                    .getLayoutParams();
            mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);

            observationID = observationHeader.getObservationId();
            strStatus = observationHeader.getStatus();
            spinnerIncident.setEnabled(false);
            spinnerVertical.setEnabled(false);
            spinnerRisk.setEnabled(false);
            countEdit_brief_discription.setFocusable(false);
            edittext_direct_cause.setFocusable(false);
            edit_text_underlaying_cause.setFocusable(false);
            addAttachment.setVisibility(View.GONE);
            addInjurePerson.setVisibility(View.GONE);
            injuried_yes.setClickable(false);
            injuried_no.setClickable(false);
            countEdit_route_cause.setFocusable(false);
            correctiveEdit.setFocusable(false);
            preventiveEdit.setFocusable(false);
            et_analysis.setFocusable(false);
            et_clasificationremarks.setFocusable(false);
            et_subcontractor.setFocusable(false);
            sp_clasification.setEnabled(false);
            countEdit_brief_discription.setText(observationHeader.getObservationItemsDetailsModels().get(0).getBriefDescription());
            edittext_direct_cause.setText(observationHeader.getObservationItemsDetailsModels().get(0).getDirectCause());
            edit_text_underlaying_cause.setText(observationHeader.getObservationItemsDetailsModels().get(0).getUnderlayingCause());
            countEdit_route_cause.setText(observationHeader.getObservationItemsDetailsModels().get(0).getRouteCause());

            correctiveEdit.setText(observationHeader.getObservationItemsDetailsModels().get(0).getCorrectiveAction());
            preventiveEdit.setText(observationHeader.getObservationItemsDetailsModels().get(0).getPreventiveAction());
            et_analysis.setText(observationHeader.getObservationItemsDetailsModels().get(0).getAnalysis());
            et_subcontractor.setText(observationHeader.getObservationItemsDetailsModels().get(0).getSubcontractor());
            et_clasificationremarks.setText(observationHeader.getObservationItemsDetailsModels().get(0).getRemarkreason());
            textTwo.setText("Injuried Person details");
            textThree.setVisibility(View.GONE);

            // observationHeader=(ObservationHeader)observationHeader;

            // selectedIncidentType=observationHeader.getObservationItemsDetailsModels().get(0).getIncidentType();
            classification.add(observationHeader.getObservationItemsDetailsModels().get(0).getClassification());
            listVertical.add(observationHeader.getObservationItemsDetailsModels().get(0).getVertical());
            listIncidentType.add(observationHeader.getObservationItemsDetailsModels().get(0).getIncidentType());
            listRiskType.add(observationHeader.getObservationItemsDetailsModels().get(0).getRisk());

            if (observationHeader.getObservationItemsDetailsModels().get(0).getInjured().equalsIgnoreCase("yes")) {
                injuried_yes.setChecked(true);
            } else {
                injuried_no.setChecked(true);
            }
            list.addAll(observationHeader.getObservationItemsDetailsModels().get(0).getInjuredDetailsModels());

        } else {
            incidentItemList.setStackFromBottom(false);
            observationID = null;
            addInjurePerson.setVisibility(View.VISIBLE);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gv.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


//        incidentItemList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                parentScroll.requestDisallowInterceptTouchEvent(true);
//                int action = event.getActionMasked();
//                switch (action) {
//                    case MotionEvent.ACTION_UP:
//                        h.requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });
//


        addInjurePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(IncidentActivity.this, R.style.MyDialogTheme);
                alertDialog.setMessage("Do you want to add Injured Person?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        InjuredDetailsModel injuredDetailsModel = new InjuredDetailsModel();
                        list.add(injuredDetailsModel);
                        int i = list.indexOf(injuredDetailsModel);


                        Handler handler = new Handler();
                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                incidentItemList.smoothScrollToPosition(finalI);
                            }
                        }, 100);
                        incidentItemList.setSelection(finalI);

//                        incidentItemList.smoothScrollToPosition(i); // after adding item moving to that position


//                        hazardAdapter.notifyDataSetChanged();

                        incidentAdapter.notifyDataSetChanged();
                        incidentItemList.setStackFromBottom(false);


                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.show();


            }
        });
        incidentItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
//                    long viewId = view.getId();
//                    ObservationItemsDetailsModel enqRetrieve = (ObservationItemsDetailsModel) parent.getItemAtPosition(position);
                    try {
                        if (view.getId() == R.id.addmedicalReport) {
                            itemposition = position;
                            androidx.appcompat.app.AlertDialog.Builder myAlertDialog = new androidx.appcompat.app.AlertDialog.Builder(
                                    IncidentActivity.this, R.style.MyDialogTheme);
                            myAlertDialog.setTitle("Upload Document");
//                        myAlertDialog.setMessage("How do you want to set your picture?");
                            myAlertDialog.setPositiveButton("Upload Image / Video",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                            intent.setType("*/*");
                                            startActivityForResult(intent, ADAPTER_PICK_IMAGE_MULTIPLE);

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
//                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                            intent.addCategory(Intent.CATEGORY_OPENABLE);
//                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                            intent.setType("*/*");
//                            startActivityForResult(intent, ADAPTER_PICK_IMAGE_MULTIPLE);

                        }
                    } catch (Exception e) {
                        Log.d("ItemId", "" + view.getId());
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder myAlertDialog = new androidx.appcompat.app.AlertDialog.Builder(
                        IncidentActivity.this, R.style.MyDialogTheme);
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
                                startActivityForResult(intent, CAMERA_REQUEST);

                            }
                        });
                myAlertDialog.show();
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                intent.setType("*/*");
//                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
            }
        });

        injuied_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.injuried_yes) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(radioGroup.getApplicationWindowToken(), 0);
                    injuredLayout.setVisibility(View.VISIBLE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.injuried_no) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(radioGroup.getApplicationWindowToken(), 0);
                    injuredLayout.setVisibility(View.GONE);
                    nonInjuredLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (observationHeader.getObservationId() != null) {
//                    ObservationItemsDetailsModel ObservationItemsDetailsModelModel = new ObservationItemsDetailsModel();
//                    ObservationItemsDetailsModelModelslist.add(ObservationItemsDetailsModelModel);

                    //  ObservationItemsDetailsModelModelslist.addAll(observationHeader.getObservationItemsDetailsModels());

                    //  if (ObservationItemsDetailsModelModelslist.get(0).getItemsActionDetailsModels() != null && ObservationItemsDetailsModelModelslist.get(0).getItemsActionDetailsModels().size() > 0) {
                    updateObservation();
//                    } else {
//                        Utilities.showAlertDialog("Please add atleast one action taken", IncidentActivity.this);
//                    }
                } else {
                    if (!selectedVertical.equalsIgnoreCase("Select Vertical *")) {
                        if (!selectedIncidentType.equalsIgnoreCase("Select Incident Type *")) {
                            if (!selectedRiskType.equalsIgnoreCase("Select Risk Type *")) {
                                //if (!selectedRiskType.equalsIgnoreCase("Select Classification *")) {
                                    if (isValid()) {
                                        if (injuied_radio_group.getCheckedRadioButtonId() == R.id.injuried_yes && list.size() > 0) {
                                            if (validateItems(list)) {

                                                ObservationItemsDetailsModel observationItemsDetailsModel = new ObservationItemsDetailsModel();
                                                observationItemsDetailsModel.setVertical(selectedVertical);
                                                observationItemsDetailsModel.setIncidentType(selectedIncidentType);
                                                observationItemsDetailsModel.setRisk(selectedRiskType);
                                                observationItemsDetailsModel.setBriefDescription(countEdit_brief_discription.getText().toString());
                                                observationItemsDetailsModel.setDirectCause(edittext_direct_cause.getText().toString());
                                                observationItemsDetailsModel.setUnderlayingCause(edit_text_underlaying_cause.getText().toString());
                                                observationItemsDetailsModel.setRouteCause(countEdit_route_cause.getText().toString());
                                                observationItemsDetailsModel.setCorrectiveAction(correctiveEdit.getText().toString());
                                                observationItemsDetailsModel.setPreventiveAction(preventiveEdit.getText().toString());
                                                observationItemsDetailsModel.setAnalysis(et_analysis.getText().toString());
                                                observationItemsDetailsModel.setClassification(selectedClassification);
                                                observationItemsDetailsModel.setRemarkreason(et_clasificationremarks.getText().toString());
                                                observationItemsDetailsModel.setSubcontractor(et_subcontractor.getText().toString());

                                                observationItemsDetailsModel.setItemAttachments(arrayList);
                                                observationItemsDetailsModel.setObservationAttachmentModels(linkarraylist);
                                                if (injuied_radio_group.getCheckedRadioButtonId() == R.id.injuried_yes) {
                                                    observationItemsDetailsModel.setInjured("Yes");
                                                } else {
                                                    observationItemsDetailsModel.setInjured("No");
                                                }

                                                observationItemsDetailsModel.setTypeOfObservation(observationHeader.getTypeOfObservation());
                                                if (observationItemsDetailsModel.getInjured().equalsIgnoreCase("Yes") && list.size() > 0) {
                                                    observationItemsDetailsModel.setInjuredDetailsModels(list);
                                                }
                                                ObservationItemsDetailsModelModelslist = new ArrayList<>();
                                                ObservationItemsDetailsModelModelslist.add(observationItemsDetailsModel);
                                                observationHeader.setObservationItemsDetailsModels(ObservationItemsDetailsModelModelslist);


                                                Gson gson = new Gson();
                                                String json = gson.toJson(observationHeader);

                                                Log.d("jsonRequest", json);

                                                if (list != null && list.size() > 0) {
                                                    if (list.get(itemposition).getAge() >= 18) {
                                                        createObservation();
                                                    } else {
                                                        Utilities.showAlertDialog("Age field is below 18 please enter valid age", IncidentActivity.this);
                                                    }

                                                }
                                            } else {
                                                Utilities.showAlertDialog("Please Enter Mandatory Fields", IncidentActivity.this);
                                            }

                                        } else if (injuied_radio_group.getCheckedRadioButtonId() == R.id.injuried_no) {
                                            ObservationItemsDetailsModel observationItemsDetailsModel = new ObservationItemsDetailsModel();
                                            observationItemsDetailsModel.setVertical(selectedVertical);
                                            observationItemsDetailsModel.setIncidentType(selectedIncidentType);
                                            observationItemsDetailsModel.setRisk(selectedRiskType);
                                            observationItemsDetailsModel.setBriefDescription(countEdit_brief_discription.getText().toString());
                                            observationItemsDetailsModel.setDirectCause(edittext_direct_cause.getText().toString());
                                            observationItemsDetailsModel.setUnderlayingCause(edit_text_underlaying_cause.getText().toString());
                                            observationItemsDetailsModel.setRouteCause(countEdit_route_cause.getText().toString());
                                            observationItemsDetailsModel.setCorrectiveAction(correctiveEdit.getText().toString());
                                            observationItemsDetailsModel.setPreventiveAction(preventiveEdit.getText().toString());
                                            observationItemsDetailsModel.setAnalysis(et_analysis.getText().toString());
                                            observationItemsDetailsModel.setClassification(selectedClassification);
                                            observationItemsDetailsModel.setRemarkreason(et_clasificationremarks.getText().toString());
                                            observationItemsDetailsModel.setSubcontractor(et_subcontractor.getText().toString());

                                            if (injuied_radio_group.getCheckedRadioButtonId() == R.id.injuried_yes) {
                                                observationItemsDetailsModel.setInjured("Yes");
                                            } else {
                                                observationItemsDetailsModel.setInjured("No");
                                            }

                                            observationItemsDetailsModel.setTypeOfObservation(observationHeader.getTypeOfObservation());
                                            observationItemsDetailsModel.setItemAttachments(arrayList);
                                            observationItemsDetailsModel.setObservationAttachmentModels(linkarraylist);
                                            ObservationItemsDetailsModelModelslist = new ArrayList<>();
                                            ObservationItemsDetailsModelModelslist.add(observationItemsDetailsModel);
                                            observationHeader.setObservationItemsDetailsModels(ObservationItemsDetailsModelModelslist);


                                            Gson gson = new Gson();
                                            String json = gson.toJson(observationHeader);

                                            Log.d("jsonRequest", json);


                                            if (observationHeader.getObservationId() != null) {

                                                updateObservation();
                                            } else {
                                                // if (list.get(itemposition).getAge() >= 18) {
                                                createObservation();
                                                // } else {
                                                // Utilities.showAlertDialog("Please Enter Valid Age", IncidentActivity.this);
                                                // }
                                            }

                                        } else {
                                            Utilities.showAlertDialog("Please add  atleast one person", IncidentActivity.this);
                                        }

                                    }
                                /*}
                                else{
                                    Utilities.showAlertDialog("Please Select Classification", IncidentActivity.this);
                                }*/
                            } else {
                                Utilities.showAlertDialog("Please Select Risk Type", IncidentActivity.this);
                            }
                        } else {
                            Utilities.showAlertDialog("Please Select Incident Type", IncidentActivity.this);
                        }
                    }
                    else{
                        Utilities.showAlertDialog("Please Select Vertical", IncidentActivity.this);
                    }
                }

            }
        });
    }

    public void updateObservation() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);

            Gson gson = new Gson();
            String json = gson.toJson(observationHeader);

            Log.d("jsonRequest", json);

            Call<CreateResponse> call = apiInterface.updateObservation("Bearer " + sharedPreferences.getString("Bearertoken", null), observationHeader);
            showProgressDialog(IncidentActivity.this);

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {
                        CreateResponse loginResponse = response.body();
                        Log.d("observationresponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            dismissProgress();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(IncidentActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Action Item has been created.")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            intent = new Intent(IncidentActivity.this, ObservationViewActivity.class);
                                           /* if(observationHeader.getReason().equalsIgnoreCase("Hazard")) {
                                                intent.putExtra("Name", "HAZARD REPORTING");
                                            }else if(observationHeader.getReason().equalsIgnoreCase("Accident")){*/
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("Name", "INCIDENT REPORTING");
                                            /*}
                                            else{
                                                intent.putExtra("Name", "NEAR MISS REPORTING");
                                            }*/
                                            startActivity(intent);
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

    private boolean validateItems(List<InjuredDetailsModel> list) {
        boolean flag = true;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName() != null && !list.get(i).getName().equalsIgnoreCase("") &&
                        list.get(i).getAge() != null &&
                        list.get(i).getDesignation() != null && !list.get(i).getDesignation().equalsIgnoreCase("") &&
                        list.get(i).getGender() != null && !list.get(i).getGender().equalsIgnoreCase("select gender *") &&
                        list.get(i).getInjuredDateTime() != null && !list.get(i).getInjuredDateTime().equalsIgnoreCase("") &&
                        list.get(i).getEmployeeType() != null && !list.get(i).getEmployeeType().equalsIgnoreCase("select employee type *") &&
                        //list.get(i).get() != null && !list.get(i).getDescription().equalsIgnoreCase("Select Injury Type *") &&
                        list.get(i).getTreatment() != null && !list.get(i).getTreatment().equalsIgnoreCase("select treatment *")) {


                } else {
                    Utilities.showAlertDialog("Please Enter Person Details", IncidentActivity.this);
                    flag = false;
                    Handler handler = new Handler();
                    final int finalI = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            incidentItemList.smoothScrollToPosition(finalI);
                        }
                    }, 100);
                    incidentItemList.setSelection(finalI);

                    break;

                }

            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return flag;
    }

    private boolean isValid() {
        if (countEdit_brief_discription.getText().toString().trim().length() == 0) {

            String errorString = "Please Enter Description";

            countEdit_brief_discription.setError(errorString);

            countEdit_brief_discription.requestFocus();
            return false;
        }
        if (edittext_direct_cause.getText().toString().trim().length() == 0) {

            String errorString = "Please Enter Direct Cause";

            edittext_direct_cause.setError(errorString);

            edittext_direct_cause.requestFocus();
            return false;
        }
       /* if (edit_text_underlaying_cause.getText().toString().trim().length() == 0) {

            String errorString = "Please Enter Underlaying Cause";

            edit_text_underlaying_cause.setError(errorString);

            edit_text_underlaying_cause.requestFocus();
            return false;
        }*/
        if (countEdit_route_cause.getText().toString().trim().length() == 0) {

            String errorString = "Please Enter Route Cause";

            countEdit_route_cause.setError(errorString);

            countEdit_route_cause.requestFocus();
            return false;
        }

        return true;
    }

    private void setFonts() {

        tv_analysis.setTypeface(Utilities.fontRegular(getApplicationContext()));
        tv_clasificationremarks.setTypeface(Utilities.fontRegular(getApplicationContext()));
        //tv_rootcause.setTypeface(Utilities.fontRegular(context));
        tv_subcontractor.setTypeface(Utilities.fontRegular(getApplicationContext()));
        et_analysis.setTypeface(Utilities.fontRegular(getApplicationContext()));
        et_clasificationremarks.setTypeface(Utilities.fontRegular(getApplicationContext()));
        //et_rootcause.setTypeface(Utilities.fontRegular(context));
        et_subcontractor.setTypeface(Utilities.fontRegular(getApplicationContext()));
        create_title.setTypeface(Utilities.fontBold(getApplicationContext()));
        cancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
        submit.setTypeface(Utilities.fontRegular(getApplicationContext()));
        riskTypeHeader.setTypeface(Utilities.fontBold(getApplicationContext()));
        classificationHeader.setTypeface(Utilities.fontBold(getApplicationContext()));
        riskHeader.setTypeface(Utilities.fontBold(getApplicationContext()));

        riskCatHeader.setTypeface(Utilities.fontBold(getApplicationContext()));
        verticalHeader.setTypeface(Utilities.fontBold(getApplicationContext()));
        textOne.setTypeface(Utilities.fontRegular(getApplicationContext()));
        textTwo.setTypeface(Utilities.fontBold(getApplicationContext()));
        textThree.setTypeface(Utilities.fontRegular(getApplicationContext()));
        idAttachText.setTypeface(Utilities.fontBold(getApplicationContext()));
        attachDiscript.setTypeface(Utilities.fontRegular(getApplicationContext()));
        countEdit_brief_discription.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edittext_direct_cause.setTypeface(Utilities.fontRegular(getApplicationContext()));
        edit_text_underlaying_cause.setTypeface(Utilities.fontRegular(getApplicationContext()));

        correctiveText.setTypeface(Utilities.fontRegular(getApplicationContext()));
        preventiveText.setTypeface(Utilities.fontRegular(getApplicationContext()));
        correctiveEdit.setTypeface(Utilities.fontRegular(getApplicationContext()));
        preventiveEdit.setTypeface(Utilities.fontRegular(getApplicationContext()));
    }

    public void initialisation() {

        tv_clasificationremarks = findViewById(R.id.tv_clasificationremarks);
        et_clasificationremarks = findViewById(R.id.et_clasificationremarks);
        tv_subcontractor = findViewById(R.id.subcontractorText);
        et_subcontractor = findViewById(R.id.subcontractorEdit);
       // tv_rootcause = itemView.findViewById(R.id.tv_rootcause);
       // et_rootcause = itemView.findViewById(R.id.countEdit_route_cause);
        tv_analysis = findViewById(R.id.analysisText);
        et_analysis = findViewById(R.id.analysisEdit);
        sp_clasification = findViewById(R.id.sp_clasification);

        injuied_radio_group = (RadioGroup) findViewById(R.id.injuied_radio_group);
        injuried_yes = findViewById(R.id.injuried_yes);
        injuried_no = findViewById(R.id.injuried_no);
        injuredLayout = (LinearLayout) findViewById(R.id.injuredLayout);
        nonInjuredLayout = (LinearLayout) findViewById(R.id.nonInjuredLayout);
        action_layout = (LinearLayout) findViewById(R.id.action_layout);
        addInjurePerson = findViewById(R.id.addButton);
        create_title = findViewById(R.id.header);
        descriptionCount = findViewById(R.id.descriptionCount);
        cancel = findViewById(R.id.cancel);
        submit = findViewById(R.id.submit);
        incidentItemList = (ListView) findViewById(R.id.incidentItemList);
        spinnerVertical = (Spinner)findViewById(R.id.spinner_vertical);
        spinnerIncident = (Spinner) findViewById(R.id.spinnerIncident);
        spinnerRisk = (Spinner) findViewById(R.id.spinnerRisk);
        verticalHeader = (TextView)findViewById(R.id.verticalHeader);
        riskTypeHeader = (TextView) findViewById(R.id.riskTypeHeader);
        classificationHeader = (TextView)findViewById(R.id.classificationHeader);
        riskCatHeader = (TextView) findViewById(R.id.riskCatHeader);
        riskHeader = (TextView) findViewById(R.id.riskHeader);
        textOne = (TextView) findViewById(R.id.textOne);
        textTwo = (TextView) findViewById(R.id.textTwo);
        textThree = (TextView) findViewById(R.id.textThree);
        idAttachText = (TextView) findViewById(R.id.idAttachText);
        attachDiscript = (TextView) findViewById(R.id.attachDiscript);
        countEdit_brief_discription = (EditText) findViewById(R.id.countEdit_brief_discription);
        edittext_direct_cause = (EditText) findViewById(R.id.edittext_direct_cause);
        edit_text_underlaying_cause = (EditText) findViewById(R.id.edit_text_underlaying_cause);
        countEdit_route_cause = (EditText) findViewById(R.id.countEdit_route_cause);
        addAttachment = (ImageView) findViewById(R.id.addAttachment);

        correctiveEdit = findViewById(R.id.correctiveEdit);
        preventiveEdit = findViewById(R.id.preventiveEdit);
        correctiveLayout = findViewById(R.id.correctiveLayout);
        preventiveLayout = findViewById(R.id.preventiveLayout);
        inputlayout_corrective_action = findViewById(R.id.inputlayout_corrective_action);
        inputlayout_preventive_action = findViewById(R.id.inputlayout_preventive_action);
        correctiveText = findViewById(R.id.correctiveText);
        preventiveText = findViewById(R.id.preventiveText);
        countCorrective = findViewById(R.id.countCorrective);
        countPreventive = findViewById(R.id.countPreventive);

        inputlayout_brief_description = (TextInputLayout) findViewById(R.id.inputlayout_brief_description);
        gv = (GridView) findViewById(R.id.gv);

        add_actions = findViewById(R.id.add_actions);

        parentScroll = findViewById(R.id.parentScroll);

        actions_listview = findViewById(R.id.actions_listview);

        action_description = findViewById(R.id.action_description);
        action_remarks = findViewById(R.id.action_remarks);

        action_name = findViewById(R.id.action_name);
        count_text = findViewById(R.id.count_text);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<String>();
            if (data.getData() != null) {
                Uri mImageUri = data.getData();
                file = new File(FileUtils.getRealPath(IncidentActivity.this, mImageUri));//create path from uri
                final String[] split = file.getPath().split(":");//split the path.//
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
                base64Image = Utilities.getStringFile(new File(file.getPath()));

                long filesize = filebytes.length();
                long filesizeInKB = filesize / 1024;
                //long filesizeinMB = filesizeInKB / 1024;
                double filesizeinMB = (double) filesize / (1024 * 1024);

                Log.d("Filesize", filesizeinMB + "");
                Log.d("Filebytes", bytes + "");
                Log.d("FilePAth", file.getPath());
                Log.d("FileName", file.getName());


                // Get the cursor
                Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded = cursor.getString(columnIndex);
                cursor.close();

                if (filesizeinMB <= 3) {
                    if (Utilities.isConnectingToInternet(IncidentActivity.this)) {
                        attachmentsAdding(itemposition);
                    }

                } else {
                    Utilities.showAlertDialog("File size is not more than 3MB", IncidentActivity.this);
                }

            } else {
//
            }
        } else if (requestCode == ADAPTER_PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<String>();
            if (data.getData() != null) {
                Uri mImageUri = data.getData();
                file = new File(FileUtils.getRealPath(IncidentActivity.this, mImageUri));//create path from uri
                final String[] split = file.getPath().split(":");//split the path.
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
                base64Image = Utilities.getStringFile(new File(file.getPath()));

                long filesize = filebytes.length();
                long filesizeInKB = filesize / 1024;
                //long filesizeinMB = filesizeInKB / 1024;
                double filesizeinMB = (double) filesize / (1024 * 1024);


                Log.d("Filesize", filesizeinMB + "");
                Log.d("Filebytes", bytes + "");
                Log.d("FilePAth", file.getPath());
                Log.d("FileName", file.getName());

                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded = cursor.getString(columnIndex);
                cursor.close();

                if (filesizeinMB <= 3) {
                    if (Utilities.isConnectingToInternet(IncidentActivity.this)) {
                        injurePersonAttachment(itemposition);
                    }

                } else {
                    Utilities.showAlertDialog("File size is not more than 3MB", IncidentActivity.this);
                }

            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<String>();

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
                    if (Utilities.isConnectingToInternet(IncidentActivity.this)) {
                        attachmentsAdding(itemposition);
                    }

                } else {
                    showAlert("File size is not more than 3MB");
                }
//                Log.v("LOG_TAG", "Selected Images " + mArrayUri.size());


            } else {
                Log.d("Data", data.toString());
            }

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
                    if (Utilities.isConnectingToInternet(IncidentActivity.this)) {
                        injurePersonAttachment(itemposition);
                    }

                } else {
                    showAlert("File size is not more than 3MB");
                }
//                Log.v("LOG_TAG", "Selected Images " + mArrayUri.size());


            } else {
                Log.d("Data", data.toString());
            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void attachmentsAdding(final int itemposition) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<FileResponse> call = apiInterface.uploadObservationAttachment("Bearer " + sharedPreferences.getString("Bearertoken", null), new AttachmentUpload(base64Image, file.getName()));
            mProgressDialog = new ProgressDialog(IncidentActivity.this, R.style.MyDialogTheme);
            mProgressDialog.setMessage("Please wait ....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            call.enqueue(new Callback<FileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        FileResponse loginResponse = response.body();
                        Log.d("ImageId", response.body() + "");
                        if (loginResponse.getSuccess()) {
                            try {
                                Thread.sleep(300);
                                mProgressDialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            ObservationAttachmentModel observationAttachmentModel = new ObservationAttachmentModel();
                            observationAttachmentModel.setId(response.body().getResult());
                            observationAttachmentModel.setUrl(file.getPath());
                            Log.d("Id", "" + observationAttachmentModel.getId());

//                            arrayList = list.get(itemposition).getItemAttachments();

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
                            galleryAdapter = new InjuridGalleryAdapter(IncidentActivity.this, linkarraylist, observationHeader.getObservationId());
                            gv.setAdapter(galleryAdapter);
                            gv.setVerticalSpacing(gv.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv
                                    .getLayoutParams();
                            mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);

                        } else {
                            mProgressDialog.dismiss();
                            Utilities.showAlertDialog(loginResponse.getErrors() + "", IncidentActivity.this);

                        }
                    } else {
                        mProgressDialog.dismiss();
                        Utilities.showAlertDialog(response.message() + "", IncidentActivity.this);
                    }
                }


                public void onFailure(Call<FileResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            mProgressDialog.dismiss();
            e.getMessage();
        }
    }

    private void injurePersonAttachment(final int position) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<FileResponse> call = apiInterface.uploadObservationAttachment("Bearer " + sharedPreferences.getString("Bearertoken", null), new AttachmentUpload(base64Image, file.getName()));
            mProgressDialog = new ProgressDialog(IncidentActivity.this, R.style.MyDialogTheme);
            mProgressDialog.setMessage("Please wait ....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            call.enqueue(new Callback<FileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        FileResponse loginResponse = response.body();
                        Log.d("ImageId", response.body() + "");
                        if (loginResponse.getSuccess()) {
                            try {
                                Thread.sleep(300);
                                mProgressDialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            injuredArray = list.get(position).getItemAttachments();

                            ObservationAttachmentModel observationAttachmentModel = new ObservationAttachmentModel();
                            observationAttachmentModel.setId(response.body().getResult());
                            observationAttachmentModel.setUrl(file.getPath());

                            if (injuredArray != null) {

                            } else {
                                injuredArray = new ArrayList<>();
                            }

                            injuredArray.add(response.body().getResult());
                            list.get(position).setItemAttachments(injuredArray);
                            Log.d("ItemsModelslistsize", list.get(position).getItemAttachments().size() + "" + " position " + position);

                            injuredLinkarraylist = list.get(position).getObservationAttachmentModels();


                            if (injuredLinkarraylist != null) {

                            } else {
                                injuredLinkarraylist = new ArrayList<>();
                            }
                            injuredLinkarraylist.add(observationAttachmentModel);
                            list.get(position).setObservationAttachmentModels(injuredLinkarraylist);
                            incidentAdapter.notifyDataSetChanged();
                        } else {
                            mProgressDialog.dismiss();
                            Utilities.showAlertDialog(loginResponse.getErrors() + "", IncidentActivity.this);

                        }
                    } else {
                        mProgressDialog.dismiss();
                        Utilities.showAlertDialog(response.message() + "", IncidentActivity.this);
                    }
                }


                public void onFailure(Call<FileResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Log.d("LoginResponse", t.getMessage() + "");
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void createObservation() {
        try {
            gpsTracker = new GPSTracker(getApplicationContext());

            if (gpsTracker.canGetLocation()) {
                observationHeader.setLatitude(gpsTracker.getLatitude() + "");
                observationHeader.setLongitude(gpsTracker.getLongitude() + "");
            }
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(IncidentActivity.this, Locale.getDefault());

            try {

                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    observationHeader.setLocation(address + "");
                    Log.d("locationAddress", address + "");
                } else {

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
            mProgressDialog = new ProgressDialog(IncidentActivity.this, R.style.MyDialogTheme);
            mProgressDialog.setMessage("Please wait ....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {
                        CreateResponse loginResponse = response.body();
                        Log.d("observationresponse", response.body().getResult() + "");

                        if (loginResponse.getSuccess()) {
                            mProgressDialog.dismiss();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(IncidentActivity.this, R.style.MyDialogTheme);
                            builder.setTitle(getResources().getString(R.string.information))
                                    .setMessage("Thank you for Reporting.\n\nYour Observation ID "+ Html.fromHtml("<b>"+response.body().getResult()+"</b>")+ "\n\nYou will recieve a confirmation mail shortly.")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String LoginUserID = sharedPreferences.getString("LoginUserID", null);
                                            if (LoginUserID.equalsIgnoreCase("Guest")) {
                                                intent = new Intent(IncidentActivity.this, LandingPageActivity.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("Name", "INCIDENT REPORTING");

                                                startActivity(intent);
                                            } else {
                                                intent = new Intent(IncidentActivity.this, ObservationViewActivity.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("Name", "INCIDENT REPORTING");
                                                startActivity(intent);
                                            }
//                                            intent = new Intent(IncidentActivity.this, ObservationViewActivity.class);
//                                           /* if(observationHeader.getReason().equalsIgnoreCase("Hazard")) {
//                                                intent.putExtra("Name", "HAZARD REPORTING");
//                                            }else if(observationHeader.getReason().equalsIgnoreCase("Accident")){*/
////                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            intent.putExtra("Name", "INCIDENT REPORTING");
                                           /* }
                                            else{
                                                intent.putExtra("Name", "NEAR MISS REPORTING");
                                            }*/
//                                            startActivity(intent);
                                            // dialogInterface.dismiss();
                                        }
                                    }).create().show();
                        } else {
                            mProgressDialog.dismiss();
                            Utilities.showAlertDialog(loginResponse.getErrors() + "", IncidentActivity.this);

                        }
                    } else {
                        mProgressDialog.dismiss();
                        Utilities.showAlertDialog(response.message() + "", IncidentActivity.this);
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

    private void entityMapValues() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<EntityResponse> call = apiInterface.getEntityMapData("Bearer " + sharedPreferences.getString("Bearertoken", null), "observation");
            mProgressDialog = new ProgressDialog(IncidentActivity.this, R.style.MyDialogTheme);
            mProgressDialog.setMessage("Please wait ....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            call.enqueue(new Callback<EntityResponse>() {
                public void onResponse(Call<EntityResponse> call, Response<EntityResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        EntityResponse loginResponse = response.body();
                        Log.d("LoginResponse", response.body().getResult() + "");
                        if (loginResponse.getSuccess()) {
                            entityResponseArrayList = new ArrayList<>();
                            entityResponseArrayList.addAll(response.body().getResult());
                            Collections.sort(entityResponseArrayList, EntityResult.valueComparator);

                            mProgressDialog.dismiss();
                            listVertical.add("Select Vertical *");
                            listIncidentType.add("Select Incident Type *");
                            listRiskType.add("Select Risk Type *");
                            classification.add("Select Classification *");

                            incidentAdapter = new IncidentAdapter(IncidentActivity.this, list, entityResponseArrayList, observationID, observationHeader, observationHeader.getObservationId());
                            incidentItemList.setAdapter(incidentAdapter);

                            for (int i = 0; i < response.body().getResult().size(); i++) {

                                if (loginResponse.getResult().get(i).getLookUpName().equalsIgnoreCase("IncidentType")) {

                                    listIncidentType.add(loginResponse.getResult().get(i).getLookUpValue());
                                }
                            }

                            for (int i = 0; i < response.body().getResult().size(); i++) {

                                if (loginResponse.getResult().get(i).getLookUpName().equalsIgnoreCase("Risk")) {

                                    listRiskType.add(loginResponse.getResult().get(i).getLookUpValue());
                                }
                            }

                            for (int i = 0; i < response.body().getResult().size(); i++) {

                                if (loginResponse.getResult().get(i).getLookUpName().equalsIgnoreCase("Vertical")) {
                                    listVertical.add(loginResponse.getResult().get(i).getLookUpValue());
                                }
                            }

                            for (int i = 0; i < response.body().getResult().size(); i++) {

                                if (loginResponse.getResult().get(i).getLookUpName().equalsIgnoreCase("Classification")) {
                                    classification.add(loginResponse.getResult().get(i).getLookUpValue());
                                }
                            }
                            //Collections.sort(listIncidentType);
                            spinnerVertical.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                                    return false;
                                }
                            });
                            spinnerIncident.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                                    return false;
                                }
                            });
                            spinnerRisk.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                                    return false;
                                }
                            });

                            sp_clasification.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                                    return false;
                                }
                            });

                            ArrayAdapter verticalType = new ArrayAdapter(IncidentActivity.this, R.layout.list_view_items, R.id.lbl_name, listVertical);
                            spinnerVertical.setAdapter(verticalType);

                            ArrayAdapter incidentType = new ArrayAdapter(IncidentActivity.this, R.layout.list_view_items, R.id.lbl_name, listIncidentType);
                            spinnerIncident.setAdapter(incidentType);

                            ArrayAdapter riskType = new ArrayAdapter(IncidentActivity.this, R.layout.list_view_items, R.id.lbl_name, listRiskType);
                            spinnerRisk.setAdapter(riskType);

                            ArrayAdapter classificationAdapter = new ArrayAdapter(IncidentActivity.this, R.layout.list_view_items, R.id.lbl_name, classification);
                            sp_clasification.setAdapter(classificationAdapter);

                            spinnerVertical.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                    selectedVertical = adapterView.getSelectedItem().toString();
                                }


//                                spinnerRisk

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            spinnerIncident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                    selectedIncidentType = adapterView.getSelectedItem().toString();
                                }


//                                spinnerRisk

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            spinnerRisk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                    selectedRiskType = adapterView.getSelectedItem().toString();
                                }


//                                spinnerRisk

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            sp_clasification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                    selectedClassification = adapterView.getSelectedItem().toString();
                                }


//                                spinnerRisk

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } else {
                            mProgressDialog.dismiss();
                            Utilities.showAlertDialog(loginResponse.getErrors() + "", IncidentActivity.this);
                        }
                    } else {
                        mProgressDialog.dismiss();
                        Utilities.showAlertDialog(response.message() + "", IncidentActivity.this);
                    }
                }


                public void onFailure(Call<EntityResponse> call, Throwable t) {
                    mProgressDialog.dismiss();

                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void onBackPressed() {
    }

}
