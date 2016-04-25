package com.wang.masssms.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hanks.library.AnimateCheckBox;
import com.wang.masssms.R;
import com.wang.masssms.adapter.ContactListAdapter;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.model.util.SharedPreferencesUtil;
import com.wang.masssms.proxy.ContactProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;

import java.util.ArrayList;
import java.util.List;

public class AllContactActivity extends BaseActivity implements SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

    public static final int IMPORT_FOR_DATA_TYPE = 0;

    public static final int VIEW_ALL_DATA_TYPE = 1;

    public static final int MY_SEND_DATA_TYPE = 2;

    private IMHeadView mHeadView;

    private SwipeMenuListView mSwipeMenuListView;

    private SwipeMenuCreator mMenuCreator;

    protected ContactProxy mProxy;

    private ContactListAdapter mAdapter;

    private ArrayList<Contacts> mAllContactData;

    private ArrayList<Boolean> isChecks;

    private AddDailog mAddDailog;

    private RelativeLayout mBottomBar;

    private TextView mHandleBT;

    private Animation mInAnim;

    private Animation mOutAnim;

    private int mType;

    //    可能为空
    private Long mGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contact_fragment);
        mType = getIntent().getIntExtra("type", -1);
        mHeadView = (IMHeadView) findViewById(R.id.activity_all_contact_headbar);
        mHeadView.setTitle("联系人");
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                AllContactActivity.this.finish();
            }
        });
        mHeadView.addPopupMenu(R.menu.all_contact_menu, this);
        mBottomBar = (RelativeLayout) findViewById(R.id.cotact_all_handle_bar);
        mBottomBar.setVisibility(View.GONE);
        mHandleBT = (TextView) findViewById(R.id.contact_all_import);
        createListViewMenu();
        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.activity_all_contact_listview);
        mSwipeMenuListView.setMenuCreator(mMenuCreator);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        mAllContactData = new ArrayList<Contacts>();
        isChecks = new ArrayList<Boolean>();
        mAdapter = new ContactListAdapter(this, mAllContactData, isChecks);
        mSwipeMenuListView.setAdapter(mAdapter);
        mSwipeMenuListView.setOnItemClickListener(this);
        mAddDailog = new AddDailog(this, false);
        mInAnim = AnimationUtils.loadAnimation(this, R.anim.float_down_in);
        mOutAnim = AnimationUtils.loadAnimation(this, R.anim.float_down_in);
        initViewData();
        mProxy = new ContactProxy(this, getCallbackHandler());
        if (SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.AUTO_IMPORT_FROM_PHONE_FLAG, false)) {
            mProxy.viewAllContactFromPhone();
        } else {
            mProxy.getALLContactList();
        }
        setOnBusy(true);
    }

    private void initViewData() {
        switch (mType) {
            case IMPORT_FOR_DATA_TYPE:
                mGid = getIntent().getLongExtra("gid", -1);
                if (mGid != -1) {
                    mBottomBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<Long> arrayList = new ArrayList<Long>();
                            for (int i = 0; i < isChecks.size(); i++) {
                                if (isChecks.get(i)) {
                                    arrayList.add(mAllContactData.get(i).getId());
                                }
                            }
                            if (arrayList.size() == 0) {
                                Toast.makeText(AllContactActivity.this, "为空", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mProxy.importContactToGroup(arrayList, mGid);
                            setOnBusy(true);
                        }
                    });
                }
                break;
            case MY_SEND_DATA_TYPE:

                break;
            case VIEW_ALL_DATA_TYPE:
            default:
                Drawable drawable = getResources().getDrawable(R.drawable.delete_deep);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mHandleBT.setCompoundDrawablePadding(20);
                mHandleBT.setCompoundDrawables(drawable, null, null, null);
                mHandleBT.setText("删除");
                mBottomBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Long> arrayList = new ArrayList<Long>();
                        for (int i = 0; i < isChecks.size(); i++) {
                            if (isChecks.get(i)) {
                                arrayList.add(mAllContactData.get(i).getId());
                                mAllContactData.remove(i);
                                isChecks.remove(i);
                            }
                        }
                        if (arrayList.size() == 0) {
                            Toast.makeText(AllContactActivity.this, "为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mProxy.deleteContacts(arrayList);
                        mAdapter.notifyDataSetChanged();

                        setOnBusy(true);
                    }
                });
                break;
        }
    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(ContactProxy.GET_ALL_CONTACT_LIST_SUCCESS)) {
            mAllContactData.clear();
            mAllContactData.addAll((List<Contacts>) proxyEntity.data);
            for (int i = 0; i < mAllContactData.size(); i++) {
                isChecks.add(false);
            }
            mAdapter.notifyDataSetChanged();
            setOnBusy(false);
        } else if (action.equals(ContactProxy.GET_ALL_CONTACT_LIST_FAILED)) {
            setOnBusy(false);
        } else if (action.equals(ContactProxy.DELETE_DEEP_CONTACT_SUCCESS)) {
            showBottomBar(false);
            setResult(Activity.RESULT_OK);
            setOnBusy(false);
        } else if (action.equals(ContactProxy.EDIT_CONTACT_SUCCESS)) {
            setResult(Activity.RESULT_OK);
            setOnBusy(false);
        } else if (action.equals(ContactProxy.IMPORT_CONTACT_TO_GROUP_SUCCESS)) {
            setOnBusy(false);
            setResult(Activity.RESULT_OK);
            this.finish();
        } else if (action.equals(ContactProxy.VIEW_ALL_CONTACT_SUCCESS)) {
            mProxy.getALLContactList();
        }
    }

    /**
     * 创建ListViewMenu
     */
    private void createListViewMenu() {
        mMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x54,
                        0xAA, 0x00)));
                // set item width
                openItem.setWidth(180);
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(16);
                // set item title font color #54aa00
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "disable" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
                deleteItem.setBackground(new ColorDrawable(Color.WHITE));
                // set item width
                deleteItem.setWidth(180);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

    }

    /**
     * ListView左划Menu点击事件
     */
    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        final Contacts contacts = mAllContactData.get(position);
        switch (index) {
            case 0:
                mAddDailog.setmTitle("编辑:" + contacts.getName());
                mAddDailog.setIsShowNameAndNumber(true);
                mAddDailog.setmPsitive("更改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] str = mAddDailog.getContactNameAndNumber();
                        if (!str[0].equals("") && !str[1].equals("")) {
                            if (str[0].equals(contacts.getName()) && str[1].equals(contacts.getPhonenumber())) {
                                return;
                            }
                            contacts.setName(str[0]);
                            contacts.setPhonenumber(str[1]);
                            mProxy.editContact(contacts);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AllContactActivity.this, "用户名或电话号码为空，添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mAddDailog.setmNegative("取消", null);
                mAddDailog.creatAndShow();
                //编辑
                break;
            case 1:
                //删除
                mProxy.deleteContact(contacts.getId());
                mAllContactData.remove(position);
                isChecks.remove(position);
                mAdapter.notifyDataSetChanged();
                setOnBusy(true);
                break;
            default:
                break;
        }
        return true;
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

    private void showBottomBar(boolean flag) {
        if (flag) {
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

    //悬浮Menu点击
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_contact_refresh:
//                AddDailog.showMsg(this, "未添加");
                mProxy.viewAllContactFromPhone();
                setOnBusy(true);
                break;
            case R.id.all_contact_all_select:
                boolean all_flag = false;
                for (int i = 0; i < isChecks.size(); i++) {
                    isChecks.set(i, true);
                    all_flag = true;
                }
                showBottomBar(all_flag);
                Log.d("isChecks", isChecks.toString());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.all_contact_other_select:
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
}
