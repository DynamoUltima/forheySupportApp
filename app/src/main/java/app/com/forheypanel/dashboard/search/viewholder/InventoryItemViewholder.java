package app.com.forheypanel.dashboard.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.com.forheypanel.R;

public class InventoryItemViewholder extends RecyclerView.ViewHolder{

    public TextView tvGarment,tvQty,tvService;
    public View itemView;

    public InventoryItemViewholder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        this.tvGarment=itemView.findViewById(R.id.tvGarment);
        this.tvQty=itemView.findViewById(R.id.tvQty);
        this.tvService=itemView.findViewById(R.id.tvService);
    }
}
