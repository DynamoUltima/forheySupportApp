package app.com.forheypanel.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nayram on 2/26/15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    SharedPreferences mprefs;
//    String regid;
    @Override
    public void onReceive(Context context, Intent intent) {
        mprefs=context.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
       String regid=intent.getExtras().getString("registration_id");
        if (regid!=null && !regid.equals("")){

        }
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }


}
