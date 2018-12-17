package app.com.forheypanel.tools;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import app.com.forheypanel.service.SupportApiService;
import app.com.forheypanel.service.SupportService;
import io.fabric.sdk.android.Fabric;


/**
 * Created by nayram on 11/17/15.
 */
public class App extends MultiDexApplication {

    public static String TAG;
    public static Context context;

    public static App supportApp;
    public static SupportService supportService;

    public static SupportApiService supportApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this,new Crashlytics());
        context=getApplicationContext();
        supportApp=this;
//        OkHttpClient httpClient = new OkHttpClient();

        supportService = SupportService.retrofit.create(SupportService.class);

        supportApiService=SupportApiService.retrofit.create(SupportApiService.class);


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
