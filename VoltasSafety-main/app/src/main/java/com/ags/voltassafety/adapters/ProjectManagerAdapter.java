package com.ags.voltassafety.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.SiteEngineerResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectManagerAdapter extends ArrayAdapter<SiteEngineerResult> implements Filterable {

    private List<SiteEngineerResult> retrieveAll;
    private int resource;
    private Context context;
    LayoutInflater inflate;
    public ProjectManagerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SiteEngineerResult> retrieveAll) {
        super(context, resource, retrieveAll);
        this.retrieveAll = retrieveAll;
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        SiteEngineerResult rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        try {
            if (rowview==null) {

                holder = new viewHolder();
                inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = inflate.inflate(R.layout.list_view_items, null, false);

                holder.txtTitle = (TextView) rowview.findViewById(R.id.lbl_name);
                // holder.imageView = (ImageView) rowview.findViewById(R.id.icon);
                rowview.setTag(holder);
            }else{
                holder = (viewHolder) rowview.getTag();
            }
            // holder.imageView.setImageResource(rowItem.getImageId());
            holder.txtTitle.setText(rowItem.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowview;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<SiteEngineerResult> custName = new ArrayList<>();

                    // Assign the data to the FilterResults
                    filterResults.values = custName;
                    filterResults.count = custName.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    retrieveAll = (List<SiteEngineerResult>) results.values;
                    //Collections.sort(retrieveAll,SiteEngineerResult.nameComparator);
                    notifyDataSetChanged();
                }
            }};
        return filter;
    }

    private class viewHolder{
        TextView txtTitle;
        //ImageView imageView;
    }
    @Override
    public void notifyDataSetChanged() {
        try {
            this.setNotifyOnChange(false);

            this.sort(new Comparator<SiteEngineerResult>() {
                @Override
                public int compare(SiteEngineerResult lhs, SiteEngineerResult rhs) {
                    return lhs.getUserName().trim().toUpperCase().compareTo(rhs.getUserName().trim().toUpperCase());
                }
            });
            if(this != null) {
                //adapter.notifyDataSetChanged();
                this.setNotifyOnChange(true);
            }

        }catch (IllegalStateException e){
            e.getMessage();
        }
    }
}
