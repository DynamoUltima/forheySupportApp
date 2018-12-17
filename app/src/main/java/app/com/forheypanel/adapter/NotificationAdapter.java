package app.com.forheypanel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.forheypanel.activity.ActivityClient;
import app.com.forheypanel.activity.ActivityHeyGirl;
import app.com.forheypanel.database.LocalDatabase;
import app.com.forheypanel.activity.NotificationList;
import app.com.forheypanel.R;
import app.com.forheypanel.activity.SearchActivity;
import app.com.forheypanel.model.NotificationDataModel;

/**
 * Created by nayram on 6/2/15.
 */
public class NotificationAdapter extends BaseAdapter implements View.OnClickListener {


    Context context;
    private ArrayList<NotificationDataModel>dataModels;
    String TAG=getClass().getName();

    public NotificationAdapter(Context context, ArrayList<NotificationDataModel> dataModels) {
        this.context = context;
        this.dataModels = dataModels;
    }



    @Override
    public int getCount() {
        return dataModels.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationList.MyNotificationHolder holder;
        if (convertView ==null){
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            holder=new NotificationList.MyNotificationHolder();
            convertView = mInflater.inflate(R.layout.notification_item, null);
            holder.tvTitle=(TextView)convertView.findViewById(R.id.tvTitle);
            holder.tvMessage=(TextView)convertView.findViewById(R.id.tvMessage);
            holder.imgNew=(ImageView)convertView.findViewById(R.id.imgNew);

            convertView.setTag(holder);
        }else{
            holder=(NotificationList.MyNotificationHolder) convertView.getTag();
        }
        final NotificationDataModel d=dataModels.get(position);
        holder.model=d;
        holder.tvMessage.setText(d.getMessage());
        holder.tvTitle.setText(d.getTitle());
        holder.orderid=d.getOrderId();
        holder.id=d.getId();
        if (d.getState()==0){

        }
        Log.e(TAG,"TITLE "+d.getTitle());
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        NotificationList.MyNotificationHolder myViewHolder=(NotificationList.MyNotificationHolder)v.getTag();
        if (v instanceof View) {
            Bundle group = new Bundle();
            if (myViewHolder.model.getType().contains("heygirl_checkin")) {
                group.putString("name", myViewHolder.model.getName());
                group.putString("image", myViewHolder.model.getImage());
                group.putString("email", myViewHolder.model.getEmail());
                Intent intent = new Intent(context, ActivityHeyGirl.class);
                intent.putExtras(group);
                LocalDatabase db = new LocalDatabase(context);
                db.open();
                db.changeState(1, myViewHolder.id);
                db.close();
                context.startActivity(intent);
            } else if (myViewHolder.model.getType().contains("new_signup")){
                int id=myViewHolder.model.getClient_id();
                group.putInt("client_id",id);
                Log.e(TAG,"Client Id "+myViewHolder.model.getClient_id());
                Intent intent=new Intent(context, ActivityClient.class);
                intent.putExtras(group);
                LocalDatabase db = new LocalDatabase(context);
                db.open();
                db.changeState(1, myViewHolder.id);
                db.close();
                context.startActivity(intent);
            }else {
                group.putString("OrderId", myViewHolder.orderid);
                group.putString("ClassName", "HomeClass");
                group.putString("OrderType", "from app");
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtras(group);
                System.out.println("id " + myViewHolder.id);
                myViewHolder.imgNew.setVisibility(View.GONE);
                LocalDatabase db = new LocalDatabase(context);
                db.open();
                db.changeState(1, myViewHolder.id);
                db.close();
                context.startActivity(intent);
            }
        }

    }
}
