package com.ags.voltassafety.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.ClosedActionItemActivity;
import com.ags.voltassafety.EditActionItemActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.ReOpenActionItemActivity;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.utility.Utilities;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ObservationDetailsAdapter extends RecyclerView.Adapter<ObservationDetailsAdapter.MyViewHolder> {
    Context context;
    List<ObservationItemsDetailsModel> observationItemsDetailsModels;
    List<ObservationAttachmentModel> observationAttachmentModelList;
    private OnItemClicked onClick;
    private View v;
    private MyViewHolder vh;
    ObservationHeader observationHeader;
    IncidentMainGalleryAdapter incidentMainGalleryAdapter;
    //    public ListView actionList;
    GalleryAdapter galleryAdapter;
    DetailsViewAdapter detailsViewAdapter;
    List<ObservationAttachmentModel> linkarraylist;
//    private String observationType;

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public ObservationDetailsAdapter(Context observationDetails, List<ObservationItemsDetailsModel> observationItemsDetailsModels, List<ObservationAttachmentModel> observationAttachmentModelList, OnItemClicked onClick, ObservationHeader observationHeader) {
        context = observationDetails;
        this.observationHeader = observationHeader;
        this.observationItemsDetailsModels = observationItemsDetailsModels;
        this.observationAttachmentModelList = observationAttachmentModelList;
        Log.d("Header", observationHeader + "");

        this.onClick = onClick;

//        this.observationType = observationType;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_observationdetails_adapter, parent, false);
            vh = new MyViewHolder(v);
        } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_observation_detail_adapter_layout, parent, false);
            vh = new MyViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_observationdetails_nearmiss_adapter, parent, false);
            vh = new MyViewHolder(v);
        }
        // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Bearer", MODE_PRIVATE);
        String roleId = sharedPreferences.getString("roleId", null);
        String LoginUserID = sharedPreferences.getString("LoginUserID", null);

        if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
