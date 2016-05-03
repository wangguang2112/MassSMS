package com.wang.masssms.proxy;

import android.content.Context;
import android.os.Handler;

/**
 * Created by wangguang on 2016/5/3.
 */
public class SendMsgProxy extends BaseProxy{

    /**
     * 构造函数
     *
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public SendMsgProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
    }

}
