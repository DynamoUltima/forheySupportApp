package app.com.forheypanel.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import app.com.forheypanel.R;

import static android.content.ContentValues.TAG;

public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().isEmpty())
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        else
            showNotification(remoteMessage.getData());


    }

    private void showNotification(Map<String, String> data) {
        String title= data.get("title").toString();
        String body = data.get("body").toString();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.fcmcloudmessaging.test";



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.cast_ic_notification_on)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        notificationManager.notify(999, notificationBuilder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "app.com.forheypanel.service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Forhey Test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "app.com.forheypanel.service";



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.cast_ic_notification_on)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        notificationManager.notify(999, notificationBuilder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "app.com.forheypanel.service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Forhey Test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
//        FirebaseMessaging.getInstance().subscribeToTopic("general")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Sucessful";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
//
//                       // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }


//    @Override
//    public void onNewToken(String token) {
//
////        token = FirebaseInstanceId.getInstance().getToken();
//        Log.d("tokenizer", "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        //sendRegistrationToServer(token);
//    }

//    private void sendRegistrationToServer(String token) {
//        // Add custom implementation, as needed.
//        SharedPreferenceUtils.getInstance(this).setValue(getString(R.string.firebase_cloud_messaging_token), token);
//
//        // To implement: Only if user is registered, i.e. UserId is available in preference, update token on server.
//        int userId = SharedPreferenceUtils.getInstance(this).getIntValue(getString(R.string.user_id), 0);
//        if(userId != 0){
//            // Implement code to update registration token to server
//        }
 //   }


}
