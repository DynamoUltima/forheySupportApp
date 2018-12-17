package app.com.forheypanel.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.ActivityCheckin;
import app.com.forheypanel.activity.ActivityHome;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.CheckInCallback;
import me.drakeet.materialdialog.MaterialDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 11/25/15.
 */
public class FragmentCheckIn extends Fragment {

    String email;
    ProgressDialog progressDialog;
    Calendar calendar;
    SharedPreferences preferences;
    View dialogView;
    TextView tvTitle,tvMessage;
    MaterialDialog dialog;
    CheckInCallback callback;
    String TAG=getClass().getName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.layout_hey_options,container,false);
        ImageButton btnCheckin=(ImageButton)rootView.findViewById(R.id.btnCheckin);
        dialogView=inflater.inflate(R.layout.layout_dialog_box,container,false);
        tvTitle=(TextView)dialogView.findViewById(R.id.tvTitle);
        tvMessage=(TextView)dialogView.findViewById(R.id.tvMessage);

        dialog=new MaterialDialog(getActivity());
        dialog.setView(dialogView);
        dialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.setCheckInCallback();
                dialog.dismiss();
            }
        });

        preferences=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        email=preferences.getString("email", "");
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");

        calendar= Calendar.getInstance();
        preferences=getActivity().getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        final boolean check_in=preferences.getBoolean("check_in",false);

        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//                new LoadCheck_inTask().execute();
                    if (!check_in) {
                        progressDialog.show();
                        App.supportService.check_in("heygirl_check_in",email).enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                progressDialog.dismiss();
                                Log.d(TAG,response.toString());
                                if (response.body().success == 1) {
                                    SharedPreferences.Editor mEditor = preferences.edit();
                                    mEditor.putBoolean("check_in", true);
                                    mEditor.commit();
                                    tvTitle.setText("Check In Success");
                                    tvMessage.setText("You have successfully checked in.");
                                    dialog.show();
                                } else {
                                    Toast.makeText(getActivity(), "Sorry, Check in failed. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                t.printStackTrace();
                                progressDialog.dismiss();
                                Log.e(TAG,t.getMessage());
                                Toast.makeText(getActivity(), "Sorry, Check in failed. Please try again", Toast.LENGTH_SHORT).show();

                            }
                        });
                        /*App.supportService.check_in("heygirl_check_in", email, new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {

                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                 }
                        });*/



                    }else{
                        Toast.makeText(getActivity(),"Sorry, you have already checked in",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Sorry, you can't check in on sundays",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (CheckInCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSignUpSelectedListener");
        }


    }

}
