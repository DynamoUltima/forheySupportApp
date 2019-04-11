package app.com.forheypanel.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.forheypanel.model.Facility;
import app.com.forheypanel.model.HeyGirlOptions;
import app.com.forheypanel.model.IdvOrder;
import app.com.forheypanel.model.InventorySummary;
import app.com.forheypanel.model.OrderBill;
import app.com.forheypanel.model.OrderList;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.Helper;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.R;
import butterknife.Bind;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nayram on 3/10/15.
 */
public class SearchActivity extends AppCompatActivity {

    SharedPreferences sp;

//    @Bind(R.id.invoice_transfer)
//    Button invoiceButton;


    private TextView tvPickupTime,tvPickupDate,tvDeliveryTime
            ,tvDeliveryDate,tvUpdateState,tvPayment,tvAmount
            ,tvComment,tvAddressPlace,tvCancelComment,tvFacAssign,tvServiceType;

    private TextView tvName,tvPhone,tvPoint,tvlocation,tvAddress
            ,tvAssign,tvPromo,tvOrderId,tvBookingFee,tvTotalDiscount
            ,tvtotal,tvSubtotal,tvServiceCost,tvServices,tvPaymentStatus,tvNumOfItems,tvWeight;

    private Button btnViewMap,btnGo,paymentButton,confirmBillBtn,cancelBillBtn;

    private EditText etSearch,etAmount;

    private CheckBox chkFold,chkPress,chkDryClean,chkPressOnly,chkHome;

    ProgressDialog pDialog;
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
   private String latitude,longitude,serviceCost,bookingFee,promotionValue,totalCost,receipt_url="";
   private LinearLayout container,llservice,llPlace,llAddress,llCancelComment;

    boolean assigned=false;
    ScrollView scroll;
    String clientGcm,serverCode;
    CharSequence locationOptions[] = new CharSequence[] {"En route to Pickup", "Pickup", "Cleaning","En route to Delivery", "Delivery","Cancel"};

    CharSequence []assignees;
    String []emails;
    String []phone;
    String []admins;
    String []heygirlImg;
    String []hey_code;
    String []gender;
    int state,fold=0,dry_clean=0,press=0,w_press=0,home_service=0;
    SharedPreferences mprefs;
   ActionBar actionBar;
    RelativeLayout rlSearchView;
    private Bundle getgroup;
    private ImageView imgCancel;
    MaterialDialog paymentDialog,orderBillDialog,addressDialog;
    View paymentView,orderBillView,addressView;
    private String orderType="";
    String TAG="Search";
    String url=SupportService.domain+"forhey_mobile_scripts/access_credentials.php";
    ConnectionDetector connectionDetector;
    String orderId,orderState="0",medium;
    RatingBar ratingBar;
    Button invoiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_search_details);
        tvPickupDate=(TextView)findViewById(R.id.tvPickupDate);
        tvPickupTime=(TextView)findViewById(R.id.tvPickupTime);
        tvDeliveryDate=(TextView)findViewById(R.id.tvDeliveryDate);
        tvDeliveryTime=(TextView)findViewById(R.id.tvDeliveryTime);
        btnGo=(Button)findViewById(R.id.btnGo);
        etSearch=(EditText)findViewById(R.id.etSearch);
        //btnViewMap=(Button)findViewById(R.id.btnViewMap);
        tvName=(TextView)findViewById(R.id.tvName);
        tvPhone=(TextView)findViewById(R.id.tvPhone);
        tvlocation=(TextView)findViewById(R.id.tvLocation);
        tvPoint=(TextView)findViewById(R.id.tvPickupPoint);
