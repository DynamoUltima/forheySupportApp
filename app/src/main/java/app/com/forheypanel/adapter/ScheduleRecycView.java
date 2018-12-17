package app.com.forheypanel.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.PickupOrders;
import app.com.forheypanel.activity.SearchActivity;
import app.com.forheypanel.model.Options;
import app.com.forheypanel.tools.Helper;

/**
 * Created by nayram on 11/20/15.
 */
public class ScheduleRecycView extends RecyclerView.Adapter<ScheduleRecycView.ViewHolder> {

    String TAG=getClass().getName();
    String email;
    SharedPreferences preferences;
    public ArrayList<String>items;
    public ArrayList<Options>orders;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View itemView;
        public final TextView tvDay;
        public final TextView tvAm,tvPM;


        public ViewHolder(View itemView) {
            super(itemView);
           this.itemView=itemView;
            tvDay=(TextView)itemView.findViewById(R.id.tvDay);
            tvAm=(TextView)itemView.findViewById(R.id.tvAM);
            tvPM=(TextView)itemView.findViewById(R.id.tvPM);
        }
    }

    public ScheduleRecycView(ArrayList<String> items,Context context,String email) {
        this.items = items;
        this.context=context;
        this.email=email;
        preferences=this.context.getSharedPreferences("Credentials",Context.MODE_PRIVATE);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_items,parent,false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvPM.setText("12:30pm - 8:30pm");
        holder.tvAm.setText("7am - 12pm");
        holder.tvDay.setText(factorDate(items.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, " pressed");
                String role=preferences.getString("role","");
                if (role.contains("heygirl")){
                    boolean checkin=preferences.getBoolean("check_in",false);
                    if (checkin){
                        Bundle group=new Bundle();
                        group.putString("date", items.get(position));
                        group.putString("email",email);
                        Intent intent=new Intent(context, PickupOrders.class);
                        intent.putExtras(group);
                        context.startActivity(intent);
                    }else{
                        Helper.setLastView(context, "Schedule");
                        Toast.makeText(context,"You have to check in first",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Bundle group=new Bundle();
                    group.putString("date", items.get(position));
                    group.putString("email",email);
                    Intent intent=new Intent(context, PickupOrders.class);
                    intent.putExtras(group);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String factorDate(String params){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dayOfTheWeek="",mn="",res="";

        Date d=null;
        try{

            d = formatter.parse(params);//catch exception
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", d);
            mn = (String) android.text.format.DateFormat.format("MMM", d);

            res=dayOfTheWeek+" "+d.getDate()+"/"+mn;
        }catch (ParseException e){
            e.printStackTrace();
        }

        return res;
    }
}
