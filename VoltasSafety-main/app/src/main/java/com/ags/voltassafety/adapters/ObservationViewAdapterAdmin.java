package com.ags.voltassafety.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.GoogleMapActivity;
import com.ags.voltassafety.ObservationDetailsActivity;
import com.ags.voltassafety.ObservationDetailsActivityForAdmin;
import com.ags.voltassafety.ObservationViewActivity;
import com.ags.voltassafety.ObservationViewActivityAdmin;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.utility.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ObservationViewAdapterAdmin extends RecyclerView.Adapter<ObservationViewAdapterAdmin.MyViewHolder> {

    Context context;
    List<ObservationHeader> observationData;
    onItemClickListener onItemClickListener;
    private String strSubType;
    //private List<ObservationViewResult> items;
    private int lastPosition = -1;

    public ObservationViewAdapterAdmin(ObservationViewActivityAdmin observationView, List<ObservationHeader> observationDataArrayList) {
        context = observationView;
        observationData = observationDataArrayList;
//        this.strSubType = strSubType;
        //tems = observationDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_view_list_items, parent, false);

        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {

            String observatonType = observationData.get(position).getTypeOfObservation();
            String subType = observationData.get(position).getReason();
            if (observatonType.equalsIgnoreCase("Hazard")) {
                holder.typeOfObservation.setText("Hazard Id");
                holder.date.setText("Hazard Date & Time");
            }
            if (subType.equalsIgnoreCase("Accident")) {
                holder.typeOfObservation.setText("Accident Id");
                holder.date.setText("Accident Date & Time");

            } else if (subType.equalsIgnoreCase("Near Miss")) {
                holder.typeOfObservation.setText("NearMiss Id");
                holder.date.setText("NearMiss Date & Time");

            }

            holder.article_id.setText(observationData.get(position).getObservationId());
            holder.edit_customer_name.setText(observationData.get(position).getCustomerName());
            holder.edit_location.setText(observationData.get(position).getLocation());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            try {

                holder.edit_incident_date_time.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(df.parse(observationData.get(position).getDateOfIncidence())));
                holder.edit_created_on.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(df.parse(observationData.get(position).getDateOfIncidence())));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.edit_observation_status.setText(observationData.get(position).getStatus());
            if (observationData.get(position).getStatus().equalsIgnoreCase("Open")) {
                holder.edit_observation_status.setTextColor(Color.RED);

            } else if (observationData.get(position).getStatus().equalsIgnoreCase("Assigned")) {
                holder.edit_observation_status.setTextColor(Color.parseColor("#FF8800"));

            } else if (observationData.get(position).getStatus().equalsIgnoreCase("Closed")) {
                holder.edit_observation_status.setTextColor(Color.parseColor("#1CC861"));

            }
            if (observationData.get(position).getStatus().equalsIgnoreCase("Delete")) {
                holder.itemView.setVisibility(View.GONE);
                return;
            }
            if (observationData.get(position).getReason().equalsIgnoreCase("Near Miss")) {
                holder.edit_observation_type.setText(observationData.get(position).getReason());
            } else {
                holder.edit_observation_type.setText(observationData.get(position).getTypeOfObservation());
            }

            holder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (observationData.get(position).getLatitude() != null) {
                        Intent intent = new Intent(context, GoogleMapActivity.class);
                        Bundle bd = new Bundle();
                        bd.putSerializable("Observation", observationData.get(position));
                        bd.putSerializable("FLAG", "Observation");
                        intent.putExtras(bd);
                        context.startActivity(intent);

                    } else {
                        Utilities.showAlertDialog("Location not found", context);
                    }
                }
            });

            setAnimation(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent observationdetails = new Intent(context, ObservationDetailsActivityForAdmin.class);
                        observationdetails.putExtra("observationId", observationData.get(position).getObservationId());
                        observationdetails.putExtra("Status", observationData.get(position).getStatus());
                        observationdetails.putExtra("ObservationType", observationData.get(position).getTypeOfObservation());
                        observationdetails.putExtra("SubType", observationData.get(position).getReason());
                        context.startActivity(observationdetails);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_enter);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return observationData == null ? 0 : observationData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView article_id, typeOfObservation, date;
        ImageView location;
        EditText edit_customer_name, edit_location, edit_incident_date_time, edit_created_on, edit_observation_status, edit_observation_type;
        LinearLayout mainlayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                edit_customer_name = itemView.findViewById(R.id.edit_customer_name);
                edit_location = itemView.findViewById(R.id.edit_location);
                edit_incident_date_time = itemView.findViewById(R.id.edit_incident_date_time);
                edit_created_on = itemView.findViewById(R.id.edit_created_on);
                edit_observation_status = itemView.findViewById(R.id.edit_observation_status);
                edit_observation_type = itemView.findViewById(R.id.edit_observation_type);
                article_id = itemView.findViewById(R.id.article_id);
                location = (ImageView) itemView.findViewById(R.id.location);
                typeOfObservation = (TextView) itemView.findViewById(R.id.typeOfObservation);
                date = (TextView) itemView.findViewById(R.id.date);

                mainlayout = itemView.findViewById(R.id.mainlayout);

                //fonts

                edit_customer_name.setTypeface(Utilities.fontBold(context));
                edit_observation_status.setTypeface(Utilities.fontBold(context));
                edit_location.setTypeface(Utilities.fontBold(context));
                edit_incident_date_time.setTypeface(Utilities.fontBold(context));
                edit_created_on.setTypeface(Utilities.fontBold(context));
                edit_observation_status.setTypeface(Utilities.fontBold(context));
                article_id.setTypeface(Utilities.fontBold(context));
                edit_observation_type.setTypeface(Utilities.fontBold(context));
                location.setVisibility(View.GONE);
                location.setOnClickListener(this);
                itemView.setOnClickListener(this);

            } catch (Exception e) {
                e.getMessage();
            }
        }


        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());

            }

        }
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
