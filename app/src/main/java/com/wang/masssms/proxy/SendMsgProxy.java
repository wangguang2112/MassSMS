package com.wang.masssms.proxy;

import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.Contacts;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.SmsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangguang on 2016/5/3.
 */
public class SendMsgProxy extends BaseProxy{

    public static final String SEND_MSG_SUCCESS_ACTION="SEND_MSG_SUCCESS_ACTION";
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
    public void sendMsgForContacts(final List<Contacts> contacts, final String text){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = SEND_MSG_CONTACT;
                entity.errorCode = ErrorCode.RESULT_OK;
                ArrayList<PendingIntent> intents = new ArrayList<PendingIntent>();
                SmsManager manager = SmsManager.getDefault();
                for (int i = 0; i < contacts.size(); i++) {
//                    Intent intent=new Intent(SEND_MSG_SUCCESS_ACTION);
                    manager.sendTextMessage(contacts.get(i).getPhonenumber(), null, handleMsgBody(contacts.get(i),text), null, null);
                }
                callback(entity);
            }
        });

    }
    public void sendMsgForGroups(List<ContactGroup> groups,String text){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=SEND_MSG_GROUP;
                entity.errorCode=ErrorCode.RESULT_OK;
                callback(entity);
            }
        });
    }
    private String handleMsgBody(Contacts contacts,String text){
        String result="";
        if(text.indexOf("#")==-1){
            result=text;
        }else {
            result= text.replace("#名字#",contacts.getName());
            result=result.replace("#名称#",contacts.getName().substring(0,1)+"先生");
            result=result.replace("#时间#",getData("hh:mm"));
            result=result.replace("#日期#",getData("MM:dd"));
        }
        return result;
    }
    private String getData(String data){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(data);
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }
}
