package app.com.forheypanel.dashboard.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.com.forheypanel.R;

public class InventoryHeader extends RecyclerView.ViewHolder {

    public View itemView;
    public TextView tvWeight;

    public InventoryHeader(View itemView) {
        super(itemView);
        this.tvWeight=(TextView) itemView.findViewById(R.id.tvWeight);
        this.itemView=itemView;
    }
}
