package com.wang.masssms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.wang.masssms.R;
import com.wang.masssms.adapter.ContactListAdapter;
import com.wang.masssms.adapter.GroupListAdapter;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.proxy.ContactProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangguang on 2016/4/18.
 */
public class HandleContactActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    public static String ADD_TO_GROUP = "add_to_group";
    public static int ADD_RESQUEST_CODE = 1;
    public static String DELETE_FROM_GROUP = "delete_from_group";
    public static int DELETE_REQUEST_CODE = 2;
    private ListView mContactView;
    private ContactListAdapter mAdapter;
    private ArrayList<Contacts> mContactData;
    private IMHeadView mHeadView;
    private ContactProxy mProxy;
    private AddDailog mDailog;
    private Long mGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_contact_layout);
        initView();
        if (getIntent().getStringExtra("type").equals(ADD_TO_GROUP)) {
            initAdd();
        } else if (getIntent().getStringExtra("type").equals(DELETE_FROM_GROUP)) {
            initDelete();
        }
        mGid=getIntent().getLongExtra("gid",-1);
        mProxy = new ContactProxy(this, getCallbackHandler());
        mProxy.getContactForGroup(mGid);
        setOnBusy(true);
    }

    private void initView() {
        mDailog=new AddDailog(this,false);
        mDailog.setmTitle("添加联系人");
        mDailog.setmNegative("取消", null);
        mDailog.setmPsitive("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProxy.addUserContact(mDailog.getContactNameAndNumber(), mGid);
                setOnBusy(true);
            }
        });
        mDailog.setIsShowNameAndNumber(true);
        mDailog.create();
        mHeadView = (IMHeadView) findViewById(R.id.activity_handle_contact_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                HandleContactActivity.this.finish();
            }
        });
        mHeadView.addPopupMenu(R.menu.contact_more_menu, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.contact_menu_add_from_phone:
                        Intent intent=new Intent(HandleContactActivity.this,AllContactActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.contact_menu_add_by_user:
                        mDailog.show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mContactView = (ListView) findViewById(R.id.activity_contact_listview);
        mContactData = new ArrayList<Contacts>();
        mAdapter = new ContactListAdapter(this, mContactData);
        mContactView.setAdapter(mAdapter);
        mContactView.setOnItemSelectedListener(this);
    }

    private void initAdd() {
        mHeadView.setTitle("添加联系人");
        setResult(ADD_RESQUEST_CODE);
    }

    private void initDelete() {
        mHeadView.setTitle("删除联系人");
        setResult(DELETE_REQUEST_CODE);
    }


    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(ContactProxy.GET_GROUP_CONTACT_LIST_SUCCESS)) {
            mContactData.clear();
            mContactData.addAll((List<Contacts>) proxyEntity.data);
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        } else if (action.equals(ContactProxy.GET_GROUP_CONTACT_LIST_FAILED)) {
            AddDailog.showMsg(this, "出错了啊-》-");
            setOnBusy(false);
        }else if(action.equals(ContactProxy.ADD_USER_CONTACT_SUCCESS)){
            mProxy.getContactForGroup(mGid);
        }else if(action.equals(ContactProxy.ADD_USER_CONTACT_FAILED)){
            Toast.makeText(this,"失败，",Toast.LENGTH_SHORT).show();
            setOnBusy(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
