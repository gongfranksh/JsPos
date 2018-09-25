package personal.wl.jspos.pos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 3): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        ProductDao.createTable(db, ifNotExists);
        ProductBarCodeDao.createTable(db, ifNotExists);
        ProductBranchRelDao.createTable(db, ifNotExists);
        ProductBarCodeBranchRelDao.createTable(db, ifNotExists);
        ProductClassDao.createTable(db, ifNotExists);
        SaleDailyDao.createTable(db, ifNotExists);
        SalePayModeDao.createTable(db, ifNotExists);
        BranchDao.createTable(db, ifNotExists);
        BranchEmployeeDao.createTable(db, ifNotExists);
        PosMachineDao.createTable(db, ifNotExists);
        MobileDeviceDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        ProductDao.dropTable(db, ifExists);
        ProductBarCodeDao.dropTable(db, ifExists);
        ProductBranchRelDao.dropTable(db, ifExists);
        ProductBarCodeBranchRelDao.dropTable(db, ifExists);
        ProductClassDao.dropTable(db, ifExists);
        SaleDailyDao.dropTable(db, ifExists);
        SalePayModeDao.dropTable(db, ifExists);
        BranchDao.dropTable(db, ifExists);
        BranchEmployeeDao.dropTable(db, ifExists);
        PosMachineDao.dropTable(db, ifExists);
        MobileDeviceDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ProductDao.class);
        registerDaoClass(ProductBarCodeDao.class);
        registerDaoClass(ProductBranchRelDao.class);
        registerDaoClass(ProductBarCodeBranchRelDao.class);
        registerDaoClass(ProductClassDao.class);
        registerDaoClass(SaleDailyDao.class);
        registerDaoClass(SalePayModeDao.class);
        registerDaoClass(BranchDao.class);
        registerDaoClass(BranchEmployeeDao.class);
        registerDaoClass(PosMachineDao.class);
        registerDaoClass(MobileDeviceDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
