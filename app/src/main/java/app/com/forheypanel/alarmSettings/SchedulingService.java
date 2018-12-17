package app.com.forheypanel.alarmSettings;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;


import java.util.Calendar;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nayram on 5/8/15.
 */
public class SchedulingService extends IntentService {

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    Calendar calendar;
    SharedPreferences mpref;
    String email="";

    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Intent intent1=new Intent(getApplicationContext(), AlarmScreen.class);

        mpref=getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor=mpref.edit();
        mEditor.putBoolean("check_in",false);
        mEditor.commit();


        AlarmReceiver.completeWakefulIntent(intent);

    }

}
