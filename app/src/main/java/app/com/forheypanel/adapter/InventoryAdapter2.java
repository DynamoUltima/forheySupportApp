package app.com.forheypanel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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
    String TAG = getClass().getName();
    InvoiceActivity invLAct;

    AdapterCallback mAdapterCallback;



    public void onAttach(Context context) {

    }

    private AdapterView.OnItemClickListener mListener;
    //private View.OnClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
        return new InventoryAdapter2.ViewHolder(itemView);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static interface AdapterCallback {
        void onMethodCallback(int sum,int sumItems,String itemGarment,String itemCode,String itemQuantity,String typeOfService);
    }

    public void setOnItemClickListener( AdapterView.OnItemClickListener listener) {
        mListener = listener;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Inventory inventory = itemList.get(position);
        holder.tvGarment.setText(inventory.getItem());
        holder.tvQty.setText(inventory.getNoOfITems());
        holder.tvService.setText(inventory.getType());
        holder.tvPrice.setText("₵" + inventory.getPrice() + ".0");




        int sum = 0;
        int sumItems=0;
        for (int i = 0; i < itemList.size(); i++) {
            Inventory list = itemList.get(i);
           double price = Double.parseDouble(list.getPrice());
//            int noITems= Integer.parseInt(list.getNoOfITems());
            sum+= price;
//            sumItems+=noITems;

        }
        sumItems = itemList.size();

        String itemGarment = inventory.getItem();
        String itemCode= inventory.getItemCode();
        String itemQuantity = inventory.getNoOfITems();
        String typeOfService = inventory.getType();

        mAdapterCallback.onMethodCallback(sum,sumItems,itemGarment,itemCode,itemQuantity,typeOfService);









        switch (inventory.getType()) {
            case "Wash & Fold":
                holder.tvService.setTextColor(context.getResources().getColor(R.color.washnFold));
                holder.tvQty.setText(inventory.getNoOfITems() + "kg");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(R.array.inv_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Position " + i);

                        if (i == 0) {
                            EditInventoryActivity.item = inventory;
                            invLAct.startActivityForResult(new Intent(context,
                                    EditInventoryActivity.class), 102);

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
        this.itemList = clientInventory;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final TextView tvGarment;
        public final TextView tvQty, tvService, tvPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            tvGarment = (TextView) itemView.findViewById(R.id.tvGarment2);
            tvQty = (TextView) itemView.findViewById(R.id.tvQty2);
            tvService = (TextView) itemView.findViewById(R.id.tvService2);
            tvPrice = itemView.findViewById(R.id.invoice_price);

            int position = getAdapterPosition();

//            int sum = 0;
//            int sumItems=0;
//            for (int i = 0; i < itemList.size(); i++) {
//                Inventory list = itemList.get(i);
//                int price = list.getPrice();
//                int noITems= Integer.parseInt(list.getNoOfITems());
//                sum+= price;
//                sumItems+=noITems;
//
//            }
//
//            mAdapterCallback.onMethodCallback(sum,sumItems);


        }
    }

    public InventoryAdapter2(ArrayList<Inventory> itemList, Context context, InvoiceActivity invLAct) {
        this.itemList = itemList;
        this.context = context;
        this.invLAct = invLAct;

        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }
}

