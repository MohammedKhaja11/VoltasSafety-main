package com.ags.voltassafety.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.CreateObservationActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.Customer;
import com.ags.voltassafety.utility.Utilities;

import java.util.ArrayList;
import java.util.List;


public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {
    Context context;
    ArrayList<Customer> branchArrayList;
    ArrayList<Customer> filteredList = new ArrayList<>();
    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(View view, Customer position);

    }

    public BranchAdapter(CreateObservationActivity createObservation, ArrayList<Customer> branchArrayList, OnItemClicked onClick) {
        context = createObservation;
        this.branchArrayList = branchArrayList;
        filteredList.addAll(branchArrayList);
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public BranchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_items, parent, false);
        BranchAdapter.MyViewHolder vh = new BranchAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final BranchAdapter.MyViewHolder holder, final int position) {

        try {
            holder.branch_textview.setText(filteredList.get(position).getName());
            String sourceString = "<b>" + "Address :" + "</b> " + filteredList.get(position).getAddress();
            if (filteredList.get(position).getAddress() != null) {
                holder.branch_address.setText(Html.fromHtml(sourceString));
            } else {
                holder.branch_address.setText("");
            }

        } catch (Exception e) {
            e.getMessage();
        }
//        holder.branch_address.setText(context.getResources().getString(R.string.Address) + filteredList.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    onClick.onItemClick(view, filteredList.get(holder.getAdapterPosition()));
//                edit_customer_name.setText(branchArrayList.get(position).getName());
//                dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView branch_textview, branch_address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            branch_textview = itemView.findViewById(R.id.branch_textview);
            branch_textview.setTypeface(Utilities.fontBold(context));
            branch_address = itemView.findViewById(R.id.branch_address);
//            branch_address.setTypeface(Utilities.fontRegular(context));
        }
    }


    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //filteredList.clear();
                // FilterResults results = new FilterResults();
                String charString = constraint.toString().trim();
                if (charString.isEmpty()) {
                    filteredList.addAll(branchArrayList);
                } else {
                    // String filterPattern = constraint.toString().toLowerCase().trim();
                    ArrayList<Customer> filterList = new ArrayList<>();
                    for (Customer item : branchArrayList) {
                        if (item.getName() != null) {
                            if (item.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filterList.add(item);
                            }
                        }
                    }
                    filteredList = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                // filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null) {
                    filteredList = (ArrayList<Customer>) results.values;
                    //   hideSoftKeyboard(RecyclerView.);
                    notifyDataSetChanged();
                    //notifyItemRangeInserted(0, filteredList.size());
                }


            }
        };
        return filter;
    }
}