package app.com.forheypanel.dashboard.inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.BaseActivity;
import app.com.forheypanel.dashboard.inventory.adapter.InventorySearchAdapter;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.model.WashNFold;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardOrderInventory extends BaseActivity {

    String orderid;
    @Bind(R.id.recInventory)
    RecyclerView recInventory;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_inventory_layout);
        ButterKnife.bind(this);
        setTitle("Inventory");
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recInventory.setLayoutManager(new LinearLayoutManager(recInventory.getContext()));
        recInventory.setHasFixedSize(true);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        if (bundle !=null)
        {
             orderid = bundle.getString("order_id");

            if (orderid != null)
            {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();

        return super.onOptionsItemSelected(item);
    }

    void loadData(){

        App.supportApiService.getInventoryByServerCode(orderid)
                .enqueue(new Callback<InventoryList>() {
                    @Override
                    public void onResponse(Call<InventoryList> call, Response<InventoryList> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful())
                        {
                            try {

                                ArrayList<Inventory> list=response.body().clientInventory;

                                /*Iterator itr = list.iterator();
                                while (itr.hasNext())
                                {
                                    Inventory x =(Inventory) itr.next();
                                    if (x.getType().equalsIgnoreCase("Wash & Fold"))
                                        itr.remove();
                                }*/


                                InventorySearchAdapter adapter=new InventorySearchAdapter(getApplication(),
                                        list,response.body());

                                recInventory.setAdapter(adapter);

                            }catch (NullPointerException ex){
                                ex.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<InventoryList> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        t.printStackTrace();
                        Toast.makeText(DashboardOrderInventory.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
