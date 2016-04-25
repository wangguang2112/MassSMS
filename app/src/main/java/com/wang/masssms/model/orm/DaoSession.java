package com.wang.masssms.model.orm;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.Message;
import com.wang.masssms.model.orm.ContactToGroup;
import com.wang.masssms.model.orm.ContactToMessage;
import com.wang.masssms.model.orm.GroupToMessage;

import com.wang.masssms.model.orm.ContactsDao;
import com.wang.masssms.model.orm.ContactGroupDao;
import com.wang.masssms.model.orm.MessageDao;
import com.wang.masssms.model.orm.ContactToGroupDao;
import com.wang.masssms.model.orm.ContactToMessageDao;
import com.wang.masssms.model.orm.GroupToMessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig contactsDaoConfig;
    private final DaoConfig contactGroupDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig contactToGroupDaoConfig;
    private final DaoConfig contactToMessageDaoConfig;
    private final DaoConfig groupToMessageDaoConfig;

    private final ContactsDao contactsDao;
    private final ContactGroupDao contactGroupDao;
    private final MessageDao messageDao;
    private final ContactToGroupDao contactToGroupDao;
    private final ContactToMessageDao contactToMessageDao;
    private final GroupToMessageDao groupToMessageDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        contactsDaoConfig = daoConfigMap.get(ContactsDao.class).clone();
        contactsDaoConfig.initIdentityScope(type);

        contactGroupDaoConfig = daoConfigMap.get(ContactGroupDao.class).clone();
        contactGroupDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        contactToGroupDaoConfig = daoConfigMap.get(ContactToGroupDao.class).clone();
        contactToGroupDaoConfig.initIdentityScope(type);

        contactToMessageDaoConfig = daoConfigMap.get(ContactToMessageDao.class).clone();
        contactToMessageDaoConfig.initIdentityScope(type);

        groupToMessageDaoConfig = daoConfigMap.get(GroupToMessageDao.class).clone();
        groupToMessageDaoConfig.initIdentityScope(type);

        contactsDao = new ContactsDao(contactsDaoConfig, this);
        contactGroupDao = new ContactGroupDao(contactGroupDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        contactToGroupDao = new ContactToGroupDao(contactToGroupDaoConfig, this);
        contactToMessageDao = new ContactToMessageDao(contactToMessageDaoConfig, this);
        groupToMessageDao = new GroupToMessageDao(groupToMessageDaoConfig, this);

        registerDao(Contacts.class, contactsDao);
        registerDao(ContactGroup.class, contactGroupDao);
        registerDao(Message.class, messageDao);
        registerDao(ContactToGroup.class, contactToGroupDao);
        registerDao(ContactToMessage.class, contactToMessageDao);
        registerDao(GroupToMessage.class, groupToMessageDao);
    }
    
    public void clear() {
        contactsDaoConfig.getIdentityScope().clear();
        contactGroupDaoConfig.getIdentityScope().clear();
        messageDaoConfig.getIdentityScope().clear();
        contactToGroupDaoConfig.getIdentityScope().clear();
        contactToMessageDaoConfig.getIdentityScope().clear();
        groupToMessageDaoConfig.getIdentityScope().clear();
    }

    public ContactsDao getContactsDao() {
        return contactsDao;
    }

    public ContactGroupDao getContactGroupDao() {
        return contactGroupDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public ContactToGroupDao getContactToGroupDao() {
        return contactToGroupDao;
    }

    public ContactToMessageDao getContactToMessageDao() {
        return contactToMessageDao;
    }

    public GroupToMessageDao getGroupToMessageDao() {
        return groupToMessageDao;
    }

}
