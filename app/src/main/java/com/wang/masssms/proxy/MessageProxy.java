package com.wang.masssms.proxy;

import com.wang.masssms.App;
import com.wang.masssms.model.orm.ContactToMessage;
import com.wang.masssms.model.orm.ContactToMessageDao;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.model.orm.GroupToMessage;
import com.wang.masssms.model.orm.GroupToMessageDao;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.model.orm.MessageDao;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by wangguang on 2016/4/26.
 */
public class MessageProxy extends BaseProxy{

    private MessageDao mMessageDao;
    private GroupToMessageDao mGroupToMessageDao;
    private ContactToMessageDao mContactToMessageDao;
    public static final String INSERT_DRAFT_MESSAGE_SUCCESS="insert_draft_message_success";
    public static final String GET_ALL_HAVE_SEND_MESSAGE_SUCCESS="get_all_have_send_message_success";
    public static final String GET_ALL_HAVE_SEND_MESSAGE_NAME_SUCCESS="get_all_have_send_message_name_success";
    public static final String GET_ALL_COLLECTION_MESSAGE_SUCCESS="get_all_collection_message_success";
    public static final String GET_ALL_DRAFT_MESSAGE_SUCCESS="get_all_draft_message_success";
    /**
     * 构造函数
     *
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public MessageProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
        mMessageDao= App.getDaoSession(mContext).getMessageDao();
        mContactToMessageDao=App.getDaoSession(mContext).getContactToMessageDao();
        mGroupToMessageDao=App.getDaoSession(mContext).getGroupToMessageDao();
    }
    public void getAllHaveSendMessage(){
        cachedThreadPool.execute(new Runnable() {

            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_ALL_HAVE_SEND_MESSAGE_SUCCESS;
                QueryBuilder builder=mMessageDao.queryBuilder();
                builder.where(MessageDao.Properties.Isdraft.eq(false));
                builder.orderDesc(MessageDao.Properties.Sendtime);
                entity.data=builder.list();
                callback(entity);
            }
        });

    }
    public void getAllCollectionMessage(){
        cachedThreadPool.execute(new Runnable() {

            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_ALL_COLLECTION_MESSAGE_SUCCESS;
                QueryBuilder builder=mMessageDao.queryBuilder();
                builder.where(MessageDao.Properties.Iscollect.eq(true));
                builder.orderDesc(MessageDao.Properties.Sendtime);
                entity.data=builder.list();
                callback(entity);
            }
        });

    }

    public void getAllDraftMessage(){
        cachedThreadPool.execute(new Runnable() {

            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_ALL_DRAFT_MESSAGE_SUCCESS;
                QueryBuilder builder=mMessageDao.queryBuilder();
                builder.where(MessageDao.Properties.Isdraft.eq(true));
                builder.orderDesc(MessageDao.Properties.Sendtime);
                entity.data=builder.list();
                callback(entity);
            }
        });

    }
    public void insertDraftMsg(final String message){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=INSERT_DRAFT_MESSAGE_SUCCESS;
                Message m=new Message();
                m.setId(null);
                m.setSendtime(new Date(System.currentTimeMillis()));
                m.setIscollect(false);
                m.setText(message);
                m.setIsdraft(true);
                mMessageDao.insert(m);
                entity.action=INSERT_DRAFT_MESSAGE_SUCCESS;
                callback(entity);
            }
        });
    }
    public void insertMsgFromGroup(final String message, final Long gid){
        ArrayList<Long> gids=new ArrayList<>();
        gids.add(gid);
        insertMsgFromGroups(message, gids);
    }
    public void insertMsgFromGroups(final String message, final ArrayList<Long> gids){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=INSERT_DRAFT_MESSAGE_SUCCESS;
                Message m=new Message();
                m.setId(null);
                Date date=new Date(System.currentTimeMillis());
                m.setSendtime(date);
                m.setIscollect(false);
                m.setText(message);
                m.setIsdraft(false);
                mMessageDao.insertOrReplace(m);
                for(Long gid:gids) {
                    GroupToMessage gtm = new GroupToMessage();
                    gtm.setId(null);
                    Log.d("id",m.getId()+"");
                    gtm.setMid(m.getId());
                    gtm.setGid(gid);
                    mGroupToMessageDao.insertOrReplace(gtm);
                }

                entity.action=INSERT_DRAFT_MESSAGE_SUCCESS;
                callback(entity);
            }
        });
    }
    public void updataCollection(final Message message){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mMessageDao.update(message);
            }
        });
    }
    public void deleteMessage(final Message message){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mMessageDao.delete(message);
                mGroupToMessageDao.deleteInTx(message.getGroupToMessageList());
                mContactToMessageDao.deleteInTx(message.getMid());
            }
        });
    }

    public void getAllHaveSendMessageNames(final List<Message> messageData) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ProxyEntity entity=new ProxyEntity();
                entity.action=GET_ALL_HAVE_SEND_MESSAGE_NAME_SUCCESS;
                List<String> names=new ArrayList<String>();
                for(Message m:messageData){
                    if(m.getIsdraft()){
                        //草稿直接添加草稿
                        names.add("草稿");
                    }else {
                        //当有组的时候 添加组名，没有组的时候添加用户名
                        List<GroupToMessage> gtms = m.getGroupToMessageList();
                        if (gtms.size() > 0) {
                            StringBuilder sb=new StringBuilder();
                            for (GroupToMessage gtm : gtms) {
                                sb.append(gtm.getContactGroup().getName()+",");
                            }
                            sb.deleteCharAt(sb.lastIndexOf(","));
                            names.add(sb.toString());
                        }else {
                            StringBuilder sb = new StringBuilder();
                            List<ContactToMessage> ctms=m.getMid();
                            for (ContactToMessage ctm : ctms) {
                                sb.append(ctm.getContacts().getName() + ",");
                            }
                            if(sb.lastIndexOf(",")!=-1) {
                                sb.deleteCharAt(sb.lastIndexOf(","));
                            }
                            names.add(sb.toString());
                        }

                    }
                }
                entity.data=names;
                callback(entity);
            }
        });
    }
}