//        tvAddress=(TextView)findViewById(R.id.tvHouseNum);
        tvUpdateState=(TextView)findViewById(R.id.tvState);
        tvAssign=(TextView)findViewById(R.id.tvAssignee);
        container=(LinearLayout)findViewById(R.id.layoutPickDel);
        llCancelComment=(LinearLayout)findViewById(R.id.llCancelComment);
        scroll=(ScrollView)findViewById(R.id.scroll);
        rlSearchView=(RelativeLayout)findViewById(R.id.rlSearchView);
        tvPromo=(TextView)findViewById(R.id.tvPromotion);
        imgCancel=(ImageView)findViewById(R.id.imgCancelled);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        tvPayment=(TextView)findViewById(R.id.tvPayment);
        tvAmount=(TextView)findViewById(R.id.tvAmount);
        tvComment=(TextView)findViewById(R.id.tvComment);
        tvOrderId=(TextView)findViewById(R.id.tvOrderId);
        tvServices=(TextView)findViewById(R.id.tvService);
        tvCancelComment=(TextView)findViewById(R.id.tvCancelComment);
        tvFacAssign=(TextView)findViewById(R.id.tvAssignedFac);
        tvServiceType=(TextView)findViewById(R.id.tvServiceType);
        tvPaymentStatus=(TextView)findViewById(R.id.tv_payment_status);
        tvWeight=(TextView)findViewById(R.id.tvWeight);
        tvNumOfItems=(TextView)findViewById(R.id.tvNumOfItems);
        paymentDialog=new MaterialDialog(this);
        orderBillDialog=new MaterialDialog(this);
        addressDialog=new MaterialDialog(this);


       invoiceButton = findViewById(R.id.invoice_transfer);

        LayoutInflater mInflater = (LayoutInflater)
                this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

        paymentView=mInflater.inflate(R.layout.payment_dialog,null);
        orderBillView=mInflater.inflate(R.layout.order_bill,null);
        addressView=mInflater.inflate(R.layout.address_layout,null);

        tvAddressPlace=(TextView)addressView.findViewById(R.id.tvAddress);
        btnViewMap=(Button)addressView.findViewById(R.id.btnMap);

        addressDialog.setView(addressView);
        paymentDialog.setView(paymentView);


        etAmount=(EditText)paymentView.findViewById(R.id.etAmount);
        paymentButton=(Button)paymentView.findViewById(R.id.btnSendPayment);
        orderBillDialog.setView(orderBillView);
        tvtotal=(TextView)orderBillView.findViewById(R.id.tvTotal);
        tvTotalDiscount=(TextView)orderBillView.findViewById(R.id.total_discount);
        tvBookingFee=(TextView)orderBillView.findViewById(R.id.tvBookingFee);
        tvSubtotal=(TextView)orderBillView.findViewById(R.id.tvSubTotal);
        tvServiceCost=(TextView)orderBillView.findViewById(R.id.tvServiceCost);
        confirmBillBtn=(Button)orderBillView.findViewById(R.id.btnOrderBillSend);
        cancelBillBtn=(Button)orderBillView.findViewById(R.id.btnOrderCancel);

        cancelBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBillDialog.dismiss();
            }
        });

        confirmBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfirmPayment(bookingFee,totalCost,serviceCost,promotionValue).execute();
            }
        });


        pDialog=new ProgressDialog(this);
        connectionDetector=new ConnectionDetector(this);
        mprefs=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        actionBar = getSupportActionBar();
      //  actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.drawable.ic_action_status_bar_logo);
        getgroup=getIntent().getExtras();
        orderId=getgroup.getString("OrderId");
        String type=getgroup.getString("ClassName");
        if (type.equals("HomeClass")){
            actionBar.setTitle("Details");
            etSearch.setText(orderId);
            serverCode=orderId;
            orderType=getgroup.getString("OrderType");
            Log.e(TAG,"order type "+orderType);
            if (orderType!=null){
                loadTask();
                loadInventorySummary();
            }

            rlSearchView.setVisibility(View.GONE);
        }

        tvlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressDialog.show();
            }
        });

        sp = this.getSharedPreferences("com.forheypanel", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("orderId",orderId );
        editor.commit();

        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentID = new Intent(SearchActivity.this,InvoiceActivity.class);
                //intent.putExtra("IDorder",orderId);
                startActivity(intentID);
            }
        });


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSearch.getText().toString().isEmpty()){
                    //serverCode=etSearch.getText().toString();
                    if(connectionDetector.isConnectingToInternet()){
                        loadTask();
                    }else{
                        Toast.makeText(SearchActivity.this,"Internet connection not detected",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderState.contains("5"))
            if(!latitude.contains("0.0") && !longitude.contains("0.0")){
                    Intent intent =new Intent(SearchActivity.this,GoogleMapLoc.class);
                    Bundle bundle=new Bundle();
                    bundle.putDouble("latitude",Double.parseDouble(latitude));
                    bundle.putDouble("longitude",Double.parseDouble(longitude));
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else{
                    Toast.makeText(SearchActivity.this,"Coordinates not provided ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()){
                   String emailP= mprefs.getString("email",null);
                    if (emailP !=null){
                        System.out.println("email " + emailP);
                        if (findAdmin(emailP)){
                            if (!orderState.contains("5"))
                            createAssignDialog();
                        }else{
                            Toast.makeText(SearchActivity.this,"Sorry, you can't assign anyone",Toast.LENGTH_SHORT).show();
                        }
                    }


                }else{
                    Toast.makeText(SearchActivity.this,"Internet connection not detected ",Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvUpdateState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionDetector.isConnectingToInternet()){
                    if (assigned){
                        if (!orderState.contains("5"))
                            createOptionDialog();
                        else{
                            Toast.makeText(SearchActivity.this,"Sorry, this order has been cancelled ",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(SearchActivity.this,"This order has not been assigned yet",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(SearchActivity.this,"Internet connection not detected ",Toast.LENGTH_SHORT).show();
                }

            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etAmount.getText().toString().trim().isEmpty()){


                        paymentDialog.dismiss();

                        pDialog.setMessage("Generating Bill..");
                        pDialog.setIndeterminate(false);
                            pDialog.setCancelable(false);
                            pDialog.show();
                            App.supportService.generateBill("send_service_code", serverCode, etAmount.getText().toString())
                                    .enqueue(new Callback<OrderBill>() {
                                        @Override
                                        public void onResponse(Call<OrderBill> call, Response<OrderBill> response) {
                                            pDialog.dismiss();
                                            if (response.body().success==1) {
                                                totalCost = String.valueOf(response.body().total);
                                                serviceCost = response.body().service_cost;
                                                bookingFee = response.body().bookingFee;
                                                promotionValue = String.valueOf(response.body().promotion);
                                                String refer_reward = String.valueOf(response.body().referal_disc);
                                                String promo_discount = String.valueOf(response.body().promo_discount);
                                                double bf = Double.parseDouble(bookingFee);
                                                double serv_cost = Double.parseDouble(serviceCost);
                                                double subT = serv_cost + bf;
                                                Bundle bundle = new Bundle();
                                                bundle.putString("total", totalCost);
                                                bundle.putString("service_cost", serviceCost);
                                                bundle.putString("booking_fee", bookingFee);
                                                bundle.putString("total_discount", promotionValue);
                                                bundle.putString("sub_total", String.valueOf(subT));
                                                bundle.putString("server_code", serverCode);
                                                bundle.putString("reg_id", clientGcm);
                                                bundle.putString("promo_discount", promo_discount);
                                                bundle.putString("refer_discount", refer_reward);
                                                bundle.putString("refer_list", new Gson().toJson(response.body()));
                                                Intent intent = new Intent(SearchActivity.this, ActivityOrderBill.class);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<OrderBill> call, Throwable t) {
                                                pDialog.dismiss();
                                                Toast.makeText(SearchActivity.this,"Sorry, failed to generate bill",Toast.LENGTH_SHORT).show();

                                            }
                                    });




                }else {
                    Toast.makeText(SearchActivity.this,"Please enter the amount",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loadInventorySummary() {

        App.supportService.getInventorySummary("GetNumberOfItems",serverCode).enqueue(new Callback<InventorySummary>() {
            @Override
            public void onResponse(Call<InventorySummary> call, Response<InventorySummary> response) {

                InventorySummary summary=response.body();
                if (summary!=null)
                    if (summary.success ==1){
                        tvNumOfItems.setText(String.valueOf(summary.no_of_items));
                        tvWeight.setText(String.valueOf(summary.weight)+"Kg");
                    }

            }

            @Override
            public void onFailure(Call<InventorySummary> call, Throwable t) {
                t.printStackTrace();
                tvNumOfItems.setVisibility(View.GONE);
                tvWeight.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.menu_payment){
            confirmBillBtn.setVisibility(View.VISIBLE);
            paymentDialog.show();
        }else if (id==R.id.menu_orderBill){
            confirmBillBtn.setVisibility(View.GONE);
            orderBillDialog.show();
            new GetOrderBillTask().execute();
        }else if (id==R.id.menu_inventory){
            InventoryListActivity.orderId=serverCode;
            startActivity(new Intent(SearchActivity.this,InventoryListActivity.class));
        }else if (id==R.id.menu_add_weight){
            createWeightDialog();

        }else{
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public String factorDate(String params){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dayOfTheWeek="",mn="",res="";
        Date d=null;
        try{
            d = formatter.parse(params);//catch exception
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", d);
            mn = (String) android.text.format.DateFormat.format("MMM", d);
            res=dayOfTheWeek+" "+d.getDate()+"/"+mn;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return res;
    }

    public void createOptionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Update State Of Order");
        builder.setItems(locationOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence s=locationOptions[which];
                    if (dry_clean==1||w_press==1||press==1||fold==1) {
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
                            which=6;
                        }else if (s== "Cancel"){
                            which=5;
                        }
                    }
                    confirmUpdate(s,which);

            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_details, menu);
        return true;
    }

    public void createAssignDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Assign this order to ...");
        builder.setItems(assignees, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence s=assignees[which];
                String emailParam=emails[which];
                String phoneParam=phone[which];
                String image=heygirlImg[which];
                String code=hey_code[which];
                String gn=gender[which];
                System.out.println("email address "+emailParam+" phone "+phoneParam);
                confirmAssignment(s.toString(), emailParam, phoneParam,image,code,gn);
            }
        });
        builder.show();
    }

    public void confirmAssignment(final String param,final String emails,
                                  final String phoneParam,final String img,
                                  final String code,final String gender){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Assign Order");
        builder.setMessage("Would you like to assign this order to "+param+"?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new SendAssign(serverCode,emails,phoneParam,param,img,code,gender).execute();
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

    private void confirmUpdate(final CharSequence order, final int status){

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Send Update?");
        builder.setMessage("Would you like to update this order?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                state=status;

                new UpdateTask(order).execute();
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

    void addWeight(final String weight){
        pDialog.setMessage("Adding Weight...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        App.supportService.addWashNFold("AddWeight",serverCode,weight).enqueue(new Callback<InventorySummary>() {
            @Override
            public void onResponse(Call<InventorySummary> call, Response<InventorySummary> response) {
                pDialog.dismiss();
                tvWeight.setText(weight+"Kg");
                Toast.makeText(SearchActivity.this, "Weight saved successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<InventorySummary> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(SearchActivity.this, "failed to save weight", Toast.LENGTH_SHORT).show();

            }
        });
    }


    void loadTask (){
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        App.supportService.getOrdersByServerCode("getOrderbyServerCode",orderId)
                .enqueue(new Callback<OrderList>() {
                    @Override
                    public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                        pDialog.dismiss();
                        OrderList orderList=response.body();
                        pDialog.hide();
                        if (orderList.success==1){
                            for (IdvOrder order: orderList.order_list){
                                tvPickupDate.setText(factorDate(order.pickup_date)+" "+order.pick_from_time);


                                sp = getSharedPreferences("com.forheypanel", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("orderDate",order.pickup_date);
                                editor.putString("orderTime",order.pick_from_time);
                                editor.putString("orderNote",order.note);
                                editor.commit();



                                tvPickupTime.setText(order.pick_from_time);
                                tvName.setText(order.client_name);
                                tvPhone.setText(order.client_phone);

                                medium=order.medium;
                                if (order.pickup_point.contains("home"))
                                    tvPoint.setText("Home : ");
                                else{
                                    tvPoint.setText("Office : ");
                                }
                                clientGcm=order.client_gcm;
                                serverCode=order.server_code;
                                tvOrderId.setText(serverCode.toUpperCase());
                                if (order.promotion!=null && !order.promotion.isEmpty() && !order.promotion.equals("null"))
                                    tvPromo.setText(order.promotion);
                                else {
                                    tvPromo.setVisibility(View.GONE);
                                }
                                if (order.fold.equalsIgnoreCase("1")){
                                    fold=1;
                                    tvServices.setText("Wash & Fold");

                                }
                                if (order.press_only.equalsIgnoreCase("1")){
                                    w_press=1;
                                    if (tvServices.getText().toString().trim().isEmpty())
                                        tvServices.setText("Press Only ");
                                    else tvServices.setText(tvServices.getText().toString()+", Press Only");
                                }

                                if (order.press.equalsIgnoreCase("1")){
                                    press=1;
                                    if (tvServices.getText().toString().trim().isEmpty())
                                        tvServices.setText(tvServices.getText().toString()+"Laundry");
                                    else tvServices.setText(tvServices.getText().toString()+", Laundry");

                                }

                                if (order.dry_clean.equalsIgnoreCase("1")){
                                        dry_clean=1;
                                    if (tvServices.getText().toString().trim().isEmpty())
                                        tvServices.setText("Dry Cleaning");
                                    else tvServices.setText(tvServices.getText().toString()+", Dry Cleaning");

                                }

                                if (order.home_service.equalsIgnoreCase("1")){
                                        home_service=1;
                                    if (tvServices.getText().toString().trim().isEmpty())
                                        tvServices.setText(tvServices.getText().toString()+"Home Service");
                                    else tvServices.setText(tvServices.getText().toString()+" & Home Service");

                                }

                                if (order != null)
                                if (order.status!=null)
                                switch (order.status){
                                    case "0":
                                        tvUpdateState.setText("Order");
                                        orderState="0";
                                        break;
                                    case "1":
                                        if (dry_clean==1||w_press==1|| press==1|| fold==1) {
                                            tvUpdateState.setText("Pickup");
                                        }else{
                                            tvUpdateState.setText("Heygirl has arrived");
                                        }
                                        orderState="1";
                                        break;
                                    case "2":
                                        tvUpdateState.setText("Cleaning");
                                        orderState="2";
                                        break;
                                    case "3":
                                        if (dry_clean==1||w_press==1|| press==1|| fold==1) {
                                            tvUpdateState.setText("Delivery");
                                        }else{
                                            tvUpdateState.setText("Clothes are now clean");
                                        }
                                        orderState="3";
                                        break;
                                    case "4":
                                        tvUpdateState.setText("En route to delivery");
                                        orderState="4";
                                        break;
                                    case "5":
                                        imgCancel.setVisibility(View.VISIBLE);
                                        orderState="5";
                                        tvUpdateState.setText("Order Cancelled");
                                        llCancelComment.setVisibility(View.VISIBLE);
                                        tvCancelComment.setText(order.cancel_message);
                                        break;
                                    case "6":
                                        tvUpdateState.setText("En route to pick up");
                                        orderState="4";
                                        break;

                                }
                                if(Integer.valueOf(order.payment_status)==1){
                                    tvPaymentStatus.setText("PAID");
                                }

                                if (dry_clean==1||w_press==1||press==1||fold==1){

                                }else{
                                    locationOptions = new CharSequence[] {"Order", "Heygirl has arrived", "Cleaning", "Clothes are now clean"};
                                }

                                ratingBar.setEnabled(false);
                                if (Integer.valueOf(order.payment) ==1){

                                    tvPayment.setText("Payment made.");
                                    tvAmount.setText("Amount is GHC"+order.amount);
                                }else{
                                    tvAmount.setVisibility(View.GONE);
                                }
                                if(order.assigned_to !=null){
                                    tvAssign.setText(order.assigned_to);
                                    assigned=true;
                                }else{

                                    tvAssign.setText("Not Assigned");
                                }
                                if (order.note !=null && !order.note.equals("null") && !order.note.isEmpty()){
                                    tvComment.setText(order.note);
                                }
                                if(order.pickup_point.equalsIgnoreCase("home")){
                                    tvlocation.setText(order.home_location);
                                    tvAddressPlace.setText(order.home_housenumber+", "+order.home_street_name+", "+
                                            order.home_reference);
                                    latitude=order.home_latitude;
                                    longitude=order.home_longitude;

                                    tvPoint.setText("Home : ");
                                }else{
                                    tvPoint.setText("Office : ");
                                    tvlocation.setText(order.office_company_name+" - "+order.office_location);
                                    tvAddressPlace.setText(order.office_housenumber+" "+order.office_street_name+"\n"+
                                            order.office_reference);
                                    latitude=order.office_latitude;
                                    longitude=order.office_longitude;
                                }
                            }

                            assignees=new CharSequence[orderList.assignees.size()];
                            emails=new String[orderList.assignees.size()];
                            phone=new String[orderList.assignees.size()];
                            heygirlImg=new String[orderList.assignees.size()];
                            hey_code=new String[orderList.assignees.size()];
                            gender=new String[orderList.assignees.size()];
                            int i=0;
                            int adm=0;
                            for (HeyGirlOptions heyGuys:orderList.assignees){
                                    assignees[i]=heyGuys.name;
                                    emails[i]=heyGuys.email;
                                    phone[i]=heyGuys.phone;
                                    heygirlImg[i]=heyGuys.image;
                                    hey_code[i]=heyGuys.hey_code;
                                    gender[i]=heyGuys.gender;
                                    i++;
                            }

                            admins=new String[orderList.order_admin.size()];

                            for (HeyGirlOptions ans:orderList.order_admin){
                                admins[adm]=ans.email;
                                adm++;
                            }

                            for (int j=0;j<orderList.assigned_fac.size();j++){
                                if (j==0){
                                    tvFacAssign.setText(orderList.assigned_fac.get(j).facility_name);
                                    tvServiceType.setText(getServiceType(orderList.assigned_fac.get(j).service_type)+": ");
                                }else {
                                    tvFacAssign.setText(tvFacAssign.getText().toString()+"\n\n"+orderList.assigned_fac.get(j).facility_name);
                                    tvServiceType.setText(tvServiceType.getText().toString()+"\n\n"+getServiceType(orderList.assigned_fac.get(j).service_type));

                                }
                            }
                            // container.setVisibility(View.VISIBLE);
                            scroll.setVisibility(View.VISIBLE);
                            if (!tvAssign.getText().toString().equals("null")){
                                for (int tvA=0;tvA<emails.length;tvA++){
                                    if(emails[tvA].equals(tvAssign.getText().toString())){
                                        tvAssign.setText(assignees[tvA]);
                                        tvA=emails.length;
                                    }
                                }
                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<OrderList> call, Throwable t) {
                        pDialog.dismiss();
                        Toast.makeText(SearchActivity.this,"failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });

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

    class UpdateTask extends AsyncTask <Void,Void,String>{

        CharSequence Type="";

        UpdateTask(CharSequence type) {
            this.Type = type;
        }

        JSONParser jparser=new JSONParser();
        String urlGCM=SupportService.domain+"forhey_mobile_scripts/support_api.php";

        @Override
        protected String doInBackground(Void... params) {
            String message="";
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("server_code",serverCode));
            args.add(new BasicNameValuePair("tag","updateStatus"));
            args.add(new BasicNameValuePair("notification","true"));

            String ss="";
            switch (state){
                case 0:
                    ss="0";

                    break;
                case 1:
                    ss="1";

                    break;
                case 2:
                    ss="2";

                    break;
                case 3:
                   ss="3";

                    break;
                case 4:
                    ss="4";

                    break;
                case 5:
                    ss="5";

                    break;
                case 6:
                    ss="6";
                    break;

            }

            args.add(new BasicNameValuePair("status",ss));
            args.add(new BasicNameValuePair("phone",tvPhone.getText().toString()));
            args.add(new BasicNameValuePair("medium",medium));

            String json=jparser.makeHttpRequest(urlGCM, "POST", args);
            System.out.println("json item "+json);

            return json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Sending Update ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s!=null && !s.isEmpty()){

                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);
                    setResult(RESULT_OK);
                    if(success == 1){
                        Toast.makeText(SearchActivity.this,"Order updated Successfully",Toast.LENGTH_SHORT).show();
                        tvUpdateState.setText(Type);
                        orderState=String.valueOf(state);


                    }else{
                        Toast.makeText(SearchActivity.this,"Order update failed",Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(SearchActivity.this,"Order update failed",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(SearchActivity.this,"Order update failed",Toast.LENGTH_SHORT).show();
            }
             pDialog.dismiss();
        }
    }

    class SendAssign extends AsyncTask<Void,Void,String>{
        String server_code,email,phone,name,img,code,gender;
        JSONParser jparser=new JSONParser();

        SendAssign(String server_code, String email, String phone,String name,String img,String code,String gender) {
            this.server_code = server_code;
            this.email = email;
            this.phone = phone;
            this.name=name;
            this.img=img;
            this.code=code;
            this.gender=gender;
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag","AssignTo"));
            args.add(new BasicNameValuePair("email",email));
            args.add(new BasicNameValuePair("server_code",serverCode));
            args.add(new BasicNameValuePair("name",name));
            args.add(new BasicNameValuePair("image",img));
            args.add(new BasicNameValuePair("type","assign_heygirl"));
            args.add(new BasicNameValuePair("typeOfOrder",orderType));
            args.add(new BasicNameValuePair("sent_by",mprefs.getString("email","Default")));
            args.add(new BasicNameValuePair("regId",clientGcm));
            args.add(new BasicNameValuePair("hey_code",code));
            args.add(new BasicNameValuePair("gender",gender));
            String json=jparser.makeHttpRequest(url, "POST", args);
            System.out.println("json item "+json);
            return json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Assigning..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null && !s.isEmpty()){
                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);
                    if(success == 1){
                        tvAssign.setText(name);
                        assigned=true;
                        Toast.makeText(SearchActivity.this,"Order assigned to "+name,Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();

                    }else{
                        pDialog.dismiss();
                        Toast.makeText(SearchActivity.this," failed",Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(SearchActivity.this,"Assignment failed",Toast.LENGTH_SHORT).show();
                }

            }else{
                pDialog.dismiss();
                Toast.makeText(SearchActivity.this,"Assignment update failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*class SendText extends AsyncTask<Void,Void,String>{
        String phone,name;
        JSONParser jsonParser=new JSONParser();

        SendText(String phone,String name) {
            this.phone = phone;
            this.name=name;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlGCM=SupportService.domain+"forhey_mobile_scripts/txt_message_terminal.php";
           String message=tvName.getText().toString() +' has placed an order \n Pickup Date= '+tvPickupDate.getText().toString()+' '+
                    tvPickupTime.getText().toString()+' \n Pick Up Point= '+ tvPoint.getText().toString()+'\n Phone Number= '+tvPhone.getText().toString();

            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("phonenumber",phone));
            args.add(new BasicNameValuePair("sms_message",message));

            String json=jsonParser.makeHttpRequest(urlGCM, "POST", args);
            System.out.println("json item "+json);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null && !s.isEmpty()){

                if(s.contains("message sent successfully")){
                    tvAssign.setText(name);
                    assigned=true;
                    Toast.makeText(SearchActivity.this,s,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SearchActivity.this,"Failed to Assign Request",Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(SearchActivity.this,"Failed to Assign",Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();

        }
    }*/

    boolean findAdmin(String email){
        boolean res=false;
        for (int i=0;i<admins.length;i++){
            if (email.equalsIgnoreCase(admins[i])) {
                res=true;
                return res;
            }
        }
        return res;
    }


    class ConfirmPayment extends AsyncTask<Void,Void,String>{
        String booking,total,service_cost,promotion;
        JSONParser jsonParser=new JSONParser();
        String urlGCM=SupportService.domain+"forhey_mobile_scripts/access_credentials.php";

        public ConfirmPayment(String booking, String total, String service_cost, String promotion) {
            this.booking = booking;
            this.total = total;
            this.service_cost = service_cost;
            this.promotion = promotion;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Sending..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            if(s!=null && !s.isEmpty()){
                super.onPostExecute(s);

                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);
                    if(success == 1){
                        orderBillDialog.dismiss();
                        final MaterialDialog paymentConfirm=new MaterialDialog(SearchActivity.this);
                        paymentConfirm.setTitle("Service Cost");
                        paymentConfirm.setMessage("Service cost has been sent successfully");
                        paymentConfirm.setPositiveButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                paymentConfirm.dismiss();
                            }
                        });
                        paymentConfirm.show();

                    }else{
                       // pDialog.dismiss();
                        orderBillDialog.dismiss();
                        final MaterialDialog paymentConfirm=new MaterialDialog(SearchActivity.this);
                        paymentConfirm.setTitle("Service Cost");
                        paymentConfirm.setMessage(jobj.getString("error_msg"));
                        paymentConfirm.setPositiveButton("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                paymentConfirm.dismiss();
                                new ConfirmPayment(bookingFee,totalCost,serviceCost,promotionValue).execute();
                            }
                        });
                        paymentConfirm.setNegativeButton("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                paymentConfirm.dismiss();
                            }
                        });
                        paymentConfirm.show();

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(SearchActivity.this,"Failed to reach server",Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(SearchActivity.this,"Failed to reach server",Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }


        @Override
        protected String doInBackground(Void... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag","send_final_service_cost"));
            args.add(new BasicNameValuePair("server_code",serverCode));
            args.add(new BasicNameValuePair("service_cost",service_cost));
            args.add(new BasicNameValuePair("promotion",promotion));
            args.add(new BasicNameValuePair("bookingFee",booking));
            args.add(new BasicNameValuePair("regid",clientGcm));
            args.add(new BasicNameValuePair("total",total));
            String json=jsonParser.makeHttpRequest(urlGCM, "POST", args);
            System.out.println("json item "+json);
            return json;
        }
    }

    class GetOrderBillTask extends AsyncTask<Void,Void,String>{
        JSONParser jsonParser=new JSONParser();
        String urlGCM=SupportService.domain+"forhey_mobile_scripts/access_credentials.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null && !s.isEmpty()){
                super.onPostExecute(s);

                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);
                    if(success == 1){
                        DecimalFormat df=new DecimalFormat("0.00");
                        if (!jobj.getString("subtotal").contains("null")) {
                            if (!jobj.getString("subtotal").isEmpty()) {
                                double subT = Double.parseDouble(jobj.getString("subtotal"));

                                tvSubtotal.setText(df.format(subT));
                            }
                        }

                        if (!jobj.getString("total").contains("null")) {
                            if (!jobj.getString("total").isEmpty()){
                                double total=Double.parseDouble(jobj.getString("total"));
                                tvtotal.setText("GHC"+df.format(total));
                            }

                        }
                        if (!jobj.getString("service_cost").contains("null")) {
                            if (!jobj.getString("service_cost").isEmpty()) {
                                double serC=Double.parseDouble(jobj.getString("service_cost"));
                                tvServiceCost.setText(df.format(serC));
                            }
                        }
                        if (!jobj.getString("bookingFee").contains("null")) {
                            if (!jobj.getString("bookingFee").isEmpty()) {
                                double bC=Double.parseDouble(jobj.getString("bookingFee"));
                                tvBookingFee.setText("+"+df.format(bC));
                            }
                        }

                        if (!jobj.getString("promotion").contains("null")) {
                            if (!jobj.getString("promotion").isEmpty()) {
                                double bC=Double.parseDouble(jobj.getString("promotion"));
                                tvTotalDiscount.setText("-"+df.format(bC));
                            }else{
//                                tvTotalDiscount.setText("No Promotion");
                            }
                        }else{
//                            tvTotalDiscount.setText("No Promotion");

                        }

                    }else{
                        // pDialog.dismiss();


                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(SearchActivity.this,"Failed to reach server",Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(SearchActivity.this,"Failed to reach server",Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();

        }

        @Override
        protected String doInBackground(Void... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag","order_bill"));
            args.add(new BasicNameValuePair("server_code",serverCode));

            String json=jsonParser.makeHttpRequest(urlGCM, "POST", args);
            System.out.println("json item "+json);
            return json;
        }
    }
}