//            holder.gv.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (holder.gv.hasFocus()) {
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        switch (event.getAction() & MotionEvent.ACTION_UP) {
//                            case MotionEvent.ACTION_SCROLL:
//                                v.getParent().requestDisallowInterceptTouchEvent(false);
//                                return true;
//                        }
//                    }
//                    return false;
//                }
//            });

            if (observationHeader.getStatus().equalsIgnoreCase("assigned")) {

                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getProjectManagerId())) {
                    holder.item_edit.setVisibility(View.VISIBLE);
                } else {
                    holder.item_edit.setVisibility(View.GONE);
                }

            } else {
                holder.item_edit.setVisibility(View.GONE);
            }

            if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                if (getValidateObservation(observationItemsDetailsModels.get(position).getItemsActionDetailsModels())) {
                    holder.item_edit.setVisibility(View.GONE);
                }
            }

            holder.listoftask_header.setText("ITEM  " + (position + 1) + "");
            holder.edit_verticle.setText(observationItemsDetailsModels.get(position).getVertical());
            holder.edit_hazard_type.setText(observationItemsDetailsModels.get(position).getHazardType());
            holder.edit_risk.setText(observationItemsDetailsModels.get(position).getRisk());
            holder.edit_category.setText(observationItemsDetailsModels.get(position).getCategory());
            holder.countEdit.setText(observationItemsDetailsModels.get(position).getBriefDescription());
            holder.edit_description2.setText(observationItemsDetailsModels.get(position).getDescription2());
            holder.correctiveAction.setText(observationItemsDetailsModels.get(position).getCorrectiveAction());
            holder.preventiveAction.setText(observationItemsDetailsModels.get(position).getPreventiveAction());
            holder.subcontractor.setText(observationItemsDetailsModels.get(position).getSubcontractor());

            holder.listoftask_header.setTypeface(Utilities.fontBold(context));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.images_view.setLayoutManager(linearLayoutManager);

            //   holder.images_view.setNestedScrollingEnabled(false);


            Log.d("AttachmentsHazard", observationItemsDetailsModels.get(position).getObservationAttachmentModels().size() + "");

            if (observationItemsDetailsModels.get(position).getObservationAttachmentModels() != null) {

                detailsViewAdapter = new DetailsViewAdapter(context, observationItemsDetailsModels.get(position).getObservationAttachmentModels());
                holder.gv.setAdapter(detailsViewAdapter);
                holder.gv.setVerticalSpacing(holder.gv.getHorizontalSpacing());
                setGridViewHeightBasedOnChildren(holder.gv, 5);
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.gv.getLayoutParams();
                mlp.setMargins(0, holder.gv.getHorizontalSpacing(), 0, 0);
            }


            LinearLayout.LayoutParams llhead_layoutParamh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llhead_layoutParamh.setMargins(5, 20, 0, 10);
            LinearLayout tableRowhead = new LinearLayout(context);
            tableRowhead.setOrientation(LinearLayout.HORIZONTAL);
            tableRowhead.setWeightSum(1);
            tableRowhead.setLayoutParams(llhead_layoutParamh);

            LinearLayout.LayoutParams layoutParams_textvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
            layoutParams_textvt1.setMargins(5, 0, 0, 10);
            TextView textview1 = new TextView(context);
            textview1.setLayoutParams(layoutParams_textvt1);
            textview1.setTextColor(Color.parseColor("#ff33b5e5"));
            textview1.setTypeface(Utilities.fontBold(context));
            textview1.setText("CLOSE OBSERVATION HERE");
            textview1.setTextSize(16);
            tableRowhead.addView(textview1);

            if(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                holder.action_item_view.addView(tableRowhead);
            }

            for (int i = 0; i < observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size(); i++) {

                LinearLayout.LayoutParams llhead_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParams.setMargins(5, 20, 0, 10);
                LinearLayout tableRow = new LinearLayout(context);
                tableRow.setOrientation(LinearLayout.VERTICAL);
                tableRow.setLayoutParams(llhead_layoutParams);

                LinearLayout.LayoutParams llhead_layoutParamsh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParamsh.setMargins(5, 20, 0, 10);
                LinearLayout tableRowh = new LinearLayout(context);
                tableRowh.setOrientation(LinearLayout.HORIZONTAL);
                tableRowh.setWeightSum(1);
                tableRowh.setLayoutParams(llhead_layoutParamsh);

                LinearLayout.LayoutParams layoutParams_tvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
                layoutParams_tvt1.setMargins(5, 0, 0, 10);
                TextView textview = new TextView(context);
                textview.setLayoutParams(layoutParams_tvt1);
                textview.setTextColor(Color.parseColor("#ff33b5e5"));
                textview.setTypeface(Utilities.fontBold(context));
                textview.setText("ACTION ITEMS" + "   " + (i + 1));
                textview.setTextSize(16);
                tableRowh.addView(textview);

                LinearLayout.LayoutParams layoutParams_tvth = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvth.setMargins(5, 0, 10, 10);
                TextView textviewh = new TextView(context);
                textviewh.setLayoutParams(layoutParams_tvth);
                textviewh.setText("Edit");
                textviewh.setPadding(0, 10, 0, 10);
                textviewh.setGravity(Gravity.CENTER);
                textviewh.setTextSize(14);
                textviewh.setTextColor(Color.parseColor("#000000"));
                textviewh.setBackgroundResource(R.drawable.closebutton_color);


                LinearLayout.LayoutParams layoutParams_tvt1h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt1h.setMargins(10, 0, 5, 10);
                TextView textview1h = new TextView(context);
                textview1h.setLayoutParams(layoutParams_tvt1h);
                textview1h.setText("Close");
                textview1h.setPadding(0, 10, 0, 10);
                textview1h.setGravity(Gravity.CENTER);
                textview1h.setTextSize(14);
                textview1h.setTextColor(Color.parseColor("#000000"));
                textview1h.setBackgroundResource(R.drawable.closebutton_color);

                LinearLayout.LayoutParams layoutParams_tvt2h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt2h.setMargins(0, 0, 0, 10);
                TextView textview2h = new TextView(context);
                textview2h.setLayoutParams(layoutParams_tvt2h);
                textview2h.setText("ReOpen");
                textview2h.setPadding(10, 10, 10, 10);
                textview2h.setGravity(Gravity.CENTER);
                textview2h.setTextSize(14);
                textview2h.setTextColor(Color.parseColor("#000000"));
                textview2h.setBackgroundResource(R.drawable.closebutton_color);
                textview2h.setVisibility(View.GONE);

                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getProjectManagerId())) {
                    textviewh.setVisibility(View.VISIBLE);
                    textview1h.setVisibility(View.VISIBLE);
                } else {
                    textviewh.setVisibility(View.GONE);
                    textview1h.setVisibility(View.GONE);
                    //textview2h.setVisibility(View.VISIBLE);
                }

                if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus().equalsIgnoreCase("Closed")) {

                    textview1h.setVisibility(View.GONE);
                    textviewh.setVisibility(View.GONE);

                    if(observationHeader.getStatus().equalsIgnoreCase("Closed")){
                        textview2h.setVisibility(View.GONE);
                    }
                    else{
                        if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getUserEmployeeId())) {
                            textview2h.setVisibility(View.VISIBLE);
                        }
                    }

                }

                final int finalI = i;
                textview1h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent closeActionItem = new Intent(context, ClosedActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                textviewh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent closeActionItem = new Intent(context, EditActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                textview2h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent closeActionItem = new Intent(context, ReOpenActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                tableRowh.addView(textviewh);
                tableRowh.addView(textview1h);
                tableRowh.addView(textview2h);
                tableRow.addView(tableRowh);

                LinearLayout.LayoutParams layoutParams_action_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_header.setMargins(10, 10, 0, 10);
                TextView action_header = new TextView(context);
                action_header.setLayoutParams(layoutParams_action_header);
                action_header.setTextColor(Color.parseColor("#000000"));
                action_header.setTypeface(Utilities.fontRegular(context));
                action_header.setText("Action Taken");
                action_header.setTextSize(12);
                tableRow.addView(action_header);

                LinearLayout.LayoutParams layoutParams_action_name = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_name.setMargins(10, 10, 0, 10);
                TextView action_name = new TextView(context);
                action_name.setLayoutParams(layoutParams_action_name);
                action_name.setTextColor(Color.parseColor("#000000"));
                action_name.setTypeface(Utilities.fontBold(context));
                action_name.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getActionName());
                tableRow.addView(action_name);
                //  holder.action_item_view.addView(action_name);

                LinearLayout.LayoutParams layoutParams_action_header1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_header1.setMargins(10, 10, 0, 10);
                TextView action_header1 = new TextView(context);
                action_header1.setLayoutParams(layoutParams_action_header);
                action_header1.setTextColor(Color.parseColor("#000000"));
                action_header1.setTypeface(Utilities.fontRegular(context));
                action_header1.setText("Description of Action");
                action_header1.setTextSize(12);
                tableRow.addView(action_header1);

                LinearLayout.LayoutParams layoutParams_action_descr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_descr.setMargins(10, 10, 0, 10);
                TextView action_descr = new TextView(context);
                action_descr.setLayoutParams(layoutParams_action_name);
                action_descr.setTextColor(Color.parseColor("#000000"));
                action_descr.setTypeface(Utilities.fontBold(context));
                action_descr.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getDescription());
                tableRow.addView(action_descr);

                LinearLayout.LayoutParams layoutParams_remarks_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks_header.setMargins(10, 10, 0, 10);
                TextView remarks_header = new TextView(context);
                remarks_header.setLayoutParams(layoutParams_remarks_header);
                remarks_header.setTextColor(Color.parseColor("#000000"));
                remarks_header.setTypeface(Utilities.fontRegular(context));
                remarks_header.setText("Corrective Action and Preventive Action Remarks");
                remarks_header.setTextSize(12);
                tableRow.addView(remarks_header);

                LinearLayout.LayoutParams layoutParams_remarks = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks.setMargins(10, 10, 0, 10);
                TextView remarks = new TextView(context);
                remarks.setLayoutParams(layoutParams_remarks);
                remarks.setTypeface(Utilities.fontBold(context));
                remarks.setTextColor(Color.parseColor("#000000"));
                remarks.setText(observationItemsDetailsModels.get(position).getAdminRemarks());
                tableRow.addView(remarks);

                LinearLayout.LayoutParams layoutParams_remarks_header1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks_header1.setMargins(10, 10, 0, 10);
                TextView remarks_header1 = new TextView(context);
                remarks_header1.setLayoutParams(layoutParams_remarks_header1);
                remarks_header1.setTextColor(Color.parseColor("#000000"));
                remarks_header1.setTypeface(Utilities.fontRegular(context));
                remarks_header1.setText("Action Status");
                remarks_header1.setTextSize(12);
                tableRow.addView(remarks_header1);

                LinearLayout.LayoutParams layoutParams_remarks1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks1.setMargins(10, 10, 0, 10);
                TextView remarks1 = new TextView(context);
                remarks1.setLayoutParams(layoutParams_remarks1);
                remarks1.setTypeface(Utilities.fontBold(context));
                remarks1.setTextColor(Color.parseColor("#000000"));
                remarks1.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus());
                tableRow.addView(remarks1);

                holder.action_item_view.addView(tableRow);

