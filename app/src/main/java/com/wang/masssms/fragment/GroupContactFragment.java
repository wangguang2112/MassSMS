package com.wang.masssms.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wang.masssms.R;
import com.wang.masssms.activity.HandleContactActivity;
import com.wang.masssms.activity.SendMsgActivity;
import com.wang.masssms.adapter.GroupListAdapter;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.proxy.ContactProxy;
import com.wang.masssms.proxy.GroupListProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 58 on 2016/3/9.
 */
public class GroupContactFragment extends BaseFragment implements ListView.OnItemClickListener{
    private IMHeadView mHeadView;
    private ListView mGroupListView;
    private GroupListAdapter mAdapter;
    private ArrayList<ContactGroup> mData;
    private GroupListProxy mProxy;
    private ContactProxy contactProxy;
    private AddDailog mAddDailog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy=new GroupListProxy(mContext,getCallbackHandler());
        contactProxy=new ContactProxy(mContext,getCallbackHandler());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_groupcontact,null);
        mHeadView= (IMHeadView) view.findViewById(R.id.fragment_groupcontact_headbar);
        mHeadView.setTitle("分组");
        createAddDialg();
        mHeadView.setOnRightButtonClickListener(new IMHeadView.OnRightButtonClickListener() {
            @Override
            public void onRightClick(View view) {
                mAddDailog.show();
            }
        });
        mGroupListView= (ListView) view.findViewById(R.id.fragment_groupcontact_listview);
        mData=new ArrayList<>();
        mAdapter=new GroupListAdapter(getActivity(),mData,null);
        mAdapter.setOnAddButtonClickListener(new GroupListAdapter.OnAddButtonClickListener() {
            @Override
            public void onItemClick(int position, ContactGroup item) {
                Intent intent=new Intent(getActivity(),HandleContactActivity.class);
                intent.putExtra("type",HandleContactActivity.ADD_TO_GROUP);
                intent.putExtra("gid",item.getId());
                startActivityForResult(intent, HandleContactActivity.ADD_RESQUEST_CODE);
            }
        });
        mAdapter.setOnDeleteButtonClickListener(new GroupListAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onItemClick(int position, ContactGroup item) {
                Intent intent=new Intent(getActivity(),HandleContactActivity.class);
                intent.putExtra("type",HandleContactActivity.DELETE_FROM_GROUP);
                intent.putExtra("gid",item.getId());
                startActivityForResult(intent, HandleContactActivity.DELETE_REQUEST_CODE);
            }
        });
        mGroupListView.setAdapter(mAdapter);
        mGroupListView.setOnItemClickListener(this);
        mProxy.getGroupList();
        setOnBusy(true);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mData.get(position);
        Intent intent=new Intent(getActivity(),SendMsgActivity.class);
        intent.putExtra("gid",id);
        startActivity(intent);
    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action=proxyEntity.action;
        if(action.equals(GroupListProxy.GET_GROUP_LIST_SUCCESS)){
            mData.clear();
            mData.addAll((List<ContactGroup>) proxyEntity.data);
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        }else if(action.equals(GroupListProxy.GET_GROUP_LIST_FAILED)){
            setOnBusy(false);
        }else if(action.equals(GroupListProxy.ADD_GROUP_NAME_FAILED)){
            setOnBusy(false);
            AddDailog.showMsg(mContext,"添加失败了");
        }else if(action.equals(GroupListProxy. ADD_GROUP_NAME_SUCCESS)){
            mProxy.getGroupList();
        }

    }
    public void createAddDialg(){
        mAddDailog=new AddDailog(mContext);
        mAddDailog.setmTitle("添加分组");
        mAddDailog.setmPsitive("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mAddDailog.getName()!=null&&mAddDailog.getName()!=""){
                    mProxy.addGroup(mAddDailog.getName());
                    setOnBusy(true);
                }else{

                }
            }
        });
        mAddDailog.setmNegative("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mAddDailog.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK)
            switch (requestCode) {
            case 1:
            case 2:
                mProxy.getGroupList();
                setOnBusy(true);
                break;
        }
    }
}
