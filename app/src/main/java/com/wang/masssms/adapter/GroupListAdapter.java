package com.wang.masssms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wang.masssms.R;
import com.wang.masssms.model.orm.ContactGroup;

import java.util.ArrayList;

/**
 * Created by wangguang on 2016/4/5.
 */
public class GroupListAdapter extends BaseAdapter{
    private ArrayList<ContactGroup> data;
    private Context mContext;
    private LayoutInflater mLayoutInflate;
    public GroupListAdapter(Context context,ArrayList<ContactGroup> groups){
        mContext=context;
        mLayoutInflate=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= mLayoutInflate.inflate(R.layout.group_item_layout,null);
             holder=new ViewHolder();
            holder.tv= (TextView) convertView.findViewById(R.id.group_item_name);
            holder.tv.setText(data.get(position).getName());
            convertView.setTag(holder);
        }else{
           holder = (ViewHolder) convertView.getTag();
            holder.tv.setText(data.get(position).getName());
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv;
    }
}
