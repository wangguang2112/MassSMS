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
import com.wang.masssms.model.util.PhoneContactUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangguang on 2016/4/18.
 */
public class ContactProxy extends BaseProxy {

    public static String GET_ALL_CONTACT_LIST_SUCCESS = "get_all_contact_list_success";

    public static String GET_ALL_CONTACT_LIST_FAILED = "get_all_contact_list_failed";

    public static String GET_GROUP_CONTACT_LIST_SUCCESS = "get_group_contact_list_success";

    public static String GET_GROUP_CONTACT_LIST_FAILED = "get_group_contact_list_failed";

    public static String GET_ALL_GROUP_CONTACT_LIST_SUCCESS = "get_group_contact_list_success";

    public static String GET_ALL_GROUP_CONTACT_LIST_FAILED = "get_group_contact_list_failed";

    public static String ADD_USER_CONTACT_SUCCESS = "add_user_contact_success";

    public static String ADD_USER_CONTACT_FAILED = "add_user_contact_failed";

    public static String DELETE_CONTACT_SUCCESS = "delete_contact_success";

    public static String DELETE_CONTACT_FAILED = "delete_contact_failed";

    public static String DELETE_DEEP_CONTACT_SUCCESS = "delete_deep_contact_success";

    public static String DELETE_DEEP_CONTACT_FAILED = "delete_deep_contact_failed";

    public static String EDIT_CONTACT_SUCCESS = "edit_contact_success";

    public static String EDIT_CONTACT_FAILED = "edit_contact_failed";

    public static String IMPORT_CONTACT_TO_GROUP_SUCCESS = "import_contact_to_group_success";
    public static String IMPORT_CONTACT_TO_GROUP_FAILED = "import_contact_to_group_failed";

    public static String VIEW_ALL_CONTACT_SUCCESS="VIEW_ALL_CONTACT_SUCCESS";

    ContactsDao mContactsDao;

    ContactToGroupDao mContactToGroupDao;

