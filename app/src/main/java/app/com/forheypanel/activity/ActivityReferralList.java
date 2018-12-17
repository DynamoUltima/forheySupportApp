package app.com.forheypanel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.model.OrderBill;
import app.com.forheypanel.model.Referral;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nayram on 4/11/16.
 */
public class ActivityReferralList extends BaseActivity {

    @Bind(R.id.lvReferralList)
    ListView lvReferralList;

    @Bind(R.id.tvAmount)
    TextView tvAmt;

    @Bind(R.id.btnSave)
    Button btnSave;


    String referralList;
    OrderBill referal_obj;
    double service_cost;
    double referral_discount;
    String selected_referral="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_list);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        referralList=bundle.getString("referral_list");
        service_cost=bundle.getDouble("service_cost");
        referral_discount=bundle.getDouble("referral_discount");
        tvAmt.setText("ReferralDiscount : "+String.valueOf(referral_discount));
        referal_obj=new Gson().fromJson(referralList,OrderBill.class);
        ReferralListAdapter adapter=new ReferralListAdapter(this);
        lvReferralList.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data=getIntent();
                data.putExtra("referral_list_res",new Gson().toJson(referal_obj));
                data.putExtra("referral_discount_res",referral_discount);
                for (Referral obj:referal_obj.referrals){
                    if (obj.isSelected){
                        if (selected_referral.trim().isEmpty()){
                            selected_referral=String.valueOf(obj.id);
                        }else{
                            selected_referral=selected_referral+","+obj.id;
                        }
                    }
                }
                data.putExtra("selected_referral",selected_referral);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

    class ReferralListAdapter extends BaseAdapter {

        Context context;

        public ReferralListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView==null){
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.referral_item_layout, null);
                holder.chkAdd=(CheckBox)convertView.findViewById(R.id.chkBoxAdd);
                holder.tvName=(TextView)convertView.findViewById(R.id.tvName);
                holder.tvMessage=(TextView)convertView.findViewById(R.id.tvMessage);

                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(referal_obj.referrals.get(position).name);
            holder.tvMessage.setText(referal_obj.referrals.get(position).token);
            holder.chkAdd.setChecked(referal_obj.referrals.get(position).isSelected);
            holder.chkAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    referal_obj.referrals.get(position).isSelected=b;
                    if (b){
                        Referral obj=referal_obj.referrals.get(position);
                        if (obj.type==1){
                            referral_discount=referral_discount+obj.value;
                            tvAmt.setText("ReferralDiscount : "+String.valueOf(referral_discount));
                        }else{
                            double perc=(obj.value/100)*service_cost;
                            referral_discount=referral_discount+perc;
                        }
                    }else{
                        Referral obj=referal_obj.referrals.get(position);
                        if (obj.type==1){
                            referral_discount=referral_discount-obj.value;
                            tvAmt.setText("ReferralDiscount : "+String.valueOf(referral_discount));
                        }else{
                            double perc=(obj.value/100)*service_cost;
                            referral_discount=referral_discount-perc;
                            tvAmt.setText("ReferralDiscount : "+String.valueOf(referral_discount));
                        }
                    }

                }
            });
            return convertView;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return referal_obj.referrals.get(i);
        }

        @Override
        public int getCount() {
            return referal_obj.referrals.size();
        }

        class ViewHolder {
            TextView tvName,tvMessage;
            CheckBox chkAdd;
        }
    }
}
