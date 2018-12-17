package app.com.forheypanel.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import app.com.forheypanel.activity.MainClass;

/**
 * Created by nayram on 11/30/15.
 */
public class Helper {

    public static String getRole(Context ctx){
        SharedPreferences preferences=ctx.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        return preferences.getString("role","");
    }

    public static void setLastView(Context ctx,String args){
        SharedPreferences preferences=ctx.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = preferences.edit();
        mEditor.putString("last_view",args);
        mEditor.commit();
    }

    public static String getLastView(Context ctx){
        SharedPreferences preferences=ctx.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        return preferences.getString("last_view","");
    }

    public static void setLogout(Context ctx){
        SharedPreferences preferences=ctx.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = preferences.edit();
        mEditor.putBoolean("isLoggedIn", false);
        mEditor.commit();

    }
}
