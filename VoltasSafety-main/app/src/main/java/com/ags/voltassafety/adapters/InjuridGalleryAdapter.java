package com.ags.voltassafety.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.AttachmentDeleteResponse;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class InjuridGalleryAdapter extends BaseAdapter {
    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    List<ObservationAttachmentModel> mArrayUri;
    Bitmap ImageBit;
    float ImageRadius = 40.0f;
    boolean imageTouch = false;
    boolean click1 = true;

    ProgressDialog progressDialog;
    String observationHeaderId;
    public InjuridGalleryAdapter(Context ctx, List<ObservationAttachmentModel> mArrayUri,String observationHeaderId) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
        this.observationHeaderId=observationHeaderId;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.injury_gallery_gv_item, parent, false);

        ImageView remove_image=itemView.findViewById(R.id.remove_image);
        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);
        LinearLayout images_view1 = itemView.findViewById(R.id.images_view1);
        ImageBit = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher_background);
        ivGallery.setImageBitmap(ImageBit);

        ivGallery.setImageResource(R.drawable.iitem_add);

        if(observationHeaderId!=null){
            remove_image.setVisibility(View.VISIBLE);
        }else{
            remove_image.setVisibility(View.VISIBLE);
        }

        remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(ctx,mArrayUri.get(position).getId()+"",Toast.LENGTH_SHORT).show();


                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ctx, R.style.MyDialogTheme);
                alertDialog.setMessage("Are you sure, Do you want to delete the Attachment ?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        int imageId = mArrayUri.get(position).getId();
                        try {
                            SharedPreferences sharedPreferences = ctx.getSharedPreferences("Bearer", MODE_PRIVATE);
                            Log.d("Value", sharedPreferences.getString("Bearertoken", null));
                            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                            Call<AttachmentDeleteResponse> call = apiInterface.deleteattachment("Bearer " + sharedPreferences.getString("Bearertoken", null), imageId);
                            progressDialog = new ProgressDialog(ctx, R.style.MyDialogTheme);
                            progressDialog.setMessage("Data loading");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);
                            call.enqueue(new Callback<AttachmentDeleteResponse>() {
                                public void onResponse(Call<AttachmentDeleteResponse> call, Response<AttachmentDeleteResponse> response) {

                                    if (response.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Log.d("", response.toString());



                                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx, R.style.MyDialogTheme);
                                        builder.setTitle(ctx.getResources().getString(R.string.information))
                                                .setMessage("Successfully removed")
                                                .setCancelable(false)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        mArrayUri.remove(mArrayUri.get(position));
                                                        notifyDataSetChanged();
                                                    }
                                                }).create().show();


                                    } else {
                                        progressDialog.dismiss();
                                    }

                                }


                                public void onFailure(Call<AttachmentDeleteResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Log.d("AttachmentResponse", t.getMessage() + "");
                                }


                            });
                        } catch (
                                Exception e) {
                            e.getMessage();
                            progressDialog.dismiss();

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

        if (mArrayUri.size() > 0 && mArrayUri.get(position).getUrl() != null) {
            String url = mArrayUri.get(position).getUrl();
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            String extension = url.substring(url.lastIndexOf("."));
            Log.d("fileName", extension);

            if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg")) {
                ivGallery.setImageResource(R.mipmap.ic_attachment);
            } else if (extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".mp3")) {
                ivGallery.setImageResource(R.drawable.ic_video);
            } else {
                ivGallery.setImageResource(R.drawable.ic_pdf);
            }
        }
        return itemView;
    }


}
