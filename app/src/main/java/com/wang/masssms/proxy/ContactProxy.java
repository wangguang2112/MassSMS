package com.wang.masssms.proxy;

import android.content.Context;
import android.os.Handler;

import com.wang.masssms.App;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.ContactGroupDao;
import com.wang.masssms.model.orm.ContactToGroup;
import com.wang.masssms.model.orm.ContactToGroupDao;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.model.orm.ContactsDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangguang on 2016/4/18.
 */
public class ContactProxy extends BaseProxy{
    public static String GET_ALL_CONTACT_LIST_SUCCESS="get_all_contact_list_success";
    public static String GET_ALL_CONTACT_LIST_FAILED="get_all_contact_list_failed";

    public static String GET_GROUP_CONTACT_LIST_SUCCESS="get_group_contact_list_success";
    public static String GET_GROUP_CONTACT_LIST_FAILED="get_group_contact_list_failed";

    public static String GET_ALL_GROUP_CONTACT_LIST_SUCCESS="get_group_contact_list_success";
    public static String GET_ALL_GROUP_CONTACT_LIST_FAILED="get_group_contact_list_failed";

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
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = GET_ALL_CONTACT_LIST_SUCCESS;
                callback(entity);
            }
        });
    }
    public void getContactForGroup(final long gid){
        Runnable runnable = new Runnable() {
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_GROUP_CONTACT_LIST_SUCCESS;
                List<ContactToGroup> ctglist=mContactToGroupDao._queryContactGroup_Gid(gid);
                ArrayList<Contacts> data=new ArrayList<Contacts>(ctglist.size());
                for(int i=0;i<ctglist.size();i++){
                    data.add(ctglist.get(i).getContacts());
                }
                entity.data=data;
                callback(entity);
            }
        };
        cachedThreadPool.execute(runnable);
    }
    public void getContactForAllGroup(final ArrayList<ContactGroup> groups){
        Runnable runnable = new Runnable() {
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_ALL_GROUP_CONTACT_LIST_SUCCESS;
                ArrayList<String> names = new ArrayList<String>();
                StringBuilder builder=new StringBuilder();
                List<ContactToGroup> ctglist;
                int index=-1;
                for(int i=0;i<groups.size();i++) {
                   builder.delete(0, builder.length());
                    ctglist = mContactToGroupDao._queryContactGroup_Gid(groups.get(i).getId());
                    for (int j = 0; j < ctglist.size(); j++) {
                       builder.append(ctglist.get(i).getContacts().getName() + ",");
                    }
                    index=builder.lastIndexOf(",");
                    if(index>=0) {
                        builder.deleteCharAt(index);
                    }
                    names.add(builder.toString());
                }
                entity.data=names;
                callback(entity);
            }
        };
        cachedThreadPool.execute(runnable);
    }


}
