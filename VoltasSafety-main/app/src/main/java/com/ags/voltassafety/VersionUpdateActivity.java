package com.ags.voltassafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.ags.voltassafety.model.ObservationResponse;
import com.ags.voltassafety.model.VersionUpdateModel;
import com.ags.voltassafety.model.Verticleresponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionUpdateActivity extends AppCompatActivity {
    private AppCompatButton btn_download_upgrade;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertdialog;

    String versionCodeOne;
    private TextView textView;

    int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    static String strFileName = "";
    String mStrFilePath = "";
//    LoginService mLoginService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_update);
        btn_download_upgrade = findViewById(R.id.btn_download_upgrade);
        textView = findViewById(R.id.textView);
        textView.setTypeface(Utilities.fontBold(VersionUpdateActivity.this));
        btn_download_upgrade.setTypeface(Utilities.fontBold(VersionUpdateActivity.this));

        btn_download_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isConnectingToInternet(VersionUpdateActivity.this)) {
                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
                        // Configure the notification channel.
                        notificationChannel.setDescription("Voltas Safety Download");
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    notificationBuilder = new NotificationCompat.Builder(VersionUpdateActivity.this, NOTIFICATION_CHANNEL_ID);
                    notificationBuilder.setContentTitle("Voltas Safety Download")
                            .setContentText("Download in progress")
                            .setSmallIcon(R.drawable.ic_file_download);
                    notificationBuilder.setProgress(100, 0, true);
                    notificationManager.notify(id, notificationBuilder.build());

                    progressDialog = new ProgressDialog(VersionUpdateActivity.this, R.style.MyDialogTheme);
                    progressDialog.setMessage("Downloading app. Please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    downloadLatestApp();


                } else {
                    alertdialog = new AlertDialog.Builder(VersionUpdateActivity.this, R.style.MyDialogTheme);
//                    alertdialog.setTitle("Information");
                    alertdialog.setMessage("Please check your internet connection");
                    alertdialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertdialog.show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        // progress = null;
    }

    private void downloadLatestApp() {
        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = pInfo.versionCode;
            versionCodeOne = String.valueOf(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
        ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
        Call<VersionUpdateModel> call = apiInterface.getUpdateAppVersion(versionCodeOne, "Android");
        call.enqueue(new Callback<VersionUpdateModel>() {
            @Override
            public void onResponse(Call<VersionUpdateModel> call, Response<VersionUpdateModel> response) {


                if (response.isSuccessful()) {
                    try {
//                        progressDialog.dismiss();
                        if (response.body().getResult() != null) {
                            mStrFilePath = response.body().getResult();
                            strFileName = response.body().getResult().split("/")[4];
                            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                            apiInterface.downloadFileWithDynamicUrl(mStrFilePath).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.isSuccessful()) {
                                        try {
                                            byte data[] = new byte[1024];
                                            int bufferLength;
                                            long lengthOfFile = response.body().contentLength();
                                            String dirPath = Environment.getExternalStorageDirectory() + "/Download/"; // getApplicationContext().getFilesDir().getPath();//
                                            InputStream inputStream = new BufferedInputStream(response.body().byteStream(), 1024 * 8);
                                            File f = new File(dirPath);
                                            if (!f.isDirectory()) {
                                                f.mkdir();
                                            }
                                            try {
                                                //create a new file, to save the downloaded file
                                                File file = new File(dirPath, strFileName);
                                                FileOutputStream fileOutput = new FileOutputStream(file);
                                                long downloadedSize = 0;
                                                int intProgress;
                                                while ((bufferLength = inputStream.read(data)) != -1) {

                                                    downloadedSize += bufferLength;
                                                    intProgress = (int) ((downloadedSize * 100) / lengthOfFile);
                                                    notificationBuilder.setProgress(100, intProgress, false);
                                                    //progressText.setText(intProgress + "");
                                                    fileOutput.write(data, 0, bufferLength);
                                                }
                                                fileOutput.flush();
                                                fileOutput.close();
                                                inputStream.close();

                                                if (progressDialog != null && progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }

                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                                                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                PendingIntent pIntent = PendingIntent.getActivity(VersionUpdateActivity.this, 0, intent, 0);

                                                notificationBuilder.setContentTitle(strFileName);
                                                notificationBuilder.setContentText("File downloaded, Click here to install ");
                                                // Removes the progress bar
                                                notificationBuilder.setProgress(0, 0, false);
                                                notificationBuilder.setContentIntent(pIntent);
                                                notificationBuilder.setSmallIcon(R.drawable.ic_file_download);
                                                notificationBuilder.setAutoCancel(true);
                                                //notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                                notificationManager.notify(id, notificationBuilder.build());


                                                Uri Uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                                                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                                                intent1.setDataAndType(Uri, "application/vnd.android.package-archive");
                                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                startActivity(intent1);
                                                //}
                                                finish();
                                            } catch (IOException e) {
                                                e.getMessage();
                                            }

                                        } catch (Exception e) {
                                            if (progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                            e.getMessage();
                                        }

                                    } else {
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        int statusCode = response.code();

                                        notificationBuilder.setContentTitle("Dowload Error");
                                        notificationBuilder.setContentText("Download Failed, Please try again to download");
                                        notificationBuilder.setProgress(0, 0, false);
                                        notificationBuilder.setAutoCancel(true);
                                        notificationManager.notify(id, notificationBuilder.build());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    //showErrorMessage();
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                Utilities.showAlertDialog(response.body().getErrors()[0], VersionUpdateActivity.this);
                            }

                        }
                    } catch (Exception e) {
                        e.getMessage();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        Utilities.showAlertDialog(response.message() + "", VersionUpdateActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<VersionUpdateModel> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
//                    Utilities.showAlertDialog(response.code() + "", VersionUpdateActivity.this);
                }
            }
        });
    }

}