    /**
     * 构造函数
     *
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public ContactProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
        mContactsDao = App.getDaoSession(mContext).getContactsDao();
        mContactToGroupDao = App.getDaoSession(mContext).getContactToGroupDao();
    }

    public void getALLContactList() {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = GET_ALL_CONTACT_LIST_SUCCESS;
                List<Contacts> data=mContactsDao.queryBuilder().list();
                entity.data=data;
                callback(entity);
            }
        });
    }

    /**
     * 加载属于租的成员
     */
    public void getContactForGroup(final long gid) {
        Runnable runnable = new Runnable() {
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = GET_GROUP_CONTACT_LIST_SUCCESS;
                if (gid == -1) {
                    entity.action = GET_GROUP_CONTACT_LIST_FAILED;
                } else {
                    List<ContactToGroup> ctglist = mContactToGroupDao._queryContactGroup_Gid(gid);
                    ArrayList<Contacts> data = new ArrayList<Contacts>(ctglist.size());
                    for (int i = 0; i < ctglist.size(); i++) {
                        data.add(ctglist.get(i).getContacts());
                    }
                    entity.data = data;
                }
                callback(entity);
            }
        };
        cachedThreadPool.execute(runnable);
    }

    /**
     * 夹杂所给组的成员
     */
    public void getContactForAllGroup(final ArrayList<ContactGroup> groups) {
        Runnable runnable = new Runnable() {
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = GET_ALL_GROUP_CONTACT_LIST_SUCCESS;
                ArrayList<String> names = new ArrayList<String>();
                StringBuilder builder = new StringBuilder();
                List<ContactToGroup> ctglist;
                int index = -1;
                for (int i = 0; i < groups.size(); i++) {
                    builder.delete(0, builder.length());
                    ctglist = mContactToGroupDao._queryContactGroup_Gid(groups.get(i).getId());
//                    ctglist = groups.get(i).getGid();//gid是相应的to 写的不好 这个方法是读取缓存的 最好别用
                    for (int j = 0; j < ctglist.size(); j++) {
                        if (ctglist.get(j).getContacts() == null) {
                            mContactToGroupDao.delete(ctglist.get(j));//处理脏数据
                        } else {
                            builder.append(ctglist.get(j).getContacts().getName() + ",");
                        }
                    }
                    index = builder.lastIndexOf(",");
                    if (index >= 0) {
                        builder.deleteCharAt(index);
                    }
                    names.add(builder.toString());
                }
                entity.data = names;
                callback(entity);
            }
        };
        cachedThreadPool.execute(runnable);
    }

    /**
     * 添加联系人 组内添加
     */
    public void addUserContactForGroup(final String[] data, final Long gid) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                if (data != null && !data[0].equals("") && !data[1].equals("")) {
                    entity.action = ADD_USER_CONTACT_SUCCESS;
                    Contacts contacts = new Contacts();
                    contacts.setId(null);
                    contacts.setCreattime(new Date(System.currentTimeMillis()));
                    contacts.setLastmodify(new Date(System.currentTimeMillis()));
                    contacts.setName(data[0]);
                    contacts.setPhonenumber(data[1]);
                    if (mContactsDao.queryBuilder().where(ContactsDao.Properties.Name.eq(data[0])).list().size() > 0 || mContactsDao.insertOrReplace(contacts) == -1) {
                        entity.action = ADD_USER_CONTACT_FAILED;
                    } else {
                        List<Contacts> cids = mContactsDao.queryBuilder().where(ContactsDao.Properties.Name.eq(data[0])).list();
                        for (Contacts c : cids) {
                            ContactToGroup ctg = new ContactToGroup();
                            ctg.setId(null);
                            ctg.setCid(c.getId());
                            ctg.setGid(gid);
                            mContactToGroupDao.insertOrReplace(ctg);
                        }
                        callback(entity);
                    }
                } else {
                    entity.action = ADD_USER_CONTACT_FAILED;
                }
                callback(entity);
            }
        });
    }

    /**
     * 彻底删除
     */
    public void deleteContactDeppForGroup(final ArrayList<Long> cids, final Long gid) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = DELETE_DEEP_CONTACT_SUCCESS;
                mContactsDao.deleteByKeyInTx(cids);
                List<ContactToGroup> ctg = mContactToGroupDao._queryContactGroup_Gid(gid);
                for (int j = 0; j < ctg.size(); j++) {
                    for (int i = 0; i < cids.size(); i++) {
                        if (ctg.get(j).getCid() == cids.get(i)) {
                            mContactToGroupDao.delete(ctg.get(i));
                        }
                    }
                }
                callback(entity);
            }
        });
    }

    /**
     * 批量删除联系人
     * @param cids
     */
    public void deleteContacts(final ArrayList<Long> cids) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = DELETE_DEEP_CONTACT_SUCCESS;
                List<ContactToGroup> ctgs = new ArrayList<ContactToGroup>();
                for (int i = 0; i < cids.size(); i++) {
                    ctgs.addAll(mContactToGroupDao._queryContacts_Cid(cids.get(i)));
                }
                mContactToGroupDao.deleteInTx(ctgs);
                mContactsDao.deleteByKeyInTx(cids);
                callback(entity);
            }
        });
    }

    /**
     * 删除联系人
     * @param cid
     */
    public void deleteContact(final Long cid) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = DELETE_DEEP_CONTACT_SUCCESS;
                mContactToGroupDao.deleteInTx(mContactToGroupDao._queryContacts_Cid(cid));
                mContactsDao.deleteByKeyInTx(cid);
                callback(entity);
            }
        });
    }
    /**
     * 删除
     */
    public void deleteContactForGroup(final ArrayList<Long> cids, final Long gid) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = DELETE_CONTACT_SUCCESS;
                List<ContactToGroup> ctg = mContactToGroupDao._queryContactGroup_Gid(gid);

                for (int j = 0; j < ctg.size(); j++) {
                    for (int i = 0; i < cids.size(); i++) {
                        if (ctg.get(j).getCid() == cids.get(i)) {
                            mContactToGroupDao.delete(ctg.get(i));
                        }
                    }
                }
                callback(entity);
            }
        });
    }

    /**
     *编辑联系人
     * @param contacts
     */
    public void editContact(final Contacts contacts){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action = EDIT_CONTACT_SUCCESS;
                mContactsDao.update(contacts);
                callback(entity);
            }
        });
    }

    public void importContactToGroup(final ArrayList<Long> cids,final Long gid){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity = new ProxyEntity();
                entity.action =IMPORT_CONTACT_TO_GROUP_SUCCESS;
                ArrayList<ContactToGroup> ctgs=new ArrayList<ContactToGroup>();
               List<ContactToGroup> isHave=mContactToGroupDao._queryContactGroup_Gid(gid);
                Collections.sort(cids);
                for(ContactToGroup c:isHave){
                    int index=cids.indexOf(c.getCid());
                    if(index!=-1){
                        cids.remove(index);//去重
                    }
                }
                for(Long cid:cids){
                    ContactToGroup ctg=new ContactToGroup(null,cid,gid);
                    ctgs.add(ctg);
                }
                mContactToGroupDao.insertOrReplaceInTx(ctgs);
                callback(entity);
            }
        });
    }
    public void viewAllContactFromPhone(){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=VIEW_ALL_CONTACT_SUCCESS;
                List<Contacts> contacts=PhoneContactUtil.readContacts(mContext);
                mContactsDao.insertOrReplaceInTx(contacts);
                callback(entity);
            }
        });
    }
}
