package personal.wl.jspos.pos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MOBILE_DEVICE".
*/
public class MobileDeviceDao extends AbstractDao<MobileDevice, Long> {

    public static final String TABLENAME = "MOBILE_DEVICE";

    /**
     * Properties of entity MobileDevice.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Deviceid = new Property(1, String.class, "deviceid", false, "DEVICEID");
        public final static Property Posno = new Property(2, String.class, "posno", false, "POSNO");
        public final static Property Status = new Property(3, String.class, "Status", false, "STATUS");
        public final static Property SourceId = new Property(4, Long.class, "SourceId", false, "SOURCE_ID");
    }


    public MobileDeviceDao(DaoConfig config) {
        super(config);
    }
    
    public MobileDeviceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MOBILE_DEVICE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DEVICEID\" TEXT," + // 1: deviceid
                "\"POSNO\" TEXT," + // 2: posno
                "\"STATUS\" TEXT," + // 3: Status
                "\"SOURCE_ID\" INTEGER);"); // 4: SourceId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MOBILE_DEVICE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MobileDevice entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceid = entity.getDeviceid();
        if (deviceid != null) {
            stmt.bindString(2, deviceid);
        }
 
        String posno = entity.getPosno();
        if (posno != null) {
            stmt.bindString(3, posno);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(4, Status);
        }
 
        Long SourceId = entity.getSourceId();
        if (SourceId != null) {
            stmt.bindLong(5, SourceId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MobileDevice entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceid = entity.getDeviceid();
        if (deviceid != null) {
            stmt.bindString(2, deviceid);
        }
 
        String posno = entity.getPosno();
        if (posno != null) {
            stmt.bindString(3, posno);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(4, Status);
        }
 
        Long SourceId = entity.getSourceId();
        if (SourceId != null) {
            stmt.bindLong(5, SourceId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MobileDevice readEntity(Cursor cursor, int offset) {
        MobileDevice entity = new MobileDevice( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // deviceid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // posno
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Status
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // SourceId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MobileDevice entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeviceid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPosno(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStatus(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSourceId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MobileDevice entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MobileDevice entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MobileDevice entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
