package app.com.forheypanel.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.iid.FirebaseInstanceId;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.com.forheypanel.model.User;
import app.com.forheypanel.model.UserClient;
import app.com.forheypanel.model.UserLocation;
import app.com.forheypanel.service.LocationService;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.model.OrderDataModel;
import app.com.forheypanel.adapter.OrderlistAdater;
import app.com.forheypanel.R;

import static app.com.forheypanel.model.Constants.ERROR_DIALOG_REQUEST;
import static app.com.forheypanel.model.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static app.com.forheypanel.model.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;


public class MainActivity extends BaseActivity {
   private ListView listView;
    private ArrayList<OrderDataModel>dataModels;
    SharedPreferences mprefs;
    ProgressDialog pDialog;

    //vars
    private FirebaseFirestore mDb;

    private static final String TAG = "MainActivity";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = FirebaseFirestore.getInstance();


        // mDb = FirebaseFirestore.getInstance();

//        registerNewEmail("dynamo.joey@gmail.com", "password");

        Log.d("FCM-Test", "Token" + FirebaseInstanceId.getInstance().getToken());

        Log.d("FCM-Test", "TokenID" + FirebaseInstanceId.getInstance().getInstanceId());


        listView = (ListView) findViewById(R.id.listView);
        pDialog = new ProgressDialog(MainActivity.this);
        mprefs = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        ConnectionDetector conection = new ConnectionDetector(this);
        if (conection.isConnectingToInternet()) {
            //   new RegisterTask(this).execute();
            new loadOrderTask().execute();
        } else {
            logItem("Please check your internet connection");
        }
        Toast.makeText(this, "MainAct", Toast.LENGTH_SHORT).show();

    }


    //Experiments






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public static class MyViewHolder{
        public LinearLayout llStatus;
        public TextView tvPickupDate,tvOrderId,tvDeliveryDate,tvOrdertype,tvLocation;
        public OrderDataModel model;
        public String orderid,orderType;
    }
private void setListData(ArrayList<OrderDataModel> param){
    this.dataModels=param;
    listView.setAdapter(new OrderlistAdater(this,param));

}


    class loadOrderTask extends AsyncTask<Void,Void,String>{

        JSONParser jparser=new JSONParser();
        String url= SupportService.domain+"forhey_mobile_scripts/access_credentials.php";
        private  String KEY_SUCCESS = "success";
        private  String KEY_ERROR = "error";
        private  String KEY_ERROR_MSG = "error_msg";

        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag","getAllOrders"));


            String json=jparser.makeHttpRequest(url, "POST", args);
            System.out.println("json item "+json);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           ArrayList<OrderDataModel>  orderDataModel1=new ArrayList<OrderDataModel>();
            try{
                JSONObject jobj=new JSONObject(s);
                int success=jobj.getInt(KEY_SUCCESS);
                if (success==1){
                    System.out.println("success "+jobj);
                    JSONArray products = jobj.getJSONArray("order_list");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject jsonObject = products.getJSONObject(i);
                        SimpleDateFormat parserSDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
                        try {
                            Date date = parserSDF.parse(jsonObject.getString("created_at"));
                            Date createdDate=new Date(date.getYear(),date.getMonth(),date.getDate());
                            Date todayDate=new Date();
                            Date compareDate=new Date(todayDate.getYear(),todayDate.getMonth(),todayDate.getDate());
                            System.out.println("item created at "+createdDate.toString());
                            System.out.println("today's date "+todayDate.toString());
                            if(createdDate.equals(compareDate)){
                                orderDataModel1.add(new OrderDataModel(jsonObject.getString("server_code")
                                        ,jsonObject.getString("pickup_point"),jsonObject.getString("pickup_date"),jsonObject.getString("status"),jsonObject.getString("created_at"),
                                        jsonObject.getString("order_type"),jsonObject.getString("client_name"),jsonObject.getString("client_phone"),jsonObject.getString("client_location")));
                            }
                        }catch (ParseException e){
                            e.printStackTrace();
                        }


                    }
                    setListData(orderDataModel1);
                }else{
                    logItem(jobj.getString(KEY_ERROR_MSG));
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
    }

    class RegisterTask extends AsyncTask<Void,Void,String >{
        String msg;
        private GoogleCloudMessaging gcm;
        private Context context;
        private static final String SENDER_ID = "773454760063";

        RegisterTask(Context context) {
            this.context = context;
        }

        JSONParser jparser=new JSONParser();
        String url="http://www.forhey.com/forhey_mobile_scripts/access_credentials.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regId=mprefs.getString("gcm_registration_id",null);
                if (regId ==null)
                {
                    regId = gcm.register(SENDER_ID);
                    System.out.println("regid " + regId);
                    List<NameValuePair> args = new ArrayList<NameValuePair>();
                    args.add(new BasicNameValuePair("tag","registerGCM"));
                    args.add(new BasicNameValuePair("gcm_key",regId));



                   // System.out.println("json item "+json);
                    msg = regId;
                    if(msg !=null){
                        String json=jparser.makeHttpRequest(url, "POST", args);
                        SharedPreferences.Editor mEditor = mprefs.edit();
                        mEditor.putString("gcm_registration_id", regId);
                        mEditor.commit();
                    }
                }else{
                    msg = regId;
                }
            }catch (IOException e){
                e.printStackTrace();
            }


            return msg;
        }
        @Override
        protected void onPostExecute(String msg) {
            if(msg !=null){
                super.onPostExecute(msg);



            }else{

            }
            new loadOrderTask().execute();


        }
    }

    private void logItem(String param) {
        Toast.makeText(this.getApplicationContext(),param,Toast.LENGTH_SHORT).show();
    }

}
