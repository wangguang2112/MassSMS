package com.wang.masssms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tencent.bugly.crashreport.CrashReport;
import com.wang.masssms.R;
import com.wang.masssms.adapter.GroupListAdapter;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.proxy.GroupListProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.IMHeadView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_groupcontact,null);
        mHeadView= (IMHeadView) view.findViewById(R.id.fragment_groupcontact_headbar);
        mHeadView.setTitle("分组");
        mHeadView.setOnRightButtonClickListener(new IMHeadView.OnRightButtonClickListener() {
            @Override
            public void onRightClick(View view) {

            }
        });
        mGroupListView= (ListView) view.findViewById(R.id.fragment_groupcontact_listview);
        mData=new ArrayList<>();
        mAdapter=new GroupListAdapter(getActivity(),mData);
        mGroupListView.setAdapter(mAdapter);
        mGroupListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action=proxyEntity.action;
        if(action.equals(GroupListProxy.GET_GROUP_LIST_SUCCESS)){
            mData.clear();
            mData.addAll((List<ContactGroup>) proxyEntity.data);
            mAdapter.notifyDataSetChanged();
        }else if(action.equals(GroupListProxy.GET_GROUP_LIST_FAILED)){

        }

    }
}
