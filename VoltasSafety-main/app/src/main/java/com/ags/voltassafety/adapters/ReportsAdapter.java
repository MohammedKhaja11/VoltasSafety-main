package com.ags.voltassafety.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.EscalationActivity;
import com.ags.voltassafety.HazardReport;
import com.ags.voltassafety.IncidentReport;
import com.ags.voltassafety.R;
import com.ags.voltassafety.ReportsActivity;
import com.ags.voltassafety.ReportsView;
import com.ags.voltassafety.model.ObservationReportSummaryResult;
import com.ags.voltassafety.model.ObservationReportSummaryStatus;
import com.ags.voltassafety.utility.Utilities;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> plans;
    List<ObservationReportSummaryStatus> observationReportSummaryStatusList;
    List<ObservationReportSummaryResult> observationReportSummaryList;


    public ReportsAdapter(ReportsActivity reportsActivity, ArrayList<String> plans, List<ObservationReportSummaryResult> observationReportSummaryList) {
        context = reportsActivity;
        this.plans = plans;
        this.observationReportSummaryStatusList = observationReportSummaryStatusList;
        this.observationReportSummaryList = observationReportSummaryList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textview_plans.setText(plans.get(position));
        holder.textview_plans.setTypeface(Utilities.fontBold(context));

        ArrayList<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.ic_observation);
        imageList.add(R.drawable.ic_hazardreporting);
        imageList.add(R.drawable.ic_incident);


        holder.image_view.setImageResource(imageList.get(position));

//        if (position == 2) {
//
//            holder.all.setVisibility(View.GONE);
//            holder.assigned.setVisibility(View.GONE);
//            holder.closed.setVisibility(View.GONE);
//            holder.open_text.setVisibility(View.GONE);
//            holder.assigned_text.setVisibility(View.GONE);
//            holder.closed_text.setVisibility(View.GONE);
//            holder.textview_plans.setGravity(Gravity.CENTER_VERTICAL);
//            holder.textview_plans.setHeight(150);
//        } else {
        String styledTextall = "" + " <font color=\'#2CAACD\'>" + observationReportSummaryList.get(position).getOpen().toString() + "</font>";
        String styledTextassigned = "" + " <font color=\'#2CAACD\'>" + observationReportSummaryList.get(position).getAssigned().toString() + "</font>";
        String styledTextclosed = "" + " <font color=\'#2CAACD\'>" + observationReportSummaryList.get(position).getClosed().toString() + "</font>";
        holder.all.setText(Html.fromHtml(styledTextall), TextView.BufferType.SPANNABLE);
        holder.assigned.setText(Html.fromHtml(styledTextassigned), TextView.BufferType.SPANNABLE);
        holder.closed.setText(Html.fromHtml(styledTextclosed), TextView.BufferType.SPANNABLE);

        holder.assigned.setTypeface(Utilities.fontRegular(context));
        holder.closed.setTypeface(Utilities.fontRegular(context));
        holder.all.setTypeface(Utilities.fontRegular(context));
        holder.textview_plans.setTypeface(Utilities.fontBold(context));



//        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (position == 0) {
                    Intent reportview = new Intent(context, ReportsView.class);
                    reportview.putExtra("ReportName", plans.get(position));
                    context.startActivity(reportview);
                } else*/
                if (position == 0) {
                    Intent reportview = new Intent(context, HazardReport.class);
                    reportview.putExtra("ReportName", plans.get(position));
                    context.startActivity(reportview);
                } else if (position == 1) {
                    Intent reportview = new Intent(context, HazardReport.class);
                    reportview.putExtra("ReportName", plans.get(position));
                    context.startActivity(reportview);
                } else if (position == 2){
                    Intent reportview = new Intent(context, HazardReport.class);
                    reportview.putExtra("ReportName", plans.get(position));
                    context.startActivity(reportview);
                }



              /*  else {
//                    Intent reportview = new Intent(context, EscalationActivity.class);
//                    reportview.putExtra("ReportName", plans.get(position));
//                    context.startActivity(reportview);
                }*/
//                Intent reportview= new Intent(context, ReportsView.class);
//                reportview.putExtra("ReportName",plans.get(position));
//                context.startActivity(reportview);
            }
//                Intent reportview= new Intent(context, ReportsView.class);
//                reportview.putExtra("ReportName",plans.get(position));
//                context.startActivity(reportview);

        });
    }


    @Override
    public int getItemCount() {
        return plans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        TextView textview_plans, all, assigned, closed, open_text, assigned_text, closed_text;
        ImageView image_view;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            textview_plans = itemView.findViewById(R.id.textview_plans);
            all = itemView.findViewById(R.id.all);
            assigned = itemView.findViewById(R.id.assigned);
            closed = itemView.findViewById(R.id.closed);
            image_view = itemView.findViewById(R.id.image_view);
            open_text = itemView.findViewById(R.id.open_text);
            assigned_text = itemView.findViewById(R.id.assigned_text);
            closed_text = itemView.findViewById(R.id.closed_text);
        }
    }
}
