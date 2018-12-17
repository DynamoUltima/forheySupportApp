package app.com.forheypanel.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.ScheduleRecycView;
import app.com.forheypanel.model.Options;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.CheckInCallback;
import app.com.forheypanel.tools.Helper;
import app.com.forheypanel.tools.OrdersAdapter;
import app.com.forheypanel.tools.WeekComparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 11/21/15.
 */
public class FragmentOrders extends Fragment {

    RecyclerView recyclerView;
    String TAG=getClass().getName();
    ProgressWheel progressWheel;
    ArrayList<Options> arrayThisWeek=new ArrayList<Options>();
    ArrayList<Options>arrayNextWeek=new ArrayList<Options>();
    String error_msg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.frame_layout,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        progressWheel=(ProgressWheel)rootView.findViewById(R.id.progress_wheel);
        SharedPreferences mpref=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//        progressWheel.setVisibility(View.VISIBLE);
        String email=mpref.getString("email","");
        boolean check_in=mpref.getBoolean("check_in",false);

        if (check_in) {
            progressWheel.setVisibility(View.VISIBLE);
            App.supportService.getOrdersByAssigned("getOrdersbyAssigned",email).enqueue(new Callback<Order.OrderList>() {
                @Override
                public void onResponse(Call<Order.OrderList> call, Response<Order.OrderList> response) {
                    progressWheel.setVisibility(View.GONE);
                    if (response.body().success == 1) {
                        recyclerView.setAdapter(new OrdersAdapter(response.body().order_list, getActivity()));
                    } else if (response.body().error == 1) {
                        Toast.makeText(getActivity(), response.body().error_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Order.OrderList> call, Throwable t) {
                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Sorry, Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Helper.setLastView(getActivity(),"AllOrders");
            View checkInView=inflater.inflate(R.layout.layout_not_checked_in,container,false);
            TextView tvCheckIn=(TextView)checkInView.findViewById(R.id.tvCheck_in);
            return checkInView;

        }

        return rootView;
    }



}
