package com.ags.voltassafety.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.SiteEngineerResult;

import java.util.ArrayList;
import java.util.List;

public class SiteEnginnerAdapter extends ArrayAdapter<SiteEngineerResult> {
    Context context;
    int resource, textViewResourceId;
    ArrayList<SiteEngineerResult> items, tempItems, suggestions;

    public SiteEnginnerAdapter(Context context, int resource, int textViewResourceId, ArrayList<SiteEngineerResult> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<SiteEngineerResult>(items); // this makes the difference.
        suggestions = new ArrayList<SiteEngineerResult>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_view_items, parent, false);
            }
            SiteEngineerResult people = items.get(position);
            if (people != null) {
                TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
                if (lblName != null)
                    lblName.setText(people.getUserName());
            }
        }catch (Exception e){
            e.getMessage();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((SiteEngineerResult) resultValue).getUserName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SiteEngineerResult people : tempItems) {
                    if (people.getUserName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<SiteEngineerResult> filterList = (ArrayList<SiteEngineerResult>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SiteEngineerResult people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
