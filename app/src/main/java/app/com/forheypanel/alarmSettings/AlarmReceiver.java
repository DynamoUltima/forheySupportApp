package app.com.forheypanel.alarmSettings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;


import java.util.Calendar;

/**
 * Created by nayram on 5/6/15.
 */
public class AlarmReceiver  extends WakefulBroadcastReceiver{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private static String dayWeek="";
    private static int hour=19,minute=00;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service=new Intent(context,SchedulingService.class);

        startWakefulService(context,service);

    }

    public void setAlarmMgr(Context context){
        alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReceiver.class);
        alarmIntent=PendingIntent.getBroadcast(context,0,intent,0);

        SharedPreferences mpref= context.getSharedPreferences("Credentials",Context.MODE_PRIVATE);

        //System.out.println("Alarm manager started");


        hour=00;
        minute=00;
        System.out.println("hour "+ hour);

        Calendar calendar = Calendar.getInstance();

        // calendar.set(Calendar.DAY_OF_WEEK,day);
        calendar.setTimeInMillis(System.currentTimeMillis());
        // calendar.set(Calendar.YEAR,2015);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);


        long r=AlarmManager.INTERVAL_DAY*7;
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


    }

    private static int getDayEquivalent(String param){


        int result=1;
        switch(param){
            case "Monday":
                result=Calendar.MONDAY;
                break;
            case "Tuesday":
                result=Calendar.TUESDAY;
                break;
            case "Wednessday":
                result=Calendar.WEDNESDAY;
                break;
            case "Thursday":
                result =Calendar.THURSDAY;
                break;
            case "Friday":
                result =Calendar.FRIDAY;
                break;
            case "Saturday":
                result=Calendar.SATURDAY;
                break;


        }
        return result;
    }
    public String showTime(int hour, int min) {
        String result;
        String format;
        String stMin=String.valueOf(min);
        if(stMin.length()==1){
            stMin="0"+min;
        }
        if (hour == 0) {
            hour += 12;
            format = "am";
        } else if (hour == 12) {
            format = "pm";
        } else if (hour > 12) {
            hour -= 12;
            format = "pm";
        } else {
            format = "am";
        }
        result= String.valueOf(new StringBuilder().append(hour).append(" : ").append(stMin).append(" ").append(format));
        return  result;
    }
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
