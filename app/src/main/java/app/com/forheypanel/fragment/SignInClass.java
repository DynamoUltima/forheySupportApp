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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.com.forheypanel.activity.ActivityCheckin;
import app.com.forheypanel.activity.ActivityHome;
import app.com.forheypanel.alarmSettings.AlarmReceiver;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.R;
import app.com.forheypanel.activity.HomeTabActivity;

/**
 * Created by nayram on 4/5/15.
 */
public class SignInClass extends Fragment{
    SignInClass.OnSignUpSelected mCallback;
    EditText loginEmail,loginPass;
    Button btnSignIn,btnSignReg;
    private SharedPreferences mprefs;
    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private ProgressDialog pDialog;
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    GoogleCloudMessaging gcm;


    public interface OnSignUpSelected{
        public void onSingup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.login_layout,container,false);
        pattern = Pattern.compile(EMAIL_PATTERN);
        loginEmail=(EditText)rootView.findViewById(R.id.etLogEmail);
        loginPass=(EditText)rootView.findViewById(R.id.etLogPassword);
        btnSignIn=(Button)rootView.findViewById(R.id.btnSignIn);
        btnSignReg=(Button)rootView.findViewById(R.id.btnSignInReg);
        mprefs = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        final boolean loggedIn=mprefs.getBoolean("isLoggedIn",false);
        pDialog=new ProgressDialog(getActivity());
        if(loggedIn){
            String role=mprefs.getString("role","");
            if (role.equalsIgnoreCase("heygirl")){
                Intent intent=new Intent(getActivity(),ActivityHome.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }else{
                Intent intent=new Intent(getActivity(),HomeTabActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }/*
            Intent intent =new Intent(getActivity(),HomeTabActivity.class);
            startActivity(intent);*/

        }
        btnSignReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSingup();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
                if (connectionDetector.isConnectingToInternet()){
                    if (!loginEmail.getText().toString().isEmpty() && !loginPass.getText().toString().isEmpty()){
                        if (validate(loginEmail.getText().toString())){
                            new SingInTask().execute();
                        }else{
                            logitem("Please check your email address");
                        }

                    }else{
                        logitem("Please enter your email address and password");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback=(OnSignUpSelected) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnSignUpSelectedListener");
        }
    }

    private void logitem(String param){
        Toast.makeText(getActivity().getApplicationContext(),param,Toast.LENGTH_SHORT).show();
    }
    public boolean validate(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();

    }


    class SingInTask extends AsyncTask<Void,Void,String>{
       private String url= "http://www.forhey.com/forhey_mobile_scripts/access_credentials.php";
       private JSONParser jsonParser=new JSONParser();


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
            //Calendar calendar=Calendar.getInstance();
            final String SENDER_ID = "270876920187";
            String json="";

            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(getActivity());
            }
            try{
                String regId = gcm.register(SENDER_ID);
                System.out.println("regid " + regId);
                SharedPreferences.Editor mEditor=mprefs.edit();
                mEditor.putString("gcm_registration_id",regId);
                mEditor.commit();
                List<NameValuePair> args = new ArrayList<NameValuePair>();
                args.add(new BasicNameValuePair("tag","admin_login"));
                args.add(new BasicNameValuePair("email", loginEmail.getText().toString()));
                args.add(new BasicNameValuePair("password", loginPass.getText().toString()));
                args.add(new BasicNameValuePair("gcm", regId));

                json=jsonParser.makeHttpRequest(url, "POST", args);
                System.out.println("json item " + json);
                return json;
            }catch (IOException e){
                e.printStackTrace();
            }


           return json;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!= null && !s.isEmpty()){
                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);

                    if(success ==1){
                        JSONObject json_user = jobj.getJSONObject("user");
                        SharedPreferences.Editor mEditor=mprefs.edit();
                        mEditor.putString("email",json_user.getString("email"));
                        mEditor.putString("name",json_user.getString("name"));
                        mEditor.putString("role",json_user.getString("role"));
                        mEditor.putString("image",json_user.getString("image"));
                        mEditor.putBoolean("isLoggedIn",true);
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
                    pDialog.dismiss();
                    logitem("Sign in failed");
                }
            }else{
                logitem("Sign in failed");
            }

            pDialog.dismiss();
        }
    }
}
