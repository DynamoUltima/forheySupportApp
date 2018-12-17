package app.com.forheypanel.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.pnikosis.materialishprogress.ProgressWheel;
import app.com.forheypanel.R;
import app.com.forheypanel.model.Client;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.OrdersAdapter;
import app.com.forheypanel.tools.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/5/15.
 */
public class ActivityCustOrders extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView rv;
    String TAG=getClass().getName();

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setHasFixedSize(true);



        Bundle bundle=getIntent().getExtras();
        if (bundle !=null){
            String email=bundle.getString("email","");
            if (bundle.getString("type").contains("all_orders")){
                progressWheel.setVisibility(View.VISIBLE);
               App.supportService.getAllCustOrders("getOrdersByEmail",email).enqueue(new Callback<Order.OrderList>() {
                   @Override
                   public void onResponse(Call<Order.OrderList> call, Response<Order.OrderList> response) {
                       progressWheel.setVisibility(View.GONE);
                       if (response.body().success==1){
                           rv.setAdapter(new OrdersAdapter(response.body().order_list, ActivityCustOrders.this));
                       }else{
                           Toast.makeText(ActivityCustOrders.this,"Sorry, failed to load orders",Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<Order.OrderList> call, Throwable t) {
                       progressWheel.setVisibility(View.GONE);
                       Toast.makeText(ActivityCustOrders.this,"Sorry, failed to load orders",Toast.LENGTH_SHORT).show();

                   }
               });



            }else{
                progressWheel.setVisibility(View.VISIBLE);
                App.supportService.getAllCustOrders("getCancelledOrdersByEmail",email).enqueue(new Callback<Order.OrderList>() {
                    @Override
                    public void onResponse(Call<Order.OrderList> call, Response<Order.OrderList> response) {
                        progressWheel.setVisibility(View.GONE);
                        if (response.body().success==1){
                            rv.setAdapter(new OrdersAdapter(response.body().order_list, ActivityCustOrders.this));
                        }else{
                            Toast.makeText(ActivityCustOrders.this,"Sorry, failed to load orders",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order.OrderList> call, Throwable t) {
                        progressWheel.setVisibility(View.GONE);
                        Toast.makeText(ActivityCustOrders.this,"Sorry, failed to load orders",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
