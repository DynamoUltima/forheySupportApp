package app.com.forheypanel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.TreeSet;

import app.com.forheypanel.model.OrderDataModel;

/**
 * Created by nayram on 8/31/15.
 */
public class AllDataAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    Context ctx;
    ArrayList<OrderDataModel>dataItem=new ArrayList<OrderDataModel>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private LayoutInflater mInflater;

    public AllDataAdapter(Context ctx) {
        this.ctx = ctx;
        mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final OrderDataModel item) {
        dataItem.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final OrderDataModel item) {
        dataItem.add(item);
        sectionHeader.add(dataItem.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    
}
