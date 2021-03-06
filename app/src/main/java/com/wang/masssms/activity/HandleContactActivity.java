package com.wang.masssms.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanks.library.AnimateCheckBox;
import com.wang.masssms.R;
import com.wang.masssms.adapter.ContactListAdapter;
import com.wang.masssms.adapter.GroupListAdapter;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.proxy.ContactProxy;
import com.wang.masssms.proxy.GroupListProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;

/**
 * Created by wangguang on 2016/4/18.
 */
public class HandleContactActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    public static String ADD_TO_GROUP = "add_to_group";

    public static int ADD_RESQUEST_CODE = 1;

    public static String DELETE_FROM_GROUP = "delete_from_group";

    public static int DELETE_REQUEST_CODE = 2;

    private ListView mContactView;

    private ContactListAdapter mAdapter;

    private ArrayList<Contacts> mContactData;

    private ArrayList<Boolean> isChecks;

    private IMHeadView mHeadView;

    private ContactProxy mProxy;

    private AddDailog mDailog;

    private Long mGid;

    private ExpandableMenuOverlay mMenuOverlay;

    private RelativeLayout mBottomBar;

    private TextView mDeleteBT;

    private TextView mDeepDeleteBT;
    private Animation mInAnim;

    private Animation mOutAnim;

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
        mGid = getIntent().getLongExtra("gid", -1);
        mProxy = new ContactProxy(this, getCallbackHandler());
        mProxy.getContactForGroup(mGid);
        setOnBusy(true);
    }

    private void initView() {
        mDailog = new AddDailog(this, false);
        mDailog.setmTitle("添加联系人");
        mDailog.setmNegative("取消", null);
        mDailog.setmPsitive("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProxy.addUserContactForGroup(mDailog.getContactNameAndNumber(), mGid);
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
                    case R.id.select_all:
                        boolean all_flag = false;
                        for (int i = 0; i < isChecks.size(); i++) {
                            isChecks.set(i, true);
                            all_flag = true;
                        }
                        showBottomBar(all_flag);
                        Log.d("isChecks", isChecks.toString());
                        mAdapter.notifyDataSetChanged();
                        break;
                    case R.id.select_other:
                        boolean flag = false;
                        for (int i = 0; i < isChecks.size(); i++) {
                            isChecks.set(i, !isChecks.get(i));
                            flag = flag || isChecks.get(i);
                        }
                        showBottomBar(flag);
                        Log.d("isChecks", isChecks.toString());
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mHeadView.setTitle(getIntent().getStringExtra("title"));
        mContactView = (ListView) findViewById(R.id.activity_contact_listview);
        mContactData = new ArrayList<Contacts>();
        isChecks = new ArrayList<Boolean>();
        mAdapter = new ContactListAdapter(this, mContactData, isChecks);
        mContactView.setAdapter(mAdapter);
        mContactView.setOnItemClickListener(this);
        mMenuOverlay = (ExpandableMenuOverlay) findViewById(R.id.activity_handle_contact_bottom_menu);
        mMenuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {
                switch (action) {
                    case LEFT:
                        // do stuff and dismiss
                        Intent intent = new Intent(HandleContactActivity.this, AllContactActivity.class);
                        intent.putExtra("type",AllContactActivity.IMPORT_FOR_DATA_TYPE);
                        intent.putExtra("gid",mGid);
                        startActivityForResult(intent,0);
                        mMenuOverlay.getButtonMenu().toggle();//收起按钮
                        break;
                    case RIGHT:
                        // do stuff
                        mDailog.show();
                        mMenuOverlay.getButtonMenu().toggle();
                        break;
                    case MID:
                        // do stuff
                        break;
                }
            }
        });
        mBottomBar = (RelativeLayout) findViewById(R.id.cotact_handle_bar);
        mDeleteBT = (TextView) findViewById(R.id.contact_handle_delete);
        mDeleteBT.setOnClickListener(this);
        mDeepDeleteBT= (TextView) findViewById(R.id.contact_handle_deep_delete);
        mDeepDeleteBT.setOnClickListener(this);
        mInAnim = AnimationUtils.loadAnimation(this, R.anim.float_down_in);
        mOutAnim = AnimationUtils.loadAnimation(this, R.anim.float_down_in);

    }

    /**
     * 显示底部操作栏
     */
    private void showBottomBar(boolean isShow) {
        if (isShow) {
            if (mBottomBar.getVisibility() == View.GONE) {
                mBottomBar.startAnimation(mInAnim);
                mBottomBar.setVisibility(View.VISIBLE);
            }
        } else {
            if (mBottomBar.getVisibility() == View.VISIBLE) {
                //不加动画了
                mBottomBar.setVisibility(View.GONE);
            }
        }
    }

    private void initAdd() {
        setResult(Activity.RESULT_OK);
        mMenuOverlay.show();
    }

    private void initDelete() {
        setResult(Activity.RESULT_OK);

    }


    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(ContactProxy.GET_GROUP_CONTACT_LIST_SUCCESS)) {
            mContactData.clear();
            mContactData.addAll((List<Contacts>) proxyEntity.data);
            for (int i = 0; i < mContactData.size(); i++) {
                isChecks.add(false);
            }
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        } else if (action.equals(ContactProxy.GET_GROUP_CONTACT_LIST_FAILED)) {
            AddDailog.showMsg(this, "出错了啊-》-");
            setOnBusy(false);
        } else if (action.equals(ContactProxy.ADD_USER_CONTACT_SUCCESS)) {
            mProxy.getContactForGroup(mGid);
            setResult(Activity.RESULT_OK);
        } else if (action.equals(ContactProxy.ADD_USER_CONTACT_FAILED)) {
            Toast.makeText(this, "失败，可能已存在该联系人，请选择从手机导入，", Toast.LENGTH_SHORT).show();
            setOnBusy(false);
        } else if (action.equals(ContactProxy.DELETE_CONTACT_SUCCESS)) {
            mAdapter.notifyDataSetChanged();
            showBottomBar(false);
            setOnBusy(false);
            setResult(Activity.RESULT_OK);
        } else if (action.equals(ContactProxy.DELETE_CONTACT_FAILED)) {
            Toast.makeText(this, "失败，", Toast.LENGTH_SHORT).show();
            setOnBusy(false);
        }else if (action.equals(ContactProxy.DELETE_DEEP_CONTACT_SUCCESS)) {
            mAdapter.notifyDataSetChanged();
            showBottomBar(false);
            setOnBusy(false);
            setResult(Activity.RESULT_OK);
        }else  if(action.equals(ContactProxy.DELETE_DEEP_CONTACT_FAILED)){
            Toast.makeText(this, "失败，", Toast.LENGTH_SHORT).show();
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
        if(resultCode==Activity.RESULT_OK){
            mProxy.getContactForGroup(mGid);
            setOnBusy(true);
            Toast.makeText(HandleContactActivity.this, "导入成功", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AnimateCheckBox checkBox = (AnimateCheckBox) view.findViewById(R.id.contact_item_checkbox);
        Log.d("isCheck1", checkBox.isChecked() + "");
        isChecks.set(position, !checkBox.isChecked());
        checkBox.setChecked(!checkBox.isChecked());
        Log.d("isCheck2", checkBox.isChecked() + "");
        boolean flag = false;
        for (int i = 0; i < isChecks.size(); i++) {
            flag = flag || isChecks.get(i);
        }
        Log.d("isCheck item", flag + "");
        showBottomBar(flag);
    }

    @Override
    public void onClick(View v) {
        final ArrayList<Long> arrayList = new ArrayList<>();
        switch (v.getId()) {

            case R.id.contact_handle_delete:
                for (int i = 0; i < isChecks.size(); i++) {
                    if (isChecks.get(i)) {
                        arrayList.add(mContactData.get(i).getId());
                        mContactData.remove(i);
                        isChecks.remove(i);
                    }
                }
                if (arrayList.size() == 0) {
                    Toast.makeText(HandleContactActivity.this, "为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProxy.deleteContactForGroup(arrayList,mGid);
                setOnBusy(true);
                break;
            case R.id.contact_handle_deep_delete:
                AddDailog.showMsgWithCancle(HandleContactActivity.this, "此操作将会彻底删除联系人，确定要执行吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < isChecks.size(); i++) {
                            if (isChecks.get(i)) {
                                arrayList.add(mContactData.get(i).getId());
                                mContactData.remove(i);
                                isChecks.remove(i);
                            }
                        }
                        if (arrayList.size() == 0) {
                            Toast.makeText(HandleContactActivity.this, "为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mProxy.deleteContactDeppForGroup(arrayList,mGid);
                        setOnBusy(true);
                    }
                });
            default:
                break;
        }
    }
}
