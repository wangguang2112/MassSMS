package com.wang.masssms.fragment;


import com.wang.masssms.R;
import com.wang.masssms.adapter.WordAdapter;
import com.wang.masssms.model.WordData;
import com.wang.masssms.proxy.ProxyEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by wangguang on 2016/5/18.
 */
public class WordFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    public static final String WORD_FRAGMENT_CALLBACK="Word_Fragment_CallBack";
    GridView mGridView;
    WordAdapter mWordAdapter;
    private ArrayList<WordData> mWordDatas;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_layout, container, false);
        mWordDatas=new ArrayList<>();
        creatData();
        mWordAdapter=new WordAdapter(getActivity(),mWordDatas);
        mGridView= (GridView) view.findViewById(R.id.fragment_word_gridview);
        mGridView.setAdapter(mWordAdapter);
        mGridView.setOnItemClickListener(this);
        return view;
    }

    private void creatData() {
        String text[]=getResources().getStringArray(R.array.word_text);
        String des[]=getResources().getStringArray(R.array.word_description);
        for(int i=0;i<text.length;i++){
            WordData data=new WordData();
            data.setText(text[i]);
            data.setDescription(des[i]);
            mWordDatas.add(data);
        }
    }

    @Override
    public void onResponse(ProxyEntity entity) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(WORD_FRAGMENT_CALLBACK);
        intent.putExtra("text",mWordDatas.get(position).getText());
        mFragmentCallbackListener.onFragmentCallback(intent);
    }

}