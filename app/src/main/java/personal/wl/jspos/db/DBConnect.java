package personal.wl.jspos.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import personal.wl.jspos.pos.DaoMaster;
import personal.wl.jspos.pos.DaoSession;

public class DBConnect extends Application {
    private SQLiteDatabase db;
    private DaoMaster master;
    private DaoSession session;


    private static final String DB_NAME = "jsPos.db";

    public static DBConnect instances;

    public DBConnect() {
    }

    private void setDatabase() {
        db = new DaoMaster.DevOpenHelper(this, DB_NAME, null).getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
    }

    public DaoSession getDaoSession() {
        return session;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.printf("database onCreate");
        Log.v("database", "CreateON--");
        instances = this;
        setDatabase();
    }

    public static DBConnect getInstances() {
        return instances;
    }
}
