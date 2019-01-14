package app.com.forheypanel.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Facility;
import app.com.forheypanel.model.HeyGirlOptions;
import app.com.forheypanel.model.IdvOrder;
import app.com.forheypanel.model.InventorySummary;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.model.OrderList;
import app.com.forheypanel.model.Results;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nayram on 2/17/17.
 */

public class ActivityOrderDetail extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    SharedPreferences sp;

    @Bind(R.id.tvClientName)
    TextView tvClientName;

    @Bind(R.id.tvOrderId)
    TextView tvOrderId;

    @Bind(R.id.tvPickupDate)
    TextView tvPickupDate;

    @Bind(R.id.tvClientPhone)
    TextView tvClientPhone;

    @Bind(R.id.tvPickupPoint)
    TextView tvPickupPoint;

    @Bind(R.id.tvClientAdd)
    TextView tvClientAddress;

    @Bind(R.id.tvService)
    TextView tvService;

    @Bind(R.id.tvNotes)
    TextView tvNotes;

    @Bind(R.id.tvPromo)
    TextView tvPromo;

    @Bind(R.id.tvOrderStatus)
    TextView tvOrderStatus;

    @Bind(R.id.tvAssignedFac)
            TextView tvAssignedFac;

    @Bind(R.id.tvServiceType)
            TextView tvServiceType;

    @Bind(R.id.tvAssignedPerson)
            TextView tvAssignedPerson;

    @Bind(R.id.tvAssignedPhone)
            TextView tvAssignedPhone;

    @Bind(R.id.order_det_toolbar)
    Toolbar toolbar_order_det;

    @Bind(R.id.tvOrderNumOfItems)
            TextView tvNumberOfITems;
    @Bind(R.id.tvOrderWeight)
    TextView tvWeight;

//    @Bind(R.id.invoice_transfer)
//    Button invoiceButton;


    ProgressDialog pDialog;
    String orderId,medium;

    SharedPreferences mprefs;


    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private String phone,clientGcm;

    private String TAG=getClass().getName();

    CharSequence locationOptions[] = new CharSequence[] {"En route to Pickup", "Pickup", "Cleaning","En route to Delivery", "Delivery"};
    private int state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_det);
        ButterKnife.bind(this);


