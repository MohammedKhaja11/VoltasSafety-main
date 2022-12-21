package com.ags.voltassafety.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.BranchObservationstatus;
import com.ags.voltassafety.model.EscalationModel;
import com.ags.voltassafety.model.EscalationResult;
import com.ags.voltassafety.model.HazardBranchObservationStatusResponse;

import java.util.ArrayList;
import java.util.List;

public class EscalationUpdatedAdapter extends RecyclerView.Adapter<EscalationUpdatedAdapter.MyViewHolder> {

    public List<EscalationResult> branches;
    Context context;
    //  private ArrayList<BranchObservation> filteredList = new ArrayList<>();
    public List<EscalationResult> filteredLists;
    EscalationModel hazardBranchModelrequest;

    HazardBranchObservationStatusResponse hazardBranchObservationStatusResponse;

    ArrayList<BranchObservationstatus> branchObservationstatuses;

  /*  public EscalationUpdatedAdapter(Context context, ArrayList<EscalationResult> branches, EscalationModel hazardBranchModelrequest) {
        this.branches = branches;
        this.context = context;
        filteredLists = branches;
        this.hazardBranchModelrequest = hazardBranchModelrequest;

    }*/

    public EscalationUpdatedAdapter(Context context, List<EscalationResult> branches, EscalationModel escalationModel) {

        this.context = context;
        this.branches = branches;
        this.hazardBranchModelrequest = escalationModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.escalation_row_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String test = branches.get(position).getBranch().toString();
        char first = test.charAt(0);
        holder.title.setText("" + first);
        holder.tv_count.setText(branches.get(position).getTotal().toString());
        holder.tv_branchname.setText(branches.get(position).getBranch());

        holder.RelativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BranchName", holder.tv_branchname.getText().toString());
//                holder.getPosition();
                ProgressDialog progressDialog;
            }
        });
        if (position % 8 == 0) {
            holder.title.setBackgroundResource(R.drawable.red_selector);
//                getTv_count.setBackgroundResource(R.drawable.red_selector);
        } else if (position % 8 == 1) {
            holder.title.setBackgroundResource(R.drawable.blue_selector);
//                getTv_count.setBackgroundResource(R.drawable.blue_selector);
        } else if (position % 8 == 2) {
            holder.title.setBackgroundResource(R.drawable.brown_selector);
//                getTv_count.setBackgroundResource(R.drawable.brown_selector);
        } else if (position % 8 == 3) {
            holder.title.setBackgroundResource(R.drawable.green_selector);
//                getTv_count.setBackgroundResource(R.drawable.green_selector);
        } else if (position % 8 == 4) {
            holder.title.setBackgroundResource(R.drawable.violet_selector);
        } else if (position % 8 == 5) {
            holder.title.setBackgroundResource(R.drawable.yellow_selector);

        } else if (position % 8 == 6) {
            holder.title.setBackgroundResource(R.drawable.lightgreen_selector);

        } else if (position % 8 == 7) {
            holder.title.setBackgroundResource(R.drawable.skyblue_selector);

        }
    }

    @Override
    public int getItemCount() {
        // return filteredList.size();
        return branches.size();
    }

//    public Filter getFilter() {
//        Filter filter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                filteredList.clear();
//                // FilterResults results = new FilterResults();
//                String charString = constraint.toString().trim();
//                if (charString.isEmpty()) {
//                    filteredList.addAll(branches);
//                } else {
//                    // String filterPattern = constraint.toString().toLowerCase().trim();
//                    ArrayList<BranchObservation> filterList = new ArrayList<>();
//                    for (BranchObservation item : branches) {
//                        if (item.getName() != null) {
//                            if (item.getName().toLowerCase().contains(charString.toLowerCase())) {
//                                filterList.add(item);
//                            }
//                        }
//                    }
//                    filteredList = filterList;
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filteredList;
//                // filterResults.count = filteredList.size();
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null) {
//                    filteredList = (ArrayList<BranchObservation>) results.values;
//                    //   hideSoftKeyboard(RecyclerView.);
//                    notifyDataSetChanged();
//                    //notifyItemRangeInserted(0, filteredList.size());
//                }
//
//
//            }
//        };
//        return filter;
//    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, tv_branchname, tv_count;
        RelativeLayout RelativeLayout2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            tv_branchname = itemView.findViewById(R.id.tv_branchname);
            tv_count = itemView.findViewById(R.id.tv_count);
            RelativeLayout2 = itemView.findViewById(R.id.RelativeLayout2);


        }
    }

   /* public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().replaceAll(" ", "").toLowerCase();
                Log.d("fileterText", charString);
                Log.d("ArraylistSize", filteredLists.size() + "");
                if (charString.isEmpty()) {
                    branches = filteredLists;

                } else {
                    ArrayList<EscalationResult> filteredList = new ArrayList<>();
                    for (EscalationResult patients : filteredLists) {
                        if (patients.getBranch().replaceAll(" ", "").toLowerCase().contains(charString)) {
                            filteredList.add(patients);
                        }
                    }
                    branches = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = branches;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                branches = (ArrayList<EscalationResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/


}
