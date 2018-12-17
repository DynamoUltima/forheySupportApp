package app.com.forheypanel.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.FactorDateClass;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.model.OrderDataModel;
import app.com.forheypanel.adapter.OrderlistAdater;
import app.com.forheypanel.R;

/**
 * Created by nayram on 4/25/15.
 */
public class AllOrders extends Fragment {

    private ListView listView;
    public static AllOrders instance;
    private ArrayList<OrderDataModel> dataModels;
    ProgressDialog pDialog;
    private ProgressWheel progressWheel;
    private FactorDateClass factorDate;

    public static Fragment newInstance() {
        AllOrders fragment = new AllOrders();
        return  fragment;
    }

    private PullToRefreshListView mPullRefreshListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_main,container,false);
        instance=this;
        final ConnectionDetector conection=new ConnectionDetector(getActivity());
        mPullRefreshListView=(PullToRefreshListView)rootView.findViewById(R.id.listView);
        progressWheel = (ProgressWheel)rootView.findViewById(R.id.progress_wheel);
        pDialog=new ProgressDialog(getActivity());
        factorDate=new FactorDateClass();
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                //Log.d(PullToRefreshCustomLoadingLayoutActivity.class.getCanonicalName(), "onRefreshing..." + label);

                // Do work to refresh the list here.
                if (conection.isConnectingToInternet()){

                    new GetData().execute();
                }else {
                    logItem("Please check your internet connection");
                }
            }
        });



        if (conection.isConnectingToInternet()){
            //   new RegisterTask(this).execute();
            progressWheel.setVisibility(View.VISIBLE);
            new loadOrderTask().execute();
        }else{
            logItem("Please check your internet connection");
        }
        return rootView;
    }

    private void setListData(ArrayList<OrderDataModel> param){
        this.dataModels=param;
        mPullRefreshListView.setAdapter(new OrderlistAdater(getActivity(), param));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode==getActivity().RESULT_OK){
                progressWheel.setVisibility(View.VISIBLE);
                new loadOrderTask().execute();
            }
        }

    }

    class loadOrderTask extends AsyncTask<Void,Void,String> {

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
            //pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag", "getAllOrders"));


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
                    for (int i = products.length()-1; i>=0; i--) {
                        JSONObject jsonObject = products.getJSONObject(i);

                        try {

                                if (jsonObject.getString("client_name").equals("cobba")){
                                    Log.e("All Order "," cobba created at "+jsonObject.getString("created_at"));
                                }
                                orderDataModel1.add(new OrderDataModel(jsonObject.getString("server_code")
                                        ,jsonObject.getString("pickup_point"),factorDate.factorDate(jsonObject.getString("pickup_date")),jsonObject.getString("status"),jsonObject.getString("created_at"),
                                        jsonObject.getString("order_type"),jsonObject.getString("client_name"),jsonObject.getString("client_phone"),jsonObject.getString("client_location")));

                        }catch (Exception e){
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
            progressWheel.setVisibility(View.GONE);
            //pDialog.dismiss();
        }
    }

    private void logItem(String param) {
        Toast.makeText(getActivity(), param, Toast.LENGTH_SHORT).show();
    }

    class GetData extends AsyncTask<Void,Void,String> {

        JSONParser jparser=new JSONParser();
        String url=SupportService.domain+"forhey_mobile_scripts/access_credentials.php";
        private  String KEY_SUCCESS = "success";
        private  String KEY_ERROR = "error";
        private  String KEY_ERROR_MSG = "error_msg";

        @Override
        protected void onPreExecute() {

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
                    for (int i =products.length()-1 ; i >= 0; i--) {
                        JSONObject jsonObject = products.getJSONObject(i);
                        try {
                                orderDataModel1.add(new OrderDataModel(jsonObject.getString("server_code")
                                        ,jsonObject.getString("pickup_point"),factorDate.factorDate(jsonObject.getString("pickup_date")),jsonObject.getString("status"),jsonObject.getString("created_at"),
                                        jsonObject.getString("order_type"),jsonObject.getString("client_name"),jsonObject.getString("client_phone"),jsonObject.getString("client_location")));

                        }catch (Exception e){
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
            mPullRefreshListView.onRefreshComplete();
            //pDialog.dismiss();

        }
    }

}
