package com.ags.voltassafety.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;

import com.ags.voltassafety.CreateManpowerListActivity;
import com.ags.voltassafety.HazardActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.EntityResult;
import com.ags.voltassafety.model.External;
import com.ags.voltassafety.model.Internal;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.model.ObservationItemsDetailsModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ags.voltassafety.HazardActivity.strStatus;

public class ManPowerInternalAdapter extends BaseAdapter {
    Context context;
    List<EntityResult> entityResponses;
    ArrayList<String> internalType;
    ArrayList<String> internalTypeId;
    public AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;
    List<Internal> internalArrayList;
    private Spinner mSPType;
    private EditText mETTrainingSessions, mETStaff, mETLabours;
    private ImageView mBtnDelete;

    public ManPowerInternalAdapter(CreateManpowerListActivity hazard_activity, List<Internal> internalArrayList, List<EntityResult> entityResponseArrayList) {

        context = hazard_activity;
        this.internalArrayList = internalArrayList;
        entityResponses = entityResponseArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return internalArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return internalArrayList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {

        final Internal objItem = (Internal) getItem(position);
        View itemView = LayoutInflater.from(context).inflate(R.layout.manpower_internal_items, viewGroup, false);

        mSPType = itemView.findViewById(R.id.sp_type);
        mETTrainingSessions = itemView.findViewById(R.id.edit_no_of_training_sessions);
        mETStaff = itemView.findViewById(R.id.edit_staff);
        mETLabours = itemView.findViewById(R.id.edit_labors);
        mBtnDelete = itemView.findViewById(R.id.item_delete);

        mETTrainingSessions.setTypeface(Utilities.fontRegular(context));
        mETStaff.setTypeface(Utilities.fontRegular(context));
        mETLabours.setTypeface(Utilities.fontRegular(context));

        final Internal objInternal = internalArrayList.get(position);
        internalType = new ArrayList<>();
        internalTypeId = new ArrayList<>();
        if (internalArrayList.get(position).getType() != null) {
            internalType.add(internalArrayList.get(position).getType());
        } else {

            internalType.add("Select Type *");
        }
        if (internalArrayList.get(position).getNoOfTrainingSessions() != null) {

            mETTrainingSessions.setText(internalArrayList.get(position).getNoOfTrainingSessions() + "");
        } else {
            mETTrainingSessions.setText("");
        }
        if (internalArrayList.get(position).getStaff() != null) {
            mETStaff.setText(internalArrayList.get(position).getStaff() + "");
        } else {
            mETStaff.setText("");
        }
        if (internalArrayList.get(position).getLabour() != null) {
            mETLabours.setText(internalArrayList.get(position).getLabour() + "");
        } else {
            mETLabours.setText("");
        }


        for (int i = 0; i < entityResponses.size(); i++) {

            if (entityResponses.get(i).getLookUpName().equalsIgnoreCase("Internal")) {
                internalType.add(entityResponses.get(i).getLookUpValue());
                internalTypeId.add(entityResponses.get(i).getLookUpId());
            }
        }

        ArrayAdapter hazardtype = new ArrayAdapter(context, R.layout.list_view_items, R.id.lbl_name, internalType);
        mSPType.setAdapter(hazardtype);
        int pos = 0;
        for (int i = 0; i < internalTypeId.size(); i++) {
            if (objInternal.getType().equalsIgnoreCase(internalTypeId.get(i)))
                pos = i;
        }
        mSPType.setSelection(pos);
//        mSPType.setSelection(hazardtype.getPosition(objInternal.getType()));

        mSPType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return false;
            }
        });
        mSPType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                internalArrayList.get(position).setType((String) adapterView.getItemAtPosition(i));
                internalArrayList.get(position).setType(internalTypeId.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mETTrainingSessions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                internalArrayList.get(position).setNoOfTrainingSessions(Integer.parseInt(editable.toString().trim()));
            }
        });
        mETStaff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                internalArrayList.get(position).setStaff(Integer.parseInt(editable.toString().trim()));
            }
        });
        mETLabours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                internalArrayList.get(position).setLabour(Integer.parseInt(editable.toString().trim()));
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                alertDialog.setMessage("Are you sure, Do you want to delete the item ?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (objItem.getId() != null) {
                            //deleteObservationItem(position, objItem.getId());
                        } else {
                            internalArrayList.remove(position);
                            notifyDataSetChanged();
                        }
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

    public void deleteObservationItem(final int id, int itemId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Bearer", MODE_PRIVATE);
        ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
        progressDialog.setMessage("Data loading");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
//        Gson gson = new Gson();
//        String json = gson.toJson(observationHeader);
//        Log.d("jsonRequest", json);
        Call<CreateResponse> call = apiInterface.deleteObservationItem("Bearer " + sharedPreferences.getString("Bearertoken", null), itemId);
        progressDialog.show();

        call.enqueue(new Callback<CreateResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getSuccess()) {
                        if (response.body().getResult() != null) {

                            CreateResponse loginResponse = response.body();
                            Log.d("observationresponse", response.body().getResult() + "");
                            progressDialog.dismiss();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            builder.setTitle(context.getResources().getString(R.string.information))
                                    .setMessage("Item Deleted Successfully ")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            internalArrayList.remove(id);
                                            notifyDataSetChanged();
                                        }
                                    }).create().show();
//
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
                                .setMessage(response.body().getErrors()[0])
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


            public void onFailure(Call<CreateResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("LoginResponse", t.getMessage() + "");
            }

        });

    }

}

