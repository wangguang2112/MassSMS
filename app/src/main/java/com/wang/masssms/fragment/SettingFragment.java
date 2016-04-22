package com.wang.masssms.fragment;

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

import com.wang.masssms.R;
import com.wang.masssms.adapter.SettingPagerAdapter;
import com.wang.masssms.uiview.viewpager.AutoScrollViewPager;
import com.wang.masssms.uiview.viewpager.PagerAdapter;
import com.wang.masssms.uiview.viewpager.vo.ViewPagerVo;
import com.wang.masssms.uiview.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by 58 on 2016/3/9.
 */
public class SettingFragment extends BaseFragment {
    private SettingPagerAdapter mPagerAdapter;
    AutoScrollViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private ArrayList<ViewPagerVo> mViewData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting, container, false);
        mViewPager= (AutoScrollViewPager) view.findViewById(R.id.setting_viewpager);
        mPagerAdapter=new SettingPagerAdapter(getFragmentManager());
        mViewData=new ArrayList<ViewPagerVo>();
        for(int i=0;i<3;i++) {
            mViewData.add(new ViewPagerVo());
        }
        mPagerAdapter.setData(mViewData);
        mViewPager.setCurrentItem(0);
        mViewPager.setInterval(3000);
        mViewPager.startAutoScroll();
        mViewPager.setAdapter(mPagerAdapter);
        //导航滑块设置
        mIndicator = (CirclePageIndicator)view.findViewById(R.id.setting_viewpager_indicator);
        mIndicator.setViewPager(mViewPager);
        return view;
    }

}
