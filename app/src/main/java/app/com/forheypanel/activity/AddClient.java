package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.R;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nayram on 6/24/15.
 */
public class AddClient extends BaseActivity {
    private EditText etFullName,etPhone,etAddress,etLocation,etEmail;
    private Button btnSend;
    private MaterialDialog errorDialog;
    private ProgressDialog progressDialog;
    private SharedPreferences mprefs;
    private static String KEY_SUCCESS = "success";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pattern = Pattern.compile(EMAIL_PATTERN);
        setContentView(R.layout.add_client);
       ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayUseLogoEnabled(true);
        if (actionBar !=null)
        actionBar.setDisplayHomeAsUpEnabled(true);
        etFullName=(EditText)findViewById(R.id.etClientName);
        etPhone=(EditText)findViewById(R.id.etClientPhone);
        etAddress=(EditText)findViewById(R.id.etClientAddress);
        etEmail=(EditText)findViewById(R.id.etClientEmail);
        etLocation=(EditText)findViewById(R.id.etClientLocation);
        btnSend=(Button)findViewById(R.id.btnAddClient);
        mprefs=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        progressDialog=new ProgressDialog(this);
         errorDialog =new MaterialDialog(this);


        errorDialog.setTitle("Error!").setMessage("Please provide inputs for all fields. the email field is optional")
                .setPositiveButton("ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        errorDialog.dismiss();
                    }
                });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFullName.getText().toString().isEmpty() && !etPhone.getText().toString().isEmpty() && !etAddress.getText().toString().isEmpty() && !etLocation.getText().toString().isEmpty()){
                    if (!etEmail.getText().toString().isEmpty()){
                        if (validate(etEmail.getText().toString())){
                            new AddClientTask().execute();
                        }else{
                            errorDialog.setTitle("Error!").setMessage("Email address is in the wrong format ");
                            errorDialog.show();
                        }
                    }else{
                        new AddClientTask().execute();
                    }

                }else{

                    errorDialog.show();

                }
            }
        });



    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==R.id.mn_place_order){
            startActivity(new Intent(this,PlaceClientOrder.class));
        }else{
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;

    }

    class AddClientTask extends AsyncTask<Void,Void,String>{
        JSONParser jparser=new JSONParser();
        String url= SupportService.domain+"forhey_mobile_scripts/forhey_order.php";
        String emailP= mprefs.getString("email",null);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Sending  ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            if(s!=null && !s.isEmpty()){

                super.onPostExecute(s);
                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);
                    progressDialog.dismiss();
                    if(success == 1){

                       errorDialog.setTitle("Client added").setMessage("Client added successfully").show();
                        etEmail.getText().clear();
                        etLocation.getText().clear();
                        etAddress.getText().clear();
                        etFullName.getText().clear();
                        etPhone.getText().clear();
                    }else{

                        errorDialog.setTitle("Failed").setMessage(jobj.getString("error_msg")).show();

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    errorDialog.setTitle("Failed").setMessage("Failed to get response from server").show();
                }

            }else{
                errorDialog.setTitle("Failed").setMessage("Failed to get response from server").show();

            }
            progressDialog.dismiss();


        }

        @Override
        protected String doInBackground(Void... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("name",etFullName.getText().toString()));
            args.add(new BasicNameValuePair("phone",etPhone.getText().toString()));
            args.add(new BasicNameValuePair("address",etAddress.getText().toString()));
            args.add(new BasicNameValuePair("location",etLocation.getText().toString()));
            args.add(new BasicNameValuePair("official",emailP));
            args.add(new BasicNameValuePair("email",etEmail.getText().toString()));
            args.add(new BasicNameValuePair("tag","offline_client"));
            System.out.println("phone " + etPhone.getText().toString());
            String json=jparser.makeHttpRequest(url,"POST",args);
            System.out.println("result "+json);
            return json;
        }
    }
}
