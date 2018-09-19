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

import personal.wl.jspos.db.IReportBack;
import personal.wl.jspos.sync.SyncJspotDB;

public class MainActivity extends AppCompatActivity implements IReportBack {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    LoadPos();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    LoadHelp();
//                    showPreference();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    syncjsportdb();
                    return true;
                case R.id.navigation_setting:
                    mTextMessage.setText("设置启动。。。。");
                    LoadSetting();
                    return true;
                case R.id.navigation_login:
                    LoadLogin();
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
    }

    @Override
    public void reportBack(String tag, String message) {

    }

    @Override
    public void reportTransient(String tag, String message) {

    }



    private void syncjsportdb() {
        SyncJspotDB task = new SyncJspotDB(this, this, "SyncJspotDB", 6);
        task.execute();
    }

    private void LoadSetting(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    private void LoadLogin(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }


    private void LoadHelp(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,PosTransList.class);
        startActivity(intent);
    }

    private void LoadPos(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,POS.class);
        startActivity(intent);
    }


    private void showPreference(){
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        String branch_selected = pre.getString("branch_selected", "0");
        String pos_machine_selected = pre.getString("pos_machine", "0");
        String[] branch = getResources().getStringArray(R.array.pref_branch_list_name);


        Toast.makeText(this,branch.toString(),Toast.LENGTH_LONG).show();


        mTextMessage.setText(branch_selected+pos_machine_selected);
    }
}
