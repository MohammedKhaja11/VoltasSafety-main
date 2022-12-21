package com.ags.voltassafety.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
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


import com.ags.voltassafety.HazardActivityForEmployee;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ags.voltassafety.HazardActivityForEmployee.strStatus;


public class HazardAdapterForEmployee extends BaseAdapter {
    Context context;
    public List<ObservationItemsDetailsModel> al;
    private GridView gv;
    List<EntityResult> entityResponses;
    ArrayList<String> hazardType;
    ArrayList<String> risktype;
    ArrayList<String> categoty;
    ArrayList<String> verticleResponseResultArrayList;
    private OnItemClicked onClick;
    public AlertDialog.Builder alertDialog;
    private GalleryAdapter galleryAdapter;
    private String strObservationID;
    ItemsActionDetailsModel itemsActionDetailsModel;
    List<ItemsActionDetailsModel> observationitemsactondetailsModel = new ArrayList<>();
    ProgressDialog progressDialog;
    int itemposition;
    String typeOfObservation;


    //make interface like this
    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    ObservtionActionAdapter observtionActionAdapter;

    Map<Integer, Integer> mSpinnerhazard;
    Map<Integer, Integer> mSpinnerrisk;
    Map<Integer, Integer> mSpinnercategory;
    Map<Integer, Integer> mSpinnerverticle;

    String observationIdStr;
    String dateofCloser;


