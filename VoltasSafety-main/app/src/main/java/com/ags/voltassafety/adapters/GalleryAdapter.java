package com.ags.voltassafety.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;


import com.ags.voltassafety.CreateObservationActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.model.AttachmentDeleteResponse;
import com.ags.voltassafety.model.ObservationAttachmentModel;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class GalleryAdapter extends BaseAdapter {
    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    List<ObservationAttachmentModel> mArrayUri;
    Bitmap ImageBit;
    float ImageRadius = 40.0f;
    boolean imageTouch = false;
    boolean click1 = true;
    String strObservationID;

    ProgressDialog progressDialog;
    ObservationAttachmentModel observationAttachmentModel;

    String observationIdStr;

    public GalleryAdapter(Context ctx, List<ObservationAttachmentModel> mArrayUri,String observationIdStr) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
        this.observationIdStr=observationIdStr;

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

        View itemView = inflater.inflate(R.layout.gv_item, parent, false);

        ImageView remove_image = itemView.findViewById(R.id.remove_image);
        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);
        LinearLayout images_view1 = itemView.findViewById(R.id.images_view1);
        ImageBit = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher_background);
        ivGallery.setImageBitmap(ImageBit);

        ivGallery.setImageResource(R.drawable.iitem_add);
        if(observationIdStr!=null){
            remove_image.setVisibility(View.VISIBLE);
        }else{
            remove_image.setVisibility(View.VISIBLE);
        }

        remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //    Toast.makeText(ctx,mArrayUri.get(position).getId()+"",Toast.LENGTH_SHORT).show();


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


        if (mArrayUri.size() > 0 && mArrayUri.get(position).

                getUrl() != null) {
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
            ivGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mArrayUri.get(position).getUrl();
                    String fileName = url.substring(url.lastIndexOf('/') + 1);
                    String extension = url.substring(url.lastIndexOf("."));
                    Log.d("fileName", extension);

                    if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg")) {

                        final Dialog imagedialog = new Dialog(ctx, R.style.ImageDialogTheme);
                        imagedialog.setContentView(R.layout.zoom_image_dialog);
                        imagedialog.show();
                        ImageView zoom_image = imagedialog.findViewById(R.id.zoom_image);
                        ImageView cancel_image = imagedialog.findViewById(R.id.cancel_image);
                        imagedialog.setCanceledOnTouchOutside(false);
                        Glide.with(ctx).load(mArrayUri.get(position).getUrl()).into(zoom_image);
                        //  Glide.with(context).load(R.drawable.login_logo).into(zoom_image);
                        cancel_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imagedialog.dismiss();
                            }
                        });

                    } else if (extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".mp3")) {

                        final Dialog videodialog = new Dialog(ctx);
                        videodialog.setContentView(R.layout.video_layout);
                        videodialog.show();
                        VideoView video_view = videodialog.findViewById(R.id.video_view);

                        video_view.setVideoPath(mArrayUri.get(position).getUrl());

                        video_view.start();

                    } else {
                        new DownloadTask().execute(mArrayUri.get(position).getUrl());
                    }
                }
            });
        }


        return itemView;
    }

    public class DownloadTask extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(ctx);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Voltas Safety/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            String path = message;
// it contains your image path...I'm using a temp string...
            String filename = path.substring(path.lastIndexOf("/") + 1);

            // Display File path after downloading
            Log.d("Message", filename);
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.MyDialogTheme);
            builder.setTitle(ctx.getResources().getString(R.string.information))
                    .setMessage("You can see your file at Voltas Safety Folder " + filename)
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }

                    }).create().show();
//            Toast.makeText(context,
//                    message, Toast.LENGTH_LONG).show();
        }
    }
}
