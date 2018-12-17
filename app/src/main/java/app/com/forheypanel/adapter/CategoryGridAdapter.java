package app.com.forheypanel.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.ActivityPrices;
import app.com.forheypanel.model.PriceCartegory;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Created by nayram on 12/9/15.
 */
public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {

    public ArrayList<PriceCartegory> item;
    public Context context;
    private int lastPosition = -1;
    String TAG=getClass().getName();

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_img);
            mTextView = (TextView) view.findViewById(R.id.tvItemName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public CategoryGridAdapter(Context context, ArrayList<PriceCartegory> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_price_grid,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (item.get(position).image!=null){
            if (!item.get(position).image.isEmpty()){
                Picasso.with(holder.mImageView.getContext())
                        .load("http://www.forhey.com/clothes/"+item.get(position).image)
                        .placeholder(R.drawable.ic_photos)
                        .fit()
                        .into(holder.mImageView);
            }
        }

        holder.mTextView.setText(item.get(position).name);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"id "+item.get(position).id);
                Bundle bundle=new Bundle();
                bundle.putInt("cart_id", item.get(position).id);
                Intent intent=new Intent(context, ActivityPrices.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


}
