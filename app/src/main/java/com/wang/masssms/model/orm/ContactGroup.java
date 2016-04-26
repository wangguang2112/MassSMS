package com.wang.masssms.model.orm;

import java.util.List;
import com.wang.masssms.model.orm.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CONTACT_GROUP".
 */
public class ContactGroup {

    private Long id;
    private String name;
    private java.util.Date creatTime;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ContactGroupDao myDao;

    private List<ContactToGroup> gid;
    private List<GroupToMessage> groupToMessageList;

    public ContactGroup() {
    }

    public ContactGroup(Long id) {
        this.id = id;
    }

    public ContactGroup(Long id, String name, java.util.Date creatTime) {
        this.id = id;
        this.name = name;
        this.creatTime = creatTime;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContactGroupDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(java.util.Date creatTime) {
        this.creatTime = creatTime;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ContactToGroup> getGid() {
        if (gid == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactToGroupDao targetDao = daoSession.getContactToGroupDao();
            List<ContactToGroup> gidNew = targetDao._queryContactGroup_Gid(id);
            synchronized (this) {
                if(gid == null) {
                    gid = gidNew;
                }
            }
        }
        return gid;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGid() {
        gid = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<GroupToMessage> getGroupToMessageList() {
        if (groupToMessageList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupToMessageDao targetDao = daoSession.getGroupToMessageDao();
            List<GroupToMessage> groupToMessageListNew = targetDao._queryContactGroup_GroupToMessageList(id);
            synchronized (this) {
                if(groupToMessageList == null) {
                    groupToMessageList = groupToMessageListNew;
                }
            }
        }
        return groupToMessageList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGroupToMessageList() {
        groupToMessageList = null;
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
