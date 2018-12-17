package app.com.forheypanel.tools;

/**
 * Created by nayram on 12/4/15.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.ActivityClient;
import app.com.forheypanel.activity.ActivityClientSumm;
import app.com.forheypanel.database.LocalDatabase;
import app.com.forheypanel.model.Client;


public class AdapterSearchClient extends RecyclerView.Adapter<AdapterSearchClient.ViewHolder> {

    public ArrayList<Client> items;

    Context ctx;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setVisibility(View.GONE);

        if(items.get(position).name.substring(0,1).contains(" ")){
            holder.tvName.setText(items.get(position).name.substring(1));
            items.get(position).name=items.get(position).name.substring(1);
        }else{
            holder.tvName.setText(items.get(position).name);
        }
        if (items.get(position).location.isEmpty()){
            holder.tvLocation.setText("Location : Not Set");
        }else {
            holder.tvLocation.setText("Location : "+items.get(position).location);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = items.get(position).user_id;

                Bundle group = new Bundle();
                group.putInt("client_id", id);
                group.putString("name", items.get(position).name);
                group.putString("location", items.get(position).location);
                group.putString("email", items.get(position).email);
                group.putString("phone", items.get(position).phone);
                group.putInt("no_cancel", items.get(position).num_of_cancelled);
                group.putInt("no_orders", items.get(position).num_of_orders);
                Intent intent = new Intent(ctx, ActivityClientSumm.class);
                intent.putExtras(group);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView tvName,tvLocation;
        public final View itemView;
        public final ImageView imageView;

        public ViewHolder (View itemView){
            super (itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvTitle);
            tvLocation=(TextView)itemView.findViewById(R.id.tvMessage);
            imageView=(ImageView)itemView.findViewById(R.id.imgNew);
            this.itemView=itemView;
        }
    }

    public AdapterSearchClient(ArrayList<Client> items,Context ctx){
        this.items=items;
        this.ctx=ctx;
    }



}
