package com.ags.voltassafety.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.EscalationModel;
import com.ags.voltassafety.model.EscalationResult;
import com.ags.voltassafety.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {

    Context context;
    public List<EscalationResult> filteredLists;
    EscalationModel hazardBranchModelrequest;
    List<EscalationResult> escalationResultList;

    public CustomExpandableListAdapter(Context context, List<EscalationResult> escalationResultList, EscalationModel escalationModel) {

        this.context = context;
        this.escalationResultList = escalationResultList;
        filteredLists = escalationResultList;
        this.hazardBranchModelrequest = escalationModel;
    }


    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return escalationResultList.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
//        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.escalation_child_layout, null);
        }
        TextView openData = (TextView) convertView.findViewById(R.id.openData);
        TextView assignData = (TextView) convertView.findViewById(R.id.assignData);
        TextView closeData = (TextView) convertView.findViewById(R.id.closeData);
        TextView inTimeAssignData = (TextView) convertView.findViewById(R.id.inTimeAssignData);
        TextView outTimeAssignData = (TextView) convertView.findViewById(R.id.outTimeAssignData);
        TextView inTimeClosedData = (TextView) convertView.findViewById(R.id.inTimeClosedData);
        TextView outTimeClosedData = (TextView) convertView.findViewById(R.id.outTimeClosedData);

        TextView openText = (TextView) convertView.findViewById(R.id.openText);
        TextView assignText = (TextView) convertView.findViewById(R.id.assignText);
        TextView closeText = (TextView) convertView.findViewById(R.id.closeText);
        TextView inTimeAssignText = (TextView) convertView.findViewById(R.id.inTimeAssignrText);
        TextView outTimeAssignText = (TextView) convertView.findViewById(R.id.outTimeAssignText);
        TextView inTimeClosedText = (TextView) convertView.findViewById(R.id.inTimeClosedText);
        TextView outTimeClosedText = (TextView) convertView.findViewById(R.id.outTimeClosedText);

        openData.setTypeface(Utilities.fontRegular(context));
        assignData.setTypeface(Utilities.fontRegular(context));
        closeData.setTypeface(Utilities.fontRegular(context));
        inTimeAssignData.setTypeface(Utilities.fontRegular(context));
        outTimeAssignData.setTypeface(Utilities.fontRegular(context));
        inTimeClosedData.setTypeface(Utilities.fontRegular(context));
        outTimeClosedData.setTypeface(Utilities.fontRegular(context));

        assignText.setTypeface(Utilities.fontRegular(context));
        closeText.setTypeface(Utilities.fontRegular(context));
        inTimeAssignText.setTypeface(Utilities.fontRegular(context));
        outTimeAssignText.setTypeface(Utilities.fontRegular(context));
        inTimeClosedText.setTypeface(Utilities.fontRegular(context));
        outTimeClosedText.setTypeface(Utilities.fontRegular(context));
        openText.setTypeface(Utilities.fontRegular(context));
        outTimeAssignText.setTypeface(Utilities.fontRegular(context));

        CardView cardOpen = (CardView) convertView.findViewById(R.id.cardOpen);
        CardView cardAssign = (CardView) convertView.findViewById(R.id.cardAssign);
        CardView cardClose = (CardView) convertView.findViewById(R.id.cardClose);
        CardView cardInTimeAssigned = (CardView) convertView.findViewById(R.id.cardInTimeAssigned);
        CardView cardOutTimeAssigned = (CardView) convertView.findViewById(R.id.cardOutTimeAssigned);
        CardView cardInTimeClosed = (CardView) convertView.findViewById(R.id.cardInTimeClosed);
        CardView cardOutTimeClosed = (CardView) convertView.findViewById(R.id.cardOutTimeClosed);

        openData.setText("" + escalationResultList.get(listPosition).getOpen());
        assignData.setText("" + escalationResultList.get(listPosition).getAssigned());
        closeData.setText("" + escalationResultList.get(listPosition).getClosed());
        outTimeAssignData.setText("" + escalationResultList.get(listPosition).getOutTimeAssigned());
        inTimeAssignData.setText("" + escalationResultList.get(listPosition).getInTimeAssigned());
        inTimeClosedData.setText("" + escalationResultList.get(listPosition).getInTimeClosed());
        outTimeClosedData.setText("" + escalationResultList.get(listPosition).getOutTimeClosed());

        cardOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getOpen(), Toast.LENGTH_SHORT).show();
            }
        });
        cardAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getAssigned(), Toast.LENGTH_SHORT).show();
            }
        });
        cardClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getClosed(), Toast.LENGTH_SHORT).show();
            }
        });
        cardInTimeAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getInTimeAssigned(), Toast.LENGTH_SHORT).show();
            }
        });
        cardOutTimeAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getOutTimeAssigned(), Toast.LENGTH_SHORT).show();
            }
        });
        cardInTimeClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getInTimeClosed(), Toast.LENGTH_SHORT).show();
            }
        });
        cardOutTimeClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + escalationResultList.get(listPosition).getOutTimeClosed(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return escalationResultList.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.escalationResultList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.escalation_row_item, null);
        }

        TextView titleFirst = (TextView) convertView.findViewById(R.id.title);
        TextView countText = (TextView) convertView.findViewById(R.id.tv_toteng);
        TextView tv_branchname = (TextView) convertView.findViewById(R.id.tv_branchname);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        String branch = escalationResultList.get(position).getBranch();
        char charat = branch.charAt(0);
        titleFirst.setText("" + charat);
        tv_branchname.setTypeface(Utilities.fontRegular(context));
        tv_count.setTypeface(Utilities.fontRegular(context));
        countText.setTypeface(Utilities.fontRegular(context));
        tv_count.setText(escalationResultList.get(position).getTotal().toString());
        tv_branchname.setText(escalationResultList.get(position).getBranch());


        if (position % 8 == 0) {
            titleFirst.setBackgroundResource(R.drawable.skyblue_selector);

        } else if (position % 8 == 1) {
            titleFirst.setBackgroundResource(R.drawable.blue_selector);
//                getTv_count.setBackgroundResource(R.drawable.blue_selector);
        } else if (position % 8 == 2) {
            titleFirst.setBackgroundResource(R.drawable.brown_selector);
//                getTv_count.setBackgroundResource(R.drawable.brown_selector);
        } else if (position % 8 == 3) {
            titleFirst.setBackgroundResource(R.drawable.green_selector);
//                getTv_count.setBackgroundResource(R.drawable.green_selector);
        } else if (position % 8 == 4) {
            titleFirst.setBackgroundResource(R.drawable.violet_selector);
        } else if (position % 8 == 5) {
            titleFirst.setBackgroundResource(R.drawable.yellow_selector);

        } else if (position % 8 == 6) {
            titleFirst.setBackgroundResource(R.drawable.lightgreen_selector);

        } if (position % 8 == 7) {
            titleFirst.setBackgroundResource(R.drawable.red_selector);
//                getTv_count.setBackgroundResource(R.drawable.red_selector);
        }
        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().replaceAll(" ", "").toLowerCase();
                Log.d("fileterText", charString);
                Log.d("ArraylistSize", filteredLists.size() + "");
                if (charString.isEmpty()) {
                    escalationResultList = filteredLists;

                } else {
                    ArrayList<EscalationResult> filteredList = new ArrayList<>();
                    for (EscalationResult patients : filteredLists) {
                        if (patients.getBranch().replaceAll(" ", "").toLowerCase().contains(charString)) {
                            filteredList.add(patients);
                        }
                    }
                    escalationResultList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = escalationResultList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                escalationResultList = (ArrayList<EscalationResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
