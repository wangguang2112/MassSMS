package com.wang.masssms.proxy;

import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.Contacts;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangguang on 2016/5/3.
 */
public class SendMsgProxy extends BaseProxy{

    public static final String SEND_MSG_CONTACT="SEND_MSG_CONTACT";
    public static final String SEND_MSG_GROUP="SEND_MSG_GROUP";

    /**
     * 构造函数
     *
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public SendMsgProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
    }
    public void sendMsgForContacts(List<Contacts> contacts){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=SEND_MSG_CONTACT;
                entity.errorCode=ErrorCode.RESULT_OK;

            }
        });

    }
    public void sendMsgForGroups(List<ContactGroup> groups){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=SEND_MSG_CONTACT;
                entity.errorCode=ErrorCode.RESULT_OK;
            }
        });
    }
}
