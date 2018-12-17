package app.com.forheypanel.tools;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.ActivityClient;
import app.com.forheypanel.activity.ActivityHeyGirl;
import app.com.forheypanel.activity.ActivityOrderDetail;
import app.com.forheypanel.activity.HomeClass;
import app.com.forheypanel.activity.HomeTabActivity;
import app.com.forheypanel.activity.NotificationList;
import app.com.forheypanel.activity.SearchActivity;
import app.com.forheypanel.database.LocalDatabase;
import app.com.forheypanel.tools.GcmBroadcastReceiver;

/**
 * Created by nayram on 2/26/15.
 */
public class GcmIntentService extends IntentService {
    LocalDatabase db;
    SharedPreferences mprefs;
    String userEmail;
    String TAG=getClass().getName();
    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        db=new LocalDatabase(getApplicationContext());
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        mprefs=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        userEmail=mprefs.getString("email","Default");
        Notification noti = new Notification();
        NotificationManager mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;


        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());
                String role=Helper.getRole(this);
                switch (extras.getString("status")){
                    case "heygirl_checkin":
                        String heygirl=mprefs.getString("role","");
                        if (!heygirl.contains("heygirl")) {
                            db.open();
                            db.addHeyGirlCheckIn(extras.getString("status"), extras.getString("email"), extras.getString("name"), extras.getString("image"),
                                    extras.getString("title"), extras.getString("message"), userEmail);
                            db.close();


                            noti = forheyCheckIn(this, extras.getString("name"), extras.getString("image"), extras.getString("message"), extras.getString("email"), extras.getString("title"));
                        }
                        break;
                    case "new_signup":
                        if (!role.contains("heygirl")) {
                            db.open();
                            String uid=extras.getString("user_id");
                            Log.e(TAG,"First id "+uid);
                            int id=Integer.parseInt(uid);
                            Log.e(TAG,"ID "+id);
                            db.addNewSignUp(extras.getString("status"), extras.getString("title"), extras.getString("message"), id, userEmail);
                            db.close();
                            noti = signupNotification(extras.getString("message"), extras.getString("title"), id);
                        }
                        break;
                    case "cancel_order":
                        if (!role.contains("heygirl")) {
                            db.open();
                            if (!db.notificationExists(extras.getString("orderId")))
                                db.addNotification(extras.getString("status"), extras.getString("orderId"), extras.getString("message"), 0, userEmail, extras.getString("title"));
                            else {
                                db.updateNotification(extras.getString("orderId"), extras.getString("message"), 0, extras.getString("status"), extras.getString("title"));
                            }
                            System.out.println("Order id " + extras.getString("orderId"));
                            db.close();
                            noti = getBigTextStyleNotification(extras);
                        }
                        break;
                    case "update_order":
                        if (!role.contains("heygirl")) {
                            db.open();
                            if (!db.notificationExists(extras.getString("orderId")))
                                db.addNotification(extras.getString("status"), extras.getString("orderId"), extras.getString("message"), 0, userEmail, extras.getString("title"));
                            else {
                                db.updateNotification(extras.getString("orderId"), extras.getString("message"), 0, extras.getString("status"), extras.getString("title"));
                            }
                            db.close();
                            System.out.println("Order id " + extras.getString("orderId"));
                            noti = getBigTextStyleNotification(extras);
                        }
                         break;
                    case "new_order":
                        if (!role.contains("heygirl")) {
                            db.open();
                            if (!db.notificationExists(extras.getString("orderId")))
                                db.addNotification(extras.getString("status"), extras.getString("orderId"), extras.getString("message"), 0, userEmail, extras.getString("title"));
                            else {
                                db.updateNotification(extras.getString("orderId"), extras.getString("message"), 0, extras.getString("status"), extras.getString("title"));
                            }
                            System.out.println("Order id " + extras.getString("orderId"));
                            db.close();
                            noti = getBigTextStyleNotification(extras);
                        }
                        break;
                    case "assign_heygirl":
                        if (role.equals("heygirl")){
                            db.open();
                            if (!db.notificationExists(extras.getString("orderId")))
                                db.addNotification(extras.getString("status"), extras.getString("orderId"), extras.getString("message"), 0, userEmail, extras.getString("title"));
                            else {
                                db.updateNotification(extras.getString("orderId"), extras.getString("message"), 0, extras.getString("status"), extras.getString("title"));
                            }
                            db.close();

                            noti = getBigTextStyleNotification(extras);
                        }
                        break;
                    case "payment":
                        if (!role.equals("heygirl")){
                            db.open();
                            if (!db.notificationExists(extras.getString("orderId")))
                                db.addNotification(extras.getString("status"), extras.getString("orderId"), extras.getString("message"), 0, userEmail, extras.getString("title"));
                            else {
                                db.updateNotification(extras.getString("orderId"), extras.getString("message"), 0, extras.getString("status"), extras.getString("title"));
                            }
                            db.close();
                            noti=getBigTextStyleNotification(extras);
                        }
                        break;
                }

                noti.defaults |= Notification.DEFAULT_LIGHTS;
                noti.defaults |= Notification.DEFAULT_VIBRATE;
                noti.defaults |= Notification.DEFAULT_SOUND;

                noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

                mNotificationManager.notify(0, noti);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private static void generateNotification(Context context, String server_code) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        String mess="Order with id "+server_code+" has been cancelled";

        Notification notification = new Notification(icon, mess, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = new Intent(context, SearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("OrderId", server_code);
        bundle.putString("ClassName", "HomeClass");
        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, mess, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);

    }

    private static void generateRatingNotification(Context context,String rating,String server_code){
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        String mess="Order with id "+server_code+" has been rated "+rating+" star/s";

        Notification notification = new Notification(icon, mess, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = new Intent(context, SearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("OrderId", server_code);
        bundle.putString("ClassName", "HomeClass");
        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, mess, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);
    }

    private static void generateNewOrderNotification(Context context,String server_code){
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        String mess="A new order has been placed";

        Notification notification = new Notification(icon, mess, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = new Intent(context, SearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("OrderId", server_code);
        bundle.putString("ClassName", "HomeClass");
        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, mess, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(2, notification);

    }



    private static void generateUpdateOrderNotification(Context context,String server_code){
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        String mess="Order with order code "+server_code+" has been updated";

        Notification notification = new Notification(icon, mess, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = new Intent(context, SearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("OrderId", server_code);
        bundle.putString("ClassName", "HomeClass");
        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, mess, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(2, notification);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification getBigTextStyleNotification(Bundle extras) {

        // Create the style object with BigTextStyle subclass.
        NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle(extras.getString("title"));
        notiStyle.setSummaryText(extras.getString("title"));

        // Add the big text to the style.
        CharSequence bigText = extras.getString("message");
        notiStyle.bigText(bigText);

        // Creates an explicit intent for an ResultActivity to receive.
        String role=Helper.getRole(this);
        Intent notificationIntent;
        if (role.equalsIgnoreCase("heygirl")) {
           notificationIntent= new Intent(this, ActivityOrderDetail.class);
        }else{  notificationIntent = new Intent(this, SearchActivity.class);}
        Bundle bundle=new Bundle();
        bundle.putString("OrderId", extras.getString("orderId"));
        bundle.putString("ClassName", "HomeClass");
        bundle.putString("OrderType", "App");

        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);
        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeTabActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(extras.getString("title"))
                .setContentText(extras.getString("message"))
                .setStyle(notiStyle).build();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification getInboxStyleNotification(int count) {


        // Create the style object with InboxStyle subclass.
        NotificationCompat.InboxStyle notiStyle = new NotificationCompat.InboxStyle();
        notiStyle.setBigContentTitle(count + " Notifications");

        // Add the multiple lines to the style.
        // This is strictly for providing an example of multiple lines.
        notiStyle=db.getNotificationList(userEmail);
        notiStyle.setSummaryText(userEmail);



        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(this, NotificationList.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeClass.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(count + " Notifications")

                .setStyle(notiStyle).build();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification forheyCheckIn(Context context,String name,String image,String message,String email,String title){
        NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();

        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(message);

        // Bitmap icon=BitmapFactory.decodeStream(image)
        Bitmap bitmap = getBitmapFromURL(image);

        // Add the big text to the style.
        CharSequence bigText =message;
        notiStyle.bigText(bigText);

        // Creates an explicit intent for an ResultActivity to receive.
        Intent notificationIntent = new Intent(this, ActivityHeyGirl.class);
        Bundle bundle=new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("image", image);
//        bundle.putBoolean("history", true);
        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(ActivityHeyGirl.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (bitmap ==null){
            bitmap=largeIcon;
        }
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(notiStyle).build();
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            final int THUMBNAIL_SIZE = 96;
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();


            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap myBitmap = BitmapFactory.decodeStream(input, null, o2);
            myBitmap = Bitmap.createScaledBitmap(myBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification signupNotification(String msg,String title ,int  id) {


        // Create the style object with BigTextStyle subclass.
        NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(title);


        // Add the big text to the style.
        CharSequence bigText = msg;
        notiStyle.bigText(bigText);

        // Creates an explicit intent for an ResultActivity to receive.
        Intent notificationIntent = new Intent(this, ActivityClient.class);
        Bundle bundle=new Bundle();
//        bundle.putString("OrderId", server_code);
        bundle.putInt("client_id", id);
        //bundle.putString("OrderId",server_code);
        notificationIntent.putExtras(bundle);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(HomeClass.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setContentText(msg)
                .setStyle(notiStyle).build();
    }
}
