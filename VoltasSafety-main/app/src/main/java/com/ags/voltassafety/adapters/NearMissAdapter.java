package com.ags.voltassafety.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ags.voltassafety.NearMissActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ags.voltassafety.NearMissActivity.strStatus;


public class NearMissAdapter extends BaseAdapter {
    Context context;
    private List<ObservationItemsDetailsModel> al;
    private GridView gv;
    List<EntityResult> entityResponses;
    ArrayList<String> hazardType;
    ArrayList<String> risktype;
    ArrayList<String> categoty,classification;
    ArrayList<String> verticleResponseResultArrayList;
    private OnItemClicked onClick;
    public static LinearLayout imageview, images_view1;
    public AlertDialog.Builder alertDialog;
    private GalleryAdapter galleryAdapter;
    private String strObservationID;
    private ObservationItemsDetailsModel objItem;
    ItemsActionDetailsModel itemsActionDetailsModel;
    List<ItemsActionDetailsModel> observationitemsactondetailsModel = new ArrayList<>();
    ProgressDialog progressDialog;
//    public static TextInputLayout inputlayout_description;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    ObservtionActionAdapter observtionActionAdapter;

    String observationHeaderid;
    String dateofCloser;

    public NearMissAdapter(NearMissActivity context, List<ObservationItemsDetailsModel> al, List<EntityResult> entityResponseArrayList, ArrayList<String> verticleResponseResultArrayList, String observationHeaderid, String dataofCloser) {
        this.context = context;
        this.al = new ArrayList<>();
        this.al = al;
        entityResponses = entityResponseArrayList;
        this.onClick = onClick;
        //this.verticleResponseResultArrayList = verticleResponseResultArrayList;
        this.observationHeaderid = observationHeaderid;
        this.dateofCloser = dataofCloser;
        if (al != null && al.size() > 0 && al.get(0).getObservationId() != null) {
            strObservationID = al.get(0).getObservationId();
            //strStatus = al.get(0).getStatus();
        }
//        else{
//            strObservationID="empty";
//        }

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int i) {
        return al.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {

        final EditText countEdit, edit_description2, action_description, action_remarks, action_name, correctiveEdit, preventiveEdit,et_clasificationremarks,
                et_subcontractor,et_rootcause,et_analysis;
        final ImageView addButton, item_delete, add_actions;
        final TextView addFileText, injuryText, description, listoftask_header, count_text, countedTwo, idText,tv_clasificationremarks,tv_subcontractor,
                preventiveText, correctiveText, counted, countCorrective, countPreventive,tv_rootcause,tv_analysis;
        LinearLayout action_layout;
        Spinner edit_hazard_type, edit_risk, edit_category, edit_verticle,sp_clasification;
        final ListView actions_listview;

        LinearLayout correctiveLayout, preventiveLayout;
        TextInputLayout inputlayout_corrective_action, inputlayout_preventive_action;

        View itemView = null;

        try {
            objItem = (ObservationItemsDetailsModel) getItem(position);

            itemView = LayoutInflater.from(context).inflate(R.layout.nearmiss_items, viewGroup, false);

            tv_clasificationremarks = itemView.findViewById(R.id.tv_clasificationremarks);
            et_clasificationremarks = itemView.findViewById(R.id.et_clasificationremarks);
            tv_subcontractor = itemView.findViewById(R.id.subcontractorText);
            et_subcontractor = itemView.findViewById(R.id.subcontractorEdit);
            tv_rootcause = itemView.findViewById(R.id.tv_rootcause);
            et_rootcause = itemView.findViewById(R.id.countEdit_route_cause);
            tv_analysis = itemView.findViewById(R.id.analysisText);
            et_analysis = itemView.findViewById(R.id.analysisEdit);
            sp_clasification = itemView.findViewById(R.id.sp_clasification);
            edit_hazard_type = itemView.findViewById(R.id.edit_hazard_type);
            edit_risk = itemView.findViewById(R.id.edit_risk);
            edit_category = itemView.findViewById(R.id.edit_category);
            countEdit = itemView.findViewById(R.id.countEdit);
            edit_description2 = itemView.findViewById(R.id.edit_description2);
            listoftask_header = itemView.findViewById(R.id.listoftask_header);
            addButton = itemView.findViewById(R.id.addButton);
            item_delete = itemView.findViewById(R.id.item_delete);
            gv = itemView.findViewById(R.id.gv);
            edit_verticle = itemView.findViewById(R.id.edit_verticle);

            imageview = itemView.findViewById(R.id.imageview);
            images_view1 = itemView.findViewById(R.id.images_view1);
            action_layout = itemView.findViewById(R.id.action_layout);
            add_actions = itemView.findViewById(R.id.add_actions);
            actions_listview = itemView.findViewById(R.id.actions_listview);
            description = itemView.findViewById(R.id.description);
            injuryText = itemView.findViewById(R.id.injuryText);
            idText = itemView.findViewById(R.id.idText);
            addFileText = itemView.findViewById(R.id.addFileText);
            //   inputlayout_description = itemView.findViewById(R.id.inputlayout_description);

            action_description = itemView.findViewById(R.id.action_description);
            action_remarks = itemView.findViewById(R.id.action_remarks);
            action_name = itemView.findViewById(R.id.action_name);
            count_text = itemView.findViewById(R.id.counted);
            countedTwo = itemView.findViewById(R.id.countedTwo);
            correctiveText = itemView.findViewById(R.id.correctiveText);
            preventiveText = itemView.findViewById(R.id.preventiveText);
            countCorrective = itemView.findViewById(R.id.countCorrective);
            countPreventive = itemView.findViewById(R.id.countPreventive);

            counted = itemView.findViewById(R.id.counted);

            correctiveEdit = itemView.findViewById(R.id.correctiveEdit);
            preventiveEdit = itemView.findViewById(R.id.preventiveEdit);
            correctiveLayout = itemView.findViewById(R.id.correctiveLayout);
            preventiveLayout = itemView.findViewById(R.id.preventiveLayout);
            inputlayout_corrective_action = itemView.findViewById(R.id.inputlayout_corrective_action);
            inputlayout_preventive_action = itemView.findViewById(R.id.inputlayout_preventive_action);


            tv_analysis.setTypeface(Utilities.fontRegular(context));
            tv_clasificationremarks.setTypeface(Utilities.fontRegular(context));
            tv_rootcause.setTypeface(Utilities.fontRegular(context));
            tv_subcontractor.setTypeface(Utilities.fontRegular(context));
            et_analysis.setTypeface(Utilities.fontRegular(context));
            et_clasificationremarks.setTypeface(Utilities.fontRegular(context));
            et_rootcause.setTypeface(Utilities.fontRegular(context));
            et_subcontractor.setTypeface(Utilities.fontRegular(context));
            countCorrective.setTypeface(Utilities.fontRegular(context));
            countPreventive.setTypeface(Utilities.fontRegular(context));
            counted.setTypeface(Utilities.fontRegular(context));
            correctiveText.setTypeface(Utilities.fontRegular(context));
            preventiveText.setTypeface(Utilities.fontRegular(context));

            correctiveEdit.setTypeface(Utilities.fontRegular(context));
            preventiveEdit.setTypeface(Utilities.fontRegular(context));


            action_description.setTypeface(Utilities.fontBold(context));
            action_remarks.setTypeface(Utilities.fontBold(context));
            action_name.setTypeface(Utilities.fontBold(context));
            count_text.setTypeface(Utilities.fontRegular(context));
            countEdit.setTypeface(Utilities.fontRegular(context));
            description.setTypeface(Utilities.fontRegular(context));
            injuryText.setTypeface(Utilities.fontRegular(context));
            listoftask_header.setTypeface(Utilities.fontBold(context));
            idText.setTypeface(Utilities.fontBold(context));

            action_remarks.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(action_remarks, InputMethodManager.SHOW_IMPLICIT);
                    return false;
                }
            });
            count_text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(count_text, InputMethodManager.SHOW_IMPLICIT);
                    return false;
                }
            });
            action_name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(action_name, InputMethodManager.SHOW_IMPLICIT);
                    return false;
                }
            });
            listoftask_header.setText("ITEM " + (position + 1) + "");
            final ObservationItemsDetailsModel ObservationItemsDetailsModelModel = al.get(position);
            if (ObservationItemsDetailsModelModel.getObservationAttachmentModels() != null) {
                galleryAdapter = new GalleryAdapter(context, ObservationItemsDetailsModelModel.getObservationAttachmentModels(), observationHeaderid);
                gv.setAdapter(galleryAdapter);
                gv.setVerticalSpacing(gv.getHorizontalSpacing());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
                mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);
                countEdit.setFocusableInTouchMode(false);
                edit_description2.setFocusableInTouchMode(false);
            }
