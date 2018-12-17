package app.com.forheypanel.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.forheypanel.activity.ActivityOrderDetail;
import app.com.forheypanel.model.OrderDataModel;
import app.com.forheypanel.R;
import app.com.forheypanel.activity.MainActivity;
import app.com.forheypanel.activity.SearchActivity;
import app.com.forheypanel.tools.Helper;

/**
 * Created by nayram on 2/26/15.
 */
public class OrderlistAdater extends BaseAdapter implements View.OnClickListener{
    Activity context;
    private ArrayList<OrderDataModel> orderItems;

    public OrderlistAdater(Activity context, ArrayList<OrderDataModel> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainActivity.MyViewHolder holder;
        if (convertView ==null){
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            holder=new MainActivity.MyViewHolder();
            convertView = mInflater.inflate(R.layout.layout_list_items, null);
            holder.tvDeliveryDate=(TextView)convertView.findViewById(R.id.tvDelivery);
            holder.tvPickupDate=(TextView)convertView.findViewById(R.id.tvPickup);
            holder.tvOrderId=(TextView)convertView.findViewById(R.id.tvOrderId);
            holder.tvOrdertype=(TextView)convertView.findViewById(R.id.tvOrderType);
            holder.tvLocation=(TextView)convertView.findViewById(R.id.tvLocation);
            holder.llStatus=(LinearLayout)convertView.findViewById(R.id.llStatus);
            convertView.setTag(holder);
        }else{
            holder=(MainActivity.MyViewHolder) convertView.getTag();
        }
        final OrderDataModel d=orderItems.get(position);
        holder.model=d;

        holder.tvPickupDate.setText("Pickup:  "+d.getPickupDate()+"   7am-12pm");
        holder.tvOrderId.setText(d.getName());
        holder.orderid=d.getServer_code();
        holder.orderType=d.getOrder_type();
        holder.tvOrdertype.setText(d.getOrder_type());
        holder.tvLocation.setText("Location: "+d.getLocation());
        switch (d.getStatus()){
            case "0":
                holder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.red500));
                break;
            case "1":
                holder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.primary));
                break;
            case "2":
                holder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.purple500));
                break;
            case "3":
                holder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
                break;
            case "4":
                holder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.yellow500));
                break;
            case "5":
                holder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.black));
        }
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {

        MainActivity.MyViewHolder myViewHolder=(MainActivity.MyViewHolder)v.getTag();
        if (v instanceof View){

            Bundle group=new Bundle();
            group.putString("OrderId", myViewHolder.orderid);
            group.putString("ClassName", "HomeClass");
            group.putString("OrderType", myViewHolder.orderType);
            Intent intent;
            String role= Helper.getRole(context);
            if (role.equalsIgnoreCase("heygirl"))
             intent=new Intent(context, ActivityOrderDetail.class);
            else intent=new Intent(context, SearchActivity.class);

            intent.putExtras(group);

            context.startActivityForResult(intent,100);
            //context.finish();
        }
    }
}
