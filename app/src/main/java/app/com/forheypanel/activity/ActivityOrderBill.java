package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rey.material.widget.Switch;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.model.OrderBill;
import app.com.forheypanel.model.Referral;
import app.com.forheypanel.model.Results;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 2/27/16.
 */
public class ActivityOrderBill extends BaseActivity {

    @Bind(R.id.tvServiceCost)
    TextView tvServiceCost;
    @Bind(R.id.tvBookingFee)
    TextView tvBookingFee;
    @Bind(R.id.tvSubTotal)
    TextView tvSubTotal;
    @Bind(R.id.tvPromo)
    TextView tvPromo;
    @Bind(R.id.tvTotal)
    TextView tvTotal;

    @Bind(R.id.btnOrderCancel)
    Button btnOrderCancel;
    @Bind(R.id.btnOrderBillSend)
    Button btnOrderBillSend;
    @Bind(R.id.tvReferAmt)
            TextView tvReferAmt;
    @Bind(R.id.total_discount)
            TextView total_discount;
    @Bind(R.id.llPromotion)
    LinearLayout llPromotion;

    @Bind(R.id.llReferrals)
    LinearLayout llReferrals;
    String tt;
    MaterialDialog promotionDialog,messageDialog,bookingDialog;
    View promotionView,bookingView;
    Spinner typeSpinner;
    EditText edtAmount,edtBookingFee;
    ArrayAdapter<CharSequence> adapter;
    String TAG=getClass().getName();
    int amtSel=1;
    double promo,total_disc=0.0,refer_discount=0.0,subTot,service_cost,bookingFee;
    String reg_id;
    String server_code;
    String referral_list;
    String selected_referral="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_bill);
        ButterKnife.bind(this);
        llPromotion.setVisibility(View.VISIBLE);
        llReferrals.setVisibility(View.VISIBLE);

