package com.wang.masssms.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.wang.masssms.App;
import com.wang.masssms.activity.BaseActivity;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.IMLoadingDialog;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by 58 on 2016/3/8.
 */
public class BaseFragment  extends Fragment implements Observer{

    protected IFragmentCallbackListener mFragmentCallbackListener;
    protected Activity mActivity;
    protected Context mContext;
    private App mApp;
    //Activity是否被销毁
    protected boolean isDestroyed;
    //加载对话框
    protected IMLoadingDialog mLoadingDialog;

    public Handler getCallbackHandler() {
        return mHandler;
    }

    //绑定好的handler
    protected Handler mHandler = new MyHandler(this);

    @Override
    public void update(Observable observable, Object data) {

    }

    class MyHandler extends Handler {
        WeakReference<BaseFragment> mFragment;

        public MyHandler(BaseFragment fragment) {
            mFragment = new WeakReference<BaseFragment>(fragment);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //activity销毁后不处理消息
            if (mFragment == null) {
                return;
            }
            if (mFragment.get() == null) {
                return;
            }

            /**
             * 将activity设成弱引用，优点
             */
            BaseFragment baseFragment = mFragment.get();
            if (msg.obj instanceof ProxyEntity) {
                ProxyEntity entity = (ProxyEntity) msg.obj;
                //不加activity看行不行
                baseFragment.onResponse(entity);
            }
        }
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
        mApp=App.getApp();
        mActivity=(BaseActivity)getActivity();
        mContext=mApp.getApplicationContext();
    }

    public void onResponse(ProxyEntity proxyEntity) {
    }

    /**
     * 显示或关闭loading对话框
     *
     * @param isBusy
     */
    protected void setOnBusy(boolean isBusy) {
        if (isBusy) {//显示dialog
            if (mLoadingDialog == null) {
                IMLoadingDialog.Builder builder = new IMLoadingDialog.Builder(mActivity);
                builder.setCancelable(false);
                builder.setText("正在拼命加载中...");
                mLoadingDialog = builder.create();
                if (!mActivity.isFinishing())//容错性
                    mLoadingDialog.show();
            }

        } else {//隐藏dialog
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    protected void setOnBusy(boolean isBusy, String tip, boolean isCancelable) {
        if (isBusy) {//显示dialog
            if (mLoadingDialog == null) {
                IMLoadingDialog.Builder builder = new IMLoadingDialog.Builder(mContext);
                builder.setCancelable(isCancelable);
                builder.setText(tip);
                builder.setOnBusyDialog(true);
                mLoadingDialog = builder.create();
                if (!mActivity.isFinishing())//容错性
                    mLoadingDialog.show();
            }

        } else {//隐藏dialog
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=  activity;
        if(mActivity!=null){
            mFragmentCallbackListener=(BaseActivity)mActivity;
        }
    }

}
