package app.com.forheypanel.dashboard.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.dashboard.inventory.DashboardOrderInventory;
import app.com.forheypanel.dashboard.search.DashboardSearchHomeActivity;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.FactorDateClass;

public class SearchOrdersByDateAdapter extends RecyclerView.Adapter<SearchOrdersByDateAdapter.ViewHolder> {

    String TAG=getClass().getName();
    ArrayList<Order> orders;
    Context context;
    FactorDateClass formatDate;

    public SearchOrdersByDateAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
        this.formatDate=new FactorDateClass();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_search_home_item,parent,false);
        return new SearchOrdersByDateAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Order order=orders.get(position);
        if (holder instanceof ViewHolder)
        if (order !=null)
        {
            Log.d("MainAdapter",order.name);
            Log.d("MainAdapter",formatDate.formatDate(order.created_at));
            holder.tvName.setText(order.name);
            holder.tvDate.setText(formatDate.formatDate(order.created_at));
            holder.tvServerCode.setText(order.server_code);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, DashboardOrderInventory.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("order_id",order.server_code);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View itemView;
        public final TextView tvName;
        public final TextView tvServerCode,tvDate;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvServerCode=(TextView)itemView.findViewById(R.id.tvServerCode);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
        }
    }



}
