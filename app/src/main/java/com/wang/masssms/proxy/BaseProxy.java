package com.wang.masssms.proxy;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


/**
 * Created by 58 on 2016/2/29.
 */
public class BaseProxy {
    //    界面传过来的Handler，用于Proxy与Activity交互
    protected Handler mHandler;
    protected Context mContext;
    //    标签
    protected String mTag;

    /**
     * 构造函数
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public BaseProxy(Context context,Handler proxyCallbackHandler) {
        this.mHandler = proxyCallbackHandler;
        mContext=context;
        mTag = this.getClass().getSimpleName();
    }

    public String getmTag() {
        return mTag;
    }

    /**
     * Proxy回调通知界面
     * @param entity 回调实体
     */
    protected void callback(ProxyEntity entity){
        if(mHandler==null){
            return;
        }
        Message message=Message.obtain(mHandler);
        message.obj=entity;
        message.sendToTarget();
    }

    /**
     *设置结果并回调界面
     * @param action
     * @param errorcode
     * @param data
     */
    protected  void setResultDateAndCallback(String action,int errorcode,Object data){
        ProxyEntity entity=new ProxyEntity(action,errorcode,data);
        callback(entity);

    }
    public void destory(){
        mHandler=null;
        mContext=null;
    }
}