    public HazardAdapterForEmployee(HazardActivityForEmployee hazard_activity, List<ObservationItemsDetailsModel> al, List<EntityResult> entityResponseArrayList, ArrayList<String> verticleResponseResultArrayList, int itemposition, String typeOfObservation, String observationIdstr, String dataofCloser) {

        context = hazard_activity;
        this.al = new ArrayList<>();
        this.al = al;
        this.typeOfObservation = typeOfObservation;
        entityResponses = entityResponseArrayList;
        this.onClick = onClick;
        this.verticleResponseResultArrayList = verticleResponseResultArrayList;
        mSpinnerhazard = new HashMap<Integer, Integer>();
//        mSpinnerhazard = new HashMap<Integer, Integer>();
        mSpinnerrisk = new HashMap<Integer, Integer>();
        mSpinnercategory = new HashMap<Integer, Integer>();
        mSpinnerverticle = new HashMap<Integer, Integer>();
        //strObservationID = observationId;
        this.itemposition = itemposition;
        this.observationIdStr = observationIdstr;
        this.dateofCloser = dataofCloser;
        if (al != null && al.size() > 0 && al.get(0).getObservationId() != null) {
            strObservationID = al.get(0).getObservationId();
            //strStatus=al.get(0).getStatus();
        } else {
            //  strObservationID="empty";
        }
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
        final TextView subcontractorText,listoftask_header, count_text, description, counted, countedTwo, countCorrective, countPreventive, injuryText,
                preventiveText, correctiveText, idText, addFileText;
        final EditText et_subcontractor, countEdit, edit_description2, action_description, action_remarks, action_name, correctiveEdit, preventiveEdit;
        final ImageView addButton, item_delete, add_actions, remove_image;
//        final TextView listoftask_header, count_text, description, counted,countedTwo;
        LinearLayout correctiveLayout, preventiveLayout;
        TextInputLayout inputlayout_corrective_action, inputlayout_preventive_action;
        final NestedScrollView parent_scroll;
        LinearLayout action_layout;
        Spinner edit_hazard_type, edit_risk, edit_category, edit_verticle;
        final ListView actions_listview;
        final ObservationItemsDetailsModel objItem = (ObservationItemsDetailsModel) getItem(position);
        View itemView = LayoutInflater.from(context).inflate(R.layout.hazard_items, viewGroup, false);

        et_subcontractor = itemView.findViewById(R.id.subcontractorEdit);
        subcontractorText = itemView.findViewById(R.id.subcontractorText);
        edit_hazard_type = itemView.findViewById(R.id.edit_hazard_type);
        edit_risk = itemView.findViewById(R.id.edit_risk);
        edit_category = itemView.findViewById(R.id.edit_category);
        countEdit = itemView.findViewById(R.id.countEdit);
        counted = itemView.findViewById(R.id.counted);
        countedTwo = itemView.findViewById(R.id.countedTwo);
        edit_description2 = itemView.findViewById(R.id.edit_description2);
        listoftask_header = itemView.findViewById(R.id.listoftask_header);
        addButton = itemView.findViewById(R.id.addButton);
        item_delete = itemView.findViewById(R.id.item_delete);
        gv = itemView.findViewById(R.id.gv);
        edit_verticle = itemView.findViewById(R.id.edit_verticle);
        action_layout = itemView.findViewById(R.id.action_layout);
        add_actions = itemView.findViewById(R.id.add_actions);
        actions_listview = itemView.findViewById(R.id.actions_listview);
        description = itemView.findViewById(R.id.description);
        idText = itemView.findViewById(R.id.idText);
        addFileText = itemView.findViewById(R.id.addFileText);
        action_description = itemView.findViewById(R.id.action_description);
        action_remarks = itemView.findViewById(R.id.action_remarks);
        action_name = itemView.findViewById(R.id.action_name);
        count_text = itemView.findViewById(R.id.count_text);

        //  remove_image=itemView.findViewById(R.id.remove_image);
//        parent_scroll = itemView.findViewById(R.id.parent_scroll);

//        New Changed Ids
        correctiveEdit = itemView.findViewById(R.id.correctiveEdit);
        preventiveEdit = itemView.findViewById(R.id.preventiveEdit);
        correctiveLayout = itemView.findViewById(R.id.correctiveLayout);
        preventiveLayout = itemView.findViewById(R.id.preventiveLayout);
        inputlayout_corrective_action = itemView.findViewById(R.id.inputlayout_corrective_action);
        inputlayout_preventive_action = itemView.findViewById(R.id.inputlayout_preventive_action);
        injuryText = itemView.findViewById(R.id.injuryText);
        correctiveText = itemView.findViewById(R.id.correctiveText);
        preventiveText = itemView.findViewById(R.id.preventiveText);
        countCorrective = itemView.findViewById(R.id.countCorrective);
        countPreventive = itemView.findViewById(R.id.countPreventive);


        countCorrective.setTypeface(Utilities.fontRegular(context));
        countPreventive.setTypeface(Utilities.fontRegular(context));
        counted.setTypeface(Utilities.fontRegular(context));
        countedTwo.setTypeface(Utilities.fontRegular(context));
        listoftask_header.setTypeface(Utilities.fontBold(context));
        idText.setTypeface(Utilities.fontBold(context));
        description.setTypeface(Utilities.fontRegular(context));
        injuryText.setTypeface(Utilities.fontRegular(context));
        correctiveText.setTypeface(Utilities.fontRegular(context));
        preventiveText.setTypeface(Utilities.fontRegular(context));
        addFileText.setTypeface(Utilities.fontRegular(context));
        correctiveEdit.setTypeface(Utilities.fontRegular(context));
        preventiveEdit.setTypeface(Utilities.fontRegular(context));
        countEdit.setTypeface(Utilities.fontRegular(context));
        countEdit.setTypeface(Utilities.fontRegular(context));
        edit_description2.setTypeface(Utilities.fontRegular(context));
        action_description.setTypeface(Utilities.fontBold(context));
        action_remarks.setTypeface(Utilities.fontBold(context));
        action_name.setTypeface(Utilities.fontBold(context));
        count_text.setTypeface(Utilities.fontBold(context));
        subcontractorText.setTypeface(Utilities.fontRegular(context));
        et_subcontractor.setTypeface(Utilities.fontRegular(context));

//        Log.d("OfObservation", al.get(position).getTypeOfObservation());
        if (typeOfObservation.equalsIgnoreCase("Hazard")) {
            correctiveLayout.setVisibility(View.VISIBLE);
            preventiveLayout.setVisibility(View.VISIBLE);
            inputlayout_corrective_action.setVisibility(View.VISIBLE);
            inputlayout_preventive_action.setVisibility(View.VISIBLE);
        }
        if (al != null && al.size() > 0 && al.get(0).getObservationId() != null && strStatus.equalsIgnoreCase("assigned")) {

            listoftask_header.setText("ITEM " + (itemposition + 1) + "");
        } else {
            listoftask_header.setText("ITEM " + (position + 1) + "");
        }

        // listoftask_header.setText("ITEM " + (position + 1) + "");

        final ObservationItemsDetailsModel ObservationItemsDetailsModelModel = al.get(position);

        if (ObservationItemsDetailsModelModel.getObservationAttachmentModels() != null) {

            galleryAdapter = new GalleryAdapter(context, ObservationItemsDetailsModelModel.getObservationAttachmentModels(), observationIdStr);
            gv.setAdapter(galleryAdapter);
            gv.setVerticalSpacing(gv.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
            mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);
        }

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

        countEdit.setText(al.get(position).getBriefDescription());
        correctiveEdit.setText(al.get(position).getCorrectiveAction());
        preventiveEdit.setText(al.get(position).getPreventiveAction());
        et_subcontractor.setText(al.get(position).getSubcontractor());

        countEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (countEdit.hasFocus()) {
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

//        countEdit.post(new Runnable() {
//            @Override
//            public void run() {

        countEdit.setSelection(countEdit.getText().length());
        edit_description2.setSelection(edit_description2.getText().length());
//            }
//        });

        edit_description2.setText(al.get(position).getDescription2());

        edit_verticle.setFocusable(false);
        hazardType = new ArrayList<>();
//        String styledText = "This is <font color='red'>simple</font>.";
//        textView.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

//
//        String star= String.valueOf(Html.fromHtml("<font color=\"#000000\">text</font>"));
//
//        Toast.makeText(context,star,Toast.LENGTH_LONG).show();

        hazardType.add("Select Hazard Type *");
        risktype = new ArrayList<>();
        risktype.add("Select Risk Type *");
        categoty = new ArrayList<>();
        categoty.add("Select Category *");
        if (strObservationID != null && strStatus.equalsIgnoreCase("Assigned")) {

            countEdit.setFocusable(true);
            correctiveEdit.setFocusable(true);
            preventiveEdit.setFocusable(true);
            et_subcontractor.setFocusable(true);
            edit_hazard_type.setEnabled(true);
            edit_risk.setEnabled(true);
            edit_category.setEnabled(true);
            edit_verticle.setEnabled(true);
            edit_description2.setFocusable(true);
            item_delete.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);


            action_layout.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);


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
                    int maxLengthofYourEditText = 500;    // Set Your Limit
                    action_description.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofYourEditText)});

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

                    itemsActionDetailsModel = new ItemsActionDetailsModel();

                    // ObservationAttachmentModel observationAttachmentModel = new ObservationAttachmentModel();
