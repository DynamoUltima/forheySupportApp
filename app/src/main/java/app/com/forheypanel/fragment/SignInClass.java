package app.com.forheypanel.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

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
import app.com.forheypanel.model.User;
import app.com.forheypanel.model.UserClient;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.R;
import app.com.forheypanel.activity.HomeTabActivity;

import static android.text.TextUtils.isEmpty;

/**
 * Created by nayram on 4/5/15.
 */
public class SignInClass extends Fragment{

//    //vars
//    private FirebaseFirestore mDb;

    //FirebaseAuth
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "SignInClass";
    private ProgressBar mProgressBar;

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

    //vars
    private FirebaseFirestore mDb;


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
        mDb = FirebaseFirestore.getInstance();
        //
       // setupFirebaseAuth();






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
                            registerNewEmail(loginEmail.getText().toString(), loginPass.getText().toString());
                            //signIn();
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

//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
//        }
//    }



    /*
          ----------------------------- Firebase setup ---------------------------------
       */
//    private void setupFirebaseAuth(){
//        Log.d(TAG, "setupFirebaseAuth: started.");
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    Toast.makeText(getActivity(), "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
//
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                            .setTimestampsInSnapshotsEnabled(true)
//                            .build();
//                    db.setFirestoreSettings(settings);
//
//                    DocumentReference userRef = db.collection("Users")
//                            .document(user.getUid());
//
//                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if(task.isSuccessful()){
//                                Log.d(TAG, "onComplete: successfully set the user client.");
//                                User user = task.getResult().toObject(User.class);
//                                ((UserClient)(getContext())).setUser(user);
//                            }
//                        }
//                    });
//
////                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                    startActivity(intent);
////                    finish();
//
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };
//    }




//    private void signIn(){
//        //check if the fields are filled out
//        if(!isEmpty(loginEmail.getText().toString())
//                && !isEmpty(loginPass.getText().toString())){
//            Log.d(TAG, "onClick: attempting to authenticate.");
//
//
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail.getText().toString(),
//                    loginPass.getText().toString())
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }else{
//            Toast.makeText(getActivity(), "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void registerNewEmail(final String email, String password){

       // showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //insert some default data
                            User user = new User();
                            user.setEmail(email);
                            user.setUsername(email.substring(0, email.indexOf("@")));
                            user.setUser_id(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                    .setTimestampsInSnapshotsEnabled(true)
                                    .build();
                            mDb.setFirestoreSettings(settings);

                            DocumentReference newUserRef = mDb
                                    .collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                            newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //hideDialog();

                                    if(task.isSuccessful()){
                                        //redirectLoginScreen();
                                       // Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                                    }else{
//                                        View parentLayout = findViewById(android.R.id.content);
//                                        Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), "can't set User", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else {
                          //  View parentLayout = findViewById(android.R.id.content);
                          //  Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                           // hideDialog();
                            Toast.makeText(getContext(), "Something went wrong with creating", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
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
