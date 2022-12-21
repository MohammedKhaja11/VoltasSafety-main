package com.ags.voltassafety.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.ClosedActionItemActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.utility.Utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class ObservationDetailReportViewAdapter extends RecyclerView.Adapter<ObservationDetailReportViewAdapter.MyViewHolder> {
    Context context;
    List<ObservationItemsDetailsModel> observationItemsDetailsModels;
    List<ObservationAttachmentModel> observationAttachmentModelList;
    private ObservationDetailsAdapter.OnItemClicked onClick;
    private View v;
    private MyViewHolder vh;
    ObservationHeader observationHeader;
    private IncidentMainGalleryAdapter incidentMainGalleryAdapter;
    //    public ListView actionList;
    GalleryAdapter galleryAdapter;
    DetailsViewAdapter detailsViewAdapter;
    List<ObservationAttachmentModel> linkarraylist;

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public ObservationDetailReportViewAdapter(Context observationDetails, List<ObservationItemsDetailsModel> observationItemsDetailsModels, List<ObservationAttachmentModel> observationAttachmentModelList, ObservationDetailsAdapter.OnItemClicked onClick, ObservationHeader observationHeader) {
        context = observationDetails;
        this.observationHeader = observationHeader;
        this.observationItemsDetailsModels = observationItemsDetailsModels;
        this.observationAttachmentModelList = observationAttachmentModelList;
        this.onClick = onClick;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_observationdetails_adapter_layout, parent, false);
            vh = new MyViewHolder(v);
        } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_report_observation_detail_adapter_layout, parent, false);
            vh = new MyViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_report_observationdetails_adapter, parent, false);
            vh = new MyViewHolder(v);
        }
        // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Bearer", MODE_PRIVATE);
        String roleId = sharedPreferences.getString("roleId", null);

        if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Hazard")) {

            holder.listoftask_header.setText("ITEM  " + (position + 1) + "");
            holder.edit_verticle.setText(observationItemsDetailsModels.get(position).getVertical());
            holder.edit_hazard_type.setText(observationItemsDetailsModels.get(position).getHazardType());
            holder.edit_risk.setText(observationItemsDetailsModels.get(position).getRisk());
            holder.edit_category.setText(observationItemsDetailsModels.get(position).getCategory());
            holder.countEdit.setText(observationItemsDetailsModels.get(position).getBriefDescription());
            holder.edit_description2.setText(observationItemsDetailsModels.get(position).getDescription2());
            holder.correctiveAction.setText(observationItemsDetailsModels.get(position).getCorrectiveAction());
            holder.preventiveAction.setText(observationItemsDetailsModels.get(position).getPreventiveAction());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.images_view.setLayoutManager(linearLayoutManager);

            //   holder.images_view.setNestedScrollingEnabled(false);

            Log.d("Attachments", observationItemsDetailsModels.get(position).getItemAttachments().size() + "");

            if (observationItemsDetailsModels.get(position).getItemAttachments() != null) {
                detailsViewAdapter = new DetailsViewAdapter(context, observationItemsDetailsModels.get(position).getObservationAttachmentModels());
                holder.gv.setAdapter(detailsViewAdapter);
                holder.gv.setVerticalSpacing(holder.gv.getHorizontalSpacing());

                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.gv.getLayoutParams();
                mlp.setMargins(0, holder.gv.getHorizontalSpacing(), 0, 0);
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


                LinearLayout.LayoutParams layoutParams_tvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
                layoutParams_tvt1.setMargins(5, 0, 0, 10);
                TextView textview = new TextView(context);
                textview.setLayoutParams(layoutParams_tvt1);
                textview.setTextColor(Color.parseColor("#ff33b5e5"));
                textview.setTypeface(Utilities.fontBold(context));
                textview.setText("ACTION ITEMS" + "   " + (i + 1));
                textview.setTextSize(16);
                tableRowh.addView(textview);


                LinearLayout.LayoutParams layoutParams_tvt1h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt1h.setMargins(0, 10, 0, 10);
                TextView textviewh = new TextView(context);
                textviewh.setLayoutParams(layoutParams_tvt1h);
                textviewh.setText("Close");
                textviewh.setPadding(0, 10, 0, 10);
                textviewh.setGravity(Gravity.CENTER);
                textviewh.setTextSize(14);
                textviewh.setTextColor(Color.parseColor("#000000"));
                textviewh.setBackgroundResource(R.drawable.closebutton_color);
                textviewh.setVisibility(View.GONE);


                final int finalI = i;
//                textviewh.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent closeActionItem = new Intent(context, ClosedActionItemActivity.class);
//                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
//                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
//                        closeActionItem.putExtra("Flag", "ObservationItem");
//                        context.startActivity(closeActionItem);
//                    }
//                });
                tableRowh.addView(textviewh);

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

        } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {

//            if (observationHeader.getStatus().equalsIgnoreCase("assigned")) {
//                holder.item_edit.setVisibility(View.VISIBLE);
//            } else {
//                holder.item_edit.setVisibility(View.GONE);
//            }

            if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size() > 0) {
                if (getValidateObservation(observationItemsDetailsModels.get(position).getItemsActionDetailsModels())) {
                    holder.item_edit.setVisibility(View.GONE);
                }
            }

            holder.root_cause.setText(observationItemsDetailsModels.get(position).getRouteCause());
            holder.brief_discription.setText(observationItemsDetailsModels.get(position).getBriefDescription());
            holder.underlaying_cause.setText(observationItemsDetailsModels.get(position).getUnderlayingCause());
            holder.direct_cause.setText(observationItemsDetailsModels.get(position).getDirectCause());
            holder.injured.setText(observationItemsDetailsModels.get(position).getInjured());
            holder.incidentType.setText(observationItemsDetailsModels.get(position).getIncidentType());
            holder.riskType.setText(observationItemsDetailsModels.get(position).getRisk());
            holder.correctiveAction.setText(observationItemsDetailsModels.get(position).getCorrectiveAction());
            holder.preventiveAction.setText(observationItemsDetailsModels.get(position).getPreventiveAction());
            IncidentViewAdapter viewAdapter = new IncidentViewAdapter(context, observationItemsDetailsModels.get(position).getInjuredDetailsModels());
            holder.incidentItemShowList.setAdapter(viewAdapter);

            incidentMainGalleryAdapter = new IncidentMainGalleryAdapter(context, observationItemsDetailsModels.get(position).getObservationAttachmentModels());
            holder.mainGv.setAdapter(galleryAdapter);
            holder.mainGv.setVerticalSpacing(holder.mainGv.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.mainGv.getLayoutParams();
            mlp.setMargins(0, holder.mainGv.getHorizontalSpacing(), 0, 0);


            holder.incidentItemShowList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    holder.parentscroll.requestDisallowInterceptTouchEvent(true);
                    int action = event.getActionMasked();
                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            holder.parentscroll.requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
//

            for (int i = 0; i < observationItemsDetailsModels.get(position).getItemsActionDetailsModels().size(); i++) {


                LinearLayout.LayoutParams llhead_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParams.setMargins(10, 20, 0, 10);
                LinearLayout tableRow = new LinearLayout(context);
                tableRow.setOrientation(LinearLayout.VERTICAL);
                tableRow.setLayoutParams(llhead_layoutParams);


                LinearLayout.LayoutParams llhead_layoutParamsh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llhead_layoutParamsh.setMargins(10, 20, 0, 10);
                LinearLayout tableRowh = new LinearLayout(context);
                tableRowh.setOrientation(LinearLayout.HORIZONTAL);
                tableRowh.setWeightSum(1);
                tableRowh.setLayoutParams(llhead_layoutParamsh);


                LinearLayout.LayoutParams layoutParams_tvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
                layoutParams_tvt1.setMargins(10, 0, 0, 10);
                TextView textview = new TextView(context);
                textview.setLayoutParams(layoutParams_tvt1);
                textview.setTextColor(Color.parseColor("#ff33b5e5"));
                textview.setTypeface(Utilities.fontBold(context));
                textview.setText("ACTION ITEMS" + "   " + (i + 1));
                textview.setTextSize(16);
                tableRowh.addView(textview);


                LinearLayout.LayoutParams layoutParams_tvt1h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt1h.setMargins(0, 0, 0, 10);
                ImageView textviewh = new ImageView(context);
                textviewh.setLayoutParams(layoutParams_tvt1h);
                textviewh.setImageResource(R.drawable.ic_edit);
                if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus().equalsIgnoreCase("Closed")) {
                    textviewh.setVisibility(View.GONE);
                }
                final int finalI = i;
//                textviewh.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent closeActionItem = new Intent(context, ClosedActionItemActivity.class);
//                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
//                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
//                        closeActionItem.putExtra("Flag", "ObservationItem");
//                        context.startActivity(closeActionItem);
//                    }
//                });
                tableRowh.addView(textviewh);

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

//            if (observationHeader.getStatus().equalsIgnoreCase("assigned")) {
//                holder.item_edit.setVisibility(View.VISIBLE);
//            } else {
//                holder.item_edit.setVisibility(View.GONE);
//            }
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
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.images_view.setLayoutManager(linearLayoutManager);


            Log.d("Attachments", observationItemsDetailsModels.get(position).getItemAttachments().size() + "");


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


                LinearLayout.LayoutParams layoutParams_tvt1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
                layoutParams_tvt1.setMargins(0, 0, 0, 10);
                TextView textview = new TextView(context);
                textview.setLayoutParams(layoutParams_tvt1);
                textview.setTextColor(Color.parseColor("#ff33b5e5"));
                textview.setTypeface(Utilities.fontBold(context));
                textview.setText("ACTION ITEMS" + "   " + (i + 1));
                textview.setTextSize(16);
                tableRowh.addView(textview);


                LinearLayout.LayoutParams layoutParams_tvt1h = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                layoutParams_tvt1h.setMargins(0, 0, 0, 10);
                ImageView textviewh = new ImageView(context);
                textviewh.setLayoutParams(layoutParams_tvt1h);
                textviewh.setImageResource(R.drawable.ic_edit);
                if (observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus() != null && observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(i).getStatus().equalsIgnoreCase("Closed")) {
                    textviewh.setVisibility(View.GONE);
                }
                final int finalI = i;
//                textviewh.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent closeActionItem = new Intent(context, ClosedActionItemActivity.class);
//                        closeActionItem.putExtra("ObservationItemId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getObservationItemId());
//                        closeActionItem.putExtra("ActionId", observationItemsDetailsModels.get(position).getItemsActionDetailsModels().get(finalI).getId());
//                        closeActionItem.putExtra("Flag", "ObservationItem");
//                        context.startActivity(closeActionItem);
//                    }
//                });
                tableRowh.addView(textviewh);

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
        }
    }


    @Override
    public int getItemCount() {
        return observationItemsDetailsModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        For Hazard
        EditText edit_verticle, edit_hazard_type, edit_risk, edit_category, countEdit, edit_description2,correctiveAction,preventiveAction;
        ImageView item_edit;
        TextView listoftask_header, idText,textTwo,idAttachText;
        RecyclerView images_view;
        LinearLayout images_view1;
        public ListView actionList;
        private LinearLayout action_item_view;
        ScrollView parentscroll;
        //        For Incident
        private EditText incidentType, injured,riskType;
        private EditText brief_discription, direct_cause, underlaying_cause,root_cause;
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
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);
                preventiveAction = itemView.findViewById(R.id.preventiveEdit);
                gv = itemView.findViewById(R.id.gv);
                item_edit = itemView.findViewById(R.id.item_edit);
                images_view = itemView.findViewById(R.id.images_view);
                images_view1 = itemView.findViewById(R.id.images_view1);
                actionList = itemView.findViewById(R.id.actionList);
                action_item_view = itemView.findViewById(R.id.action_item_view);
                listoftask_header = itemView.findViewById(R.id.listoftask_header);
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
                correctiveAction.setTypeface(Utilities.fontBold(context));
                preventiveAction.setTypeface(Utilities.fontBold(context));
                listoftask_header.setTypeface(Utilities.fontBold(context));
                idText.setTypeface(Utilities.fontBold(context));

            } else if (observationHeader.getTypeOfObservation().equalsIgnoreCase("Incident") && observationHeader.getReason() != null && observationHeader.getReason().equalsIgnoreCase("Accident")) {

                incidentType = itemView.findViewById(R.id.incidentType);
                riskType = itemView.findViewById(R.id.riskType);
                injured = itemView.findViewById(R.id.injured);
//                edit_risk = itemView.findViewById(R.id.edit_risk);
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);
                preventiveAction = itemView.findViewById(R.id.preventiveEdit);

                brief_discription = itemView.findViewById(R.id.brief_discription);
                direct_cause = itemView.findViewById(R.id.direct_cause);
                underlaying_cause = itemView.findViewById(R.id.underlaying_cause);
                root_cause = itemView.findViewById(R.id.route_cause);
                item_edit = itemView.findViewById(R.id.item_edit);
                action_item_view = itemView.findViewById(R.id.action_item_view);
                incidentItemShowList = (ListView) itemView.findViewById(R.id.incidentItemList);
                parentscroll = itemView.findViewById(R.id.parentscroll);
                mainGv = (GridView) itemView.findViewById(R.id.gv);
                listoftask_header = (TextView) itemView.findViewById(R.id.listoftask_header);
                textTwo = (TextView) itemView.findViewById(R.id.textTwo);
                idAttachText = (TextView) itemView.findViewById(R.id.idAttachText);
