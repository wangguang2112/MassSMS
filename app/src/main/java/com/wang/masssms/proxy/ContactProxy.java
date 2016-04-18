package com.wang.masssms.proxy;

import android.content.Context;
import android.os.Handler;

import com.wang.masssms.App;
import com.wang.masssms.model.orm.ContactToGroupDao;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.model.orm.ContactsDao;

import java.util.ArrayList;

/**
 * Created by wangguang on 2016/4/18.
 */
public class ContactProxy extends BaseProxy{
    public static String GET_ALL_CONTACT_LIST_SUCCESS="get_all_contact_list_success";
    public static String GET_ALL_CONTACT_LIST_FAILED="get_all_contact_list_failed";

    public static String GET_GROUP_CONTACT_LIST_SUCCESS="get_group_contact_list_success";
    public static String GET_GROUP_CONTACT_LIST_FAILED="get_group_contact_list_failed";
    ContactsDao mContactsDao;
    ContactToGroupDao mContactToGroupDao;
    /**
     * 构造函数
     *
     * @param context
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public ContactProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
        mContactsDao= App.getDaoSession(mContext).getContactsDao();
        mContactToGroupDao=App.getDaoSession(mContext).getContactToGroupDao();
    }
    public void getALLContactList(){
        ProxyEntity entity=new ProxyEntity();
        entity.action=GET_ALL_CONTACT_LIST_SUCCESS;
        callback(entity);
    }
    public void getContactForGroup(long gid){
        ProxyEntity entity=new ProxyEntity();
        entity.action=GET_GROUP_CONTACT_LIST_SUCCESS;
        ArrayList<Contacts> data=new ArrayList<Contacts>();
        mContactToGroupDao._queryContactGroup_Gid(gid);
        callback(entity);
    }


}
