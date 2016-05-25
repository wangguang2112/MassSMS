package com.wang.masssms.proxy;

import com.wang.masssms.App;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.ContactGroupDao;
import com.wang.masssms.model.orm.ContactToGroup;
import com.wang.masssms.model.orm.ContactToGroupDao;
import com.wang.masssms.model.orm.ContactToMessage;
import com.wang.masssms.model.orm.ContactToMessageDao;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.model.orm.GroupToMessage;
import com.wang.masssms.model.orm.GroupToMessageDao;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.model.orm.MessageDao;

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
    private ContactToGroupDao mContactToGroupDao;
    private MessageDao mMessageDao;
    private GroupToMessageDao mGroupToMessageDao;
    private ContactToMessageDao mContactToMessageDao;
    SmsManager manager;
    /**
     * 构造函数
     *
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public SendMsgProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
        mContactToGroupDao= App.getDaoSession(context).getContactToGroupDao();
        mMessageDao=App.getDaoSession(context).getMessageDao();
        mContactToMessageDao=App.getDaoSession(context).getContactToMessageDao();
        mGroupToMessageDao=App.getDaoSession(context).getGroupToMessageDao();
        manager = SmsManager.getDefault();
    }
    public void sendMsgForContacts(final List<Contacts> contacts, final String text){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = SEND_MSG_CONTACT;
                entity.errorCode = ErrorCode.RESULT_OK;
                ArrayList<PendingIntent> intents = new ArrayList<PendingIntent>();
                Message message=new Message(null,new Date(System.currentTimeMillis()),text,false,false);
                mMessageDao.insert(message);
                Long mid=message.getId();
                for (int i = 0; i < contacts.size(); i++) {
//                    Intent intent=new Intent(SEND_MSG_SUCCESS_ACTION);
                    manager.sendTextMessage(contacts.get(i).getPhonenumber(), null, handleMsgBody(contacts.get(i), text), null, null);
                    contacts.get(i);
                    ContactToMessage ctg=new ContactToMessage(null,contacts.get(i).getId(),mid);
                    mContactToMessageDao.insertOrReplace(ctg);
                }

                callback(entity);
            }
        });

    }
    public void sendMsgForGroups(final List<ContactGroup> groups, final String text){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=SEND_MSG_GROUP;
                entity.errorCode=ErrorCode.RESULT_OK;
                Message message=new Message(null,new Date(System.currentTimeMillis()),text,false,false);
                mMessageDao.insert(message);
                Long mid=message.getId();
                for (int i = 0; i < groups.size(); i++) {
                    List<ContactToGroup> contactsList=mContactToGroupDao._queryContactGroup_Gid(groups.get(i).getId());
                    for(ContactToGroup ctg:contactsList) {
                        manager.sendTextMessage(ctg.getContacts().getPhonenumber(), null, handleMsgBody(ctg.getContacts(), text), null, null);
                    }
                    if(contactsList.size()>0) {
                        GroupToMessage gtm = new GroupToMessage(null, groups.get(i).getId(), mid);
                        mGroupToMessageDao.insertOrReplace(gtm);
                    }
                }
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
