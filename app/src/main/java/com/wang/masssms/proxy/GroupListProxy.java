package com.wang.masssms.proxy;

import android.content.Context;
import android.os.Handler;

import com.wang.masssms.App;
import com.wang.masssms.fragment.GroupContactFragment;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.ContactGroupDao;

import java.util.Date;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by wangguang on 2016/4/7.
 */
public class GroupListProxy extends BaseProxy{
    public static String GET_GROUP_LIST_SUCCESS="get_group_list_success";
    public static String GET_GROUP_LIST_FAILED="get_group_list_failed";
    public static String ADD_GROUP_NAME_SUCCESS="add_group_name_success";
    public static String ADD_GROUP_NAME_FAILED="add_group_name_failed";
    ContactGroupDao mContactGroupDao;
    /**
     * 构造函数
     *
     * @param context
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public GroupListProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
        mContactGroupDao= App.getDaoSession(mContext).getContactGroupDao();
    }

    /**
     * 数据库中查询组列表
     */
    public void getGroupList(){
        ProxyEntity entity=new ProxyEntity();
        entity.action=GET_GROUP_LIST_SUCCESS;
        QueryBuilder<ContactGroup> qb = mContactGroupDao.queryBuilder();
        entity.data=qb.list();
        callback(entity);
    }

    /**
     * 添加组名
     * @param name 组名
     */
    public void addGroup(String name){
        ProxyEntity entity=new ProxyEntity();
        entity.action=ADD_GROUP_NAME_SUCCESS;
        ContactGroup contactGroup=new ContactGroup();
        contactGroup.setId(null);//自增长
        contactGroup.setName(name);
        contactGroup.setCreatTime(new Date(System.currentTimeMillis()));
        if(mContactGroupDao.insert(contactGroup)!=-1){
            entity.action=ADD_GROUP_NAME_SUCCESS;
        }else {
            entity.action=ADD_GROUP_NAME_FAILED;
        }
        callback(entity);
    }
}
