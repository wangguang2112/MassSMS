package com.wang.masssms.adapter;


import com.wang.masssms.uiview.viewpager.vo.ViewPagerVo;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by wangguang on 2016/4/22.
 */
public class SettingPagerAdapter extends PagerAdapter {
    List<ViewPagerVo> list;
    Context mContext;
    public SettingPagerAdapter(Context context,List<ViewPagerVo> data){
        mContext=context;
        list=data;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv=new ImageView(mContext);
        iv.setImageResource(list.get(position).iconID);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(iv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}
