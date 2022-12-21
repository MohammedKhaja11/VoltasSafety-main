package com.ags.voltassafety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.model.EscalationResult;

import java.util.List;

class EscalationListAdapter extends RecyclerView.Adapter <EscalationListAdapter.MyViewHolder>{
    Context context;
    List<EscalationResult> escalationResultList;
    public EscalationListAdapter(EscalationReportList escalationReportList, List<EscalationResult> escalationResultList) {
        context=escalationReportList;
        this.escalationResultList=escalationResultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.escalation_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.branch_data.setText(escalationResultList.get(position).getBranch());
        holder.total_data.setText(escalationResultList.get(position).getTotal()+"");
        holder.oprn_data.setText(escalationResultList.get(position).getOpen()+"");
        holder.assigned_data.setText(escalationResultList.get(position).getAssigned()+"");
        holder.close_data.setText(escalationResultList.get(position).getClosed()+"");
        holder.intime_assigned_data.setText(escalationResultList.get(position).getInTimeAssigned()+"");
        holder.outtime_assigned_data.setText(escalationResultList.get(position).getOutTimeAssigned()+"");
        holder.intime_closed_data.setText(escalationResultList.get(position).getInTimeClosed()+"");
        holder.outtime_closed_data.setText(escalationResultList.get(position).getOutTimeClosed()+"");

    }

    @Override
    public int getItemCount() {
        return escalationResultList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView branch_textview,branch_data,total_textview,total_data,oprn_textview,oprn_data,assigned_text,
                assigned_data,close_text,close_data,intime_assigned_text,intime_assigned_data,outtime_assigned_text,
                outtime_assigned_data,intime_closed_text,intime_closed_data,outtime_closed_text,outtime_closed_data;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            branch_textview = (TextView) itemView.findViewById(R.id.branch_textview);
            branch_data = (TextView) itemView.findViewById(R.id.branch_data);
            total_textview = (TextView) itemView.findViewById(R.id.total_textview);
            total_data = (TextView) itemView.findViewById(R.id.total_data);
            oprn_textview = (TextView) itemView.findViewById(R.id.oprn_textview);
            oprn_data = (TextView) itemView.findViewById(R.id.oprn_data);
            assigned_text = (TextView) itemView.findViewById(R.id.assigned_text);
            assigned_data = (TextView) itemView.findViewById(R.id.assigned_data);
            close_text = (TextView) itemView.findViewById(R.id.close_text);
            close_data = (TextView) itemView.findViewById(R.id.close_data);
            intime_assigned_text = (TextView) itemView.findViewById(R.id.intime_assigned_text);
            intime_assigned_data = (TextView) itemView.findViewById(R.id.intime_assigned_data);
            outtime_assigned_text = (TextView) itemView.findViewById(R.id.outtime_assigned_text);
            outtime_assigned_data = (TextView) itemView.findViewById(R.id.outtime_assigned_data);
            intime_closed_text = (TextView) itemView.findViewById(R.id.intime_closed_text);
            intime_closed_data = (TextView) itemView.findViewById(R.id.intime_closed_data);
            outtime_closed_text = (TextView) itemView.findViewById(R.id.outtime_closed_text);
            outtime_closed_data = (TextView) itemView.findViewById(R.id.outtime_closed_data);
        }
    }
}
