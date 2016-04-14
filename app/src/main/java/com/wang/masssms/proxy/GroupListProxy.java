package com.wang.masssms.proxy;

import android.content.Context;
import android.os.Handler;

import com.wang.masssms.App;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.ContactGroupDao;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by wangguang on 2016/4/7.
 */
public class GroupListProxy extends BaseProxy{
    public static String GET_GROUP_LIST_SUCCESS="get_group_list_success";
    public static String GET_GROUP_LIST_FAILED="get_group_list_failed";
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
    public void getGroupList(){
        ProxyEntity entity=new ProxyEntity();
        entity.action=GET_GROUP_LIST_SUCCESS;
        QueryBuilder<ContactGroup> qb = mContactGroupDao.queryBuilder();
        entity.data=qb.list();
        callback(entity);
    }
}
