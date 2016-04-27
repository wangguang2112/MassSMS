package com.wang.masssms.activity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.wang.masssms.R;
import com.wang.masssms.adapter.MessageListAdapter;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.proxy.MessageProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DraftActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    IMHeadView mHeadView;
    private SwipeMenuListView mListView;
    private MessageListAdapter mAdapter;
    private List<Message> mMessageData;

    private List<String> mMessageNames;
    private MessageProxy mProxy;
    private List<Message> tempData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        mHeadView= (IMHeadView) findViewById(R.id.activity_draft_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                DraftActivity.this.finish();
            }
        });

        mListView= (SwipeMenuListView) findViewById(R.id.draft_listview);
        mMessageData = new ArrayList<>();
        mMessageNames=new ArrayList<>();
        tempData=new ArrayList<>();
        mAdapter = new MessageListAdapter(this, mMessageData,mMessageNames, new MessageListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(int position, boolean isCheck) {
                mProxy.updataCollection(mMessageData.get(position));
            }
        });
        mListView.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(DraftActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.BLUE));
                deleteItem.setWidth(180);
                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);
            }
        });
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                if (mMessageData.get(position).getIscollect()) {
                    AddDailog.showMsgWithCancle(DraftActivity.this, "此条短信已被收藏，确定要删除吗", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProxy.deleteMessage(mMessageData.get(position));
                            mMessageData.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    mProxy.deleteMessage(mMessageData.get(position));
                    mMessageData.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);

        mProxy=new MessageProxy(this,getCallbackHandler());
        mProxy.getAllDraftMessage();
        setOnBusy(true);
    }
    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(MessageProxy.GET_ALL_DRAFT_MESSAGE_SUCCESS)) {
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
//// TODO: 2016/4/27
    }
}