// changes Fonts
                incidentType.setTypeface(Utilities.fontBold(context));
                riskType.setTypeface(Utilities.fontBold(context));
                injured.setTypeface(Utilities.fontBold(context));
                correctiveAction.setTypeface(Utilities.fontBold(context));
                preventiveAction.setTypeface(Utilities.fontBold(context));
                root_cause.setTypeface(Utilities.fontBold(context));
                brief_discription.setTypeface(Utilities.fontBold(context));
                direct_cause.setTypeface(Utilities.fontBold(context));
                underlaying_cause.setTypeface(Utilities.fontBold(context));
                direct_cause.setTypeface(Utilities.fontBold(context));
                listoftask_header.setTypeface(Utilities.fontBold(context));
                textTwo.setTypeface(Utilities.fontBold(context));
                idAttachText.setTypeface(Utilities.fontBold(context));
            } else {

                edit_verticle = itemView.findViewById(R.id.edit_verticle);
                edit_hazard_type = itemView.findViewById(R.id.edit_hazard_type);
                edit_risk = itemView.findViewById(R.id.edit_risk);
                edit_category = itemView.findViewById(R.id.edit_category);
                countEdit = itemView.findViewById(R.id.countEdit);
                edit_description2 = itemView.findViewById(R.id.edit_description2);
                correctiveAction = itemView.findViewById(R.id.correctiveEdit);
                preventiveAction = itemView.findViewById(R.id.preventiveEdit);
                gv = itemView.findViewById(R.id.gv);
                item_edit = itemView.findViewById(R.id.item_edit);
                images_view = itemView.findViewById(R.id.images_view);
                images_view1 = itemView.findViewById(R.id.images_view1);
                actionList = itemView.findViewById(R.id.actionList);
                action_item_view = itemView.findViewById(R.id.action_item_view);
                listoftask_header = itemView.findViewById(R.id.listoftask_header);
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
                correctiveAction.setTypeface(Utilities.fontBold(context));
                preventiveAction.setTypeface(Utilities.fontBold(context));
                listoftask_header.setTypeface(Utilities.fontBold(context));
                idText.setTypeface(Utilities.fontBold(context));
            }

            // set LayoutManager to RecyclerView
        }

    }


    public void setOnClick(ObservationDetailsAdapter.OnItemClicked onClick) {
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

