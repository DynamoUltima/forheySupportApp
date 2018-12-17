package app.com.forheypanel.activity;

import android.app.ProgressDialog;
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

import com.rey.material.widget.Switch;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Results;
import app.com.forheypanel.model.SuccessClass;
import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import me.drakeet.materialdialog.MaterialDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 10/26/15.
 */


public class ActivityPromotion extends AppCompatActivity {
    EditText edtPromoCode,edtValue,edtMsg;
    Button btnSend;
    TextView tvDialogTitle,tvDialogMessage;
    MaterialDialog dialogError,dialogconfirmation;
    ConnectionDetector connectionDetector;
    String []amtType;
    boolean txtMsg=false,pushNoti=true;
    CheckBox chkNotification,chkTxtMsg,chkActive,chkFree;
    Spinner spnAmtType;
    int promo_type=2,active=1;
    ArrayAdapter<CharSequence>adapter;
    String TAG=getClass().getName();
    View view_summary;
    MaterialDialog summaryDialog;
    TextView tvCode,tvMessage;
    ProgressDialog progressDialog;
    Switch swActive,swNotification,swTxtMsg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Promotion");
        edtPromoCode=(EditText)findViewById(R.id.edtCode);
        edtValue=(EditText)findViewById(R.id.edtValue);
        edtMsg=(EditText)findViewById(R.id.edtMessage);
        btnSend=(Button)findViewById(R.id.btnSend);
        chkActive=(CheckBox)findViewById(R.id.chkActive);
        chkNotification=(CheckBox)findViewById(R.id.chkNotification);
        chkTxtMsg=(CheckBox)findViewById(R.id.chkTxtMsg);
        spnAmtType=(Spinner)findViewById(R.id.spnAmtType);
        chkFree=(CheckBox)findViewById(R.id.chkFree);
        progressDialog=new ProgressDialog(this);
        summaryDialog=new MaterialDialog(this);
        view_summary=LayoutInflater.from(this).inflate(R.layout.promotion_summary,null);
        tvCode=(TextView)view_summary.findViewById(R.id.tvCode);
        tvMessage=(TextView)view_summary.findViewById(R.id.tvMessage);
        swActive=(Switch)view_summary.findViewById(R.id.swActive);
        swNotification=(Switch)view_summary.findViewById(R.id.swNotification);
        swTxtMsg=(Switch)view_summary.findViewById(R.id.swTxtMsg);
        summaryDialog.setView(view_summary);

        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Adding promotion");
        dialogError=new MaterialDialog(this);
        connectionDetector=new ConnectionDetector(this);
        dialogconfirmation=new MaterialDialog(this);
        amtType=getResources().getStringArray(R.array.amount_type);
        LayoutInflater mInflater = (LayoutInflater)
                this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View dialogView=mInflater.inflate(R.layout.dialog_layout,null);
        View confirmationView=mInflater.inflate(R.layout.dialog_layout,null);
        tvDialogTitle=(TextView)dialogView.findViewById(R.id.tvDialogTitle);
        tvDialogMessage=(TextView)dialogView.findViewById(R.id.tvDialogText);
        dialogError.setView(dialogView);
        adapter = ArrayAdapter.createFromResource(this, R.array.amount_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAmtType.setAdapter(adapter);

        spnAmtType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    promo_type = 2;
                } else {
                    promo_type = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialogError.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.dismiss();
            }
        });
        summaryDialog.setPositiveButton("Save", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summaryDialog.dismiss();
                sendPromotion();

            }
        });
        summaryDialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summaryDialog.dismiss();
            }
        });

        swActive.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean b) {

                if (b) {
                    active = 1;
                    swActive.setChecked(true);
//                chkTxtMsg.setChecked(true);
                    chkTxtMsg.setEnabled(true);
//                chkNotification.setChecked(true);
                    chkNotification.setEnabled(true);
                    pushNoti = false;
                    swNotification.setEnabled(true);
                    swTxtMsg.setEnabled(true);
                    chkActive.setChecked(true);
                } else {
                    active = 0;
                    chkActive.setChecked(false);

                    swActive.setChecked(false);
                    chkTxtMsg.setChecked(false);
                    chkTxtMsg.setEnabled(false);
                    chkNotification.setChecked(false);
                    chkNotification.setEnabled(false);
                    txtMsg = false;
                    pushNoti = false;
                    swNotification.setChecked(false);
                    swTxtMsg.setChecked(false);
                    swNotification.setEnabled(false);
                    swTxtMsg.setEnabled(false);
                }
            }
        });

        swTxtMsg.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked){
                    txtMsg=checked;
                }
            }
        });

        swNotification.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked){
                    pushNoti=checked;
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    if (validator()) {
                        tvCode.setText(edtPromoCode.getText().toString());
                        tvMessage.setText(edtMsg.getText().toString());
                        summaryDialog.show();
                    }
                }else{
                    tvDialogTitle.setText("Internet Connection");
                    tvDialogMessage.setText("Please check your internet connection");
                    dialogError.show();
                }
            }
        });

        chkFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    promo_type = 1;
                    edtValue.setEnabled(false);
                    spnAmtType.setEnabled(false);
                } else {
                    promo_type = 2;
                    edtValue.setEnabled(true);
                    spnAmtType.setEnabled(true);
                    spnAmtType.setSelection(0);

                }
            }
        });

        chkTxtMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtMsg=b;
            }
        });

        chkNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pushNoti=b;
            }
        });

    chkActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                active=1;
                swActive.setChecked(true);
