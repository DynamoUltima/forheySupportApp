package app.com.forheypanel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.EditInventoryActivity;
import app.com.forheypanel.activity.InventoryListActivity;
import app.com.forheypanel.activity.InvoiceActivity;
import app.com.forheypanel.model.Inventory;

/**
 * Created by nayrammensah on 9/21/17.
 */

public class InventoryAdapter2 extends RecyclerView.Adapter<InventoryAdapter2.ViewHolder> {


    ArrayList<Inventory> itemList;
    Context context;
    String TAG=getClass().getName();
    InvoiceActivity invLAct;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout,parent,false);
        return new InventoryAdapter2.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       final Inventory inventory=itemList.get(position);
        holder.tvGarment.setText(inventory.getItem());
        holder.tvQty.setText(inventory.getNoOfITems());
        holder.tvService.setText(inventory.getType());
        switch (inventory.getType()){
            case "Wash & Fold":
                holder.tvService.setTextColor(context.getResources().getColor(R.color.washnFold));
                break;
            case "Laundry":
                holder.tvService.setTextColor(context.getResources().getColor(R.color.laundry));
                break;
            case "Dry Cleaning":
                holder.tvService.setTextColor(context.getResources().getColor(R.color.dry_clean));
                break;
            case "Press Only":
                holder.tvService.setTextColor(context.getResources().getColor(R.color.press_only));
                break;


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setItems(R.array.inv_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG,"Position "+i);

                        if (i==0){
                            EditInventoryActivity.item=inventory;
                            invLAct.startActivityForResult(new Intent(context,
                                    EditInventoryActivity.class),102);

                        }
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateList(ArrayList<Inventory> clientInventory) {
        this.itemList=clientInventory;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View itemView;
        public final TextView tvGarment;
        public final TextView tvQty,tvService;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            tvGarment=(TextView)itemView.findViewById(R.id.tvGarment2);
            tvQty=(TextView)itemView.findViewById(R.id.tvQty2);
            tvService=(TextView)itemView.findViewById(R.id.tvService2);
        }
    }

    public InventoryAdapter2(ArrayList<Inventory> itemList, Context context, InvoiceActivity invLAct) {
        this.itemList = itemList;
        this.context = context;
        this.invLAct=invLAct;
    }
}

