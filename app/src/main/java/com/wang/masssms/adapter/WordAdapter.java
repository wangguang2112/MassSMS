package com.wang.masssms.adapter;

import com.wang.masssms.R;
import com.wang.masssms.model.WordData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangguang on 2016/5/18.
 */
public class WordAdapter extends BaseAdapter{
    List<WordData> mWordDatas;

    private LayoutInflater mLayoutInflater;
    public WordAdapter(Context context,List<WordData> datas){
        mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mWordDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mWordDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.word_item,null);
        }
        TextView text= (TextView) convertView.findViewById(R.id.word_item_text);
        TextView description= (TextView) convertView.findViewById(R.id.word_item_descrition);
        text.setText(mWordDatas.get(position).getText());
        description.setText(mWordDatas.get(position).getDescription());
        return convertView;
    }
}