//                    List<ActionItemObservationAttachmentModel> observationAttachmentModelList = new ArrayList<ActionItemObservationAttachmentModel>();
//                    // observationAttachmentModelList.add(observationAttachmentModel);
//                    itemsActionDetailsModel.setObservationAttachmentModels(observationAttachmentModelList);
                    // actions_listview.setVisibility(View.VISIBLE);
//                    itemsActionDetailsModel.setActionName(action_name.getText().toString());
                    itemsActionDetailsModel.setId(0);
                    //  itemsActionDetailsModel.setObservationItemId(0);
                    itemsActionDetailsModel.setTargetDateOfClosure(dateofCloser);
                    itemsActionDetailsModel.setReason(Collections.singletonList(0));
                    itemsActionDetailsModel.setDescription(action_description.getText().toString());
                    itemsActionDetailsModel.setCpRemarks(action_remarks.getText().toString().trim());
                    if (action_name.getText().toString().trim().length() > 0) {
                        actions_listview.setVisibility(View.VISIBLE);
                        itemsActionDetailsModel.setActionName(action_name.getText().toString().trim());
                        itemsActionDetailsModel.setStatus("Open");

//                        itemsActionDetailsModel.setAdminRemarks(action_remarks.getText().toString());
                        Log.d("remarks", action_remarks.getText().toString());
//                        itemsActionDetailsModel.setDescription(action_description.getText().toString());

                        al.get(position).setActionsDescription(action_description.getText().toString().trim());
                        al.get(position).setAdminRemarks(action_remarks.getText().toString().trim());
                        itemsActionDetailsModel.setObservationItemId(al.get(position).getId());
                        observationitemsactondetailsModel.add(itemsActionDetailsModel);
                        observtionActionAdapter = new ObservtionActionAdapter(context, observationitemsactondetailsModel);
                        actions_listview.setAdapter(observtionActionAdapter);
                        action_name.setText("");
                        //observtionActionAdapter.notifyDataSetChanged();
                        al.get(position).setItemsActionDetailsModels(observationitemsactondetailsModel);

                    } else {
                        String errorString = "Please Add Task";

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
//
//            parent_scroll.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    actions_listview.getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
//                }
//            });

//            parent_scroll.fullScroll(ScrollView.FOCUS_DOWN);
//            parent_scroll.post(new Runnable() {
//                @Override
//                public void run() {
//                    action_description.requestFocus();
//                }
//            });
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


        } else {
            countEdit.setFocusable(true);
            correctiveEdit.setFocusable(true);
            preventiveEdit.setFocusable(true);
            edit_hazard_type.setEnabled(true);
            et_subcontractor.setFocusable(true);
            edit_risk.setEnabled(true);
            edit_category.setEnabled(true);
            edit_verticle.setEnabled(true);
            edit_description2.setFocusable(true);
            item_delete.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            action_layout.setVisibility(View.GONE);

        }

        if (al.size() == 1) {
            if (position == 0) {
                item_delete.setVisibility(View.GONE);
            }
        } else {
            item_delete.setVisibility(View.VISIBLE);
        }
