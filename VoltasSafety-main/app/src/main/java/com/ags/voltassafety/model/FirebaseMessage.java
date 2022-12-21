package com.ags.voltassafety.model;

import android.app.ActivityManager;

import android.app.NotificationManager;

import android.app.PendingIntent;

import android.content.ComponentName;

import android.content.Context;

import android.content.Intent;

import android.os.Build;


import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ags.voltassafety.LoginActivity;
import com.ags.voltassafety.R;
import com.google.firebase.messaging.FirebaseMessagingService;

import com.google.firebase.messaging.RemoteMessage;


import java.util.List;


public class FirebaseMessage extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";


    @Override

    public void onMessageReceived(RemoteMessage remoteMessage) {

//Log data to Log Cat

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

//
        if (isAppIsInBackground(getApplicationContext())) {


        } else {

            NotificationCompat.Builder builder =

                    new NotificationCompat.Builder(this)

                            .setSmallIcon(R.mipmap.ic_back) //set icon for notification

                            .setContentTitle("Notifications Example") //set title of notification

                            .setContentText(remoteMessage.getNotification().getBody())//this is notification message

                            .setAutoCancel(true) // makes auto cancel of notification

                            .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


            Intent notificationIntent = new Intent(this, NotificationView.class);

//            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


//notification message will get at NotificationView

            notificationIntent.putExtra("message", remoteMessage.getNotification().getBody());


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,

                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);


// Add as notification

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            manager.notify(0, builder.build());

        }
//
    }


    private boolean isAppIsInBackground(Context context) {

        boolean isInBackground = true;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {

            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {

                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                    for (String activeProcess : processInfo.pkgList) {

                        if (activeProcess.equals(context.getPackageName())) {

                            isInBackground = false;

                        }

                    }

                }

            }

        } else {

            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

            ComponentName componentInfo = taskInfo.get(0).topActivity;

            if (componentInfo.getPackageName().equals(context.getPackageName())) {

                isInBackground = false;

            }

        }

// Log.d("Venkatesh", String.valueOf(isInBackground));

        return isInBackground;

    }

}
