package com.wang.masssms.model.orm;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.wang.masssms.model.orm.ContactToGroup;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACT_TO_GROUP".
*/
public class ContactToGroupDao extends AbstractDao<ContactToGroup, Long> {

    public static final String TABLENAME = "CONTACT_TO_GROUP";

    /**
     * Properties of entity ContactToGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Cid = new Property(1, Long.class, "cid", false, "CID");
        public final static Property Gid = new Property(2, Long.class, "gid", false, "GID");
    };

    private DaoSession daoSession;

    private Query<ContactToGroup> contacts_CidQuery;
    private Query<ContactToGroup> contactGroup_GidQuery;

    public ContactToGroupDao(DaoConfig config) {
        super(config);
    }
    
    public ContactToGroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACT_TO_GROUP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CID\" INTEGER," + // 1: cid
                "\"GID\" INTEGER);"); // 2: gid
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACT_TO_GROUP\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ContactToGroup entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long cid = entity.getCid();
        if (cid != null) {
            stmt.bindLong(2, cid);
        }
 
        Long gid = entity.getGid();
        if (gid != null) {
            stmt.bindLong(3, gid);
        }
    }

    @Override
    protected void attachEntity(ContactToGroup entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ContactToGroup readEntity(Cursor cursor, int offset) {
        ContactToGroup entity = new ContactToGroup( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // cid
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2) // gid
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ContactToGroup entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCid(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setGid(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ContactToGroup entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ContactToGroup entity) {
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
    
    /** Internal query to resolve the "cid" to-many relationship of Contacts. */
    public List<ContactToGroup> _queryContacts_Cid(Long cid) {
        synchronized (this) {
            if (contacts_CidQuery == null) {
                QueryBuilder<ContactToGroup> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Cid.eq(null));
                contacts_CidQuery = queryBuilder.build();
            }
        }
        Query<ContactToGroup> query = contacts_CidQuery.forCurrentThread();
        query.setParameter(0, cid);
        return query.list();
    }

    /** Internal query to resolve the "gid" to-many relationship of ContactGroup. */
    public List<ContactToGroup> _queryContactGroup_Gid(Long gid) {
        synchronized (this) {
            if (contactGroup_GidQuery == null) {
                QueryBuilder<ContactToGroup> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Gid.eq(null));
                contactGroup_GidQuery = queryBuilder.build();
            }
        }
        Query<ContactToGroup> query = contactGroup_GidQuery.forCurrentThread();
        query.setParameter(0, gid);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getContactsDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getContactGroupDao().getAllColumns());
            builder.append(" FROM CONTACT_TO_GROUP T");
            builder.append(" LEFT JOIN CONTACTS T0 ON T.\"CID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN CONTACT_GROUP T1 ON T.\"GID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ContactToGroup loadCurrentDeep(Cursor cursor, boolean lock) {
        ContactToGroup entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Contacts contacts = loadCurrentOther(daoSession.getContactsDao(), cursor, offset);
        entity.setContacts(contacts);
        offset += daoSession.getContactsDao().getAllColumns().length;

        ContactGroup contactGroup = loadCurrentOther(daoSession.getContactGroupDao(), cursor, offset);
        entity.setContactGroup(contactGroup);

        return entity;    
    }

    public ContactToGroup loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ContactToGroup> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ContactToGroup> list = new ArrayList<ContactToGroup>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ContactToGroup> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ContactToGroup> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