//            if (ObservationItemsDetailsModelModel.getObservationAttachmentModels().size()>0){
//                gv.setStackFromBottom(true);
//            }


            correctiveLayout.setVisibility(View.VISIBLE);
            preventiveLayout.setVisibility(View.VISIBLE);
            inputlayout_corrective_action.setVisibility(View.VISIBLE);
            inputlayout_preventive_action.setVisibility(View.VISIBLE);


            countEdit.setText(al.get(position).getBriefDescription());

            correctiveEdit.setText(al.get(position).getCorrectiveAction());
            preventiveEdit.setText(al.get(position).getPreventiveAction());
            et_analysis.setText(al.get(position).getAnalysis());
            et_clasificationremarks.setText(al.get(position).getRemarkreason());
            et_rootcause.setText(al.get(position).getRouteCause());
            et_subcontractor.setText(al.get(position).getSubcontractor());
            countEdit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    countEdit.setFocusableInTouchMode(true);
                    if (countEdit.hasFocus()) {
//                        countEdit.setEnabled(true);
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
            edit_description2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    edit_description2.setFocusableInTouchMode(true);
                    if (edit_description2.hasFocus()) {
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
            countEdit.setSelection(countEdit.getText().length());

            edit_description2.setText(al.get(position).getDescription2());
//            edit_description2.setSelection(countEdit.getText().length());

            edit_verticle.setFocusable(false);

            verticleResponseResultArrayList = new ArrayList<>();
            verticleResponseResultArrayList.add("Select Vertical *");

            hazardType = new ArrayList<>();
            hazardType.add("Select Near Miss Type *");
            risktype = new ArrayList<>();
            risktype.add("Select Risk Type *");
            categoty = new ArrayList<>();
            categoty.add("Select Category *");
            classification = new ArrayList<>();
            classification.add("May have resulted in First Aid / LTI / MTI / Fatality");

            if (al.size() == 1) {
                if (position == 0) {
                    item_delete.setVisibility(View.GONE);
                }
            } else {
                item_delete.setVisibility(View.VISIBLE);
            }


            for (int i = 0; i < entityResponses.size(); i++) {

                if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Vertical")) {
                    verticleResponseResultArrayList.add(entityResponses.get(i).getLookUpValue());
                } else if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("NearMiss")) {
                    hazardType.add(entityResponses.get(i).getLookUpValue());
                } else if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Risk")) {
                    risktype.add(entityResponses.get(i).getLookUpValue());
                } else if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Category")) {
                    categoty.add(entityResponses.get(i).getLookUpValue());
                } else if(entityResponses.get(i).getLookUpName().equalsIgnoreCase("Classification")){
                    classification.add(entityResponses.get(i).getLookUpValue());
                }
            }

            ArrayAdapter verticle = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, verticleResponseResultArrayList);

            edit_verticle.setAdapter(verticle);

            for (int i = 0; i < verticleResponseResultArrayList.size(); i++) {

                if (ObservationItemsDetailsModelModel.getVertical() != null && verticleResponseResultArrayList.get(i).equalsIgnoreCase(ObservationItemsDetailsModelModel.getVertical())) {
                    edit_verticle.setSelection(i);
                    Log.d("Position", "" + position);
                    Log.d("AdapterPosition", "" + position);
                }
            }
            edit_verticle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return false;
                }
            });

            edit_verticle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    ObservationItemsDetailsModelModel.setTypeOfObservation("Hazard");
                    ObservationItemsDetailsModelModel.setVertical((String) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter hazardtype = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, hazardType);
            edit_hazard_type.setAdapter(hazardtype);

            edit_hazard_type.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return false;
                }
            });
            edit_hazard_type.setSelection(hazardtype.getPosition(ObservationItemsDetailsModelModel.getHazardType()));

            edit_hazard_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    al.get(position).setHazardType((String) adapterView.getItemAtPosition(i));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter risk = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, risktype);
            edit_risk.setAdapter(risk);
            edit_risk.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return false;
                }
            });
            edit_risk.setSelection(risk.getPosition(ObservationItemsDetailsModelModel.getRisk()));

            edit_risk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    al.get(position).setRisk((String) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter category = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, categoty);
            edit_category.setAdapter(category);
            edit_category.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return false;
                }
            });
            edit_category.setSelection(category.getPosition(ObservationItemsDetailsModelModel.getCategory()));

            edit_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    al.get(position).setCategory((String) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            ArrayAdapter classificationAdapter = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, classification);
            sp_clasification.setAdapter(classificationAdapter);
            sp_clasification.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return false;
                }
            });
            sp_clasification.setSelection(classificationAdapter.getPosition(ObservationItemsDetailsModelModel.getClassification()));

            sp_clasification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    al.get(position).setClassification((String) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            countEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (charSequence.length() == 0) {
                        count_text.setVisibility(View.GONE);
                    } else {
                        count_text.setVisibility(View.VISIBLE);
                        count_text.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    al.get(position).setBriefDescription(editable.toString().trim());
                }
            });

            edit_description2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (charSequence.length() == 0) {
                        countedTwo.setVisibility(View.GONE);
                    } else {
                        countedTwo.setVisibility(View.VISIBLE);
                        countedTwo.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    al.get(position).setDescription2(editable.toString().trim());
                }
            });

            if (strObservationID != null && strStatus.equalsIgnoreCase("Assigned")) {
                correctiveEdit.setFocusable(true);
                preventiveEdit.setFocusable(true);
                countEdit.setFocusable(true);
                countEdit.setEnabled(true);
                edit_hazard_type.setEnabled(true);
                edit_risk.setEnabled(true);
                edit_category.setEnabled(true);
                edit_verticle.setEnabled(true);
                edit_description2.setFocusable(true);
                edit_description2.setEnabled(true);
                item_delete.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                action_layout.setVisibility(View.VISIBLE);
                // description.setVisibility(View.GONE);
                action_description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        //count_text.setText(String.valueOf(s.length()) + "/" + 500);
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
                        count_text.setText(String.valueOf(s.toString().length()) + "/" + 500);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        al.get(position).setAdminRemarks(action_remarks.getText().toString());
                    }
                });

                add_actions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            itemsActionDetailsModel = new ItemsActionDetailsModel();
                            actions_listview.setVisibility(View.VISIBLE);
                            itemsActionDetailsModel.setTargetDateOfClosure(dateofCloser);
                            itemsActionDetailsModel.setId(0);
                            itemsActionDetailsModel.setReason(Collections.singletonList(0));
                            itemsActionDetailsModel.setDescription(action_description.getText().toString());
                            itemsActionDetailsModel.setCpRemarks(action_remarks.getText().toString().trim());
                            // changed from ifloop
                            al.get(position).setAdminRemarks(action_remarks.getText().toString().trim());
                            if (action_name.getText().toString().length() > 0) {
                                actions_listview.setVisibility(View.VISIBLE);
                                itemsActionDetailsModel.setActionName(action_name.getText().toString());
                                itemsActionDetailsModel.setStatus("Open");

                                al.get(position).setActionsDescription(action_description.getText().toString());
                                //al.get(position).setAdminRemarks(action_remarks.getText().toString().trim());
                                itemsActionDetailsModel.setObservationItemId(al.get(position).getId());
                                observationitemsactondetailsModel.add(itemsActionDetailsModel);
                                observtionActionAdapter = new ObservtionActionAdapter(context, observationitemsactondetailsModel);
                                actions_listview.setAdapter(observtionActionAdapter);
                                action_name.setText("");

                                al.get(position).setItemsActionDetailsModels(observationitemsactondetailsModel);

                            } else {
                                String errorString = "Please Enter User Name";

                                action_name.setError(errorString);
                                action_name.setFocusable(true);
                                action_name.setFocusableInTouchMode(true);
                                action_name.setEnabled(true);
                                action_name.requestFocus();
                            }
                        } catch (Exception e) {
                            e.getMessage();
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

            } else {

                correctiveEdit.setFocusable(true);
                preventiveEdit.setFocusable(true);
                countEdit.setFocusable(true);
                edit_hazard_type.setEnabled(true);
                edit_risk.setEnabled(true);
                edit_category.setEnabled(true);
                edit_verticle.setEnabled(true);
                edit_description2.setFocusable(true);
                item_delete.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                action_layout.setVisibility(View.GONE);
            }
            correctiveEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 0) {
                        countCorrective.setVisibility(View.GONE);
                    } else {
                        countCorrective.setVisibility(View.VISIBLE);
                        countCorrective.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    al.get(position).setCorrectiveAction(editable.toString().trim());
                }
            });


            preventiveEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 0) {
                        countPreventive.setVisibility(View.GONE);
                    } else {
                        countPreventive.setVisibility(View.VISIBLE);
                        countPreventive.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    al.get(position).setPreventiveAction(editable.toString().trim());
                }
            });


            et_analysis.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    /*if (charSequence.length() == 0) {
                        countPreventive.setVisibility(View.GONE);
                    } else {
                        countPreventive.setVisibility(View.VISIBLE);
                        countPreventive.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    al.get(position).setAnalysis(editable.toString().trim());
                }
            });


            et_clasificationremarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   /* if (charSequence.length() == 0) {
                        countPreventive.setVisibility(View.GONE);
                    } else {
                        countPreventive.setVisibility(View.VISIBLE);
                        countPreventive.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    al.get(position).setRemarkreason(editable.toString().trim());
                }
            });


            et_rootcause.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  /*  if (charSequence.length() == 0) {
                        countPreventive.setVisibility(View.GONE);
                    } else {
                        countPreventive.setVisibility(View.VISIBLE);
                        countPreventive.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    al.get(position).setRouteCause(editable.toString().trim());
                }
            });

            et_subcontractor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  /*  if (charSequence.length() == 0) {
                        countPreventive.setVisibility(View.GONE);
                    } else {
                        countPreventive.setVisibility(View.VISIBLE);
                        countPreventive.setText(String.valueOf(charSequence.length()) + "/" + 500);
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    al.get(position).setSubcontractor(editable.toString().trim());
                }
            });



            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListView) viewGroup).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
//                    gv.setStackFromBottom(true);
                }
            });

            item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                        alertDialog.setMessage("Are you sure, Do you want to delete the item ?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (objItem.getId() != null) {
//                                    deleteObservationItem(position);
                                    deleteObservationItem(position, objItem.getId());
                                } else {
                                    al.remove(position);
                                    notifyDataSetChanged();
                                }
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
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception", e.getMessage());
        }

        return itemView;
    }

    public void deleteObservationItem(final int id, int itemId) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
            progressDialog.setMessage("Data loading");
            // progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            Call<CreateResponse> call = apiInterface.deleteObservationItem("Bearer " + sharedPreferences.getString("Bearertoken", null), itemId);
            progressDialog.show();

            call.enqueue(new Callback<CreateResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            if (response.body().getResult() != null) {
                                CreateResponse loginResponse = response.body();
                                Log.d("observationresponse", response.body().getResult() + "");
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                                builder.setTitle(context.getResources().getString(R.string.information))
                                        .setMessage("Item Deleted Successfully ")
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                al.remove(id);
                                                notifyDataSetChanged();
                                            }
                                        }).create().show();

                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                                builder.setTitle(context.getResources().getString(R.string.information))
                                        .setMessage("No Data")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            builder.setTitle(context.getResources().getString(R.string.information))
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                        builder.setTitle(context.getResources().getString(R.string.information))
                                .setMessage("" + response.message())
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
                    Log.d("LoginResponse", t.getMessage() + "");
                    progressDialog.dismiss();
                }

            });
        } catch (Exception e) {

            e.getMessage();
        }
    }
}

