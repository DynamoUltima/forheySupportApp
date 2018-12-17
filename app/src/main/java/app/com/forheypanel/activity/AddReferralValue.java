package app.com.forheypanel.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.JSONParser;
import me.drakeet.materialdialog.MaterialDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 10/22/15.
 */
public class AddReferralValue extends AppCompatActivity {

    EditText edtValue,edtMessage;
    CheckBox chkTxtMsg,chkNotification;
    Button btnSend;
    boolean chkNot=true,chkTxt=false;
    ProgressDialog pDialog;
    String url= SupportService.domain+"forhey_mobile_scripts/access_credentials.php";
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    View dialogView;
    MaterialDialog materialDialog,confirmDialog;
    TextView tvDialogTitle,tvDialogText;
    ConnectionDetector connectionDetector;
    String TAG=getClass().getName();
    Spinner spnAmtType;
    ArrayAdapter<CharSequence>adapter;
    int amtSel=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        edtValue=(EditText)findViewById(R.id.edtAmount);
        edtMessage=(EditText)findViewById(R.id.edtMessage);
        chkNotification=(CheckBox)findViewById(R.id.chkNotification);
        chkTxtMsg=(CheckBox)findViewById(R.id.chkTxtMsg);
        btnSend=(Button)findViewById(R.id.btnSend);
        spnAmtType=(Spinner)findViewById(R.id.spnAmtType);

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connectionDetector=new ConnectionDetector(this);
        materialDialog=new MaterialDialog(this);
        confirmDialog=new MaterialDialog(this);
        LayoutInflater mInflater = (LayoutInflater)
                this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        dialogView=mInflater.inflate(R.layout.dialog_layout,null);
        tvDialogText=(TextView)dialogView.findViewById(R.id.tvDialogText);
        tvDialogTitle=(TextView)dialogView.findViewById(R.id.tvDialogTitle);
        materialDialog.setView(dialogView);
        adapter = ArrayAdapter.createFromResource(this, R.array.amount_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAmtType.setAdapter(adapter);

        materialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        confirmDialog.setTitle("Confirm Amount");
        confirmDialog.setMessage("Are you sure you want to save and send the referral value?");
        confirmDialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                pDialog.show();
//                new ReferralAddTask(edtValue.getText().toString(), edtMessage.getText().toString(), "1").execute();
                App.supportService.updateReferalReward("add_referral_reward",edtValue.getText().toString(),
                        edtMessage.getText().toString(), amtSel, chkTxt, chkNot).enqueue(new Callback<Order.Referal>() {
                    @Override
                    public void onResponse(Call<Order.Referal> call, Response<Order.Referal> response) {
                        if (response.body().success == 1) {
                            tvDialogTitle.setText("Success!");
                            tvDialogText.setText(response.body().success_message);
                            materialDialog.show();
                            setResult(5);
                        } else {
                            tvDialogTitle.setText("Failed!");
                            tvDialogText.setText(response.body().error_msg);
                            materialDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order.Referal> call, Throwable t) {
                        pDialog.dismiss();
                    }

                });

            }
        });
        confirmDialog.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        chkTxtMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Add Referral", "Tet message " + String.valueOf(isChecked));
                chkTxt = isChecked;
            }
        });
        chkNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Add Referral","Notification "+String.valueOf(isChecked));
                chkNot=isChecked;
            }
        });

        spnAmtType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    amtSel = 1;
                } else {
                    amtSel = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    if (!edtValue.getText().toString().isEmpty())
                        if (!edtMessage.getText().toString().isEmpty()) {
                            if(chkNot||chkTxt) {
                            confirmDialog.show();

                            }else{
                                tvDialogText.setText("Sorry, you have to select at least one option ");
                                tvDialogTitle.setText("Attention!!");
                                materialDialog.show();
                            }
                        }else{
                            tvDialogText.setText("Sorry, You have to compose a message");
                            tvDialogTitle.setText("Compose Message");
                            materialDialog.show();
                        }
                    else{
                            tvDialogText.setText("Sorry, You have to enter an amount");
                            tvDialogTitle.setText("Amount value");
                            materialDialog.show();
                        }
                }else{
                    tvDialogTitle.setText("Internet Connection");
                    tvDialogText.setText("No internet connection");
                    materialDialog.show();
                }
            }
        });
    }



    class ReferralAddTask extends AsyncTask<Void,Void,String>{

        String amount,message,type;
        JSONParser jsonParser=new JSONParser();

        public ReferralAddTask(String amount, String message, String type) {
            this.amount = amount;
            this.message = message;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            if(s!=null && !s.isEmpty()) {
                super.onPostExecute(s);
                try {
                    JSONObject jobj = new JSONObject(s);
                    int success = jobj.getInt(KEY_SUCCESS);
                    if (success == 1) {
                        tvDialogTitle.setText("Success!");
                        tvDialogText.setText(jobj.getString("success_message"));
                        materialDialog.show();
                    }else {
                        tvDialogTitle.setText("Failed!");
                        tvDialogText.setText(jobj.getString("error_msg"));
                        materialDialog.show();
                    }
                }catch (JSONException ex){
                    pDialog.dismiss();
                    ex.printStackTrace();
                }
            }
            pDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag","add_referral_reward"));
            args.add(new BasicNameValuePair("value", amount));
            args.add(new BasicNameValuePair("message",message));
            args.add(new BasicNameValuePair("category",type));
            args.add(new BasicNameValuePair("txtMsg", String.valueOf(chkTxt)));
            args.add(new BasicNameValuePair("notMsg",String.valueOf(chkNot)));
            String json=jsonParser.makeHttpRequest(url, "POST", args);
            System.out.println("json item "+json);
            return json;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
