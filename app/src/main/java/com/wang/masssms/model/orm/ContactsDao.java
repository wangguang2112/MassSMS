package com.wang.masssms.model.orm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.wang.masssms.model.orm.Contacts;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACTS".
*/
public class ContactsDao extends AbstractDao<Contacts, Long> {

    public static final String TABLENAME = "CONTACTS";

    /**
     * Properties of entity Contacts.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Phonenumber = new Property(1, Integer.class, "phonenumber", false, "PHONENUMBER");
        public final static Property Name = new Property(2, Integer.class, "name", false, "NAME");
        public final static Property Groupid = new Property(3, Integer.class, "groupid", false, "GROUPID");
        public final static Property Creattime = new Property(4, java.util.Date.class, "creattime", false, "CREATTIME");
        public final static Property Lastmodify = new Property(5, java.util.Date.class, "lastmodify", false, "LASTMODIFY");
    };


    public ContactsDao(DaoConfig config) {
        super(config);
    }
    
    public ContactsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACTS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PHONENUMBER\" INTEGER," + // 1: phonenumber
                "\"NAME\" INTEGER," + // 2: name
                "\"GROUPID\" INTEGER," + // 3: groupid
                "\"CREATTIME\" INTEGER," + // 4: creattime
                "\"LASTMODIFY\" INTEGER);"); // 5: lastmodify
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACTS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Contacts entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer phonenumber = entity.getPhonenumber();
        if (phonenumber != null) {
            stmt.bindLong(2, phonenumber);
        }
 
        Integer name = entity.getName();
        if (name != null) {
            stmt.bindLong(3, name);
        }
 
        Integer groupid = entity.getGroupid();
        if (groupid != null) {
            stmt.bindLong(4, groupid);
        }
 
        java.util.Date creattime = entity.getCreattime();
        if (creattime != null) {
            stmt.bindLong(5, creattime.getTime());
        }
 
        java.util.Date lastmodify = entity.getLastmodify();
        if (lastmodify != null) {
            stmt.bindLong(6, lastmodify.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Contacts readEntity(Cursor cursor, int offset) {
        Contacts entity = new Contacts( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // phonenumber
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // groupid
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // creattime
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // lastmodify
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Contacts entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPhonenumber(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setGroupid(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setCreattime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setLastmodify(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Contacts entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Contacts entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
