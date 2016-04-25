package com.wang.masssms.uiview.viewpager;

import com.wang.masssms.fragment.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangjinxin on 15/4/28.
 */
public abstract class BaseViewPagerFragment extends BaseFragment {

    /** Fragment当前状态是否可见 */
    protected boolean isVisible = false;

    /** 标志位，标志已经初始化完成 */
    protected boolean isPrepared = false;

    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    protected boolean mHasLoadedOnce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        isPrepared = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
