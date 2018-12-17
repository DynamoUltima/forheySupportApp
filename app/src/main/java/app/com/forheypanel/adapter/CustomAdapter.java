package app.com.forheypanel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.InventoryListActivity;
import app.com.forheypanel.activity.InvoiceActivity;
import app.com.forheypanel.model.Inventory;

public class CustomAdapter extends BaseAdapter {

    Context context;
    public static ArrayList<Inventory> inventories;
    public LayoutInflater inflater;
    InvoiceActivity invAct;


    public CustomAdapter(ArrayList<Inventory> inventories,Context context,InvoiceActivity invAct) {
        this.context = context;
        this.inventories =inventories;
        this.invAct=invAct;
        this.inflater = LayoutInflater.from(context);
    }

    @Override

    public int getCount() {
        return inventories.size();
    }
    public void updateList(ArrayList<Inventory> inventories) {
        this.inventories=inventories;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return inventories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{

        protected TextView textView_product;
        protected TextView textView_quantity;

        public ViewHolder(View view){
//             textView_product = view.findViewById(R.id.product);
//             textView_quantity = view.findViewById(R.id.quantity);

        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

       View v = view;
       ViewHolder holder = null;

       if (v == null){
           v = LayoutInflater.from(context).inflate(R.layout.custom_layout,viewGroup,false);
           holder = new ViewHolder(v);
           v.setTag(holder);
       }else{
           holder = (ViewHolder) v.getTag();
       }
        final Inventory inventory= inventories.get(i);
       holder.textView_product.setText(inventory.getItem());
       holder.textView_quantity.setText(inventory.getNoOfITems());

       return v;

    }
}
