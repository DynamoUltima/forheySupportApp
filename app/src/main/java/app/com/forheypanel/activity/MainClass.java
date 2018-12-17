package app.com.forheypanel.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

import app.com.forheypanel.R;
import app.com.forheypanel.fragment.RegisterFragment;
import app.com.forheypanel.fragment.SignInClass;

/**
 * Created by nayram on 4/5/15.
 */
public class MainClass extends FragmentActivity implements SignInClass.OnSignUpSelected {

    @Override
    public void onSingup() {
        RegisterFragment signFrag=(RegisterFragment)getSupportFragmentManager().findFragmentById(R.id.signupfragment);
        RegisterFragment s=new RegisterFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,s);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.fragment_activity);
        if (android.os.Build.VERSION.SDK_INT== Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        if(findViewById(R.id.fragment_container)!=null){
            if(savedInstanceState != null){
                return;
            }
        }

        Calendar calendar=Calendar.getInstance();

        SignInClass signInClass=new SignInClass();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,signInClass).commit();
    }



}
