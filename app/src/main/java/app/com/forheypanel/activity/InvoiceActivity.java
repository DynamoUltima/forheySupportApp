package app.com.forheypanel.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.CustomAdapter;
import app.com.forheypanel.adapter.InventoryAdapter2;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity {

//    @Bind(R.id.recInventoryCustom)
   RecyclerView recInVentory2;
    Intent intent;

    private Bundle getgroup;



    public static String orderId;

    private LinearLayoutManager lLayout;
    InventoryAdapter2 adapter;
    ArrayList<Inventory> arrayList;

    CustomAdapter customAdapter;


    String TAG = getClass().getName();
    // List<Inventory> inventList;
    ArrayList<Inventory> inventorList;
    ListView listView;


    // String  INVOICESTRING [] = {"BedSheet","BedCover","Trouser","Duvet"};

    // double PRICE [] =  {2.90,3.50,4.00,2.50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

//        listView = findViewById(R.id.invoiceList);
//        inventorList = new ArrayList<Inventory>();
//        customAdapter = new CustomAdapter(inventorList, this, this);
//        listView.setAdapter(customAdapter);
        recInVentory2 = findViewById(R.id.recInventoryCustom);

        lLayout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recInVentory2.setHasFixedSize(true);
        recInVentory2.setLayoutManager(lLayout);
        arrayList=new ArrayList<Inventory>();
        adapter=new InventoryAdapter2(arrayList,this,this);
        recInVentory2.setAdapter(adapter);

//        getgroup=getIntent().getExtras();
//        orderId=getgroup.getString("OrderId");




        loadInventory();

    }

//    void loadInventory() {
//
//        App.supportService.getInventory(orderId, "GetClientInventory").enqueue(new Callback<InventoryList>() {
//            @Override
//            public void onResponse(Call<InventoryList> call, Response<InventoryList> response) {
//                Log.d(TAG, response.toString());
//                //progressDialog.dismiss();
//                if (response.body().clientInventory.size()>0) {
//                    Log.d(TAG, response.toString());
//                    adapter.updateList(response.body().clientInventory);
//                    //customAdapter = new CustomAdapter(getApplicationContext(),inventorList);
//                    //listView.setAdapter(customAdapter);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<InventoryList> call, Throwable t) {
//                //progressDialog.dismiss();
//                t.printStackTrace();
//                // Log.d(TAG,t.printStackTrace());
//
//                Toast.makeText(InvoiceActivity.this, "Failed to load inventory", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            loadInventory();
        }
    }

    //"MO12102264"

    void loadInventory(){

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.forheypanel", MODE_PRIVATE);
       String idOrder = sharedPreferences.getString("orderId","");


       // String ID = getArguments().getString("ID");



       // String IDorder = intent.getStringExtra("IDorder");

       // Toast.makeText(this, idOrder, Toast.LENGTH_SHORT).show();

        App.supportService.getInventory(idOrder,"GetClientInventory").enqueue(new Callback<InventoryList>() {
            @Override
            public void onResponse(Call<InventoryList> call, Response<InventoryList> response) {
                Log.d(TAG,response.toString());
                //progressDialog.dismiss();
               // swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    if (response.body().success==1){
                        if (response.body().clientInventory.size()>0) {
                            adapter.updateList(response.body().clientInventory);
                            //recInVentory.setAdapter(new InventoryAdapter(response.body().clientInventory,InventoryListActivity.this,InventoryListActivity.this));
                        }else Toast.makeText(InvoiceActivity.this, "No inventory found!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(InvoiceActivity.this, "Failed to load Inventory", Toast.LENGTH_SHORT).show();
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
                //swipeRefreshLayout.setRefreshing(false);
               // Toast.makeText(InventoryListActivity.this, "Failed to load inventory", Toast.LENGTH_SHORT).show();

            }
        });
    }
//
//    class CustomAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return INVOICESTRING.length;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//           view = getLayoutInflater().inflate(R.layout.custom_layout,null);
//
//            TextView textView_product = view.findViewById(R.id.product);
//            TextView textView_price = view.findViewById(R.id.price);
//            TextView textView_quantity = view.findViewById(R.id.quantity);
//
//            textView_product.setText(INVOICESTRING[i]);
//            textView_price.setText(String.valueOf(PRICE[i])+"0");
//            textView_quantity.setText("2");
//
//
//            return view;
//        }
//    }
}