//                chkTxtMsg.setChecked(true);
                chkTxtMsg.setEnabled(true);
//                chkNotification.setChecked(true);
                chkNotification.setEnabled(true);
                pushNoti=false;

                swNotification.setEnabled(true);
                swTxtMsg.setEnabled(true);
                swActive.setChecked(true);
            }else{
                active=0;
                swActive.setChecked(false);
                chkTxtMsg.setChecked(false);
                chkTxtMsg.setEnabled(false);
                swActive.setChecked(false);
                chkNotification.setChecked(false);
                chkNotification.setEnabled(false);
                txtMsg=false;
                pushNoti=false;

                swNotification.setChecked(false);
                swTxtMsg.setChecked(false);
                swNotification.setEnabled(false);
                swTxtMsg.setEnabled(false);
            }
        }
    });

    }

    boolean validator(){
        boolean result=true;
        if (edtPromoCode.getText().toString().trim().isEmpty()){
            result=false;
            tvDialogTitle.setText("Promotion Code");
            tvDialogMessage.setText("Please enter the promotion code");
            dialogError.show();
        }else if (!chkFree.isChecked()){
            if(edtValue.getText().toString().trim().isEmpty()){
                result=false;
                tvDialogTitle.setText("Enter Amount");
                tvDialogMessage.setText("Please enter the amount");
                dialogError.show();
            }
        }else if (edtMsg.getText().toString().isEmpty()){
                result=false;
                tvDialogTitle.setText("Message");
                tvDialogMessage.setText("Please enter the promotion message");
                dialogError.show();

        }
        return result;
    }

    void sendPromotion(){
        progressDialog.show();
        App.supportService.addPromotion("add_promotion", edtValue.getText().toString(), promo_type,
                edtPromoCode.getText().toString(), pushNoti, active, txtMsg, edtMsg.getText().toString()).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                progressDialog.dismiss();
                Log.e(TAG,"Success "+response.body().toString());
                if (response.body().success == 1) {
                    tvDialogTitle.setText("Promotion");
                    tvDialogMessage.setText("Promotion added successfully");
                    dialogError.show();
                    setResult(5);
                } else {
                    tvDialogTitle.setText("Promotion");
                    tvDialogMessage.setText(response.body().error_msg);
                    dialogError.show();
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                progressDialog.dismiss();
                tvDialogTitle.setText("Promotion");
                tvDialogMessage.setText("Sorry, failed to add promotion");
                dialogError.show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