//            observationItemsDetailsModels.get(position).getAdminRemarks();
            }


//

        }
        else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {

            if (observationHeader.getStatus().equalsIgnoreCase("assigned")) {

                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getProjectManagerId())) {
                    holder.item_edit.setVisibility(View.VISIBLE);
                } else {
                    holder.item_edit.setVisibility(View.GONE);
                }

            } else {
                holder.item_edit.setVisibility(View.GONE);
            }


            if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                if (getValidateObservation(observationItemsDetailsModels.get(position).getItemsActionDetailsModels())) {
                    holder.item_edit.setVisibility(View.GONE);
                }
            }

            holder.brief_discription.setText(observationItemsDetailsModels.get(position).getBriefDescription());
            holder.underlaying_cause.setText(observationItemsDetailsModels.get(position).getUnderlayingCause());
            holder.routeCause.setText(observationItemsDetailsModels.get(position).getRouteCause());
            holder.direct_cause.setText(observationItemsDetailsModels.get(position).getDirectCause());
            holder.injured.setText(observationItemsDetailsModels.get(position).getInjured());
            holder.incidentType.setText(observationItemsDetailsModels.get(position).getIncidentType());
            holder.riskType.setText(observationItemsDetailsModels.get(position).getRisk());
            holder.correctiveAction.setText(observationItemsDetailsModels.get(position).getCorrectiveAction());
            holder.preventiveAction.setText(observationItemsDetailsModels.get(position).getPreventiveAction());
            holder.subcontractor.setText(observationItemsDetailsModels.get(position).getSubcontractor());
            //holder.routeCause.setText(observationItemsDetailsModels.get(position).getRouteCause());
            holder.analysis.setText(observationItemsDetailsModels.get(position).getAnalysis());
            holder.classification.setText(observationItemsDetailsModels.get(position).getClassification());
            holder.classificationremarks.setText(observationItemsDetailsModels.get(position).getRemarkreason());

            if (observationItemsDetailsModels.get(position).getInjuredDetailsModels() != null) {
                IncidentViewAdapter viewAdapter = new IncidentViewAdapter(context, observationItemsDetailsModels.get(position).getInjuredDetailsModels());
                holder.incidentItemShowList.setAdapter(viewAdapter);

                holder.incidentItemShowList.requestLayout();
            }


            setListViewHeightBasedOnChildren(holder.incidentItemShowList);


            holder.incidentItemShowList.post(new Runnable() {
                @Override
                public void run() {
                    holder.incidentItemShowList.scrollTo(0, 0);
                }
            });


            Log.d("AttachmentsIncident", observationItemsDetailsModels.get(position).getObservationAttachmentModels().size() + "");

            if (observationItemsDetailsModels.get(position).getObservationAttachmentModels() != null) {

                incidentMainGalleryAdapter = new IncidentMainGalleryAdapter(context, observationItemsDetailsModels.get(position).getObservationAttachmentModels());
                holder.mainGv.setAdapter(incidentMainGalleryAdapter);
                holder.mainGv.setVerticalSpacing(holder.mainGv.getHorizontalSpacing());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.mainGv.getLayoutParams();
                mlp.setMargins(0, holder.mainGv.getHorizontalSpacing(), 0, 0);
                setGridViewHeightBasedOnChildren(holder.mainGv, 5);


            } else {
                holder.mainGv.setVisibility(View.GONE);
            }
            if (observationItemsDetailsModels.get(position).getObservationAttachmentModels().size() > 0 && observationItemsDetailsModels.get(position).getObservationAttachmentModels() != null) {

            } else {
                holder.mainGv.setVisibility(View.GONE);
            }
            holder.mainGv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (holder.mainGv.hasFocus()) {
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
//            holder.gv.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (holder.gv.hasFocus()) {
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                            case MotionEvent.ACTION_SCROLL:
//                                v.getParent().requestDisallowInterceptTouchEvent(false);
//                                return true;
//                        }
//                    }
//                    return false;
//                }
//            });
//
            LinearLayout.LayoutParams llhead_layoutParamh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llhead_layoutParamh.setMargins(5, 20, 0, 10);
            LinearLayout tableRowhead = new LinearLayout(context);
            tableRowhead.setOrientation(LinearLayout.HORIZONTAL);
            tableRowhead.setWeightSum(1);
            tableRowhead.setLayoutParams(llhead_layoutParamh);

            LinearLayout.LayoutParams layoutParams_textvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
            layoutParams_textvt1.setMargins(5, 0, 0, 10);
            TextView textview1 = new TextView(context);
            textview1.setLayoutParams(layoutParams_textvt1);
            textview1.setTextColor(Color.parseColor("#ff33b5e5"));
            textview1.setTypeface(Utilities.fontBold(context));
            textview1.setText("CLOSE OBSERVATION HERE");
            textview1.setTextSize(16);
            tableRowhead.addView(textview1);

            if(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                holder.action_item_view.addView(tableRowhead);
            }

            for (int i = 0; i < observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size(); i++) {


                LinearLayout.LayoutParams llhead_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParams.setMargins(5, 20, 0, 10);
                LinearLayout tableRow = new LinearLayout(context);
                tableRow.setOrientation(LinearLayout.VERTICAL);
                tableRow.setLayoutParams(llhead_layoutParams);


                LinearLayout.LayoutParams llhead_layoutParamsh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParamsh.setMargins(5, 20, 0, 10);
                LinearLayout tableRowh = new LinearLayout(context);
                tableRowh.setOrientation(LinearLayout.HORIZONTAL);
                tableRowh.setWeightSum(1);
                tableRowh.setLayoutParams(llhead_layoutParamsh);


                LinearLayout.LayoutParams layoutParams_tvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
                layoutParams_tvt1.setMargins(5, 0, 0, 10);

                TextView textview = new TextView(context);
                textview.setLayoutParams(layoutParams_tvt1);
                textview.setTextColor(Color.parseColor("#ff33b5e5"));
                textview.setTypeface(Utilities.fontBold(context));
                textview.setText("ACTION ITEMS" + "   " + (i + 1));
                textview.setTextSize(16);
                tableRowh.addView(textview);



                LinearLayout.LayoutParams layoutParams_tvth = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvth.setMargins(5, 0, 10, 10);
                TextView textviewh = new TextView(context);
                textviewh.setLayoutParams(layoutParams_tvth);
                textviewh.setText("Edit");
                textviewh.setPadding(0, 10, 0, 10);
                textviewh.setGravity(Gravity.CENTER);
                textviewh.setTextSize(14);
                textviewh.setTextColor(Color.parseColor("#000000"));
                textviewh.setBackgroundResource(R.drawable.closebutton_color);


                LinearLayout.LayoutParams layoutParams_tvt1h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt1h.setMargins(10, 0, 5, 10);
                TextView textview1h = new TextView(context);
                textview1h.setLayoutParams(layoutParams_tvt1h);
                textview1h.setText("Close");
                textview1h.setPadding(0, 10, 0, 10);
                textview1h.setGravity(Gravity.CENTER);
                textview1h.setTextSize(14);
                textview1h.setTextColor(Color.parseColor("#000000"));
                textview1h.setBackgroundResource(R.drawable.closebutton_color);

                LinearLayout.LayoutParams layoutParams_tvt2h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt2h.setMargins(0, 0, 0, 10);
                TextView textview2h = new TextView(context);
                textview2h.setLayoutParams(layoutParams_tvt2h);
                textview2h.setText("ReOpen");
                textview2h.setPadding(10, 10, 10, 10);
                textview2h.setGravity(Gravity.CENTER);
                textview2h.setTextSize(14);
                textview2h.setTextColor(Color.parseColor("#000000"));
                textview2h.setBackgroundResource(R.drawable.closebutton_color);
                textview2h.setVisibility(View.GONE);

                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getProjectManagerId())) {
                    textviewh.setVisibility(View.VISIBLE);
                    textview1h.setVisibility(View.VISIBLE);
                } else {
                    textviewh.setVisibility(View.GONE);
                    textview1h.setVisibility(View.GONE);
                    //textview2h.setVisibility(View.VISIBLE);
                }

                if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus().equalsIgnoreCase("Closed")) {

                    textview1h.setVisibility(View.GONE);
                    textviewh.setVisibility(View.GONE);

                    if(observationHeader.getStatus().equalsIgnoreCase("Closed")){
                        textview2h.setVisibility(View.GONE);
                    }
                    else{
                        if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getUserEmployeeId())) {
                            textview2h.setVisibility(View.VISIBLE);
                        }
                    }

                }


                final int finalI = i;
                textview1h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent closeActionItem = new Intent(context, ClosedActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                textviewh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent closeActionItem = new Intent(context, EditActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                textview2h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent closeActionItem = new Intent(context, ReOpenActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                tableRowh.addView(textviewh);
                tableRowh.addView(textview1h);
                tableRowh.addView(textview2h);
                tableRow.addView(tableRowh);


                LinearLayout.LayoutParams layoutParams_action_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_header.setMargins(10, 10, 0, 10);
                TextView action_header = new TextView(context);
                action_header.setLayoutParams(layoutParams_action_header);
                action_header.setTextColor(Color.parseColor("#000000"));
                action_header.setTypeface(Utilities.fontRegular(context));
                action_header.setText("Action Taken");
                action_header.setTextSize(12);
                tableRow.addView(action_header);

                LinearLayout.LayoutParams layoutParams_action_name = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_name.setMargins(10, 10, 0, 10);
                TextView action_name = new TextView(context);
                action_name.setLayoutParams(layoutParams_action_name);
                action_name.setTextColor(Color.parseColor("#000000"));
                action_name.setTypeface(Utilities.fontBold(context));
                action_name.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getActionName());
                tableRow.addView(action_name);
                //  holder.action_item_view.addView(action_name);

                LinearLayout.LayoutParams layoutParams_action_header1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_header1.setMargins(10, 10, 0, 10);
                TextView action_header1 = new TextView(context);
                action_header1.setLayoutParams(layoutParams_action_header);
                action_header1.setTextColor(Color.parseColor("#000000"));
                action_header1.setTypeface(Utilities.fontRegular(context));
                action_header1.setText("Description of Action");
                action_header1.setTextSize(12);
                tableRow.addView(action_header1);

                LinearLayout.LayoutParams layoutParams_action_descr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_descr.setMargins(10, 10, 0, 10);
                TextView action_descr = new TextView(context);
                action_descr.setLayoutParams(layoutParams_action_name);
                action_descr.setTextColor(Color.parseColor("#000000"));
                action_descr.setTypeface(Utilities.fontBold(context));
                action_descr.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getDescription());
                tableRow.addView(action_descr);

                LinearLayout.LayoutParams layoutParams_remarks_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks_header.setMargins(10, 10, 0, 10);
                TextView remarks_header = new TextView(context);
                remarks_header.setLayoutParams(layoutParams_remarks_header);
                remarks_header.setTextColor(Color.parseColor("#000000"));
                remarks_header.setTypeface(Utilities.fontRegular(context));
                remarks_header.setText("Corrective Action and Preventive Action Remarks");
                remarks_header.setTextSize(12);
                tableRow.addView(remarks_header);

                LinearLayout.LayoutParams layoutParams_remarks = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks.setMargins(10, 10, 0, 10);
                TextView remarks = new TextView(context);
                remarks.setLayoutParams(layoutParams_remarks);
                remarks.setTypeface(Utilities.fontBold(context));
                remarks.setTextColor(Color.parseColor("#000000"));
                remarks.setText(observationItemsDetailsModels.get(position).getAdminRemarks());
                tableRow.addView(remarks);

                LinearLayout.LayoutParams layoutParams_remarks_header1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks_header1.setMargins(10, 10, 0, 10);
                TextView remarks_header1 = new TextView(context);
                remarks_header1.setLayoutParams(layoutParams_remarks_header1);
                remarks_header1.setTextColor(Color.parseColor("#000000"));
                remarks_header1.setTypeface(Utilities.fontRegular(context));
                remarks_header1.setText("Action Status");
                remarks_header1.setTextSize(12);
                tableRow.addView(remarks_header1);

                LinearLayout.LayoutParams layoutParams_remarks1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks1.setMargins(10, 10, 0, 10);
                TextView remarks1 = new TextView(context);
                remarks1.setLayoutParams(layoutParams_remarks1);
                remarks1.setTypeface(Utilities.fontBold(context));
                remarks1.setTextColor(Color.parseColor("#000000"));
                remarks1.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus());
                tableRow.addView(remarks1);


                holder.action_item_view.addView(tableRow);

