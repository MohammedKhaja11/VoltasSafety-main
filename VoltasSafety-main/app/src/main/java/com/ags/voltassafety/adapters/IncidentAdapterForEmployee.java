package com.ags.voltassafety.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ags.voltassafety.IncidentActivity;
import com.ags.voltassafety.IncidentActivityForEmployee;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.InjuredDetailsModel;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ags.voltassafety.IncidentActivityForEmployee.strStatus;

public class IncidentAdapterForEmployee extends BaseAdapter {
    private Context context;
    //    List<InjuredDetailsModel> list;
    List<InjuredDetailsModel> list;
    List<ObservationAttachmentModel> linkarraylist;
    public AlertDialog.Builder alertDialog;
    private HazardAdapter.OnItemClicked onClick;
    InjuridGalleryAdapter galleryAdapter;
    List<EntityResult> result;
    List<String> genderarraylist;
    List<String> employeetypearrylist;
    //List<String> injutytypearraylist;
    List<String> treatmentarraylist;
    String strobsrId;
    LinearLayout action_layout;
    List<ItemsActionDetailsModel> observationitemsactondetailsModel = new ArrayList<>();
    ItemsActionDetailsModel itemsActionDetailsModel;
    ObservtionActionAdapter observtionActionAdapter;
    String[] parts;
    GridView gv;
    String selecteddate = null;
    ObservationHeader observationHeader;

    String part1, part2;
    List<ObservationItemsDetailsModel> observationItemsDetailsModelModelslist;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    String strobjId;

