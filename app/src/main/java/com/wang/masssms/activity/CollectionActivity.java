package com.wang.masssms.activity;

import com.wang.masssms.R;
import com.wang.masssms.adapter.MessageListAdapter;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.proxy.MessageProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.IMHeadView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangguang on 2016/4/26.
 */
public class CollectionActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    IMHeadView mHeadView;
    private ListView mListView;
    private MessageListAdapter mAdapter;
    private List<Message> mMessageData;

    private List<String> mMessageNames;
    private MessageProxy mProxy;
    private List<Message> tempData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_layout);
        mHeadView= (IMHeadView) findViewById(R.id.activity_collection_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                CollectionActivity.this.finish();
            }
        });
        mListView= (ListView) findViewById(R.id.collection_listview);
        mMessageData = new ArrayList<>();
        mMessageNames=new ArrayList<>();
        tempData=new ArrayList<>();
        mAdapter = new MessageListAdapter(this, mMessageData,mMessageNames, new MessageListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(int position, boolean isCheck) {
                mProxy.updataCollection(mMessageData.get(position));
            }
        });
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mProxy=new MessageProxy(this,getCallbackHandler());
        mProxy.getAllCollectionMessage();
        setOnBusy(true);
    }
    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(MessageProxy.GET_ALL_COLLECTION_MESSAGE_SUCCESS)) {
            tempData.clear();
            tempData.addAll((List<Message>) proxyEntity.data);
            mProxy.getAllHaveSendMessageNames(tempData);
        } else if (action.equals(MessageProxy.INSERT_DRAFT_MESSAGE_SUCCESS)) {
//            mProxy.getAllHaveSendMessage();
//            setOnBusy(true);
//            Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
        }else if(action.equals(MessageProxy.GET_ALL_HAVE_SEND_MESSAGE_NAME_SUCCESS)){
            mMessageData.clear();
            mMessageData.addAll(tempData);
            mMessageNames.clear();
            mMessageNames.addAll((List<String>)proxyEntity.data);
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//TODO
        Toast.makeText(this,"ok", Toast.LENGTH_SHORT).show();
    }
}