//            observationItemsDetailsModels.get(position).getAdminRemarks();
            }


        } else {

            //SharedPreferences sharedPreferences = context.getSharedPreferences("ObservationStatus", Context.MODE_PRIVATE);
//            holder.correctiveAction.setVisibility(View.GONE);
//            holder.preventiveAction.setVisibility(View.GONE);

            if (observationHeader.getStatus().equalsIgnoreCase("assigned")) {

                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getProjectManagerId())) {
                    holder.item_edit.setVisibility(View.VISIBLE);
                } else {
                    holder.item_edit.setVisibility(View.GONE);
                }

            } else {
                holder.item_edit.setVisibility(View.GONE);
            }

            if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                if (getValidateObservation(observationItemsDetailsModels.get(position).getItemsActionDetailsModels())) {
                    holder.item_edit.setVisibility(View.GONE);
                }
            }


            holder.listoftask_header.setText("ITEM  " + (position + 1) + "");
            holder.edit_verticle.setText(observationItemsDetailsModels.get(position).getVertical());
            holder.edit_hazard_type.setText(observationItemsDetailsModels.get(position).getHazardType());
            holder.edit_risk.setText(observationItemsDetailsModels.get(position).getRisk());
            holder.edit_category.setText(observationItemsDetailsModels.get(position).getCategory());
            holder.countEdit.setText(observationItemsDetailsModels.get(position).getBriefDescription());
            holder.edit_description2.setText(observationItemsDetailsModels.get(position).getDescription2());
            holder.listoftask_header.setTypeface(Utilities.fontBold(context));
            holder.correctiveAction.setText(observationItemsDetailsModels.get(position).getCorrectiveAction());
            holder.preventiveAction.setText(observationItemsDetailsModels.get(position).getPreventiveAction());
            holder.subcontractor.setText(observationItemsDetailsModels.get(position).getSubcontractor());
            holder.routeCause.setText(observationItemsDetailsModels.get(position).getRouteCause());
            holder.analysis.setText(observationItemsDetailsModels.get(position).getAnalysis());
            holder.classification.setText(observationItemsDetailsModels.get(position).getClassification());
            holder.classificationremarks.setText(observationItemsDetailsModels.get(position).getRemarkreason());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.images_view.setLayoutManager(linearLayoutManager);


            Log.d("Attachments", observationItemsDetailsModels.get(position).getObservationAttachmentModels().size() + "");

            if (observationItemsDetailsModels.get(position).getObservationAttachmentModels() != null) {

                detailsViewAdapter = new DetailsViewAdapter(context, observationItemsDetailsModels.get(position).getObservationAttachmentModels());
                holder.gv.setAdapter(detailsViewAdapter);
                holder.gv.setVerticalSpacing(holder.gv.getHorizontalSpacing());

                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.gv.getLayoutParams();
                mlp.setMargins(0, holder.gv.getHorizontalSpacing(), 0, 0);
                setGridViewHeightBasedOnChildren(holder.gv, 5);
            } else {
                holder.gv.setVisibility(View.GONE);
            }
            holder.gv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (holder.gv.hasFocus()) {
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

            LinearLayout.LayoutParams llhead_layoutParamh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llhead_layoutParamh.setMargins(5, 20, 0, 10);
            LinearLayout tableRowhead = new LinearLayout(context);
            tableRowhead.setOrientation(LinearLayout.HORIZONTAL);
            tableRowhead.setWeightSum(1);
            tableRowhead.setLayoutParams(llhead_layoutParamh);

            LinearLayout.LayoutParams layoutParams_textvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
            layoutParams_textvt1.setMargins(5, 0, 0, 10);
            TextView textview1 = new TextView(context);
            textview1.setLayoutParams(layoutParams_textvt1);
            textview1.setTextColor(Color.parseColor("#ff33b5e5"));
            textview1.setTypeface(Utilities.fontBold(context));
            textview1.setText("CLOSE OBSERVATION HERE");
            textview1.setTextSize(16);
            tableRowhead.addView(textview1);

            if(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                holder.action_item_view.addView(tableRowhead);
            }

            for (int i = 0; i < observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size(); i++) {


                LinearLayout.LayoutParams llhead_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParams.setMargins(10, 20, 0, 10);
                LinearLayout tableRow = new LinearLayout(context);
                tableRow.setOrientation(LinearLayout.VERTICAL);
                tableRow.setLayoutParams(llhead_layoutParams);


                LinearLayout.LayoutParams llhead_layoutParamsh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParamsh.setMargins(5, 20, 0, 10);
                LinearLayout tableRowh = new LinearLayout(context);
                tableRowh.setOrientation(LinearLayout.HORIZONTAL);
                tableRowh.setWeightSum(1);
                tableRowh.setLayoutParams(llhead_layoutParamsh);


                LinearLayout.LayoutParams layoutParams_tvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
                layoutParams_tvt1.setMargins(5, 0, 0, 10);

                TextView textview = new TextView(context);
                textview.setLayoutParams(layoutParams_tvt1);
                textview.setTextColor(Color.parseColor("#ff33b5e5"));
                textview.setTypeface(Utilities.fontBold(context));
                textview.setText("ACTION ITEMS" + "   " + (i + 1));
                textview.setTextSize(16);
                tableRowh.addView(textview);

                LinearLayout.LayoutParams layoutParams_tvth = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvth.setMargins(5, 0, 10, 10);
                TextView textviewh = new TextView(context);
                textviewh.setLayoutParams(layoutParams_tvth);
                textviewh.setText("Edit");
                textviewh.setPadding(0, 10, 0, 10);
                textviewh.setGravity(Gravity.CENTER);
                textviewh.setTextSize(14);
                textviewh.setTextColor(Color.parseColor("#000000"));
                textviewh.setBackgroundResource(R.drawable.closebutton_color);


                LinearLayout.LayoutParams layoutParams_tvt1h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt1h.setMargins(10, 0, 5, 10);
                TextView textview1h = new TextView(context);
                textview1h.setLayoutParams(layoutParams_tvt1h);
                textview1h.setText("Close");
                textview1h.setPadding(0, 10, 0, 10);
                textview1h.setGravity(Gravity.CENTER);
                textview1h.setTextSize(14);
                textview1h.setTextColor(Color.parseColor("#000000"));
                textview1h.setBackgroundResource(R.drawable.closebutton_color);

                LinearLayout.LayoutParams layoutParams_tvt2h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt2h.setMargins(0, 0, 0, 10);
                TextView textview2h = new TextView(context);
                textview2h.setLayoutParams(layoutParams_tvt2h);
                textview2h.setText("ReOpen");
                textview2h.setPadding(10, 10, 10, 10);
                textview2h.setGravity(Gravity.CENTER);
                textview2h.setTextSize(14);
                textview2h.setTextColor(Color.parseColor("#000000"));
                textview2h.setBackgroundResource(R.drawable.closebutton_color);
                textview2h.setVisibility(View.GONE);

                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getProjectManagerId())) {
                    textviewh.setVisibility(View.VISIBLE);
                    textview1h.setVisibility(View.VISIBLE);
                } else {
                    textviewh.setVisibility(View.GONE);
                    textview1h.setVisibility(View.GONE);
                    //textview2h.setVisibility(View.VISIBLE);
                }

                if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus().equalsIgnoreCase("Closed")) {

                    textview1h.setVisibility(View.GONE);
                    textviewh.setVisibility(View.GONE);

                    if(observationHeader.getStatus().equalsIgnoreCase("Closed")){
                        textview2h.setVisibility(View.GONE);
                    }
                    else{
                        if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002") || roleId.equalsIgnoreCase("ROLE000003") || roleId.equalsIgnoreCase("ROLE000004") || roleId.equalsIgnoreCase("ROLE000005") || roleId.equalsIgnoreCase("ROLE000006") || LoginUserID.equalsIgnoreCase(observationHeader.getUserEmployeeId())) {
                            textview2h.setVisibility(View.VISIBLE);
                        }
                    }

                }


                final int finalI = i;
                textview1h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent closeActionItem = new Intent(context, ClosedActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                textviewh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent closeActionItem = new Intent(context, EditActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                textview2h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent closeActionItem = new Intent(context, ReOpenActionItemActivity.class);
                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
                        closeActionItem.putExtra("Flag", "ObservationItem");
                        closeActionItem.putExtra("Header", observationHeader);
                        Log.d("Header", observationHeader + "");
                        closeActionItem.putExtra("position", finalI);
                        closeActionItem.putExtra("size", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size());
                        context.startActivity(closeActionItem);
                    }
                });

                tableRowh.addView(textviewh);
                tableRowh.addView(textview1h);
                tableRowh.addView(textview2h);
                tableRow.addView(tableRowh);


                LinearLayout.LayoutParams layoutParams_action_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_header.setMargins(5, 10, 0, 10);
                TextView action_header = new TextView(context);
                action_header.setLayoutParams(layoutParams_action_header);
                action_header.setTextColor(Color.parseColor("#000000"));
                action_header.setTypeface(Utilities.fontRegular(context));
                action_header.setText("Action Taken");
                action_header.setTextSize(12);
                tableRow.addView(action_header);

                LinearLayout.LayoutParams layoutParams_action_name = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_name.setMargins(10, 10, 0, 10);
                TextView action_name = new TextView(context);
                action_name.setLayoutParams(layoutParams_action_name);
                action_name.setTextColor(Color.parseColor("#000000"));
                action_name.setTypeface(Utilities.fontBold(context));
                action_name.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getActionName());
                tableRow.addView(action_name);
                //  holder.action_item_view.addView(action_name);
                LinearLayout.LayoutParams layoutParams_action_header1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_header1.setMargins(10, 10, 0, 10);
                TextView action_header1 = new TextView(context);
                action_header1.setLayoutParams(layoutParams_action_header);
                action_header1.setTextColor(Color.parseColor("#000000"));
                action_header1.setTypeface(Utilities.fontRegular(context));
                action_header1.setText("Description of Action");
                action_header1.setTextSize(12);
                tableRow.addView(action_header1);

                LinearLayout.LayoutParams layoutParams_action_descr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_action_descr.setMargins(10, 10, 0, 10);
                TextView action_descr = new TextView(context);
                action_descr.setLayoutParams(layoutParams_action_name);
                action_descr.setTextColor(Color.parseColor("#000000"));
                action_descr.setTypeface(Utilities.fontBold(context));
                action_descr.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getDescription());
                tableRow.addView(action_descr);

                LinearLayout.LayoutParams layoutParams_remarks_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks_header.setMargins(5, 10, 0, 10);
                TextView remarks_header = new TextView(context);
                remarks_header.setLayoutParams(layoutParams_remarks_header);
                remarks_header.setTextColor(Color.parseColor("#000000"));
                remarks_header.setTypeface(Utilities.fontRegular(context));
                remarks_header.setText("Corrective Action and Preventive Action Remarks");
                remarks_header.setTextSize(12);
                tableRow.addView(remarks_header);

                LinearLayout.LayoutParams layoutParams_remarks = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks.setMargins(10, 10, 0, 10);
                TextView remarks = new TextView(context);
                remarks.setLayoutParams(layoutParams_remarks);
                remarks.setTypeface(Utilities.fontBold(context));
                remarks.setTextColor(Color.parseColor("#000000"));
                remarks.setText(observationItemsDetailsModels.get(position).getAdminRemarks());
                tableRow.addView(remarks);

                LinearLayout.LayoutParams layoutParams_remarks_header1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks_header1.setMargins(10, 10, 0, 10);
                TextView remarks_header1 = new TextView(context);
                remarks_header1.setLayoutParams(layoutParams_remarks_header1);
                remarks_header1.setTextColor(Color.parseColor("#000000"));
                remarks_header1.setTypeface(Utilities.fontRegular(context));
                remarks_header1.setText("Action Status");
                remarks_header1.setTextSize(12);
                tableRow.addView(remarks_header1);

                LinearLayout.LayoutParams layoutParams_remarks1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams_remarks1.setMargins(10, 10, 0, 10);
                TextView remarks1 = new TextView(context);
                remarks1.setLayoutParams(layoutParams_remarks1);
                remarks1.setTypeface(Utilities.fontBold(context));
                remarks1.setTextColor(Color.parseColor("#000000"));
                remarks1.setText(observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus());
                tableRow.addView(remarks1);


                holder.action_item_view.addView(tableRow);