//        invoiceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ActivityOrderDetail.this, "clicked", Toast.LENGTH_SHORT).show();
//                intent = new Intent(ActivityOrderDetail.this,InvoiceActivity.class);
//               // intent.putExtra("IDorder",orderId);
//                startActivity(intent);
//            }
//        });




        setSupportActionBar(toolbar_order_det);
        mprefs=getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        pDialog=new ProgressDialog(this);

        pDialog.setMessage("loading..");

        pDialog.setIndeterminate(false);

        pDialog.setCancelable(false);

        Bundle getgroup=getIntent().getExtras();

        if (getgroup != null){
            orderId=getgroup.getString("OrderId");
            pDialog.show();








            App.supportService.getOrdersByServerCode("getOrderbyServerCode",orderId).enqueue(new Callback<OrderList>() {
                @Override
                public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                    OrderList orderList=response.body();
                    pDialog.hide();
                    if (orderList.success==1){



                        for (IdvOrder order :orderList.order_list){


//                            SharedPreferences.Editor editor = sp.edit();
//                            editor.putString("order",orderId );
//                            editor.commit();


//                            Bundle args = new Bundle();
//                            args.putString("ID", orderId);


                            tvClientName.setText(order.client_name);
                            tvOrderId.setText(orderId);
                            String x = orderId;



                            tvPickupDate.setText(factorDate(order.pickup_date)+" "+order.pick_from_time);
                            tvClientPhone.setText(order.client_phone);
                            tvPickupPoint.setText(order.pickup_point.toUpperCase());
                            tvNotes.setText(order.note);
                            clientGcm=order.client_gcm;
                            phone=order.client_phone;
                            medium=order.medium;
                            if (order.pickup_point.equalsIgnoreCase("home")){
                                String location=order.home_location;
                                if (order.home_location.equalsIgnoreCase("other")){
                                    location=order.home_otherlocation;
                                }
                                tvClientAddress.setText("No."+order.home_housenumber+", "+
                                        order.home_street_name+", "+"\n"+order.home_reference+"\n"+location);
                            }else{
                                String location=order.office_location;
                                if (order.office_location.equalsIgnoreCase("other")){
                                    location=order.office_location;
                                }
                                tvClientAddress.setText(order.office_company_name+"\nNo."+order.office_housenumber+", "
                                        +order.office_street_name+"\n"+order.office_reference+"\n"+location);
                            }
                            if (order.fold.equalsIgnoreCase("1")){

                                tvService.setText("Wash & Fold");

                            }
                            if (order.press_only.equalsIgnoreCase("1")){
                                if (tvService.getText().toString().trim().isEmpty())
                                    tvService.setText("Press Only ");
                                else tvService.setText(tvService.getText().toString()+", Press Only");
                            }

                            if (order.press.equalsIgnoreCase("1")){

                                if (tvService.getText().toString().trim().isEmpty())
                                    tvService.setText(tvService.getText().toString()+"Laundry");
                                else tvService.setText(tvService.getText().toString()+", Laundry");

                            }

                            if (order.dry_clean.equalsIgnoreCase("1")){

                                if (tvService.getText().toString().trim().isEmpty())
                                    tvService.setText("Dry Cleaning");
                                else tvService.setText(tvService.getText().toString()+", Dry Cleaning");

                            }

                            if (order.home_service.equalsIgnoreCase("1")){

                                if (tvService.getText().toString().trim().isEmpty())
                                    tvService.setText(tvService.getText().toString()+"Home Service");
                                else tvService.setText(tvService.getText().toString()+" & Home Service");

                            }

                            switch (order.status){
                                case "0":
                                    tvOrderStatus.setText("Order");
                                    break;
                                case "1":
                                    tvOrderStatus.setText("Pickup");
                                    break;
                                case "2":
                                    tvOrderStatus.setText("Cleaning");
                                    break;
                                case "3":
                                    tvOrderStatus.setText("Delivery");
                                    break;
                                case "4":
                                    tvOrderStatus.setText("En route to delivery");
                                    break;
                                case "5":
                                    tvOrderStatus.setText("Order Cancelled");
                                    break;
                                case "6":
                                    tvOrderStatus.setText("En route to pickup");
                                    break;
                            }
                        }
                        int i=0;
                        for (Facility facility : orderList.assigned_fac){
                            if (i==0){
                                tvAssignedFac.setText(facility.facility_name);
                                tvServiceType.setText(getServiceType(facility.service_type)+": ");
                            }else {
                                tvAssignedFac.setText(tvAssignedFac.getText().toString()+"\n\n"+facility.facility_name);
                                tvServiceType.setText(tvServiceType.getText().toString()+"\n\n"+getServiceType(facility.service_type)+": ");

                            }

                        }

                        for (HeyGirlOptions heygirl:orderList.assignees){
                            if (heygirl.email.equalsIgnoreCase(orderList.order_list.get(0).assigned_to)){
                                tvAssignedPerson.setText(heygirl.name);
                                tvAssignedPhone.setText(heygirl.phone);
                            }

                        }
                    }else{
                        Toast.makeText(ActivityOrderDetail.this,"Order does not exist!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderList> call, Throwable t) {
                    t.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(ActivityOrderDetail.this,"Failed to load order details!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        loadInventorySummary();
        tvOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOptionDialog();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        Log.d(TAG,"AppBarLayout Total Scroll Range "+appBarLayout.getTotalScrollRange());
    }

    String factorDate(String params){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String dayOfTheWeek,mn,res="";
        Date d;
        try{
            d = formatter.parse(params);
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", d);
            mn = (String) android.text.format.DateFormat.format("MMM", d);
            res=dayOfTheWeek+" "+d.getDate()+"/"+mn;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return res;
    }

    String getServiceType(String arg){
        String result="";
        switch (arg){
            case "1":
                result="Dry Cleaning";
                break;
            case "2":
                result="Wash & Fold";
                break;
            case "3":
                result="Laundry";
                break;
            case "4":
                result="Press Only";
                break;
        }
        return result;
    }

    public void createOptionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order Status");
        builder.setItems(locationOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence s=locationOptions[which];
                    
                if (s == "Order") {
                    which = 0;
                } else if (s == "Pickup") {
                    which = 1;
                } else if (s == "Cleaning") {
                    which = 2;
                } else if (s == "En route to Delivery") {
                    which = 4;
                } else if (s == "Delivery") {
                    which = 3;
                }else if (s =="En route to Pickup"){
                    which =6;
                }
                confirmUpdate(s,which);

            }
        });
        builder.show();
    }


    private void confirmUpdate(final CharSequence order, final int status){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Order Status");
        builder.setMessage("Do you want to change the status to "+order+"?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state=status;
                updateOrderStatus(order.toString());
            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        builder.show();
    }

    void updateOrderStatus(final String order){
        String ss="",message="";

        switch (state){
            case 1:
                ss="1";
                message="Your laundry has been picked up";;
                break;
            case 2:
                ss="2";
                message="Your laundry is being cleaned";
                break;
            case 3:
                ss="3";
                message="Your laundry has been delivered! Thanks for using Forhey";
                break;
            case 4:
                ss="4";
                message="We are on our way to deliver your laundry";
                break;
            case  6:
                ss="6";
                message="We are en route to pickup your garments";
                break;

        }
        pDialog.show();
        App.supportService.updateOrderStatus( "updateStatus", orderId, String.valueOf(state),
                medium, "true", phone)
                .enqueue(new Callback<Results>() {
                    @Override
                    public void onResponse(Call<Results> call, Response<Results> response) {
                        pDialog.hide();
                        tvOrderStatus.setText(order);
                        Toast.makeText(ActivityOrderDetail.this, "Status changed successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Results> call, Throwable t) {
                        pDialog.hide();
                        Toast.makeText(ActivityOrderDetail.this, "Failed to change status", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_inventory){
            InventoryListActivity.orderId=orderId;
            startActivity(new Intent(this,InventoryListActivity.class));
        }else if(item.getItemId()==R.id.menu_add_weight){
            createWeightDialog();
        }else{
            finish();
        }
        return true;
    }

    void createWeightDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_weight, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.etWeight);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                if (!userInput.getText().toString().trim().isEmpty()){
                                    addWeight(userInput.getText().toString());
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_det, menu);
        return true;
    }


    void addWeight(final String weight){
        pDialog.setMessage("Adding Weight...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        App.supportService.addWashNFold("AddWeight",orderId,weight).enqueue(new Callback<InventorySummary>() {
            @Override
            public void onResponse(Call<InventorySummary> call, Response<InventorySummary> response) {
                pDialog.dismiss();
                tvWeight.setText(weight+"KG");
                Toast.makeText(ActivityOrderDetail.this, "Weight saved successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<InventorySummary> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(ActivityOrderDetail.this, "failed to save weight", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadInventorySummary() {

        App.supportService.getInventorySummary("GetNumberOfItems",orderId).enqueue(new Callback<InventorySummary>() {
            @Override
            public void onResponse(Call<InventorySummary> call, Response<InventorySummary> response) {

                InventorySummary summary=response.body();
                if (summary!=null)
                    if (summary.success ==1){
                        tvNumberOfITems.setText(String.valueOf(summary.no_of_items));
                        tvWeight.setText(String.valueOf(summary.weight)+"Kg");
                    }

            }

            @Override
            public void onFailure(Call<InventorySummary> call, Throwable t) {
                t.printStackTrace();
                tvNumberOfITems.setVisibility(View.GONE);
                tvWeight.setVisibility(View.GONE);
            }
        });
    }

}
