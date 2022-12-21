package com.ags.voltassafety;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import androidx.core.content.FileProvider;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SupportActivityForEmployee extends Activity {
    private ProgressDialog progress;
    private Typeface tf;
    private Typeface tf1;
    private TextView tv_subject;
    private EditText et_subject;
    private Button btn_save;
    private TextView tv_content;
    private EditText et_content;
    private ActionBar actionBar;
    private ImageView btn_logout, img_manual;
    private TextView txt_title, toolbar_title;
    private AlertDialog.Builder alertDialog;
    private TextView title_feedbackform, tv_filename;
    private String strfilename;
    int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    //private TextView tv_versionmsg;
    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;
    String mStrFileName = "Voltas_Safety_User_Manual.pdf";
    String mStrURL = "https://vsmart.voltasworld.com/USERManual/SAFETY_User_Manual.pdf";
//    https://vsmart.voltasworld.com/SafetyMobileApps/Voltas_Safety_User_Manual.pdf
   // String engURL = "https://vsmart.voltasworld.com/USERManual/Engineer_User_Manual.pdf";
    File file;
    ImageView back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_support);
        tv_filename = (TextView) findViewById(R.id.tv_file);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        img_manual = (ImageView) findViewById(R.id.img_manual);
        back = findViewById(R.id.back);

        sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String roleId = sharedPreferences.getString("roleId", null);


       /* title_feedbackform = (TextView) findViewById(R.id.title_feedbackform);
        tv_subject = (TextView) findViewById(R.id.tv_subject);
        et_subject = (EditText) findViewById(R.id.et_subject);
        tv_content = (TextView) findViewById(R.id.tv_content);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_save = (Button) findViewById(R.id.btn_save);*/


        tv_filename.setText("Please click to download the User Manual for this application");
//        Below code Is For Auto Scrolling
       /* tv_filename.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv_filename.setMarqueeRepeatLimit(10);
        tv_filename.setFocusable(true);
//        txt.setHorizontallyScrolling(true);
        tv_filename.setHorizontallyScrolling(true);
        tv_filename.setMarqueeRepeatLimit(-1);  //  At this point the view is not scrolling!

        tv_filename.setSelected(true);*/


//        title_feedbackform.setTypeface(Utilities.fontRegular(SupportActivity.this));
//        tv_subject.setTypeface(Utilities.fontRegular(SupportActivity.this));
//        tv_content.setTypeface(Utilities.fontRegular(SupportActivity.this));
        tv_filename.setTypeface(Utilities.fontRegular(SupportActivityForEmployee.this));
        toolbar_title.setTypeface(Utilities.fontBold(SupportActivityForEmployee.this));
//        et_subject.setTypeface(Utilities.fontRegular(SupportActivity.this));
//        et_content.setTypeface(Utilities.fontRegular(SupportActivity.this));
//        btn_save.setTypeface(Utilities.fontRegular(SupportActivity.this));
//        tv_filename.setTypeface(Utilities.fontRegular(SupportActivity.this));

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(SupportActivityForEmployee.this, HomeActivityForEmployee.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }
        });
     /*   btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (et_subject.getText().toString().trim().length() == 0) {
                    alertDialog = new AlertDialog.Builder(SupportActivity.this);
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Please enter subject");
                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                } else if (et_content.getText().toString().trim().length() == 0) {
                    alertDialog = new AlertDialog.Builder(SupportActivity.this);
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Please enter description");
                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                } else {
                    if (Utilities.isConnectingToInternet(getApplicationContext())) {

                    } else {
                        alertDialog = new AlertDialog.Builder(SupportActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage("Please check your internet connection");
                        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });*/
        img_manual.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                downloadFile(mStrURL);

//                if (roleId.equalsIgnoreCase("ROLE000001") || roleId.equalsIgnoreCase("ROLE000002")) {
//                    downloadFile(mStrURL);
//                } else {
//                    downloadFile(engURL);
//                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((progress != null) && progress.isShowing())
            progress.dismiss();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
    }

    public void downloadFile(String strFileURL) {
        if (Utilities.isConnectingToInternet(SupportActivityForEmployee.this)) {
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            // Set up progress before call
            final ProgressDialog progressDialog = new ProgressDialog(SupportActivityForEmployee.this, R.style.MyDialogTheme);
            progressDialog.setMessage("Please wait file downloading....");
            // progressDialog.setTitle("ProgressDialog");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            // show it
            progressDialog.show();
            apiInterface.downloadFileWithDynamicUrl(strFileURL).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        // close it after response
                        progressDialog.dismiss();
                        boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                        if (writtenToDisk) {
                            alertDialog = new AlertDialog.Builder(SupportActivityForEmployee.this, R.style.MyDialogTheme);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("File " + mStrFileName + " downloaded successfully");
                            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    downloadPDF();

                                }
                            });
                            alertDialog.show();
                        }

                        Log.d("MainActivity", "posts loaded from API");
                    } else {
                        int statusCode = response.code();
                        // close it after response
                        progressDialog.dismiss();
                        alertDialog = new AlertDialog.Builder(SupportActivityForEmployee.this, R.style.MyDialogTheme);
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(statusCode + ":" + response.message());
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        alertDialog.show();
                        // handle request errors depending on status code
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //showErrorMessage();
                    // close it after response
                    progressDialog.dismiss();
                    Log.d("MainActivity", "error loading from API");
                }
            });
        } else {
            alertDialog = new AlertDialog.Builder(SupportActivityForEmployee.this, R.style.MyDialogTheme);
            alertDialog.setTitle("Information");
            alertDialog.setMessage("Please check your internet connection");
            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            // File file=new File(Environment.getExternalStorageDirectory().getPath()+"/DRL/"+mStrFileName);
            // File file=new File(Environment.getRootDirectory()+"/DRL/"+mStrFileName);
            //File file=new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.D)));
            String strPath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
            File dirPath = new File(strPath);
            if (!dirPath.isDirectory()) {
                dirPath.mkdir();
            }
            file = new File(dirPath, "/" + mStrFileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public void downloadPDF() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + file);
            Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
            intent.setDataAndType(apkUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent1 = Intent.createChooser(intent, "Open PDF");
            startActivity(intent1);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
