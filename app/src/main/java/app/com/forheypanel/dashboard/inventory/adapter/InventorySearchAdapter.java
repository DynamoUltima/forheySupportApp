package app.com.forheypanel.dashboard.inventory.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.model.WashNFold;
import app.com.forheypanel.dashboard.search.viewholder.InventoryHeader;
import app.com.forheypanel.dashboard.search.viewholder.InventoryItemViewholder;

public class InventorySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = getClass().getName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context ctx;

    private InventoryList weight;
    private ArrayList<Inventory> arrayList;

    public InventorySearchAdapter(Context ctx, ArrayList<Inventory> list, InventoryList weight) {
        this.ctx = ctx;
        this.weight=weight;
        this.arrayList=list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wash_n_fold_weight_item, parent, false);
            return new InventoryHeader(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventoryitems_layout, parent, false);
            return new InventoryItemViewholder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        try {
            if (holder instanceof InventoryHeader){
                if (weight.weight !=null)
                    ((InventoryHeader) holder).tvWeight.setText(weight.weight.getWeight()+"Kg");

            }else if (holder instanceof InventoryItemViewholder){
                Inventory inventory=arrayList.get(position-1);
                if (inventory !=null)
                {
                    ((InventoryItemViewholder) holder).tvGarment.setText(inventory.getItem());
                    ((InventoryItemViewholder) holder).tvQty.setText(inventory.getNoOfITems());
                    ((InventoryItemViewholder) holder).tvService.setText(inventory.getType());
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
