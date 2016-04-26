package com.wang.masssms.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.wang.masssms.R;
import com.wang.masssms.activity.MainActivity;
import com.wang.masssms.activity.SendMsgActivity;
import com.wang.masssms.adapter.MessageListAdapter;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.proxy.MessageProxy;
import com.wang.masssms.proxy.ProxyEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2016/3/9.
 */
public class MessageFragment extends BaseFragment implements SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private final int MENU_MARK_POSITION = 0;

    private final int MENU_DELETE_POSITION = 1;

    //侧滑组件
    private SwipeMenuListView mSwipeMenuListView;

    private MessageListAdapter mAdapter;

    private List<Message> mMessageData;

    private MessageProxy mProxy;

    //swipemenu 创建类
    SwipeMenuCreator mCreater = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(mContext);
            openItem.setBackground(new ColorDrawable(Color.rgb(0xc9, 0xc9, 0xce)));
            openItem.setWidth(90);
            openItem.setTitle("标记");
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    mContext);
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(90);
            // set a icon
            deleteItem.setIcon(R.drawable.icon_message_delete);
            // add_up to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy = new MessageProxy(getActivity(), getCallbackHandler());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        mMessageData = new ArrayList<>();
        mAdapter = new MessageListAdapter(getActivity(), mMessageData, new MessageListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(int position, boolean isCheck) {
                mProxy.updataCollection(mMessageData.get(position));
            }
        });
        mSwipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.message_swipeMenuListView);
        mSwipeMenuListView.setMenuCreator(mCreater);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        //向左划
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mSwipeMenuListView.setOnItemClickListener(this);
        mProxy.getAllHaveSendMessage();
        setOnBusy(true);
        return view;
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case MENU_MARK_POSITION:
                mMessageData.get(position).setIscollect(true);
                mProxy.updataCollection(mMessageData.get(position));
                mAdapter.notifyDataSetChanged();
                break;
            case MENU_DELETE_POSITION:
                mProxy.deleteMessage(mMessageData.get(position));
                mMessageData.remove(position);
                mAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(MessageProxy.GET_ALL_HAVE_SEND_MESSAGE_SUCCESS)) {
            mMessageData.clear();
            mMessageData.addAll((List<Message>) proxyEntity.data);
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        } else if (action.equals(MessageProxy.INSERT_DRAFT_MESSAGE_SUCCESS)) {
            setOnBusy(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getActivity(), SendMsgActivity.class);
        intent.putExtra("mid",id);
        startActivity(intent);
    }
}
