package com.ags.voltassafety.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.InjuredDetailsModel;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.utility.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IncidentViewAdapter extends BaseAdapter {
    private Context context;
    List<InjuredDetailsModel> list;
    List<ObservationAttachmentModel> linkarraylist;
    private AlertDialog.Builder alertDialog;
    IncidentViewGalleryAdapter galleryAdapter;
    List<EntityResult> result;


    public IncidentViewAdapter(Context incidentActivity, List<InjuredDetailsModel> list) {
        context = incidentActivity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        View itemView = null;
        try {
            itemView = LayoutInflater.from(context).inflate(R.layout.incident_view_adapter_layout, viewGroup, false);
            TextView person = itemView.findViewById(R.id.person);
            TextView gender = itemView.findViewById(R.id.gender);
            TextView textOne = itemView.findViewById(R.id.textOne);
            TextView textTwo = itemView.findViewById(R.id.textTwo);
            TextView descriptionHeader = itemView.findViewById(R.id.descriptionHeader);
            TextView counted = itemView.findViewById(R.id.counted);
            TextView totalCount = itemView.findViewById(R.id.totalCount);
            TextView trearmentHead = itemView.findViewById(R.id.trearmentHead);
            TextView attachHead = itemView.findViewById(R.id.attachHead);
            EditText edit_name = itemView.findViewById(R.id.edit_name);
            //EditText edit_employee_description = itemView.findViewById(R.id.edit_employee_description);
            final EditText edit_incident_time = itemView.findViewById(R.id.edit_incident_time);
            EditText edit_designation = itemView.findViewById(R.id.edit_designation);
            EditText edit_age = itemView.findViewById(R.id.edit_age);
            ImageView deletePerson = (ImageView) itemView.findViewById(R.id.deletePerson);
            ImageView addmedicalReport = (ImageView) itemView.findViewById(R.id.addmedicalReport);
            EditText Gender = itemView.findViewById(R.id.spinnerGender);
            EditText EmpType = itemView.findViewById(R.id.spinnerEmpType);
            EditText Treatment = itemView.findViewById(R.id.spinnerTreatment);
            GridView gv = (GridView) itemView.findViewById(R.id.gv);
            person.setText("Person " + (i + 1));

            Log.d("EntityResponse", result + "");

            person.setTypeface(Utilities.fontBold(context));
            Treatment.setTypeface(Utilities.fontBold(context));
            EmpType.setTypeface(Utilities.fontBold(context));
            Gender.setTypeface(Utilities.fontBold(context));
            edit_name.setTypeface(Utilities.fontBold(context));
           // edit_employee_description.setTypeface(Utilities.fontBold(context));
            edit_designation.setTypeface(Utilities.fontBold(context));
            edit_age.setTypeface(Utilities.fontBold(context));
            edit_incident_time.setTypeface(Utilities.fontBold(context));
            person.setTypeface(Utilities.fontBold(context));
            attachHead.setTypeface(Utilities.fontBold(context));
            edit_name.setText(list.get(i).getName());

            String[] separated = list.get(i).getInjuredDateTime().split("T");
            Log.d(">>>>>>>>>>>>>time", separated[1]);

//            System.out.println(separated[1].substring(0, separated[1].length() - 2));

            //  edit_incident_time.setText(list.get(i).getInjuredDateTime());
//
            InjuredDetailsModel ObservationItemsDetailsModel = list.get(i);
            if (ObservationItemsDetailsModel.getObservationAttachmentModels().size() > 0 && ObservationItemsDetailsModel.getObservationAttachmentModels() != null) {

                galleryAdapter = new IncidentViewGalleryAdapter(context, ObservationItemsDetailsModel.getObservationAttachmentModels());
                gv.setAdapter(galleryAdapter);
                gv.setVerticalSpacing(gv.getHorizontalSpacing());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
                mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);
                setGridViewHeightBasedOnChildren(gv, 5);
            }else {
                gv.setVisibility(View.GONE);
            }

            if (ObservationItemsDetailsModel.getItemAttachments().size() > 0 && ObservationItemsDetailsModel.getItemAttachments() != null) {
                gv.setVisibility(View.VISIBLE);
            } else {
                gv.setVisibility(View.GONE);
            }
            gv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gv.hasFocus()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                    return false;
                }
            });


            edit_age.setText("" + (Integer) list.get(i).getAge());
            Gender.setText(list.get(i).getGender());
            EmpType.setText(list.get(i).getEmployeeType());
            Treatment.setText(list.get(i).getTreatment());
            edit_designation.setText(list.get(i).getDesignation());
            //edit_employee_description.setText(list.get(i).getDescription());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

            Date data = null;
            try {
                data = sdf.parse(separated[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }


           // edit_incident_date.setText(output.format(data));

            edit_incident_time.setText(output.format(data)+" "+separated[1].substring(0, separated[1].length() - 3));

            deletePerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                    alertDialog.setMessage("Are you sure, Do you want to delete the Person ?");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            list.remove(i);
                            notifyDataSetChanged();

                        }
                    });

                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
        return itemView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void setGridViewHeightBasedOnChildren(GridView gv, int i) {
        ListAdapter listAdapter = gv.getAdapter();
        if (listAdapter == null) {
            // pre-condition
//            gv.setVisibility(View.GONE);
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gv);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > i) {
            x = items / i;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gv.getLayoutParams();
        params.height = totalHeight;
        gv.setLayoutParams(params);
    }
}
