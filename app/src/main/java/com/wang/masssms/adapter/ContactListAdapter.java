package com.wang.masssms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanks.library.AnimateCheckBox;
import com.wang.masssms.R;
import com.wang.masssms.model.orm.Contacts;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangguang on 2016/4/19.
 */
public class ContactListAdapter extends BaseAdapter{
    List<Contacts> data;
    Context mContext;
    List<Boolean> isChecks;
    public ContactListAdapter(Context context,ArrayList<Contacts> data,ArrayList<Boolean> isChecks){
        mContext=context;
        this.data=data;
        this.isChecks=isChecks;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ContactViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.contact_item_layout,null);
            holder=new ContactViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.contact_item_name);
            holder.number= (TextView) convertView.findViewById(R.id.contact_item_number);
            holder.checkBox= (AnimateCheckBox) convertView.findViewById(R.id.contact_item_checkbox);
            holder.number.setText(data.get(position).getPhonenumber());
            holder.name.setText(data.get(position).getName());
            holder.checkBox.setChecked(isChecks.get(position));
            holder.checkBox.setEnabled(false);
//            holder.checkBox.setOnCheckedChangeListener(new AnimateCheckBox.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(View buttonView, boolean isChecked) {
//                    isChecks.set(position, isChecked);
//                }
//            });
            convertView.setTag(holder);
        }else{
            holder= (ContactViewHolder) convertView.getTag();
            holder.number.setText(data.get(position).getPhonenumber());
            holder.name.setText(data.get(position).getName());
            holder.checkBox.setChecked(isChecks.get(position));
            holder.checkBox.setEnabled(false);
//            holder.checkBox.setOnCheckedChangeListener(new AnimateCheckBox.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(View buttonView, boolean isChecked) {
//                    isChecks.set(position,isChecked);
//                }
//            });
        }
        return convertView;
    }
    class ContactViewHolder{
        public AnimateCheckBox checkBox;
        public TextView name;
        public TextView number;
    }
}
