package com.wang.masssms.activity;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.wang.masssms.R;
import com.wang.masssms.fragment.BaseFragment;
import com.wang.masssms.fragment.GroupContactFragment;
import com.wang.masssms.fragment.MessageFragment;
import com.wang.masssms.fragment.SettingFragment;
import com.wang.masssms.proxy.ProxyEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener {
    /**
     * 短信tab
     */
    private final static String MESSAGE = "message";

    /**
     * 联系人分组tab
     */
    private final static String CONTACTGROUP = "contactgroup";
    /**
     * 设置tab
     */
    private final static String SETTING = "setting";
    private FragmentManager mFragmentManager;
    private TabHost mTabHost;
    private ArrayList<String> mTabTag = new ArrayList<String>(Arrays.asList(CONTACTGROUP, MESSAGE, SETTING));
    private String[] mTabTitle;
    private TabWidget mTabWidget;
    private int[] mTabContent = {R.id.activity_main_tab1, R.id.activity_main_tab2, R.id.activity_main_tab3};
    private int[] mTabIcon = {R.drawable.tab_contactgroup_icon, R.drawable.tab_message_icon, R.drawable.tab_setting_icon};
    private String currentTabView;
    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        currentTabView = "";
        mLayoutInflater=LayoutInflater.from(this);
        mFragmentManager = this.getSupportFragmentManager();
        mTabHost = (TabHost) findViewById(R.id.activity_main_tabhost);
        mTabHost.setOnTabChangedListener(this);
        //findview 方式 必须用step
        mTabHost.setup();

        mTabTitle = getResources().getStringArray(R.array.main_tab_title);
        for (int i = 0; i < mTabTag.size(); i++) {
            mTabHost.addTab(mTabHost.newTabSpec(mTabTag.get(i)).setIndicator(getTabItemView(i)).setContent(mTabContent[i]));
        }
        mTabHost.setCurrentTab(0);
    }

    private View getTabItemView(final int index) {
        View view=mLayoutInflater.inflate(R.layout.tab_item,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.tab_item_icon);
        imageView.setImageResource(mTabIcon[index]);
        TextView textView= (TextView) view.findViewById(R.id.tab_item_text);
        textView.setText(mTabTitle[index]);
        return  view;
    }

    /**
     * 设置unRead标签的可见性
     * @param tag
     * @param boo
     */
    private void visibleUnreadByTag(String tag, Boolean boo) {
        int index = mTabTag.indexOf(tag);
        int tabCount = mTabHost.getTabWidget().getChildCount();
        if (index >= 0 && index < tabCount) {
            ImageView unreadIcon = (ImageView) mTabHost.getTabWidget().getChildTabViewAt(index)
                    .findViewById(R.id.tab_unread_icon);
            if (boo) {
                unreadIcon.setVisibility(View.VISIBLE);
            } else {
                unreadIcon.setVisibility(View.GONE);
            }
        }
    }
    public String getCurrentTabView() {
        return currentTabView;
    }

    public void setCurrentTabView(String currentTabView) {
        this.currentTabView = currentTabView;
    }
    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
    }

    @Override
    public void onTabChanged(String tabId) {
        if(CONTACTGROUP.equals(tabId)){
            if (mFragmentManager.findFragmentByTag(CONTACTGROUP) == null) {
                BaseFragment baseFragment=new GroupContactFragment();
                int contentViewID = mTabContent[mTabTag.indexOf(CONTACTGROUP)];
                baseFragment.onAttach(this);
//commitAllowingStateLoss activity返回键的处理
                mFragmentManager.beginTransaction().replace(contentViewID,baseFragment,CONTACTGROUP).commitAllowingStateLoss();

            }

        }else if(MESSAGE.equals(tabId)){
            if (mFragmentManager.findFragmentByTag(MESSAGE) == null) {
                BaseFragment baseFragment=new MessageFragment();
                int contentViewID = mTabContent[mTabTag.indexOf(MESSAGE)];
                baseFragment.onAttach(this);
//commitAllowingStateLoss activity返回键的处理
                mFragmentManager.beginTransaction().replace(contentViewID,baseFragment,MESSAGE).commitAllowingStateLoss();
            }

        }else if(SETTING.equals(tabId)){
            if (mFragmentManager.findFragmentByTag(SETTING) == null) {
                BaseFragment baseFragment=new SettingFragment();
                int contentViewID = mTabContent[mTabTag.indexOf(SETTING)];
                baseFragment.onAttach(this);
//commitAllowingStateLoss activity返回键的处理
                mFragmentManager.beginTransaction().replace(contentViewID,baseFragment,SETTING).commitAllowingStateLoss();
            }

        }
        setCurrentTabView(tabId);
    }

    @Override
    public void onFragmentCallback(Intent intent) {
        super.onFragmentCallback(intent);

    }
}
