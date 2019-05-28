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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.CustomAdapter;
import app.com.forheypanel.adapter.InventoryAdapter2;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.model.InvoicerList;
import app.com.forheypanel.service.Error;
import app.com.forheypanel.service.ErrorUtils;
import app.com.forheypanel.service.SupportApiService;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.com.forheypanel.tools.App.supportService;

public class InvoiceActivity extends AppCompatActivity implements InventoryAdapter2.AdapterCallback {

    //    @Bind(R.id.recInventoryCustom)
    RecyclerView recInVentory2;
    Intent intent;
    public static String orderId;

    private LinearLayoutManager lLayout;
    InventoryAdapter2 adapter;
    ArrayList<Inventory> arrayList;
    ArrayList<InvoicerList> arrayInvoice;
    //  ArrayList<InvoiceList> arrayLister;


    private TextView DateInvoice, InvoiceNo, price_total;
    private Button InvoiceSend;


    String TAG = getClass().getName();

    InventoryAdapter2.AdapterCallback adapterCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Gson gson = new Gson();

        recInVentory2 = findViewById(R.id.recInventoryCustom);

        InvoiceSend = findViewById(R.id.invoiceSent);

        price_total = findViewById(R.id.price_total);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.forheypanel", MODE_PRIVATE);
        //double priceTotal = sharedPreferences.getFloat("summer", 0);


        lLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recInVentory2.setHasFixedSize(true);
        recInVentory2.setLayoutManager(lLayout);
        arrayList = new ArrayList<Inventory>();
        adapter = new InventoryAdapter2(arrayList, this, this);
        DateInvoice = findViewById(R.id.invoicedate);
        InvoiceNo = findViewById(R.id.invoiceNo);
        recInVentory2.setAdapter(adapter);
        // adapter.setOnItemClickListener(onItemClickListener);
        //adapter.getItemCount();

        // Toast.makeText(this, String.valueOf(getIntent().getExtras().get("summer")), Toast.LENGTH_SHORT).show();


        loadInventory();


    }



//    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            //TODO: Step 4 of 4: Finally call getTag() on the view.
//            // This viewHolder will have all required values.
//            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
//            int position = viewHolder.getAdapterPosition();
//            // viewHolder.getItemId();
//            // viewHolder.getItemViewType();
//            // viewHolder.itemView;
//            Inventory inventory = arrayList.get(position);
//
//            Toast.makeText(InvoiceActivity.this, "You Clicked: " + inventory.getNoOfITems(), Toast.LENGTH_SHORT).show();
//        }
//    };

//    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            int sum = 0;
//            for (i = 0; i < arrayList.size(); i++) {
//                Inventory inventory = arrayList.get(i);
//                int price =Integer.parseInt(inventory.getPrice());
//                sum +=price;
//
//            }
//            Toast.makeText(InvoiceActivity.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();
//            price_total.setText(Integer.toString(sum));
//        }
//    };


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

                            arrayList.addAll(response.body().clientInventory);


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


    }

    @Override
    public void onMethodCallback(final int sum, final int sumItems, final String itemGarment, final String itemCode, final String itemQuantity, final String typeOfService) {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://support.forhey.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build());

        Retrofit retrofit = builder.build();
        final SupportApiService supportApiService = retrofit.create(SupportApiService.class);

        price_total.setText("₵" + sum + ".0");

        InvoiceSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInvoiceDialog();

                SharedPreferences sharedPreferences = getSharedPreferences("com.forheypanel", MODE_PRIVATE);
                String idOrder = sharedPreferences.getString("orderId", "");
                String promoCode = sharedPreferences.getString("orderNote", "None");
                String cost_total = String.valueOf(sum);
                String totalItems = String.valueOf(sumItems);


                //arrayList.size();
                // App.supportApiService.sendInventory(totalItems, promoCode, idOrder, cost_total, invoicerList)
                //App.supportApiService.sendInventory(invoicer)
//                Invoicer invoicer = new Invoicer(promoCode, idOrder, arrayList, totalItems, cost_total);
//                Call<Invoicer> call = supportService.sendInventory(invoicer);

//                    Inventory inventory = arrayList.get(pos);
//                    String item       = inventory.getItem();
//                    String quantity     = inventory.getNoOfITems();
//                    String codeItems       = inventory.getItemCode();
//                    String typeOfService    = inventory.getType();

                // arrayInvoice = new ArrayList<InvoicerList>();
                // arrayInvoice.add(invoicerList);
                // InvoicerList invoicerList = new InvoicerList(itemGarment, itemQuantity, typeOfService, itemCode);


                ArrayList<GarmentItems> garmentItems = new ArrayList<>();

                for (int i = 0; i < arrayList.size() - 1; i++) {

                    garmentItems.add(new GarmentItems(
                            arrayList.get(i).getItem()
                            , Integer.parseInt(arrayList.get(i).getNoOfITems())
                            , arrayList.get(i).getType()
                            , arrayList.get(i).getItemCode())
                    );
                }

                //Invoicer.GarmentItems g = (Invoicer.GarmentItems) itr.next();
                // System.out.println(st.rollno + " " + st.name + " " + st.age);+++


                Invoicer invoicer = new Invoicer("2", idOrder, cost_total, promoCode, garmentItems);
// App.supportApiService.sendInventory(sumItems,idOrder,cost_total,promoCode,garmentItems)
                // gson.toJson(invoicer)
                // Log.d(TAG, "DataToServer: "+ invoicer.toString());

//                Call<Invoicer> call = supportApiService.sendInventory(sumItems, idOrder, cost_total, promoCode, garmentItems);
                Call<Invoicer> call = supportApiService.sendInventory(invoicer);


                call.enqueue(new Callback<Invoicer>() {
                    @Override
                    public void onResponse(Call<Invoicer> call, Response<Invoicer> response) {

                        Log.d("responsed", response.toString());
                        // Toast.makeText(InvoiceActivity.this, "", Toast.LENGTH_SHORT).show();
                        // Log.d("responder",invoicer.items.toString());
                        //Log.d("bodyResponse",String.valueOf(response.body().getMessage()));
                        //  Toast.makeText(InvoiceActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(InvoiceActivity.this, "gone", Toast.LENGTH_SHORT).show();


//                        if(response.code() == 200){
//
//                            Log.d("responsed", response.body().toString());
//                            Log.d("responder",invoicer.items.toString());
//                            //  Toast.makeText(InvoiceActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(InvoiceActivity.this, "gone", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Error error = new ErrorUtils().parseError(response);
//                            Toast.makeText(InvoiceActivity.this, "Error: "+  error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }

                    }

                    @Override
                    public void onFailure(Call<Invoicer> call, Throwable t) {
                        //progressDialog.dismiss();
                        t.printStackTrace();
                        Toast.makeText(InvoiceActivity.this, "Failed: " + String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        Toast.makeText(InvoiceActivity.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();
        //Toast.makeText(InvoiceActivity.this, String.valueOf(length), Toast.LENGTH_LONG).show();

    }
}

