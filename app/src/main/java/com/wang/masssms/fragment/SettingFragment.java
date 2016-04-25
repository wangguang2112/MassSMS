package com.wang.masssms.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.masssms.R;
import com.wang.masssms.activity.AllContactActivity;
import com.wang.masssms.adapter.SettingPagerAdapter;
import com.wang.masssms.model.util.SharedPreferencesUtil;
import com.wang.masssms.uiview.togglebutton.IMToggleButton;
import com.wang.masssms.uiview.togglebutton.IOnToggleStateChangeListener;
import com.wang.masssms.uiview.viewpager.AutoScrollViewPager;
import com.wang.masssms.uiview.viewpager.PagerAdapter;
import com.wang.masssms.uiview.viewpager.vo.ViewPagerVo;
import com.wang.masssms.uiview.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by 58 on 2016/3/9.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private SettingPagerAdapter mPagerAdapter;

    AutoScrollViewPager mViewPager;

    private CirclePageIndicator mIndicator;

    private ArrayList<ViewPagerVo> mViewData;

    private int imageid[] = {R.drawable.image1, R.drawable.image2, R.drawable.image3};

    private LinearLayout mAllContactView;
    private LinearLayout mMySendView;
    private LinearLayout mCollectionView;
    public CirclePageIndicator getIndicator() {
        return mIndicator;
    }

    private IMToggleButton mAutoImportBT;

    private IMToggleButton mOtherImportBT;

    private TextView mLogoutTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mViewPager = (AutoScrollViewPager) view.findViewById(R.id.setting_viewpager);
        mAllContactView = (LinearLayout) view.findViewById(R.id.setting_view_all_contact);
        mAllContactView.setOnClickListener(this);
        mMySendView= (LinearLayout) view.findViewById(R.id.settint_my_send);
        mMySendView.setOnClickListener(this);
        mCollectionView= (LinearLayout) view.findViewById(R.id.setting_collection);
        mCollectionView.setOnClickListener(this);
        mAutoImportBT = (IMToggleButton) view.findViewById(R.id.setting_auto_import_from_phone);
        mAutoImportBT.setToggleState(SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.AUTO_IMPORT_FROM_PHONE_FLAG, false));
        mAutoImportBT.setIOnToggleStateChangeListener(new IOnToggleStateChangeListener() {
            @Override
            public void onToggleStateChange(View view, boolean state) {
                SharedPreferencesUtil.getInstance(getActivity()).setBoolean(SharedPreferencesUtil.AUTO_IMPORT_FROM_PHONE_FLAG, state);
            }
        });
        mOtherImportBT = (IMToggleButton) view.findViewById(R.id.setting_other1);
        mLogoutTV = (TextView) view.findViewById(R.id.setting_logout);
        mLogoutTV.setOnClickListener(this);
        mViewData = new ArrayList<ViewPagerVo>();
        mPagerAdapter = new SettingPagerAdapter(mContext, mViewData);
        for (int i = 0; i < 3; i++) {
            ViewPagerVo pagerVo = new ViewPagerVo();
            pagerVo.iconID = imageid[i];
            mViewData.add(pagerVo);
        }
        mViewPager.setCurrentItem(0);
        mViewPager.setInterval(3000);
        mViewPager.startAutoScroll();
        mViewPager.setAdapter(mPagerAdapter);
        //导航滑块设置
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.setting_viewpager_indicator);
        mIndicator.setViewPager(mViewPager);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_logout:
                getActivity().finish();
                break;
            case R.id.settint_my_send:
                Intent intent = new Intent(getActivity(), AllContactActivity.class);
                intent.putExtra("type", AllContactActivity.VIEW_ALL_DATA_TYPE);
                startActivity(intent);
                break;
            case R.id.setting_view_all_contact:
                Intent intent2 = new Intent(getActivity(), AllContactActivity.class);
                intent2.putExtra("type", AllContactActivity.MY_SEND_DATA_TYPE);
                startActivity(intent2);
                break;
            case R.id.setting_collection:

            default:
                break;
        }
    }
}