    public IncidentAdapterForEmployee(IncidentActivityForEmployee incidentActivity, List<InjuredDetailsModel> list, List<EntityResult> result, String observationId, ObservationHeader observationHeader, String strobrId) {

        context = incidentActivity;
        this.list = list;
        this.linkarraylist = linkarraylist;
        this.result = result;
        strobsrId = observationId;
        this.observationHeader = observationHeader;
        this.strobjId=strobsrId;
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
        EditText edit_incident_date, edit_incident_time;
        ImageView add_actions;
        final ListView actions_listview;
        final EditText action_description, action_remarks, action_name;
        final TextView count_text;
        final View itemView = LayoutInflater.from(context).inflate(R.layout.incident_adapter_layout, viewGroup, false);
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
        //Spinner edit_employee_description = itemView.findViewById(R.id.edit_employee_description);

        edit_incident_time = itemView.findViewById(R.id.edit_incident_time);
        edit_incident_date = itemView.findViewById(R.id.edit_incident_date);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = df.format(calendar.getTime());
        if (list.get(i).getInjuredDateTime() != null) {
            Date calSelectedDate = null;
            try {
                calSelectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(list.get(i).getInjuredDateTime());
            } catch (ParseException p) {
                p.getMessage();
            }
            String formatedDate = df.format(calSelectedDate);
            parts = formatedDate.split(" ");
            part1 = parts[0]; // 004
            part2 = parts[1];
        } else {
            parts = formattedDate.split(" ");
            part1 = parts[0]; // 004
            part2 = parts[1];
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date data = null;
            try {
                data = sdf.parse(part1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.get(i).setInjuredDateTime(output.format(data) + "T" +part2+ ":00");
        }

        edit_incident_date.setText(part1);
        edit_incident_time.setText(part2);

        edit_incident_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog calenderdialog = new Dialog(context, R.style.MyDialogTheme);
                calenderdialog.setContentView(R.layout.activity_incidentdate);
                calenderdialog.setCanceledOnTouchOutside(false);
                calenderdialog.show();
                CalendarView calendarView;
                calendarView = calenderdialog.findViewById(R.id.calendarView);
                TextView cancel = calenderdialog.findViewById(R.id.cancel);
                TextView done = calenderdialog.findViewById(R.id.done);
                calendarView.setMaxDate(new Date().getTime());
                selecteddate = null;
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        selecteddate = null;
                        int m = month + 1;
                        String currentmonth;
                        String currentday;
                        if (m < 10) {
                            currentmonth = "0" + m + "";
                        } else {
                            currentmonth = m + "";
                        }
                        if (dayOfMonth < 10) {
                            currentday = "0" + dayOfMonth;
                        } else {
                            currentday = dayOfMonth + "";
                        }
                        selecteddate = currentday + "-" + currentmonth + "-" + year;
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calenderdialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selecteddate != null) {
                            edit_incident_date.setText(selecteddate);
                        } else {
                            edit_incident_date.setText(part1);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                        Date data = null;
                        try {
                            data = sdf.parse(edit_incident_date.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        list.get(i).setInjuredDateTime(output.format(data) + "T" +edit_incident_time.getText().toString() + ":00");
                        calenderdialog.dismiss();
                       // notifyDataSetChanged();
                    }
                });
            }
        });
        edit_incident_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog timepickerdialog = new Dialog(context, R.style.MyDialogTheme);
                timepickerdialog.setContentView(R.layout.timepicker_dialog);
                timepickerdialog.setCanceledOnTouchOutside(false);
                timepickerdialog.show();
                final TimePicker timePicker = timepickerdialog.findViewById(R.id.timepicker_view);
                TextView cancel = timepickerdialog.findViewById(R.id.cancel);
                TextView done = timepickerdialog.findViewById(R.id.done);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timepickerdialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour, minute;
                        String am_pm;
                        if (Build.VERSION.SDK_INT >= 23) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        } else {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        }
                        Date selectedDate = null;
                        try {
                            selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(edit_incident_date.getText().toString() + " " + hour + ":" + minute + ":00");
                        } catch (ParseException p) {
                            p.getMessage();
                        }
                        if (selectedDate.equals(new Date()) || selectedDate.before(new Date())) {
                            edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
//                            notifyDataSetChanged();
                        } else {
                            edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
//                            notifyDataSetChanged();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                        Date data = null;
                        try {
                            data = sdf.parse(edit_incident_date.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        list.get(i).setInjuredDateTime(output.format(data) + "T" + edit_incident_time.getText().toString() + ":00");
                        timepickerdialog.dismiss();
                        //notifyDataSetChanged();
                    }
                });
            }
        });


        EditText edit_designation = itemView.findViewById(R.id.edit_designation);
        final EditText edit_age = itemView.findViewById(R.id.edit_age);
        ImageView deletePerson = (ImageView) itemView.findViewById(R.id.deletePerson);
        ImageView addmedicalReport = (ImageView) itemView.findViewById(R.id.addmedicalReport);
        Spinner spinnerGender = itemView.findViewById(R.id.spinnerGender);
        Spinner spinnerEmpType = itemView.findViewById(R.id.spinnerEmpType);
        Spinner spinnerTreatment = itemView.findViewById(R.id.spinnerTreatment);
        gv = (GridView) itemView.findViewById(R.id.gv);
        action_layout = itemView.findViewById(R.id.action_layout);

        add_actions = itemView.findViewById(R.id.add_actions);
        actions_listview = itemView.findViewById(R.id.actions_listview);
        action_description = itemView.findViewById(R.id.action_description);
        action_remarks = itemView.findViewById(R.id.action_remarks);
        action_name = itemView.findViewById(R.id.action_name);
        count_text = itemView.findViewById(R.id.count_text);

        TextInputLayout inputlayout_time = itemView.findViewById(R.id.inputlayout_time);

        person.setText("Person " + (i + 1));
        Log.d("EntityResponse", result + "");

        person.setTypeface(Utilities.fontBold(context));
        edit_name.setTypeface(Utilities.fontRegular(context));
        //  edit_employee_description.setTypeface(Utilities.fontRegular(context));
        edit_age.setTypeface(Utilities.fontRegular(context));


        edit_incident_time.setTypeface(Utilities.fontRegular(context));

        textTwo.setTypeface(Utilities.fontRegular(context));
        descriptionHeader.setTypeface(Utilities.fontBold(context));
        attachHead.setTypeface(Utilities.fontBold(context));
        spinnerGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });
        action_remarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(action_remarks, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }
        });
        count_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(count_text, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }
        });
        action_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(action_name, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }
        });
        spinnerEmpType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });

        /*edit_employee_description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });*/

        spinnerTreatment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });


        if (strobsrId != null && strStatus.equalsIgnoreCase("Assigned")) {
            spinnerGender.setEnabled(true);
            spinnerEmpType.setEnabled(true);
            spinnerTreatment.setEnabled(true);
            edit_age.setFocusable(true);
            edit_incident_time.setFocusable(true);
            edit_incident_date.setEnabled(true);
            edit_incident_time.setEnabled(true);
            edit_incident_time.setFocusable(true);
            //edit_employee_description.setEnabled(true);
            edit_name.setFocusable(true);
            //edit_employee_description.setFocusable(true);
            edit_designation.setFocusable(true);
            deletePerson.setVisibility(View.GONE);
            addmedicalReport.setVisibility(View.VISIBLE);


        } else {
            spinnerGender.setEnabled(true);
            spinnerEmpType.setEnabled(true);
            spinnerTreatment.setEnabled(true);
            edit_age.setFocusable(true);
            edit_incident_time.setFocusable(true);
            edit_name.setFocusable(true);
            //edit_employee_description.setFocusable(true);
            edit_designation.setFocusable(true);
            deletePerson.setVisibility(View.VISIBLE);
            addmedicalReport.setVisibility(View.VISIBLE);
        }
        if (list.size() == 1) {
            if (i == 0) {
                deletePerson.setVisibility(View.GONE);
//                edit_name.requestFocus();
            }
        } else {
            deletePerson.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getDesignation() != null) {
            edit_designation.setText(list.get(i).getDesignation().toString().trim());
        }
        if (list.get(i).getName() != null) {
            edit_name.setText(list.get(i).getName().toString().trim());
        }
        if (list.get(i).getAge() != null) {
            edit_age.setText(list.get(i).getAge().toString().trim());
        }
        if (list.get(i).getInjuredDateTime() != null) {

            String[] separated = list.get(i).getInjuredDateTime().split("T");
            Log.d(">>>>>>>>>>>>>time", separated[1]);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

            Date data = null;
            try {
                data = sdf.parse(separated[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            edit_incident_date.setText(output.format(data));

            // edit_incident_time.setText(list.get(i).getInjuredDateTime());
            edit_incident_time.setText(separated[1].substring(0, separated[1].length() - 3));
        }
        if (list.get(i).getDescription() != null) {


            //  edit_employee_description.setText(list.get(i).getDescription().toString().trim());
        }
        genderarraylist = new ArrayList<>();

        genderarraylist.add("Select Gender *");
        employeetypearrylist = new ArrayList<>();
        employeetypearrylist.add("Select Employee Type *");
        treatmentarraylist = new ArrayList<>();
        treatmentarraylist.add("Select Treatment *");
        //injutytypearraylist = new ArrayList<>();
        //injutytypearraylist.add("Select Injury Type *");
        for (int j = 0; j < result.size(); j++) {

            if (result.get(j).getLookUpName().equalsIgnoreCase("gender")) {
                genderarraylist.add(result.get(j).getLookUpValue());
            }
            if (result.get(j).getLookUpName().equalsIgnoreCase("EmployeeType")) {
                employeetypearrylist.add(result.get(j).getLookUpValue());
            }
            if (result.get(j).getLookUpName().equalsIgnoreCase("Treatment")) {
                treatmentarraylist.add(result.get(j).getLookUpValue());
            }
           /* if (result.get(j).getLookUpName().equalsIgnoreCase("InjuryType")) {
                injutytypearraylist.add(result.get(j).getLookUpValue());
            }*/
        }

        InjuredDetailsModel injuredDetailsModel = list.get(i);
        ArrayAdapter incidentType = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, genderarraylist);
        spinnerGender.setAdapter(incidentType);
        for (int j = 0; j < genderarraylist.size(); j++) {

            if (injuredDetailsModel.getGender() != null && genderarraylist.get(j).equalsIgnoreCase(injuredDetailsModel.getGender())) {

                spinnerGender.setSelection(j);

            }
        }
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                list.get(i).setGender(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter empadapter = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, employeetypearrylist);
        spinnerEmpType.setAdapter(empadapter);

        for (int j = 0; j < employeetypearrylist.size(); j++) {

            if (injuredDetailsModel.getEmployeeType() != null && employeetypearrylist.get(j).equalsIgnoreCase(injuredDetailsModel.getEmployeeType())) {

                spinnerEmpType.setSelection(j);

            }
        }

        spinnerEmpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                list.get(i).setEmployeeType(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       /* ArrayAdapter injuryadapter = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, injutytypearraylist);
        edit_employee_description.setAdapter(injuryadapter);

        for (int j = 0; j < injutytypearraylist.size(); j++) {

            if (injuredDetailsModel.getDescription() != null && injutytypearraylist.get(j).equalsIgnoreCase(injuredDetailsModel.getDescription())) {

                edit_employee_description.setSelection(j);

            }
        }


        edit_employee_description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                list.get(i).setDescription(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        ArrayAdapter treatmentadapter = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, treatmentarraylist);
        spinnerTreatment.setAdapter(treatmentadapter);


        for (int j = 0; j < treatmentarraylist.size(); j++) {

            if (injuredDetailsModel.getTreatment() != null && treatmentarraylist.get(j).equalsIgnoreCase(injuredDetailsModel.getTreatment())) {

                spinnerTreatment.setSelection(j);

            }
        }

        spinnerTreatment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                list.get(i).setTreatment(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addmedicalReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) viewGroup).performItemClick(view, i, 0);
            }
        });

        InjuredDetailsModel ObservationItemsDetailsModel = list.get(i);
        if (ObservationItemsDetailsModel.getObservationAttachmentModels() != null) {
            InjuridGalleryAdapter galleryAdapter = new InjuridGalleryAdapter(context, ObservationItemsDetailsModel.getObservationAttachmentModels(),strobjId);
            gv.setAdapter(galleryAdapter);
            gv.setVerticalSpacing(gv.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
            mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);

            setGridViewHeightBasedOnChildren(gv, 5);
        }
//        countEdit.setText(list.get(i).getDescription());
        //  edit_employee_description.setSelection(edit_employee_description.getText().length());

        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list.get(i).setName(editable.toString().trim());

            }
        });
        edit_designation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list.get(i).setDesignation(editable.toString().trim());
            }
        });
        edit_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.charAt(0) == 0) {
//                        Utilities.showAlertDialog("Please enter valid age", context);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (Integer.parseInt(editable.toString()) >= 18) {


//                } else {
//                    Utilities.showAlertDialog("Please enter valid age", context);
//                }
                try {
                    if (!editable.toString().equalsIgnoreCase("")) {
                        list.get(i).setAge(Integer.parseInt(editable.toString()));
                    }

                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
//        if (list.get(i).getAge() >= 18) {
//
//        }else {
//            Utilities.showAlertDialog("Please enter valid age", context);
//        }
//        edit_employee_description.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                list.get(i).setDescription(editable.toString().trim());
//            }
//        });

//        edit_incident_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar calendar = Calendar.getInstance();
//
//                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//                String formattedDate = df.format(calendar.getTime());
//                String parts[] = formattedDate.split(" ");
//                String part1 = parts[0]; // 004
//                final String part2 = parts[1];
//                final Dialog timepickerdialog = new Dialog(context, R.style.MyDialogTheme);
//
//                timepickerdialog.setContentView(R.layout.timepicker_dialog);
//                timepickerdialog.setCanceledOnTouchOutside(false);
//                timepickerdialog.show();
//
//                final TimePicker timePicker = timepickerdialog.findViewById(R.id.timepicker_view);
//                TextView cancel = timepickerdialog.findViewById(R.id.cancel);
//                TextView done = timepickerdialog.findViewById(R.id.done);
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        timepickerdialog.dismiss();
//                    }
//                });
//
//
//                done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        int hour, minute;
//
//                        //String am_pm;
//                        if (Build.VERSION.SDK_INT >= 23) {
//                            hour = timePicker.getHour();
//                            minute = timePicker.getMinute();
//                        } else {
//                            hour = timePicker.getCurrentHour();
//                            minute = timePicker.getCurrentMinute();
//                        }
//                        Log.d("Hours " + hour, "Minute" + minute);
//
//                        Date selectedDate = null;
//                        try {
//                            selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + " " + hour + ":" + minute + ":00");
//                        } catch (ParseException p) {
//                            p.getMessage();
//                        }
//                        Log.d("Date Time", selectedDate + "");
//
//                       /* if (selectedDate.before(new Date())) {
//                            edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
//                        } else {
//
//                            edit_incident_time.setText(part2);
//                        }*/
//                        if (selectedDate.before(new Date())) {
//                            edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
//                            notifyDataSetChanged();
//                        } else {
////                            edit_incident_time.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
//                            edit_incident_time.setText(part1 + " " + part2 + ":00");
//                        }
//                        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                        list.get(i).setInjuredDateTime(currentDate + " " + edit_incident_time.getText().toString() + ":00");
//                        timepickerdialog.dismiss();
//                    }
//                });
//            }
//        });

        deletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                alertDialog.setMessage("Are you sure, Do you want to delete the Person ?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        if (objItem.getId() != null) {
//                            deleteObservationItem(position);
//                        } else {
                        list.remove(i);
                        notifyDataSetChanged();
//                        }
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.show();
//
            }
        });
        return itemView;
    }


    private void setGridViewHeightBasedOnChildren(GridView gv, int i) {
        ListAdapter listAdapter = gv.getAdapter();
        if (listAdapter == null) {
            // pre-condition
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


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
