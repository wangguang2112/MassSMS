package com.wang.masssms.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.List;


/**
 * Created by 58 on 2016/3/9.
 */
public class GroupContactFragment extends BaseFragment implements ListView.OnItemClickListener {
    private IMHeadView mHeadView;
    private ListView mGroupListView;
    private GroupListAdapter mAdapter;
    private ArrayList<ContactGroup> mData;
    private ArrayList<String> mContactName;
    private GroupListProxy mProxy;
    private ContactProxy contactProxy;
    private AddDailog mAddDailog;
    private ArrayList<ContactGroup> TempData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy = new GroupListProxy(mContext, getCallbackHandler());
        contactProxy = new ContactProxy(mContext, getCallbackHandler());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupcontact, container, false);
        mHeadView = (IMHeadView) view.findViewById(R.id.fragment_groupcontact_headbar);
        createAddDialg();
        mHeadView.setOnRightButtonClickListener(new IMHeadView.OnRightButtonClickListener() {
            @Override
            public void onRightClick(View view) {
                mAddDailog.show();
            }
        });
        mGroupListView = (ListView) view.findViewById(R.id.fragment_groupcontact_listview);
        mData = new ArrayList<>();
        TempData =new ArrayList<>();
        mContactName = new ArrayList<>();
        mAdapter = new GroupListAdapter(getActivity(), mData, mContactName);
        mAdapter.setOnAddButtonClickListener(new GroupListAdapter.OnAddButtonClickListener() {
            @Override
            public void onItemClick(int position, ContactGroup item) {
                Intent intent = new Intent(getActivity(), HandleContactActivity.class);
                intent.putExtra("type", HandleContactActivity.ADD_TO_GROUP);
                intent.putExtra("gid", item.getId());
                intent.putExtra("title", item.getName());
                startActivityForResult(intent, HandleContactActivity.ADD_RESQUEST_CODE);
            }
        });
        mAdapter.setOnDeleteButtonClickListener(new GroupListAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onItemClick(int position, ContactGroup item) {
                Intent intent = new Intent(getActivity(), HandleContactActivity.class);
                intent.putExtra("type", HandleContactActivity.DELETE_FROM_GROUP);
                intent.putExtra("gid", item.getId());
                intent.putExtra("title", item.getName());
                startActivityForResult(intent, HandleContactActivity.DELETE_REQUEST_CODE);
            }
        });
        mGroupListView.setAdapter(mAdapter);
        mGroupListView.setOnItemClickListener(this);
        registerForContextMenu(mGroupListView);
        mProxy.getGroupList();
        setOnBusy(true);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mData.get(position);
        Intent intent = new Intent(getActivity(), SendMsgActivity.class);
        intent.putExtra("gid", id);
        startActivity(intent);
    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(GroupListProxy.GET_GROUP_LIST_SUCCESS)) {
            TempData.addAll((List<ContactGroup>) proxyEntity.data);
            contactProxy.getContactForAllGroup(TempData);
        } else if (action.equals(GroupListProxy.GET_GROUP_LIST_FAILED)) {
            AddDailog.showMsg(getActivity(), "刷新出错了啊");
            setOnBusy(false);
        } else if (action.equals(GroupListProxy.ADD_GROUP_NAME_FAILED)) {
            setOnBusy(false);
            AddDailog.showMsg(getActivity(), "添加失败，可能名称重复了~");
        } else if (action.equals(GroupListProxy.ADD_GROUP_NAME_SUCCESS)) {
            mProxy.getGroupList();
        } else if (action.equals(ContactProxy.GET_ALL_GROUP_CONTACT_LIST_SUCCESS)) {
            mData.clear();
            mData.addAll(TempData);
            mContactName.clear();
            mContactName.addAll((List<String>) proxyEntity.data);
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        }else if(action.equals(GroupListProxy.ALTER_GROUP_SUCCESS)){
            mAdapter.notifyDataSetChanged();
        }else if(action.equals(GroupListProxy.ALTER_GROUP_FAAILED)){
            AddDailog.showMsg(getActivity(), "添加失败，可能名称重复了~");
            mAdapter.notifyDataSetChanged();
        }

    }

    public void createAddDialg() {
        mAddDailog = new AddDailog(getActivity(), true);
        mAddDailog.setmTitle("添加分组");
        mAddDailog.setmPsitive("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mAddDailog.getName() != null && mAddDailog.getName() != "") {
                    mProxy.addGroup(mAddDailog.getName());
                    setOnBusy(true);
                } else {
                    Toast.makeText(getActivity(), "为空", Toast.LENGTH_SHORT).show();
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
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                case 2:
                    mProxy.getGroupList();
                    setOnBusy(true);
                    break;
            }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("提示信息");
        menu.setHeaderIcon(R.drawable.group_icon);
        menu.add("改名");
        menu.add("删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle().equals("改名")) {
            final int position=menuInfo.position;
            final AddDailog ma=new AddDailog(getActivity(),true);
            final ContactGroup group=mData.get(position);
            ma.setmTitle("更改组名： " +group.getName());
            ma.setmPsitive("更改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ma.getName() != null && !ma.getName().equals("")) {
                        group.setName(ma.getName());
                        mProxy.alterGroup(group);
                    } else {
                        Toast.makeText(getActivity(), "请添加文字", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ma.setmNegative("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ma.creatAndShow();
        } else if (item.getTitle().equals("删除")) {
            ContactGroup group=mData.get(menuInfo.position);
            mProxy.deleteGroup(group.getId());
            mData.remove(menuInfo.position);
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }
}
