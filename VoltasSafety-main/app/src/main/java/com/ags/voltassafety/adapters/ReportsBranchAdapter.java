package com.ags.voltassafety.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.R;
import com.ags.voltassafety.ReportsView;
import com.ags.voltassafety.model.UserBranches;
import com.ags.voltassafety.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

import static com.ags.voltassafety.HazardReport.chk_report;

public class ReportsBranchAdapter extends RecyclerView.Adapter<ReportsBranchAdapter.ViewHolder> {

    public List<UserBranches> item_list;
    Context context;
    public List<UserBranches> filteritems;

    public ReportsBranchAdapter(Context activity, List<UserBranches> zonesList) {
        context = activity;
        item_list = zonesList;
        filteritems = zonesList;
    }

    public interface ClickCallback {
        void onItemClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_items, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int pos = position;

        holder.item_name.setText(item_list.get(position).getBranchName());
        holder.item_name.setTypeface(Utilities.fontRegular(context));

        holder.chkSelected.setChecked(item_list.get(position).isSelected());

        holder.chkSelected.setTag(item_list.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                UserBranches model = (UserBranches) cb.getTag();
                model.setSelected(cb.isChecked());
            }
        });
        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try {
                    if (holder.chkSelected.isChecked()) {

                        if (item_list.get(position).isSelected()) {

                            chk_report.setChecked(true);
                        }

                    } else {

                        chk_report.setChecked(false);

                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_name;
        //        public ImageButton btn_delete;
        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            item_name = (TextView) itemLayoutView.findViewById(R.id.txt_Name);
//            btn_delete = (ImageButton) itemLayoutView.findViewById(R.id.btn_delete_unit);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chk_selected);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().replaceAll(" ", "").toLowerCase();
                Log.d("fileterText", charString);
                Log.d("ArraylistSize", filteritems.size() + "");
                if (charString.isEmpty()) {
                    item_list = filteritems;

                } else {
                    ArrayList<UserBranches> filteredList = new ArrayList<>();
                    for (UserBranches patients : filteritems) {
                        if (patients.getBranchName().replaceAll(" ", "").toLowerCase().contains(charString)) {
                            filteredList.add(patients);
                        }
                    }
                    item_list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = item_list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                item_list = (ArrayList<UserBranches>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
