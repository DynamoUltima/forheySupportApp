package app.com.forheypanel.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.com.forheypanel.activity.ActivityHome;
import app.com.forheypanel.alarmSettings.AlarmReceiver;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.R;
import app.com.forheypanel.activity.HomeTabActivity;

/**
 * Created by nayram on 4/5/15.
 */
public class RegisterFragment  extends Fragment{
    EditText emailAddress,password,name,phone;
    Button btnReg;
    private SharedPreferences mprefs;
    private Pattern pattern;
    private Matcher matcher;
    final Calendar cal = Calendar.getInstance();
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    ProgressDialog pDialog;
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    String email,ph,passwd,nme;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.register,container,false);
        pattern = Pattern.compile(EMAIL_PATTERN);
        emailAddress=(EditText)rootView.findViewById(R.id.etRegEmailAddress);
        password=(EditText)rootView.findViewById(R.id.etRegPassword);
        name=(EditText)rootView.findViewById(R.id.etRegName);
        btnReg=(Button)rootView.findViewById(R.id.btnRegister);
        phone=(EditText)rootView.findViewById(R.id.etLogPhone);
        mprefs=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        pDialog=new ProgressDialog(getActivity());

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector conection=new ConnectionDetector(getActivity());
                if (conection.isConnectingToInternet()){
                    if (!emailAddress.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()&& !name.getText().toString().trim().isEmpty() && !phone.getText().toString().trim().isEmpty()){
                        if(validate(emailAddress.getText().toString())){
                            String []parts=emailAddress.getText().toString().split("@");
                            if (parts[1].equals("forhey.com")){
                                if(phone.getText().toString().length()>=10){
                                    nme=name.getText().toString();
                                    ph=phone.getText().toString();
                                    passwd=password.getText().toString();
                                    email=emailAddress.getText().toString();
                                    new SignUpTask(getActivity()).execute();

                                }else{
                                    logitem("Please check your phone number");
                                }
                            }else{
                                logitem("Please register with your forhey email address");
                            }

                        }else{
                            logitem("Please check your email address");
                        }

                    }else{
                        logitem("Please provide us with all the required fields");
                    }

                }else{
                    logitem("Please check your internet connection");
                }


            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public boolean validate(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void logitem(String param){
        Toast.makeText(getActivity().getApplicationContext(),param,Toast.LENGTH_SHORT).show();
    }

    class SignUpTask extends AsyncTask<Void,Void,String>{
      private String url="http://www.forhey.com/forhey_mobile_scripts/access_credentials.php";
        private JSONParser jsonParser=new JSONParser();

        GoogleCloudMessaging gcm;
        private Context ctx;

        SignUpTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String param) {
            super.onPostExecute(param);
            if(param!= null && !param.isEmpty()){
                try{
                    JSONObject jobj=new JSONObject(param);
                    int success=jobj.getInt(KEY_SUCCESS);

                    if(success ==1){
                        //JSONObject json_user = jobj.getJSONObject("user");
                        JSONObject json_user = jobj.getJSONObject("user");
                        SharedPreferences.Editor mEditor = mprefs.edit();
                        mEditor.putString("email", emailAddress.getText().toString());
                        mEditor.putString("name",name.getText().toString());
                        mEditor.putBoolean("isLoggedIn", true);
                        mEditor.putString("role", json_user.getString("role"));
                        mEditor.commit();
                        String role=mprefs.getString("role","");
                        if (role.contains("heygirl")){
                            new AlarmReceiver().setAlarmMgr(getActivity());
                            Intent intent=new Intent(getActivity(),ActivityHome.class);
                            getActivity().startActivity(intent);
                        }else{
                            Intent intent=new Intent(getActivity(),HomeTabActivity.class);
                            getActivity().startActivity(intent);
                        }

                        getActivity().finish();
                    }else{
                        logitem(jobj.getString(KEY_ERROR_MSG));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }else{
                logitem("failed to register account");
            }

            pDialog.dismiss();

        }

        @Override
        protected String doInBackground(Void... params) {


            final String SENDER_ID = "270876920187";
            String json=null;
            Calendar calendar=Calendar.getInstance();
         try{
             if (gcm == null) {
                 gcm = GoogleCloudMessaging.getInstance(ctx);
             }
             System.out.println("GCM "+gcm);
             String regId=null;
             if (regId ==null){
                 regId = gcm.register(SENDER_ID);
                 System.out.println("regid " + regId);
                 SharedPreferences.Editor mEditor=mprefs.edit();
                 mEditor.putString("gcm_registration_id",regId);
                 mEditor.commit();

             }
             System.out.println(" reg gcm "+regId);
             if (regId != null){

                 List<NameValuePair> args = new ArrayList<NameValuePair>();
                 args.add(new BasicNameValuePair("tag","create_admin"));
                 args.add(new BasicNameValuePair("email",email));
                 args.add(new BasicNameValuePair("password",passwd));
                 args.add(new BasicNameValuePair("phone",ph));
                 args.add(new BasicNameValuePair("name",nme));
                 args.add(new BasicNameValuePair("created_at",calendar.getTime().toString()));
                 args.add(new BasicNameValuePair("updated_at",calendar.getTime().toString()));
                 args.add(new BasicNameValuePair("gcm_regid",regId));

                 json=jsonParser.makeHttpRequest(url, "POST", args);
                 System.out.println(" json "+json);
             }
         }catch (IOException e){
             e.printStackTrace();
         }



            return json;
        }


    }
}
