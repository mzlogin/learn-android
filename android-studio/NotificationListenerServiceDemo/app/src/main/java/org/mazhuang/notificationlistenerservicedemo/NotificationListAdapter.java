package org.mazhuang.notificationlistenerservicedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2016/8/9.
 */
public class NotificationListAdapter extends BaseAdapter {

    private List<String> mData = new ArrayList<>();
    private Context mContext;

    public NotificationListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item, null);
            itemHolder = new ItemHolder(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.mMessageTextView.setText(mData.get(position));
        return convertView;
    }

    public void addItem(String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    class ItemHolder {
        public ItemHolder(View v) {
            mMessageTextView = (TextView) v.findViewById(R.id.message);
        }
        public TextView mMessageTextView;
    }
}
