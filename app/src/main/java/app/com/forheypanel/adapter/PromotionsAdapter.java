package app.com.forheypanel.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Promotion;
import app.com.forheypanel.model.Referal;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import me.drakeet.materialdialog.MaterialDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Switch;

/**
 * Created by nayram on 12/29/15.
 */
public class PromotionsAdapter extends RecyclerView.Adapter<PromotionsAdapter.ViewHolder> {

    private ArrayList<Promotion> items;
    public Activity context;
    String TAG=getClass().getName();
    MaterialDialog dialog;
    View view_dialog;
    TextView tvCode,tvMessage,tvValue;
    Switch swActive;
    Promotion promotion_obj;
    ConnectionDetector connectionDetector;
    ProgressDialog progressDialog;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;

        public final TextView tvCode,tvMessage,tvActive;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvCode = (TextView) view.findViewById(R.id.tvCode);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvActive = (TextView) view.findViewById(R.id.tvActive);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvCode.getText();
        }

    }


    public PromotionsAdapter(final Activity context ,ArrayList<Promotion>items) {
        this.items=items;
        this.context = context;
        connectionDetector=new ConnectionDetector(context);
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Updating Promo...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        dialog=new MaterialDialog(context);
        view_dialog=LayoutInflater.from(context).inflate(R.layout.layout_promo_summary,null);
        tvCode=(TextView)view_dialog.findViewById(R.id.tvCode);
        tvMessage=(TextView)view_dialog.findViewById(R.id.tvMessage);
        tvValue=(TextView)view_dialog.findViewById(R.id.tvValue);
        swActive=(Switch)view_dialog.findViewById(R.id.swActive);
        dialog.setView(view_dialog);
        dialog.setPositiveButton("Update", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (connectionDetector.isConnectingToInternet()){
                    if (promotion_obj!=null){
                        progressDialog.show();
                        App.supportService.updatePromotion("update_promotion", promotion_obj.id, promotion_obj.active).enqueue(new Callback<Referal>() {
                            @Override
                            public void onResponse(Call<Referal> call, Response<Referal> response) {
                                progressDialog.dismiss();
                                if (response.body().success==1){
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context,"Sorry, Failed to update promotion",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Referal> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(context,"Sorry, Failed to update promotion",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }else{
                    Toast.makeText(context,"No Internet Connection!!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final Promotion promotion=items.get(position);
        holder.tvCode.setText(promotion.code);
        holder.tvMessage.setText(promotion.message);
        if (promotion.active==1){
            holder.tvActive.setTextColor(context.getResources().getColor(R.color.blue));
            holder.tvActive.setText("Active");

        }else{
            holder.tvActive.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvActive.setText("Inactive");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues(promotion,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.promotion_item,parent,false);

        return new ViewHolder(view);
    }

    void setValues(final Promotion promotion, final int position){
        tvMessage.setText(promotion.message);
        tvCode.setText(promotion.code);
        if (promotion.promo_type ==0){
            tvValue.setText(promotion.promo_value+"%");
        }else if (promotion.promo_type==2){
            tvValue.setText("GHC"+promotion.promo_value);
        }else if (promotion.promo_type==1){
            tvValue.setText("Free laundry!!");
        }else if (promotion.promo_type==4){
            tvValue.setText(promotion.message);
        }

        if (promotion.active==1){
            swActive.setChecked(true);
        }
        this.promotion_obj=promotion;
        swActive.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked){
                    promotion.active=1;
                    items.get(position).active=1;
                    promotion_obj=promotion;
                }else{
                    promotion.active=0;
                    items.get(position).active=0;
                    promotion_obj=promotion;
                }
            }
        });
        dialog.show();
    }


}