//            observationItemsDetailsModels.get(position).getAdminRemarks();
            }


//
         /*   holder.item_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("onClick: ", view + "");
                    onClick.onItemClick(view, holder.getAdapterPosition());
                }
            });*/
        }

        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick: ", view + "");
                onClick.onItemClick(view, holder.getAdapterPosition());
            }
        });
       /* holder.gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (holder.gv.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });*/


    }

    private void setGridViewHeightBasedOnChildren(GridView gv, int i) {
        ListAdapter listAdapter = gv.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gv);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > i) {
            x = items / i;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gv.getLayoutParams();
        params.height = totalHeight;
        gv.setLayoutParams(params);
    }


    private void setListViewHeightBasedOnChildren(ListView incidentItemShowList) {
        ListAdapter listAdapter = incidentItemShowList.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(incidentItemShowList.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, incidentItemShowList);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = incidentItemShowList.getLayoutParams();
        params.height = totalHeight + (incidentItemShowList.getDividerHeight() * (listAdapter.getCount() - 1));
        incidentItemShowList.setLayoutParams(params);
    }


    @Override
    public int getItemCount() {
        return observationItemsDetailsModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        For Hazard
        EditText edit_verticle, edit_hazard_type, edit_risk, edit_category, countEdit, edit_description2, correctiveAction, preventiveAction;
        ImageView item_edit;
        TextView listoftask_header, idText;
        RecyclerView images_view;
        LinearLayout images_view1;
        public ListView actionList;
        private LinearLayout action_item_view;
        //   NestedScrollView parentscroll;
        //        For Incident
        private EditText incidentType, injured, riskType,subcontractor;
        private EditText brief_discription, direct_cause, underlaying_cause, classificationremarks,classification,analysis,routeCause;
        private ListView incidentItemShowList;
        GridView mainGv, gv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
                edit_verticle = itemView.findViewById(R.id.edit_verticle);
                edit_hazard_type = itemView.findViewById(R.id.edit_hazard_type);
                edit_risk = itemView.findViewById(R.id.edit_risk);
                edit_category = itemView.findViewById(R.id.edit_category);
                countEdit = itemView.findViewById(R.id.countEdit);
                edit_description2 = itemView.findViewById(R.id.edit_description2);
                gv = itemView.findViewById(R.id.gv);
                item_edit = itemView.findViewById(R.id.item_edit);
                images_view = itemView.findViewById(R.id.images_view);
                images_view1 = itemView.findViewById(R.id.images_view1);
                actionList = itemView.findViewById(R.id.actionList);
                preventiveAction = itemView.findViewById(R.id.preventiveEdit);
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);
                incidentItemShowList = (ListView) itemView.findViewById(R.id.incidentItemList);
                action_item_view = itemView.findViewById(R.id.action_item_view);
                listoftask_header = itemView.findViewById(R.id.listoftask_header);
                subcontractor = itemView.findViewById(R.id.subcontractorEdit);
                idText = itemView.findViewById(R.id.idText);
                if (observationItemsDetailsModels.size() > 0) {

                } else {
                    actionList.setVisibility(View.GONE);
                }
                actionList.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {

                        return (event.getAction() == MotionEvent.ACTION_MOVE);
                    }
                });


                //fonts
                edit_verticle.setTypeface(Utilities.fontBold(context));
                edit_hazard_type.setTypeface(Utilities.fontBold(context));
                edit_risk.setTypeface(Utilities.fontBold(context));
                edit_category.setTypeface(Utilities.fontBold(context));
                countEdit.setTypeface(Utilities.fontBold(context));
                edit_description2.setTypeface(Utilities.fontBold(context));
                preventiveAction.setTypeface(Utilities.fontBold(context));
                correctiveAction.setTypeface(Utilities.fontBold(context));
                subcontractor.setTypeface(Utilities.fontBold(context));
                idText.setTypeface(Utilities.fontBold(context));

            } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {


                preventiveAction = itemView.findViewById(R.id.preventiveEdit);
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);

                incidentType = itemView.findViewById(R.id.incidentType);
                riskType = itemView.findViewById(R.id.riskType);
                injured = itemView.findViewById(R.id.injured);
