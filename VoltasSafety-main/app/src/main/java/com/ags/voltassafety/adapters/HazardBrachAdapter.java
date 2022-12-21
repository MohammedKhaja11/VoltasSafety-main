package com.ags.voltassafety.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.HazardBanchReport;
import com.ags.voltassafety.HazardObservationReport;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.BranchObservation;
import com.ags.voltassafety.model.BranchObservationstatus;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchObservationStatusResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HazardBrachAdapter extends RecyclerView.Adapter<HazardBrachAdapter.MyViewHolder> implements Filterable {

    public List<BranchObservation> branches;
    Context context;
    //  private ArrayList<BranchObservation> filteredList = new ArrayList<>();
    public List<BranchObservation> filteredLists;
    HazardBranchModel hazardBranchModelrequest;

    HazardBranchObservationStatusResponse hazardBranchObservationStatusResponse;

    ArrayList<BranchObservationstatus> branchObservationstatuses;

    public HazardBrachAdapter(HazardBanchReport hazardBanchReport, ArrayList<BranchObservation> branches, HazardBranchModel hazardBranchModelrequest) {
        this.branches = branches;
        context = hazardBanchReport;
        filteredLists = branches;
        this.hazardBranchModelrequest = hazardBranchModelrequest;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String test = branches.get(position).getName().toString();
        char first = test.charAt(0);
        holder.title.setText("" + first);
        holder.tv_count.setText(branches.get(position).getCount().toString());
        holder.tv_branchname.setText(branches.get(position).getName());

        holder.RelativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hazardBranchModelrequest.setBranch(holder.tv_branchname.getText().toString());
                Log.d("BranchName", holder.tv_branchname.getText().toString());
                ProgressDialog progressDialog;

                try {
                    branchObservationstatuses = new ArrayList<>();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Bearer", MODE_PRIVATE);
                    ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                    Call<HazardBranchObservationStatusResponse> call = apiInterface.getBranchObservationStatusReport("Bearer " + sharedPreferences.getString("Bearertoken", null), hazardBranchModelrequest);
                    progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
                    progressDialog.setMessage("Data loading");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    call.enqueue(new Callback<HazardBranchObservationStatusResponse>() {
                        public void onResponse(Call<HazardBranchObservationStatusResponse> call, Response<HazardBranchObservationStatusResponse> response) {

                            if (response.isSuccessful()) {

                                if (response.body().getSuccess()) {
                                    if (response.body().getResult() != null) {
                                        progressDialog.dismiss();
                                        hazardBranchObservationStatusResponse = response.body();


                                        branchObservationstatuses.addAll(hazardBranchObservationStatusResponse.getResult().getBranchObservationStatus());

                                        Intent observationreportview = new Intent(context, HazardObservationReport.class);
                                        Bundle bd = new Bundle();
                                        bd.putSerializable("observationbrachstatus", (Serializable) branchObservationstatuses);
                                        bd.putSerializable("observationbranchstatusresponse", (Serializable) hazardBranchObservationStatusResponse);
                                        bd.putSerializable("observationbranchstatusrequest", (Serializable)hazardBranchModelrequest);
                                        bd.putString("title","Hazard Report");
                                        observationreportview.putExtras(bd);
                                        context.startActivity(observationreportview);

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
                                            .setMessage(response.body().getErrors().toString())
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


                        public void onFailure(Call<HazardBranchObservationStatusResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.d("LoginResponse", t.getMessage() + "");
                        }


                    });
                } catch (
                        Exception e) {
                    e.getMessage();

                }
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

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().replaceAll(" ", "").toLowerCase();
                Log.d("fileterText", charString);
                Log.d("ArraylistSize", filteredLists.size() + "");
                if (charString.isEmpty()) {
                    branches = filteredLists;

                } else {
                    ArrayList<BranchObservation> filteredList = new ArrayList<>();
                    for (BranchObservation patients : filteredLists) {
                        if (patients.getName().replaceAll(" ", "").toLowerCase().contains(charString)) {
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
                branches = (ArrayList<BranchObservation>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
