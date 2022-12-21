package com.ags.voltassafety.utility;

;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ags.voltassafety.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {

    public static AlertDialog.Builder alertDialog;

    /**
     * Checking for all possible internet providers
     **/
    public static boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //NetworkInfo networkInfo;
            //for (Network mNetwork : networks) {
            //networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()== true ){
                // if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                return true;
                // }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Date convertStringToDate(String startDateString) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = null;
        try {
            startDate = df.parse(startDateString);
            String newDateString = df.format(startDate);
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static boolean isValidPhoneNumber(String phoneNo) {

        //String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";

        String expression = "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNo);

        return matcher.matches();
    }

    public static boolean isValidEmail(String strEmail) {

        String[] emails;
        emails = strEmail.toString().trim().split(";");
        for (int i = 0; i < emails.length; i++) {
            //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            String email = emails[i];
            if (!email.matches(emailPattern)) {
                //etCommonObjEmail.setError("Invalid email address");
                return false;
            }
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



//    public static Typeface fontRegular(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
//        return tf;
//    }
//
//    public static Typeface fontBold(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
//        return tf;
//    }
//
//    public static Typeface fontBoldItalic(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-BoldItalic.ttf");
//        return tf;
//    }
//
//    public static Typeface fontItalic(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Italic.ttf");
//        return tf;
//    }

    public static Typeface fontRegular(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
        return tf;
    }

    public static Typeface fontBold(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
        return tf;
    }

    public static Typeface fontblack(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Black.ttf");
        return tf;
    }

//    public static Typeface fontmedium(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/avenirnext_medium.otf");
//        return tf;
//    }
//
//    //avenir_book.otf
//    public static Typeface fontbook(Context context) {
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/avenir_book.otf");
//        return tf;
//    }

    public static String getStringFile(File file) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(file.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();

            encodedFile = output.toString();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return "Exception :" + e1.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception" + e.getMessage();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    public static void showAlertDialog(String message, Context context) {
        alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
//        alertDialog.setTitle("Information");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}