        final Bundle bundle=getIntent().getExtras();
        tvServiceCost.setText(bundle.getString("service_cost"));
        tvBookingFee.setText(bundle.getString("booking_fee"));
        tvSubTotal.setText(bundle.getString("sub_total"));
        tvPromo.setText(bundle.getString("promo_discount"));
        tvReferAmt.setText(bundle.getString("refer_discount"));
        total_discount.setText("-"+bundle.getString("total_discount"));
        referral_list=bundle.getString("refer_list");
        total_disc=Double.parseDouble(total_discount.getText().toString());
        promo=Double.parseDouble(bundle.getString("promo_discount"));
        refer_discount=Double.parseDouble(bundle.getString("refer_discount"));
        subTot=Double.parseDouble(bundle.getString("sub_total"));
        server_code=bundle.getString("server_code");
        if (!bundle.getString("service_cost").trim().isEmpty())
        service_cost=Double.parseDouble(bundle.getString("service_cost"));
        reg_id=bundle.getString("reg_id");
        if (!bundle.getString("booking_fee").trim().isEmpty())
        bookingFee=Double.parseDouble(bundle.getString("booking_fee"));
        if (!bundle.getString("total").trim().isEmpty())
        tvTotal.setText("GHC"+bundle.getString("total"));
        tt=bundle.getString("total");
        promotionDialog=new MaterialDialog(this);
        messageDialog=new MaterialDialog(this);
        bookingDialog=new MaterialDialog(this);
        messageDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog.dismiss();
            }
        });
        promotionView= LayoutInflater.from(this).inflate(R.layout.promotion_view,null);
        bookingView=LayoutInflater.from(this).inflate(R.layout.layout_booking_fee,null);
        bookingDialog.setView(bookingView);
        typeSpinner=(Spinner)promotionView.findViewById(R.id.spnAmtType);
        edtAmount=(EditText)promotionView.findViewById(R.id.edtAmount);
        edtBookingFee=(EditText)bookingView.findViewById(R.id.edtBookFee);

        bookingDialog.setPositiveButton("Save", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtBookingFee.getText().toString().trim().isEmpty()){
                    Double bf=Double.parseDouble(edtBookingFee.getText().toString());
                    tvBookingFee.setText(String.valueOf(bf));
                    bookingFee=bf;
                    subTot=service_cost+bookingFee;
                    tvSubTotal.setText(String.valueOf(subTot));
                    Double tot=subTot + total_disc;
                    tvTotal.setText(String.valueOf(tot));
                    bookingDialog.dismiss();
                }

            }
        });
        bookingDialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingDialog.dismiss();
            }
        });
        edtAmount.setText(bundle.getString("promo"));

        OrderBill bill_obj=new Gson().fromJson(referral_list,OrderBill.class);
        for (Referral obj: bill_obj.referrals){
            if (selected_referral.trim().isEmpty()){
                selected_referral=String.valueOf(obj.id);
            }else{
                selected_referral=selected_referral+","+String.valueOf(obj.id);
            }
        }

        Log.d(TAG,"Referral ids "+selected_referral);

        adapter = ArrayAdapter.createFromResource(this, R.array.amount_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        promotionDialog.setView(promotionView);
        promotionDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amtSel==1){
                    double subTotal=subTot;

                    if (!edtAmount.getText().toString().trim().isEmpty()){
                       double promo_val=Double.parseDouble(edtAmount.getText().toString());
                        double ref=refer_discount;
                        double total_discount=promo_val+ref;
                        if (total_discount>subTotal){
                            messageDialog.setTitle("Promotion");
                            messageDialog.setMessage("The total discount can't be more than the sub total ");
                            messageDialog.show();
                        }else{
                            promo=promo_val;
                            total_disc=total_discount;
                        }
                    }

                    tvPromo.setText(String.valueOf(promo));
                    total_discount.setText("-"+String.valueOf(total_disc));
                    double total=subTotal-total_disc;
                    tvTotal.setText("GHC"+String.valueOf(total));
                    tt=String.valueOf(total);

                }else{
                    double subTotal=subTot;
//                    promo=Double.parseDouble(bundle.getString("promo"));
                    if (!edtAmount.getText().toString().trim().isEmpty()){
                        double val=Double.parseDouble(edtAmount.getText().toString());
                        if (val<=100) {
                             val = (val / 100) * subTotal;
                            double ref=refer_discount;
                            DecimalFormat df=new DecimalFormat("0.00");
                            val=Double.parseDouble(df.format(val));
                            double total_discount=val+ref;
                            if (total_discount>subTotal){
                                messageDialog.setTitle("Promotion");
                                messageDialog.setMessage("The total discount can't be more than the sub total ");
                                messageDialog.show();
                            }else{
                                promo=val;
                                total_disc=total_discount;
                            }
                        }else{
                            messageDialog.setTitle("Promotion");
                            messageDialog.setMessage("Promotion value can't be more than 100%");
                            messageDialog.show();
                        }

                    }

                    tvPromo.setText(String.valueOf(promo));
                    total_discount.setText("-"+String.valueOf(total_disc));
                    double total=subTotal-total_disc;
                    tvTotal.setText("GHC"+String.valueOf(total));
                    tt=String.valueOf(total);
                }
                promotionDialog.dismiss();
            }
        }).setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promotionDialog.dismiss();
            }
        });
        tvPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promotionDialog.show();
            }
        });

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvReferAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1=new Bundle();
                bundle1.putDouble("referral_discount",refer_discount);
                bundle1.putDouble("service_cost",service_cost);
                bundle1.putString("referral_list",referral_list);
                Intent intent=new Intent(ActivityOrderBill.this,ActivityReferralList.class);
                intent.putExtras(bundle1);
                startActivityForResult(intent,100);
            }
        });

        tvBookingFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingDialog.show();
            }
        });

        btnOrderBillSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promotionDialog.show();
                App.supportService.sendOrderBill("send_final_service_cost", server_code, tvServiceCost.getText().toString(),
                        String.valueOf(total_disc), tvBookingFee.getText().toString(), reg_id, tt,
                        selected_referral).enqueue(new Callback<Results>() {
                    @Override
                    public void onResponse(Call<Results> call, Response<Results> response) {
                        promotionDialog.dismiss();
                        if (response.body().success==1){
                            messageDialog.setTitle("Order Bill");
                            messageDialog.setMessage("Bill has been sent successfully");
                            messageDialog.show();
                        }else{
                            messageDialog.setTitle("Order Bill");
                            messageDialog.setMessage(response.body().error_msg);
                            messageDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Results> call, Throwable t) {
                        messageDialog.setTitle("Order Bill");
                        messageDialog.setMessage("Sorry, failed to send the bill. Please try again");
                        messageDialog.show();
                    }
                });
                }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode==RESULT_OK){
                refer_discount=data.getDoubleExtra("referral_discount_res",0.0);
                referral_list=data.getStringExtra("referral_list_res");
                selected_referral=data.getStringExtra("selected_referral");
                total_disc=promo+refer_discount;
                tvReferAmt.setText(String.valueOf(refer_discount));
                total_discount.setText("-"+String.valueOf(total_disc));
                double tot=subTot-total_disc;
                tvTotal.setText(String.valueOf("GHC"+tot));
                tt=String.valueOf(tot);
                Log.e(TAG,"selected ids "+selected_referral);
            }
        }
    }
}
