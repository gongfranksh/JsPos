package personal.wl.jspos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import personal.wl.jspos.db.IReportBack;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.sync.SyncJspotDB;

public class MainActivity extends AppCompatActivity implements IReportBack {

    private TextView mTextMessage;
//    private  PosTabInfo posTabInfo = new PosTabInfo(MainActivity.this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    LoadPos();
                    showdetail();

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    LoadHelp();
                    showdetail();

//                    showPreference();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    syncjsportdb();
                    showdetail();

                    return true;
                case R.id.navigation_setting:
                    mTextMessage.setText("设置启动。。。。");
                    LoadSetting();
                    showdetail();

                    return true;
                case R.id.navigation_login:
                    LoadLogin();
                    showdetail();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showdetail();
    }

    @Override
    public void reportBack(String tag, String message) {

    }

    @Override
    public void reportTransient(String tag, String message) {

    }


    private void syncjsportdb() {
        HashMap devicelist = new HashMap<String, String>();
        PosTabInfo posTabInfo = new PosTabInfo(MainActivity.this);
        devicelist.put("deviceid", posTabInfo.getDeviceid());
        devicelist.put("posno", posTabInfo.getPosMachine());
        SyncJspotDB task = new SyncJspotDB(this, this, "SyncJspotDB", 6, devicelist);
        task.execute();
    }

    private void LoadSetting() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void LoadLogin() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    private void LoadHelp() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PosTransList.class);
        startActivity(intent);
    }

    private void LoadPos() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, POS.class);
        startActivity(intent);
    }


    private void showdetail() {
        PosTabInfo posTabInfo = new PosTabInfo(MainActivity.this);
        String display_msg = "门店编号：" + posTabInfo.getBranchCode() + "\n" +
                "收银机号：" + posTabInfo.getPosMachine() + "\n" +
                "营业员编号：" + posTabInfo.getSalerId()+ "\n";
        mTextMessage.setText(display_msg);



    }

}
