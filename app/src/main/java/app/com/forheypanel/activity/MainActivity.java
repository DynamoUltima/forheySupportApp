package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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

import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.model.OrderDataModel;
import app.com.forheypanel.adapter.OrderlistAdater;
import app.com.forheypanel.R;


public class MainActivity extends BaseActivity {
   private ListView listView;
    private ArrayList<OrderDataModel>dataModels;
    SharedPreferences mprefs;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView);
        pDialog=new ProgressDialog(MainActivity.this);
        mprefs = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        ConnectionDetector conection=new ConnectionDetector(this);
        if (conection.isConnectingToInternet()){
         //   new RegisterTask(this).execute();
            new loadOrderTask().execute();
        }else{
            logItem("Please check your internet connection");
        }


    }


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
