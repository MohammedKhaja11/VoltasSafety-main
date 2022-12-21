package com.ags.voltassafety.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ags.voltassafety.ObservationViewActivity;
import com.ags.voltassafety.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            Log.d("fcm", "From: " + remoteMessage.getFrom());
            if (remoteMessage == null)
                return;
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d("fcmnotification", "NotificationBody: " + remoteMessage.getNotification().getTitle());
                Log.d("fcmnotification", "NotificationBody: " + remoteMessage.getNotification().getBody());
                //handleNotification(remoteMessage.getNotification().getBody());

                super.onMessageReceived(remoteMessage);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.mipmap.app_icon)
                        .build();
                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
                manager.notify(123, notification);


/*//              String title = remoteMessage.getNotification().getTitle();
//                String message = remoteMessage.getNotification().getBody();

               // sendNotification(remoteMessage.getNotification().getBody());
//                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(this)
//                                .setSmallIcon(R.mipmap.app_icon).setSound(uri)
//                                .setContentTitle(remoteMessage.getNotification().getTitle())
//                                .setContentText(remoteMessage.getNotification().getBody());
//
//                Intent notificationIntent = new Intent(this, ObservationViewActivity.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//
//                // Add as notification
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());*/

            } else {

                Log.d("fcmnotification", "NotificationBody: Null ");
            }
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d("fcmbody", "DataPayload: " + remoteMessage.getData().toString());
                try {
                    JSONObject json = new JSONObject(remoteMessage.getData());
                    Log.d("sra1response", json.toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.d("sra1exception", "Exception: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception", e.getMessage().toString());
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e("sra1", "push json: " + json.toString());
        try {
            observationsNotifications(getApplicationContext(), json);

        } catch (Exception e) {

        }
    }

    private void observationsNotifications(Context context, JSONObject jsonObject) {

        Intent resultIntent;
        resultIntent = new Intent(this, ObservationViewActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Observation Notification" + jsonObject.getString("title"))
                    .setContentText("Name:" + jsonObject.getString("body"))//need to add this
                    .setSmallIcon(R.mipmap.app_icon);
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            mNotifyBuilder.setDefaults(defaults);
            // Set the content for Notification
            mNotifyBuilder.setContentText("observation:" + jsonObject.getString("body")); //need to add this
            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            Date now = new Date();
            long uniqueId = now.getTime();
            long time = new Date().getTime();
            String tmpStr = String.valueOf(time);
            String last4Str = tmpStr.substring(tmpStr.length() - 5);
            int notificationId = Integer.valueOf(last4Str);
            mNotificationManager.notify(notificationId, mNotifyBuilder.build());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, ObservationViewActivity.class).putExtra("msg", messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "idddd";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