//                edit_risk = itemView.findViewById(R.id.edit_risk);
                brief_discription = itemView.findViewById(R.id.brief_discription);
                direct_cause = itemView.findViewById(R.id.direct_cause);
                underlaying_cause = itemView.findViewById(R.id.underlaying_cause);
                routeCause = itemView.findViewById(R.id.route_cause);
                item_edit = itemView.findViewById(R.id.item_edit);
                action_item_view = itemView.findViewById(R.id.action_item_view);
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);
                subcontractor = itemView.findViewById(R.id.subcontractorEdit);
                analysis = itemView.findViewById(R.id.analysisEdit);
                classification = itemView.findViewById(R.id.classicificationEdit);
                classificationremarks = itemView.findViewById(R.id.classicificationremarksEdit);

                idText = itemView.findViewById(R.id.idAttachText);
                incidentItemShowList = (ListView) itemView.findViewById(R.id.incidentItemList);
                //    parentscroll = itemView.findViewById(R.id.parentscroll);
                mainGv = (GridView) itemView.findViewById(R.id.gv);
                // changes Fonts
                incidentType.setTypeface(Utilities.fontBold(context));
                riskType.setTypeface(Utilities.fontBold(context));
                injured.setTypeface(Utilities.fontBold(context));
                routeCause.setTypeface(Utilities.fontBold(context));
                brief_discription.setTypeface(Utilities.fontBold(context));
                direct_cause.setTypeface(Utilities.fontBold(context));
                underlaying_cause.setTypeface(Utilities.fontBold(context));
                direct_cause.setTypeface(Utilities.fontBold(context));
                idText.setTypeface(Utilities.fontBold(context));
                preventiveAction.setTypeface(Utilities.fontBold(context));
                correctiveAction.setTypeface(Utilities.fontBold(context));
                subcontractor.setTypeface(Utilities.fontBold(context));
                analysis.setTypeface(Utilities.fontBold(context));
                classification.setTypeface(Utilities.fontBold(context));
                classificationremarks.setTypeface(Utilities.fontBold(context));

            } else {
                preventiveAction = itemView.findViewById(R.id.preventiveEdit);
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);
                edit_verticle = itemView.findViewById(R.id.edit_verticle);
                edit_hazard_type = itemView.findViewById(R.id.edit_hazard_type);
                edit_risk = itemView.findViewById(R.id.edit_risk);
                edit_category = itemView.findViewById(R.id.edit_category);
                countEdit = itemView.findViewById(R.id.countEdit);
                edit_description2 = itemView.findViewById(R.id.edit_description2);
                gv = itemView.findViewById(R.id.gv);
                idText = itemView.findViewById(R.id.idText);
                item_edit = itemView.findViewById(R.id.item_edit);
                images_view = itemView.findViewById(R.id.images_view);
                images_view1 = itemView.findViewById(R.id.images_view1);
                actionList = itemView.findViewById(R.id.actionList);
                action_item_view = itemView.findViewById(R.id.action_item_view);
                listoftask_header = itemView.findViewById(R.id.listoftask_header);
                routeCause = itemView.findViewById(R.id.route_cause);
                subcontractor = itemView.findViewById(R.id.subcontractorEdit);
                analysis = itemView.findViewById(R.id.analysisEdit);
                classification = itemView.findViewById(R.id.classicificationEdit);
                classificationremarks = itemView.findViewById(R.id.classicificationremarksEdit);

