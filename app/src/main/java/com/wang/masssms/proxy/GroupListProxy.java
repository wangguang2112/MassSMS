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
    public static  String DELETE_GROUP_SUCCESS="delete_group_success";
    public static  String DELETE_GROUP_FAAILED="delete_group_faailed";
    public static  String ALTER_GROUP_SUCCESS="alter_group_success";
    public static  String ALTER_GROUP_FAAILED="alter_group_faailed";
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
        Runnable runnable = new Runnable() {
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_GROUP_LIST_SUCCESS;
                QueryBuilder<ContactGroup> qb = mContactGroupDao.queryBuilder();
                entity.data=qb.list();
                callback(entity);
            }
        };
        cachedThreadPool.execute(runnable);
    }

    /**
     * 添加组名
     * @param name 组名
     */
    public void addGroup(final String name){
        Runnable runnable = new Runnable() {
            public void run() {
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
        };
        cachedThreadPool.execute(runnable);
    }

    /**
     * 删除分组
     * @param gid
     */
   public void deleteGroup(final Long gid){
       Runnable runnable = new Runnable() {
           public void run() {
               ProxyEntity entity=new ProxyEntity();
               entity.action=DELETE_GROUP_SUCCESS;
               mContactGroupDao.deleteByKey(gid);
               entity.action=ADD_GROUP_NAME_SUCCESS;
               callback(entity);
           }
       };
       cachedThreadPool.execute(runnable);
   }

    /**
     * 更改分组
     * @param group
     */
    public void alterGroup(final ContactGroup group){
        Runnable runnable = new Runnable() {
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=ALTER_GROUP_SUCCESS;
                mContactGroupDao.update(group);
                entity.action=ADD_GROUP_NAME_SUCCESS;
                callback(entity);
            }
        };
        cachedThreadPool.execute(runnable);
    }
}
