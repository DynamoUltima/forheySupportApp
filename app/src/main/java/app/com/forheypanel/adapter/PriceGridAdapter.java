package app.com.forheypanel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.ActivityPriceDetails;
import app.com.forheypanel.model.PriceItem;

/**
 * Created by nayram on 12/9/15.
 */
public class PriceGridAdapter extends RecyclerView.Adapter <PriceGridAdapter.ViewHolder> {

    ArrayList<PriceItem>items;
    Activity ctx;
    private int lastPosition = -1;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;
        public final TextView tvPrice;
        public final TextView tvDryClean;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.img_item);
            mTextView = (TextView) view.findViewById(R.id.item_name);
            tvPrice=(TextView)view.findViewById(R.id.tv_Prices);
            tvDryClean=(TextView)view.findViewById(R.id.tv_DC);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public PriceGridAdapter(ArrayList<PriceItem> items, Activity ctx) {
        this.items = items;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (items.get(position).image!=null){
            if (!items.get(position).image.isEmpty()){
                Picasso.with(holder.mImageView.getContext())
                        .load("http://www.forhey.com/clothes/"+items.get(position).image)
                        .placeholder(R.drawable.ic_photos)
                        .into(holder.mImageView);
            }
        }
        if (items.get(position).item_name!=null)
        holder.mTextView.setText(items.get(position).item_name);

        if (items.get(position).price!=null) {
            if (!items.get(position).price.isEmpty()) {
                holder.tvPrice.setText("Price : GHC" + items.get(position).price);
                holder.tvDryClean.setVisibility(View.GONE);
            }else{
                if (items.get(position).laundry!=null) {
                    holder.tvPrice.setVisibility(View.VISIBLE);
                    if (!items.get(position).laundry.isEmpty()) {
                        holder.tvPrice.setVisibility(View.VISIBLE);
                        holder.tvPrice.setText("Laundry : GHC" + items.get(position).laundry);
                    } else {
                        holder.tvPrice.setVisibility(View.GONE);
                    }
                } else {
                    holder.tvPrice.setVisibility(View.GONE);
                }

                if (items.get(position).dry_cleaning!=null) {
                    holder.tvDryClean.setVisibility(View.VISIBLE);
                    if (!items.get(position).dry_cleaning.isEmpty()) {
                        holder.tvDryClean.setText("Dry Cleaning : GHC"+items.get(position).dry_cleaning);
                    } else {
                        holder.tvDryClean.setVisibility(View.GONE);
                    }
                }else {
                    holder.tvDryClean.setVisibility(View.GONE);
                }
            }
        }else{
            if (items.get(position).laundry!=null) {
                holder.tvPrice.setVisibility(View.VISIBLE);
                if (!items.get(position).laundry.isEmpty()) {
                    holder.tvPrice.setText("Laundry : GHC" + items.get(position).laundry);
                } else {
                    holder.tvPrice.setVisibility(View.GONE);
                }
            }else {
                holder.tvPrice.setVisibility(View.GONE);
            }

            if (items.get(position).dry_cleaning!=null) {
                holder.tvDryClean.setVisibility(View.VISIBLE);
                if (!items.get(position).dry_cleaning.isEmpty()) {
                    holder.tvDryClean.setText("Dry Cleaning : GHC"+items.get(position).dry_cleaning);
                } else {
                    holder.tvDryClean.setVisibility(View.GONE);
                }
            }else {
                holder.tvDryClean.setVisibility(View.GONE);
            }

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putInt("visible", items.get(position).visible);
                bundle.putString("name", items.get(position).item_name);
                bundle.putString("laundry",items.get(position).laundry);
                bundle.putString("dry_cleaning",items.get(position).dry_cleaning);
                bundle.putString("Category",items.get(position).category);
                bundle.putString("image", "http://www.forhey.com/clothes/" + items.get(position).image);
                bundle.putBoolean("wash_n_fold", items.get(position).wash_n_fold);
                bundle.putString("price", items.get(position).price);
                bundle.putInt("item_id", items.get(position).id);
                Intent intent=new Intent(ctx, ActivityPriceDetails.class);
                intent.putExtras(bundle);
                ctx.startActivityForResult(intent,120);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_price,parent,false);

        return new ViewHolder(view);
    }
}
