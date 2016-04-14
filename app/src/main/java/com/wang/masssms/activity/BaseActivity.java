package com.wang.masssms.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wang.masssms.fragment.IFragmentCallbackListener;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.uiview.IMLoadingDialog;

import java.lang.ref.WeakReference;

public class BaseActivity extends FragmentActivity implements IFragmentCallbackListener{

    //Activity是否被销毁
    protected boolean isDestroyed;
    //加载对话框
    protected IMLoadingDialog mLoadingDialog;

    public Handler getCallbackHandler() {
        return mHandler;
    }

    //绑定好的handler
    protected Handler mHandler = new MyHandler(this);

    /**
     * 可能为空
     * @param intent
     */
    @Override
    public void onFragmentCallback(Intent intent) {

    }

    class MyHandler extends Handler {
        WeakReference<BaseActivity> mActivity;

        public MyHandler(BaseActivity activity) {
            mActivity = new WeakReference<BaseActivity>(activity);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //activity销毁后不处理消息
            if (mActivity == null) {
                return;
            }
            if (mActivity.get() == null) {
                return;
            }
            if (isDestroyed == true) {
                return;
            }
            /**
             * 将activity设成弱引用，优点
             */
            BaseActivity baseActivity = mActivity.get();
            if (msg.obj instanceof ProxyEntity) {
                ProxyEntity entity = (ProxyEntity) msg.obj;
                //不加activity看行不行
                baseActivity.onResponse(entity);
            }
        }
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
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
                IMLoadingDialog.Builder builder = new IMLoadingDialog.Builder(this);
                builder.setCancelable(false);
                builder.setText("正在拼命加载中...");
                mLoadingDialog = builder.create();
                if (!this.isFinishing())//容错性
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
                IMLoadingDialog.Builder builder = new IMLoadingDialog.Builder(this);
                builder.setCancelable(isCancelable);
                builder.setText(tip);
                builder.setOnBusyDialog(true);
                mLoadingDialog = builder.create();
                if (!this.isFinishing())//容错性
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
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }
}
