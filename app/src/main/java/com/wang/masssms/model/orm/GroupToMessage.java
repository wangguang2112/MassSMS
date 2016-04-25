package com.wang.masssms.model.orm;

import com.wang.masssms.model.orm.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "GROUP_TO_MESSAGE".
 */
public class GroupToMessage {

    private Long id;
    private Long gid;
    private Long mid;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GroupToMessageDao myDao;

    private Contacts contacts;
    private Long contacts__resolvedKey;

    private Message message;
    private Long message__resolvedKey;


    public GroupToMessage() {
    }

    public GroupToMessage(Long id) {
        this.id = id;
    }

    public GroupToMessage(Long id, Long gid, Long mid) {
        this.id = id;
        this.gid = gid;
        this.mid = mid;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupToMessageDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    /** To-one relationship, resolved on first access. */
    public Contacts getContacts() {
        Long __key = this.gid;
        if (contacts__resolvedKey == null || !contacts__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactsDao targetDao = daoSession.getContactsDao();
            Contacts contactsNew = targetDao.load(__key);
            synchronized (this) {
                contacts = contactsNew;
            	contacts__resolvedKey = __key;
            }
        }
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        synchronized (this) {
            this.contacts = contacts;
            gid = contacts == null ? null : contacts.getId();
            contacts__resolvedKey = gid;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Message getMessage() {
        Long __key = this.mid;
        if (message__resolvedKey == null || !message__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message messageNew = targetDao.load(__key);
            synchronized (this) {
                message = messageNew;
            	message__resolvedKey = __key;
            }
        }
        return message;
    }

    public void setMessage(Message message) {
        synchronized (this) {
            this.message = message;
            mid = message == null ? null : message.getId();
            message__resolvedKey = mid;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}