//        if (al != null && al.size() > 0 && al.get(0).getObservationId() != null) {
//            item_delete.setVisibility(View.GONE);
//        }else {
//            item_delete.setVisibility(View.VISIBLE);
//        }

//        if (strObservationID != null && strStatus.equalsIgnoreCase("open") && al.size() == 0) {
//            item_delete.setVisibility(View.GONE);
//        } else {
//            item_delete.setVisibility(View.VISIBLE);
//        }

//        if (al != null && al.size() > 0 && al.get(0).getObservationId() != null) {
//            item_delete.setVisibility(View.GONE);
//        }else {
//            item_delete.setVisibility(View.VISIBLE);
//        }

//        if (strObservationID != null && strStatus.equalsIgnoreCase("open") && al.size() == 0) {
//            item_delete.setVisibility(View.GONE);
//        } else {
//            item_delete.setVisibility(View.VISIBLE);
//        }

        for (int i = 0; i < entityResponses.size(); i++) {

//            if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Verticle")) {
//                verticleResponseResultArrayList.add(entityResponses.get(i).getLookUpValue());
//            } else
            if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("HazardType")) {
                hazardType.add(entityResponses.get(i).getLookUpValue());
            } else if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Risk")) {
                risktype.add(entityResponses.get(i).getLookUpValue());
            } else if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Category")) {
                categoty.add(entityResponses.get(i).getLookUpValue());
            }
        }

        ArrayAdapter verticle = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, verticleResponseResultArrayList);

        edit_verticle.setAdapter(verticle);


//
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
                mSpinnerverticle.put(position, adapterView.getSelectedItemPosition());
                ObservationItemsDetailsModelModel.setVertical((String) adapterView.getItemAtPosition(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter hazardtype = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, hazardType);
        edit_hazard_type.setAdapter(hazardtype);

        edit_hazard_type.setSelection(hazardtype.getPosition(ObservationItemsDetailsModelModel.getHazardType()));

        edit_hazard_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });
        edit_hazard_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                al.get(position).setHazardType((String) adapterView.getItemAtPosition(i));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter risk = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, risktype);
        edit_risk.setAdapter(risk);

        edit_risk.setSelection(risk.getPosition(ObservationItemsDetailsModelModel.getRisk()));
        edit_risk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });

        edit_risk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mSpinnerrisk.put(position, adapterView.getSelectedItemPosition());
                al.get(position).setRisk((String) adapterView.getItemAtPosition(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter category = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, categoty);
        edit_category.setAdapter(category);

        edit_category.setSelection(category.getPosition(ObservationItemsDetailsModelModel.getCategory()));
        edit_category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });
        edit_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnercategory.put(position, adapterView.getSelectedItemPosition());
                al.get(position).setCategory((String) adapterView.getItemAtPosition(i));

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
                    counted.setVisibility(View.GONE);
                } else {
                    counted.setVisibility(View.VISIBLE);
                    counted.setText(String.valueOf(charSequence.length()) + "/" + 500);
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
//                countedTwo.setText(String.valueOf(charSequence.length()) + "/" + 500);
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

        et_subcontractor.addTextChangedListener(new TextWatcher() {
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
                al.get(position).setSubcontractor(editable.toString().trim());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) viewGroup).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });


        item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                alertDialog.setMessage("Are you sure, Do you want to delete the item ?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (objItem.getId() != null) {
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
//
            }

        });
        return itemView;
    }

    public void deleteObservationItem(final int id, int itemId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Bearer", MODE_PRIVATE);
        ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
        progressDialog.setMessage("Data loading");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
//        Gson gson = new Gson();
//        String json = gson.toJson(observationHeader);
//        Log.d("jsonRequest", json);
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
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
//
                        } else {
                            progressDialog.dismiss();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                    builder.setTitle(context.getResources().getString(R.string.information))
                            .setMessage(response.message() + "")
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

    }

}

