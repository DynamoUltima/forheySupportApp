package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;

import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 11/21/15.
 */
public class
ActivityCheckin extends BaseActivity {

    @Bind(R.id.btnCheckin)
    ImageButton btnCheckin;
    String email;
     ProgressDialog progressDialog;
    Calendar calendar;
    SharedPreferences preferences;
    String TAG=getClass().getName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hey_options);
        ButterKnife.bind(this);
         preferences=getSharedPreferences("Credentials", Context.MODE_PRIVATE);

         email=preferences.getString("email", "");
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");

        calendar=Calendar.getInstance();
        preferences=getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        final boolean check_in=preferences.getBoolean("check_in",false);


        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//                new LoadCheck_inTask().execute();
                    if (!check_in) {
                        if (calendar.get(Calendar.HOUR)<19) {
                            App.supportService.check_in("hey_girl_check_in",email).enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    Log.d(TAG,response.toString());
                                    if (response.body().success == 1) {
                                        SharedPreferences.Editor mEditor=preferences.edit();
                                        mEditor.putBoolean("check_in",true);
                                        mEditor.commit();
                                        Intent intent = new Intent(ActivityCheckin.this, ActivityHome.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ActivityCheckin.this, "Sorry, Check in failed. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    Log.d(TAG,t.toString());
                                    Toast.makeText(ActivityCheckin.this, "Sorry, Check in failed. Please try again", Toast.LENGTH_SHORT).show();

                                }
                            });


                        }else{
                            Toast.makeText(ActivityCheckin.this,"Sorry, you can't check in after 6:59PM",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ActivityCheckin.this,"Sorry, you have already checked in",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ActivityCheckin.this,"Sorry, you can't check in on sundays",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
