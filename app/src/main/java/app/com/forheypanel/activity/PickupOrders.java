package app.com.forheypanel.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Options;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.OrdersAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 11/20/15.
 */


public class PickupOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressWheel progressWheel;
    ArrayList<Options>it;
    String TAG=getClass().getName();
    String email="";
    String error_msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        setTitle("Pickup Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressWheel=(ProgressWheel)findViewById(R.id.progress_wheel);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        it=new ArrayList<Options>();
//        recyclerView.setAdapter(new OrdersAdapter(it,this));
        Bundle bundle=getIntent().getExtras();
        SharedPreferences mpref=getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        if (bundle!=null){
            email=bundle.getString("email","");
            if (email.isEmpty()){
                email=mpref.getString("email","");
            }
            String date=bundle.getString("date");
            Log.e(TAG, "Email " + email + "\n" + "Date " + date);
            progressWheel.setVisibility(View.VISIBLE);
            App.supportService.getOrdersByDate("getOrdersByDate",date,email).enqueue(new Callback<Order.OrderList>() {
                @Override
                public void onResponse(Call<Order.OrderList> call, Response<Order.OrderList> response) {
                    progressWheel.setVisibility(View.GONE);
                    if (response.body().success == 1)
                        recyclerView.setAdapter(new OrdersAdapter(response.body().order_list, PickupOrders.this));
                    else {
                        Toast.makeText(PickupOrders.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Order.OrderList> call, Throwable t) {
                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(PickupOrders.this, "Sorry, failed to load data", Toast.LENGTH_SHORT).show();

                }
            });

        }else{
            email=mpref.getString("email","");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
