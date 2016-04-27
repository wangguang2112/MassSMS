package com.wang.masssms.adapter;

import com.wang.masssms.R;
import com.wang.masssms.model.orm.Message;

import org.w3c.dom.Text;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangguang on 2016/4/26.
 */
public class MessageListAdapter extends BaseAdapter {

    List<Message> data;

    List<String> names;

    Context mContext;

    LayoutInflater mLayoutInflater;

    OnItemCheckListener mOnItemCheckListener;

    SimpleDateFormat simpleDateFormat;
    public MessageListAdapter(Context context, List<Message> data, List<String> names, OnItemCheckListener listener) {
        this.mContext = context;
        this.data = data;
        this.names = names;
        mOnItemCheckListener = listener;
        mLayoutInflater = LayoutInflater.from(mContext);
        simpleDateFormat = new SimpleDateFormat("MM月dd日");
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
        final MyHolder myHolder;
        final Message msg = data.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.message_item_layout, null);
            myHolder = new MyHolder();
            myHolder.iv = (ImageView) convertView.findViewById(R.id.message_item_icon);
            myHolder.nv = (TextView) convertView.findViewById(R.id.message_item_name);
            myHolder.tv = (TextView) convertView.findViewById(R.id.message_item_time);
            myHolder.mv = (TextView) convertView.findViewById(R.id.message_item_message);
            myHolder.civ = (ToggleButton) convertView.findViewById(R.id.message_item_collection);

            myHolder.iv.setImageResource(msg.getIsdraft() ? R.drawable.message_draft : R.drawable.message_complete);
            myHolder.nv.setText(names.get(position));
            myHolder.tv.setText(simpleTime(msg.getSendtime()));
            myHolder.mv.setText(msg.getText());
            myHolder.civ.setChecked(msg.getIscollect());
            myHolder.civ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mOnItemCheckListener != null) {
                        msg.setIscollect(isChecked);
                        mOnItemCheckListener.onItemCheck(position, isChecked);
                    }
                }
            });
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
            myHolder.iv.setImageResource(msg.getIsdraft() ? R.drawable.message_draft : R.drawable.message_complete);
            myHolder.nv.setText(names.get(position));
            myHolder.tv.setText(simpleTime(msg.getSendtime()));
            myHolder.mv.setText(msg.getText());
            myHolder.civ.setChecked(msg.getIscollect());
            myHolder.civ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mOnItemCheckListener != null) {
                        msg.setIscollect(isChecked);
                        mOnItemCheckListener.onItemCheck(position, isChecked);
                    }
                }
            });
        }
        return convertView;
    }

    class MyHolder {

        ImageView iv;

        TextView nv;

        TextView tv;

        TextView mv;

        ToggleButton civ;
    }

    private String simpleTime(Date date) {

        int today=new Date(System.currentTimeMillis()).getDay();
        if(date.getDay()==today){
            simpleDateFormat.applyPattern("今天 hh:mm");
        }else if (today-date.getDay()==1){
            simpleDateFormat.applyPattern("昨天 hh:mm");
        }else {
            simpleDateFormat.applyPattern("MM月dd日");
        }
        return simpleDateFormat.format(date);
    }

    public interface OnItemCheckListener {

        public void onItemCheck(int position, boolean isCheck);
    }

}
