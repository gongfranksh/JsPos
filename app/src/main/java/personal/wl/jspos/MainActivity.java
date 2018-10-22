package personal.wl.jspos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

import personal.wl.jspos.db.IReportBack;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.sync.SyncJspotDB;
import personal.wl.jspos.update.utils.Tools;
import personal.wl.jspos.update.view.CommonProgressDialog;

public class MainActivity extends AppCompatActivity implements IReportBack {

    public static int PROCESS_STEPS=8;
    private TextView mTextMessage;
    private CommonProgressDialog pBar;


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

//        getVersion();
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
        devicelist.put("braid", posTabInfo.getBranchCode());
        SyncJspotDB task = new SyncJspotDB(this, this, "SyncJspotDB", PROCESS_STEPS, devicelist);
        task.execute();
        posTabInfo.setLastUploadDate(new Date());
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
                "营业员编号：" + posTabInfo.getSalerId() + "\n" +
                "版本号：" + posTabInfo.getPackageName() + "\n" +
                "最后同步时间:" + posTabInfo.getLastUploadDate();
        mTextMessage.setText(display_msg);
    }

    //增加了版本自动检查下载功能

    // 获取更新版本号
    private void getVersion() {
//         {"data":{"content":"其他bug修复。","id":"2","api_key":"android",
//         // "version":"2.1"},"msg":"获取成功","status":1}

        int vision = Tools.getVersion(this);
        String data = "";
        //网络请求获取当前版本号和下载链接
        //实际操作是从服务器获取
        //demo写死了

        String newversion = "2.1";//更新新的版本号
        String content = "\n" +
                "就不告诉你我们更新了什么-。-\n" +
                "\n" +
                "----------万能的分割线-----------\n" +
                "\n" +
                "(ㄒoㄒ) 被老板打了一顿，还是来告诉你吧：\n" +

                "1.下架商品误买了？恩。。。我搞了点小动作就不会出现了\n" +
                "2.侧边栏、弹框优化 —— 这个你自己去探索吧，总得留点悬念嘛-。-\n";//更新内容
        String url = "http://openbox.mobilem.360.cn/index/d/sid/3429345";//安装包下载地址

        double newversioncode = Double
                .parseDouble(newversion);
        int cc = (int) (newversioncode);

        System.out.println(newversion + "v" + vision + ",,"
                + cc);
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(newversion + "v"
                        + vision);
                // 版本号不同
                ShowDialog(vision, newversion, content, url);
            }
        }




    }
    private void ShowDialog(int vision, String newversion, String content,
                            final String url) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(MainActivity.this);
                        pBar.setCanceledOnTouchOutside(false);
                        pBar.setTitle("正在下载");
                        pBar.setCustomTitle(LayoutInflater.from(
                                MainActivity.this).inflate(
                                R.layout.title_dialog, null));
                        pBar.setMessage("正在下载");
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(true);
                        // downFile(URLData.DOWNLOAD_URL);
//                        final DownloadTask downloadTask = new DownloadTask(
//                                MainActivity.this);
//                        downloadTask.execute(url);
//                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                downloadTask.cancel(true);
//                            }
//                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
