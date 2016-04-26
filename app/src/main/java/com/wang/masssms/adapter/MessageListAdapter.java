package com.wang.masssms.adapter;

import com.wang.masssms.R;
import com.wang.masssms.model.orm.Message;

import org.w3c.dom.Text;

import android.content.Context;
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
public class MessageListAdapter extends BaseAdapter{
    List<Message> data;
    Context mContext;
    OnItemCheckListener mOnItemCheckListener;
    public MessageListAdapter(Context context,List<Message> data,OnItemCheckListener listener){
        this.mContext=context;
        this.data=data;
        mOnItemCheckListener=listener;
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
        final Message msg=data.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.message_item_layout,null);
            myHolder=new MyHolder();
            myHolder.nv= (TextView) convertView.findViewById(R.id.message_item_name);
            myHolder.tv= (TextView) convertView.findViewById(R.id.message_item_time);
            myHolder.mv= (TextView) convertView.findViewById(R.id.message_item_message);
            myHolder.civ= (ToggleButton) convertView.findViewById(R.id.message_item_collection);

            myHolder.tv.setText(simpleTime(msg.getSendtime()));
            myHolder.mv.setText(msg.getText());
            myHolder.civ.setChecked(msg.getIscollect());
            myHolder.civ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mOnItemCheckListener!=null){
                        msg.setIscollect(isChecked);
                        mOnItemCheckListener.onItemCheck(position,isChecked);
                    }
                }
            });
            convertView.setTag(myHolder);
        }else{
            myHolder= (MyHolder) convertView.getTag();
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
    class MyHolder{
        TextView nv;
        TextView tv;
        TextView mv;
        ToggleButton civ;
    }
    private String simpleTime(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm月dd日 hh:MM");
        return simpleDateFormat.format(date);
    }
    public interface  OnItemCheckListener{
        public void onItemCheck(int position,boolean isCheck);
    }

}