//                idText.setTypeface(Utilities.fontBold(context));
                if (observationItemsDetailsModels.size() > 0) {

                } else {
                    actionList.setVisibility(View.GONE);
                }
                actionList.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return (event.getAction() == MotionEvent.ACTION_MOVE);
                    }
                });

                //fonts
                edit_verticle.setTypeface(Utilities.fontBold(context));
                edit_hazard_type.setTypeface(Utilities.fontBold(context));
                edit_risk.setTypeface(Utilities.fontBold(context));
                edit_category.setTypeface(Utilities.fontBold(context));
                countEdit.setTypeface(Utilities.fontBold(context));
                edit_description2.setTypeface(Utilities.fontBold(context));
                idText.setTypeface(Utilities.fontBold(context));

                preventiveAction.setTypeface(Utilities.fontBold(context));
                correctiveAction.setTypeface(Utilities.fontBold(context));
                routeCause.setTypeface(Utilities.fontBold(context));
                subcontractor.setTypeface(Utilities.fontBold(context));
                analysis.setTypeface(Utilities.fontBold(context));
                classification.setTypeface(Utilities.fontBold(context));
                classificationremarks.setTypeface(Utilities.fontBold(context));

            }

            // set LayoutManager to RecyclerView
        }

    }


    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    private boolean getValidateObservation(List<ItemsActionDetailsModel> observationItemsList) {
        boolean flag = false;
/*        if (observationItemsList.size() > 0) {
            for (int i = 0; i < observationItemsList.size(); i++) {

                if (observationItemsList.get(i).getStatus() != null && observationItemsList.get(i).getStatus().equalsIgnoreCase("Closed")) {
                    flag = true;
                    break;

                }

            }
        } else {
            Utilities.showAlertDialog("No Actions", context);
        }*/

        for (int i = 0; i < observationItemsList.size(); i++) {
            //  for (int j = 0; j < observationItemsList.get(i).getStatus().size(); j++) {
            if (observationItemsList.get(i).getStatus() != null && observationItemsList.get(i).getStatus().equalsIgnoreCase("Closed")) {
                flag = true;
                break;
//                    return false;
            }
            //}

        }
        return flag;

    }
}
