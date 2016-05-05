package com.wang.masssms.uiview.multiplechoice;


import com.wang.masssms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮帮项目组 Bangbang
 *
 * @Author zhaobo
 * @Date 2015/5/23 13:59
 * @Copyright:58.com Inc. All rights reserved.
 */
public abstract class MultipleChoiceSelectorAdapter<T> extends BaseAdapter {

    protected ArrayList<T> mData = new ArrayList<T>();
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> positions = new ArrayList<Integer>();

    public MultipleChoiceSelectorAdapter(Context ctx) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.actionsheet_multiple_listitem, parent, false);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.selector_item_title);
            holder.mCheck = (ImageView) convertView.findViewById(R.id.selector_item_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitle.setText(displayContent(position));

        if (positions != null && positions.contains(position)) {
            holder.mCheck.setImageResource(R.drawable.option_checked);
        } else {
            holder.mCheck.setImageResource(R.drawable.option_check);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView mTitle;
        public ImageView mCheck;
    }
    public void setData(ArrayList<T> al) {
        this.mData = al;
    }

    public ArrayList<T> getData() {
        return this.mData;
    }

    public void notifyDataSetChanged(List<Integer> poss) {
        positions = poss;
        notifyDataSetChanged();
    }

    public void addDataChanged(int pos) {
        if(positions != null) {
            positions.add((Integer) pos);
        }
        notifyDataSetChanged();
    }

    public void removeDataChanged(int pos) {
        if(positions != null) {
            positions.remove((Integer) pos);
        }
       notifyDataSetChanged();
    }

    public List<Integer> getPositions(){
        return positions;
    }

    public abstract String displayContent(int position);
}
