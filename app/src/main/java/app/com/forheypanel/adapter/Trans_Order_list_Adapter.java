package app.com.forheypanel.adapter;

import android.app.VoiceInteractor;
import android.content.Context;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.FactorDateClass;

/**
 * Created by nayram on 5/26/16.
 */
public class Trans_Order_list_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Order> items;
    private Context context;
//    private static final int TYPE_FOOTER = 2;


    class GenericViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;
        public final View mView;
        public final TextView tvDelivery;
        public final TextView tvPickupDate;
        public final TextView tvOrderId;
        public final TextView tvOrdertype;
        public final TextView tvLocation;
        public final LinearLayout llStatus;
        public String orderid,orderType;


        public GenericViewHolder(View view) {
            super(view);
            mView = view;
            tvDelivery=(TextView)view.findViewById(R.id.tvDelivery);
            tvPickupDate=(TextView)view.findViewById(R.id.tvPickup);
            tvOrderId=(TextView)view.findViewById(R.id.tvOrderId);
            tvOrdertype=(TextView)view.findViewById(R.id.tvOrderType);
            tvLocation=(TextView)view.findViewById(R.id.tvLocation);
            llStatus=(LinearLayout) view.findViewById(R.id.llStatus);
            orderid="";
            orderType="";
        }

        @Override
        public String toString() {
           return super.toString() + " '";//+ mTextView.getText();
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder (View itemView) {
            super (itemView);


        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        LineChart mChart;

        public HeaderViewHolder (View itemView) {
            super (itemView);
            mChart=(LineChart)itemView.findViewById(R.id.mChart);

        }
    }



    public Trans_Order_list_Adapter(ArrayList<Order> items, Context context) {
        this.context=context;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.chart_view, parent, false);
            return new HeaderViewHolder (v);
        } else if(viewType == TYPE_ITEM) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_items,parent,false);
            return  new GenericViewHolder(itemView);
        }
        return null;
    }

    private Order getItem (int position) {
        return items.get (position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        }  else if(holder instanceof GenericViewHolder) {
            Order d=items.get(position-1);

            GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
            genericViewHolder.tvPickupDate.setText("Pickup:  "+new FactorDateClass().factorDate(d.pickup_date) +"   7am-12pm");
            genericViewHolder.tvOrderId.setText(d.client_name);
            genericViewHolder.orderid=d.server_code;
            genericViewHolder.orderType=d.order_type;
            genericViewHolder.tvOrdertype.setText(d.order_type);
            genericViewHolder.tvLocation.setText("Location: "+d.client_location);
            switch (d.order_type){
                case "0":
                    genericViewHolder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.red500));
                    break;
                case "1":
                    genericViewHolder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.primary));
                    break;
                case "2":
                    genericViewHolder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.purple500));
                    break;
                case "3":
                    genericViewHolder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
                    break;
                case "4":
                    genericViewHolder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.yellow500));
                    break;
                case "5":
                    genericViewHolder.llStatus.setBackgroundColor(context.getResources().getColor(R.color.black));
            }
        }
    }

    @Override
    public int getItemCount() {
        /*if (items!=null){
            return items.size();
        }else*/
            return items.size();
    }
}


