package com.wang.masssms.uiview.viewpager;


import com.wang.masssms.uiview.viewpager.vo.ViewPagerVo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangjinxin on 15/4/28.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ViewPagerVo> data;

    public ArrayList<ViewPagerVo> getData() {
        if (data == null) {
            data = new ArrayList<ViewPagerVo>();
        }
        return data;
    }

    public void setData(ArrayList<ViewPagerVo> data) {
        this.data = data;
    }

    private Map<String, Fragment> fragmentMap = new HashMap<String, Fragment>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ViewPagerVo vo = getData().get(position);
        String result = "";
        if (vo != null) {
            result = vo.title;
        }

        return result;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment result = null;
        try {
            ViewPagerVo vo = getData().get(i);
            if(fragmentMap.get(vo.title) == null){
                Class c = Class.forName(vo.fragmentClassName);
                result = (Fragment)c.newInstance();
                fragmentMap.put(vo.title, result);
            } else {
                result = fragmentMap.get(vo.title);
            }
        } catch (Exception e) {

        }
        return result;
    }
}
