package com.wang.masssms.proxy;

import com.wang.masssms.App;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.model.orm.MessageDao;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by wangguang on 2016/4/26.
 */
public class MessageProxy extends BaseProxy{

    private MessageDao mMessageDao;
    public static final String INSERT_DRAFT_MESSAGE_SUCCESS="insert_draft_message_success";
    public static final String GET_ALL_HAVE_SEND_MESSAGE_SUCCESS="get_all_have_send_message_success";
    /**
     * 构造函数
     *
     * @param proxyCallbackHandler activity的handler传过来，用于交互
     */
    public MessageProxy(Context context, Handler proxyCallbackHandler) {
        super(context, proxyCallbackHandler);
        mMessageDao= App.getDaoSession(mContext).getMessageDao();
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
            }
        });
    }
}
