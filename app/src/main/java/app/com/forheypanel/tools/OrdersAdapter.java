package app.com.forheypanel.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.ActivityOrderDetail;
import app.com.forheypanel.activity.SearchActivity;
import app.com.forheypanel.model.Options;

/**
 * Created by nayram on 11/20/15.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    public ArrayList<Options>items;
    Context context;
    String TAG=getClass().getName();
    FactorDateClass factorDate=new FactorDateClass();

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView tvName;
        public final TextView tvLocation;
        public final TextView tvPickupDate;
        public final TextView tvStatus;
        public final View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView)itemView.findViewById(R.id.tvOrderId);
            this.tvLocation = (TextView)itemView.findViewById(R.id.tvLocation);
            this.tvPickupDate = (TextView)itemView.findViewById(R.id.tvPickup);
            this.tvStatus = (TextView)itemView.findViewById(R.id.tvOrderType);
            this.itemView = itemView;
        }


    }

    public OrdersAdapter(ArrayList<Options> items,Context context) {
        this.items = items;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_order,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(items.get(position).client_name);
        holder.tvLocation.setText("Location: "+items.get(position).client_location);
        holder.tvPickupDate.setText("Pickup: "+factorDate.factorDate(items.get(position).pickup_date));


        if (items !=null)
            if (items.get(position).status !=null)
        switch (items.get(position).status){
            case "0":
                holder.tvStatus.setText("New Order");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.blue700));
                break;
            case "1":
                    holder.tvStatus.setText("Pickup");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.purple));
                break;
            case "2":
                holder.tvStatus.setText("Cleaning");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case "3":
                holder.tvStatus.setText("Delivery");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green500));
                break;
            case "4":
                holder.tvStatus.setText("En route");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case "5":
                holder.tvStatus.setText("Cancelled");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red500));
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle group=new Bundle();
                group.putString("OrderId", items.get(position).server_code);
                group.putString("ClassName", "HomeClass");
                group.putString("OrderType", items.get(position).order_type);
                String role=Helper.getRole(context);
                Intent intent;
                if (role.equalsIgnoreCase("heygirl"))
                 intent=new Intent(context, ActivityOrderDetail.class);
                else intent=new Intent(context,SearchActivity.class);
                intent.putExtras(group);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
