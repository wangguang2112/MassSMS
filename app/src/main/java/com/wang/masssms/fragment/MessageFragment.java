package com.wang.masssms.fragment;

import android.content.DialogInterface;
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
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;

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

    private List<String> mMessageNames;

    private MessageProxy mProxy;

    private List<Message> tempData;

    private IMHeadView mHeadView;

    //swipemenu 创建类
    SwipeMenuCreator mCreater = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(
                    mContext);
            openItem.setBackground(new ColorDrawable(Color.rgb(0x54, 0xAA, 0x00)));
            openItem.setWidth(180);
            openItem.setTitle("标记");
            openItem.setTitleSize(16);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            deleteItem.setBackground(new ColorDrawable(Color.WHITE));
            deleteItem.setWidth(180);
            deleteItem.setIcon(R.drawable.delete);
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy = new MessageProxy(getActivity(), getCallbackHandler());
        //测试用 可去掉
//        addMessage();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mHeadView = (IMHeadView) view.findViewById(R.id.fragment_message_headbar);
        mHeadView.setOnRightButtonClickListener(new IMHeadView.OnRightButtonClickListener() {
            @Override
            public void onRightClick(View view) {
                Intent intent = new Intent(getActivity(), SendMsgActivity.class);
                intent.putExtra("type", SendMsgActivity.FROM_MY_SEND_TYPE);
                startActivity(intent);
            }
        });
        mMessageData = new ArrayList<>();
        mMessageNames = new ArrayList<>();
        tempData = new ArrayList<>();
        mAdapter = new MessageListAdapter(getActivity(), mMessageData, mMessageNames, new MessageListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(int position, boolean isCheck) {
                mProxy.updataCollection(mMessageData.get(position));
            }
        });
        mSwipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.message_swipeMenuListView);
        mSwipeMenuListView.setMenuCreator(mCreater);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        mSwipeMenuListView.setAdapter(mAdapter);
        //向左划 容易出问题
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mSwipeMenuListView.setOnItemClickListener(this);
        mProxy.getAllHaveSendMessage();
        setOnBusy(true);
        return view;
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        switch (index) {
            case MENU_MARK_POSITION:
                mMessageData.get(position).setIscollect(true);
                mProxy.updataCollection(mMessageData.get(position));
                mAdapter.notifyDataSetChanged();
                break;
            case MENU_DELETE_POSITION:
                if (mMessageData.get(position).getIscollect()) {
                    AddDailog.showMsgWithCancle(getActivity(), "此条短信已被收藏，确定要删除吗", new DialogInterface.OnClickListener() {
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
                break;
        }
        return true;
    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(MessageProxy.GET_ALL_HAVE_SEND_MESSAGE_SUCCESS)) {
            tempData.clear();
            tempData.addAll((List<Message>) proxyEntity.data);
            mProxy.getAllHaveSendMessageNames(tempData);
        } else if (action.equals(MessageProxy.INSERT_DRAFT_MESSAGE_SUCCESS)) {
//            mProxy.getAllHaveSendMessage();
//            setOnBusy(true);
//            Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
        } else if (action.equals(MessageProxy.GET_ALL_HAVE_SEND_MESSAGE_NAME_SUCCESS)) {
            mMessageData.clear();
            mMessageData.addAll(tempData);
            mMessageNames.clear();
            mMessageNames.addAll((List<String>) proxyEntity.data);
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), SendMsgActivity.class);
        intent.putExtra("mid", id);
        startActivity(intent);
    }

    /**
     * 测试数据集
     */
    public void addMessage() {
        mProxy.insertDraftMsg("12312412324 werf asdfasdf asdfas sdfas fas fas fasdfsdf");
        mProxy.insertDraftMsg("12312412324 werf asdfasdf asdfas sdfas fas fas fasdfsdf");
        mProxy.insertMsgFromGroup("爱上对方就爱说开了房间诶啊哦附近垃圾开始放假杀戮空间卡拉是减肥拉伸空间法奥斯卡了几分金发kla", (long) 1);
        mProxy.insertMsgFromGroup("就龙卷风上来看房看来是敬爱分水库附近阿卡丽快乐十分骄傲俩颗", (long) 2);
        mProxy.insertMsgFromGroup("卡死的减肥了可恶记录卡收费机", (long) 1);
        ArrayList<Long> list = new ArrayList();
        list.add((long) 1);
        list.add((long) 2);
        mProxy.insertMsgFromGroups("爱上对方就爱说开了房间诶啊哦附近垃圾开始放假杀戮空间卡拉是减肥拉伸空间法奥斯卡了几分金发kla", list);
    }
}
