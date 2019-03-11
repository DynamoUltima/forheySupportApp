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
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public static String orderId;

    private LinearLayoutManager lLayout;
    InventoryAdapter2 adapter;
    ArrayList<Inventory> arrayList;
    //  ArrayList<InvoiceList> arrayLister;

    private TextView DateInvoice, InvoiceNo,price_total;
    private Button InvoiceSend;


    String TAG = getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Gson gson = new Gson();

        recInVentory2 = findViewById(R.id.recInventoryCustom);

        InvoiceSend = findViewById(R.id.invoiceSent);

        price_total = findViewById(R.id.price_total);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.forheypanel", MODE_PRIVATE);
        int priceTotal =sharedPreferences.getInt("summer",0);



        lLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recInVentory2.setHasFixedSize(true);
        recInVentory2.setLayoutManager(lLayout);
        arrayList = new ArrayList<Inventory>();
        adapter = new InventoryAdapter2(arrayList, this, this);
        DateInvoice = findViewById(R.id.invoicedate);
        InvoiceNo = findViewById(R.id.invoiceNo);
        recInVentory2.setAdapter(adapter);



        InvoiceSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postInvoice();
                openInvoiceDialog();
            }
        });


        loadInventory();


        int sum = 0;
        for (int i = 0; i < arrayList.size();  i++) {
            Inventory inventory = arrayList.get(i);
            int price = inventory.getPrice();
            sum+= price;

        }
        price_total.setText(Integer.toString(sum));
    }


    private void openInvoiceDialog() {
        ExampleDialog invoiceDialog = new ExampleDialog();
        invoiceDialog.show(getSupportFragmentManager(), "invoice Dialog");

    }


    public String factorDate(String params) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dayOfTheWeek = "", mn = "", res = "";
        Date d = null;
        try {
            d = formatter.parse(params);//catch exception
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", d);
            mn = (String) android.text.format.DateFormat.format("MMM", d);
            res = dayOfTheWeek + " " + d.getDate() + "/" + mn;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadInventory();
        }
    }


    void loadInventory() {
        final Gson gson = new Gson();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.forheypanel", MODE_PRIVATE);
        String idOrder = sharedPreferences.getString("orderId", "");
        final String idDate = sharedPreferences.getString("orderDate", "");
        final String idTime = sharedPreferences.getString("orderTime", "");
        DateInvoice.setText(idDate);
        InvoiceNo.setText(idOrder);


        // String ID = getArguments().getString("ID");


        // String IDorder = intent.getStringExtra("IDorder");

        // Toast.makeText(this, idOrder, Toast.LENGTH_SHORT).show();

        App.supportService.getInventory(idOrder, "GetClientInventory").enqueue(new Callback<InventoryList>() {
            @Override
            public void onResponse(Call<InventoryList> call, Response<InventoryList> response) {
                Log.d(TAG, response.toString());
                //progressDialog.dismiss();
                // swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().success == 1) {
                        if (response.body().clientInventory.size() > 0) {
                            adapter.updateList(response.body().clientInventory);

//                            Invoicer invoicer = new Invoicer("dynamo","KJ10938",arrayList);
//                            //App.supportService.sendInventory("1234456","good Stuff",arrayList);

                            //  String json = gson.toJson(invoicer);

                        } else
                            Toast.makeText(InvoiceActivity.this, "No inventory found!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InvoiceActivity.this, "Failed to load Inventory", Toast.LENGTH_SHORT).show();
                    }
                    // Log.d(TAG,response.toString());
                } else {
                    Log.d(TAG, response.toString());
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


    private void postInvoice() {


        SharedPreferences sharedPreferences = this.getSharedPreferences("com.forheypanel", MODE_PRIVATE);
        String idOrder = sharedPreferences.getString("orderId", "");

        Invoicer invoicer = new Invoicer("dynamo", idOrder, arrayList);
        App.supportService.sendInventory(invoicer).enqueue(new Callback<Invoicer>() {
            @Override
            public void onResponse(Call<Invoicer> call, Response<Invoicer> response) {
                Log.d(TAG, response.toString());
                //progressDialog.dismiss();
                // swipeRefreshLayout.setRefreshing(false);


                Toast.makeText(InvoiceActivity.this, "gone", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Invoicer> call, Throwable t) {
                //progressDialog.dismiss();
                t.printStackTrace();
                // Log.d(TAG,t.printStackTrace());
                //swipeRefreshLayout.setRefreshing(false);
                // Toast.makeText(InventoryListActivity.this, "Failed to load inventory", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
