package app.com.forheypanel.alarmSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by nayram on 5/7/15.
 */
public class SampleBootReceiver extends BroadcastReceiver{
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
           alarm.setAlarmMgr(context);
        }
    }

}
