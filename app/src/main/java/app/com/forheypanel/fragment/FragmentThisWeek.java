package app.com.forheypanel.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import app.com.forheypanel.R;
import app.com.forheypanel.adapter.ScheduleRecycView;
import app.com.forheypanel.model.Options;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.WeekComparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by nayram on 11/20/15.
 */
public class FragmentThisWeek extends Fragment {

    RecyclerView recyclerView;
    String TAG=getClass().getName();
    ArrayList<Options> arrayThisWeek=new ArrayList<Options>();
    ArrayList<Options>arrayNextWeek=new ArrayList<Options>();
    ArrayList<String>listthisWeek=new ArrayList<String>();
    ArrayList<String>listNextWeek=new ArrayList<String>();
    Calendar calendar;
    WeekComparator wc;
    ProgressWheel progressWheel;
    String email;
    String error_msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.frame_layout,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        wc=new WeekComparator();
        calendar=Calendar.getInstance();
        progressWheel=(ProgressWheel)rootView.findViewById(R.id.progress_wheel);
        SharedPreferences mpref=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        if (getArguments()==null)
        email=mpref.getString("email","");
        else
            email=getArguments().getString("email","");
        progressWheel.setVisibility(View.VISIBLE);
        App.supportService.getOrdersByAssigned("getOrdersbyAssigned",email).enqueue(new Callback<Order.OrderList>() {
            @Override
            public void onResponse(Call<Order.OrderList> call, Response<Order.OrderList> response) {
                progressWheel.setVisibility(View.GONE);
                int week = 0;
                int woy = -1;

                if (response.body() !=null)
                if(response.body().success==1) {
                    Collections.sort(response.body().order_list, new WeekComparator());
                    for (Options options : response.body().order_list) {
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            d = sdf.parse(options.pickup_date);

                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        if (woy != getWeekOfYear(d)) {
                            woy = getWeekOfYear(d);
                            week++;
                            System.out.println("Week of year " + woy);
                            System.out.println("Week " + week + ":");
                        }


                        if (getWeekOfYear(d) == calendar.get(Calendar.WEEK_OF_YEAR)) {
                            arrayThisWeek.add(options);
                            if (!verifyThisWeek(options.pickup_date)) {
                                Log.e(TAG, "Date " + options.pickup_date);
                                listthisWeek.add(options.pickup_date);
                            }


                        } else if ((calendar.get(Calendar.WEEK_OF_YEAR) - getWeekOfYear(d)) == 1) {
                            arrayNextWeek.add(options);
                            if (!verifyNextWeek(options.pickup_date)) {
                                listNextWeek.add(options.pickup_date);
                            }

                        }
                        System.out.println(options.pickup_date + " name " + options.client_name);
                    }
                    recyclerView.setAdapter(new ScheduleRecycView(listthisWeek,getActivity(),email));
                }else{
                    error_msg=response.body().message;
                    Toast.makeText(getActivity(),error_msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order.OrderList> call, Throwable t) {
                progressWheel.setVisibility(View.GONE);
                if (getActivity()!=null)
                    Toast.makeText(getActivity(),"Sorry, failed to load orders",Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }


    boolean  verifyThisWeek(String date){
        for (int i=0;i<listthisWeek.size();i++){
            if (listthisWeek.get(i).contains(date)){
                return true;
            }
        }
        return false;
    }

    boolean verifyNextWeek(String date){
        for (int i=0;i<listNextWeek.size();i++){
            if (listNextWeek.get(i).contains(date)){
                return true;
            }
        }
        return false;
    }
    protected static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
