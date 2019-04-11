package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.InventoryAdapter;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayrammensah on 9/21/17.
 */

public class InventoryListActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Bind(R.id.recInventory)
    RecyclerView recInVentory;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;
    public static String orderId;

    private GridLayoutManager lLayout;
    InventoryAdapter adapter;
    ArrayList<Inventory> arrayList;

    String TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_inventory_layout);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Inventory..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        lLayout = new GridLayoutManager(this, 2);
        recInVentory.setHasFixedSize(true);
        recInVentory.setLayoutManager(lLayout);
        arrayList=new ArrayList<Inventory>();
        adapter=new InventoryAdapter(arrayList,this,this);
        recInVentory.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInventory();
            }
        });

         sp = this.getSharedPreferences("com.forheypanel.activity", MODE_PRIVATE);






        loadInventory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAddInventory:
                InventoryActivity.orderId=orderId;
                startActivityForResult(new Intent(InventoryListActivity.this,InventoryActivity.class),101);
                break;
            case R.id.menuRefreshList:
                loadInventory();
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            loadInventory();
        }
    }

    void loadInventory(){
        swipeRefreshLayout.setRefreshing(true);
        App.supportService.getInventory(orderId,"GetClientInventory").enqueue(new Callback<InventoryList>() {
            @Override
            public void onResponse(Call<InventoryList> call, Response<InventoryList> response) {
                Log.d("inventory",response.toString());
                Log.d("inventorybody",response.body().toString());
                //progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    if (response.body().success==1){
                        if (response.body().clientInventory.size()>0) {
                            adapter.updateList(response.body().clientInventory);
                            //recInVentory.setAdapter(new InventoryAdapter(response.body().clientInventory,InventoryListActivity.this,InventoryListActivity.this));
                        }else Toast.makeText(InventoryListActivity.this, "No inventory found!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(InventoryListActivity.this, "Failed to load Inventory", Toast.LENGTH_SHORT).show();
                    }
                   // Log.d(TAG,response.toString());
                }else{
                    Log.d(TAG,response.toString());
                }
            }

            @Override
            public void onFailure(Call<InventoryList> call, Throwable t) {
                //progressDialog.dismiss();
                t.printStackTrace();
               // Log.d(TAG,t.printStackTrace());
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(InventoryListActivity.this, "Failed to load inventory", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void deleteInventoryItem(int id){
        swipeRefreshLayout.setRefreshing(true);
        App.supportService.deleteInventory(id,"DeleteInventoryItem").enqueue(new Callback<Inventory>() {
            @Override
            public void onResponse(Call<Inventory> call, Response<Inventory> response) {
                Log.d(TAG,response.toString());
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful())
                    if (response.body().success==1){
                        loadInventory();
                    }else{
                        Toast.makeText(InventoryListActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<Inventory> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(InventoryListActivity.this, "Failed to delete items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
