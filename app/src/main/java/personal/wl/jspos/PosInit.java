package personal.wl.jspos;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;

import personal.wl.jspos.method.DESCoder;
import personal.wl.jspos.method.DeviceUtils;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.method.SystemDBInfo;
import personal.wl.jspos.method.SystemFtpInfo;
import personal.wl.jspos.sync.SyncJsSaleData;
import personal.wl.jspos.update.http.HttpToolsKits;
import personal.wl.jspos.update.utils.UpgradeUI;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.method.PosHandleDB.CleanLocalSales;

public class PosInit extends AppCompatActivity {
    private Button bt_cleanLocalSaledata;
    private Button bt_uploadLocalSaledata;
    private Button bt_syncdata;
    private Button bt_getDeviceId;
    private Button bt_checkversion;
    private Button bt_checkversion_http;
    private Button bt_checknetwork;
    private Button bt_upgrade_localdatabase;
    private Button bt_checkdes64;
    private Button bt_db_download;
    private TextView tv_display;
    private EditText device_diplay;
    private EditText adminpass;

    private CommonProgressDialog pBar;

    private View mPopupHeadViewy;//创建一个view
    private PopupWindow mHeadPopupclly;//PopupWindow

    private TextView tetle, textdz;//title,打折
    private TextView textwzdl, textckxq;//我知道了,查看详情
    private Context context;
    private String ADMINPASSWORD = "160023";
    private PosTabInfo posTabInfo;
    private SystemDBInfo systemDBInfo;
    private SystemFtpInfo systemFtpInfo;

    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_init);
        context = PosInit.this;
        posTabInfo = new PosTabInfo(context);
        bt_cleanLocalSaledata = findViewById(R.id.sync_clean_local);
        bt_db_download = findViewById(R.id.downdbtemplate);
        bt_getDeviceId = findViewById(R.id.getdeviceid);
        bt_uploadLocalSaledata = findViewById(R.id.sync_upload_sales);
        bt_checkversion = findViewById(R.id.check_apk);
        bt_checkversion_http = findViewById(R.id.check_apk_http);
        tv_display = findViewById(R.id.initdisplay);
        device_diplay = findViewById(R.id.deviceno_display);
        device_diplay.setVisibility(View.INVISIBLE);
        bt_upgrade_localdatabase = findViewById(R.id.update_localdatabase);
        bt_syncdata = findViewById(R.id.sync_download_data);
        bt_checkdes64 = findViewById(R.id.checkdes64);

        bt_checknetwork = findViewById(R.id.checknetwork);

        systemDBInfo = new SystemDBInfo(context);
        systemFtpInfo = new SystemFtpInfo(context);
        IP=systemDBInfo.get_Db_IP_Address();



        bt_upgrade_localdatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"本地数据库升级",Toast.LENGTH_LONG).show();
                HttpToolsKits httpToolsKits = new HttpToolsKits(PosInit.this, v);
                httpToolsKits.downloadUpgradeSqlfilie();
            }
        });


        bt_checknetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_display.setVisibility(View.VISIBLE);
                if (DeviceUtils.CheckDB2MSSQLConnect(systemDBInfo.get_Db_IP_Address())) {
                    tv_display.setText(IP + "服务器连接成功！！！！！");

                } else {
                    tv_display.setText(IP + "服务器连接失败");
                }

            }
        });


        bt_getDeviceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_deviceid = DeviceUtils.getUniqueId(PosInit.this);
                tv_display.setText(tmp_deviceid);
                device_diplay.setText(tmp_deviceid);
                PosTabInfo posTabInfo = new PosTabInfo(PosInit.this);
                posTabInfo.setDeviceId(tmp_deviceid);
                device_diplay.setVisibility(View.VISIBLE);


            }
        });

        bt_cleanLocalSaledata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    popupHeadWindowcll();
//                    CleanLocalSales();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bt_uploadLocalSaledata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synjssales();
            }
        });

        bt_db_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posTabInfo.isConnectingToInternet() && DeviceUtils.CheckFtpServerConnect(systemFtpInfo.getFtp_ip_address())) {
                    popupDownloadDb();
                } else {
                    Toast.makeText(context, "无法连接网络，先连接WIFI内网", Toast.LENGTH_LONG).show();
                }

            }
        });

        bt_checkversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posTabInfo.isConnectingToInternet() && DeviceUtils.CheckFtpServerConnect(systemFtpInfo.getFtp_ip_address())) {
                    UpgradeUI ugui = new UpgradeUI(PosInit.this);
                    ugui.upload();
                } else {
                    Toast.makeText(context, "无法连接网络，先连接WIFI内网", Toast.LENGTH_LONG).show();
                }
            }
        });


        bt_checkversion_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpToolsKits httpToolsKits = new HttpToolsKits(PosInit.this, v);
                httpToolsKits.downloadVersionFile();

            }
        });

        bt_checkdes64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "saleid=2071810300011&saleamt=699.00&posno=207&braid=01002&saledate=2018-10-30";
                String str_desc = null;
                String encodestr="7etcT5mYsblgKPg6WDgGU4Pqg4jVhQFcMb82579M8MgXKl0mCNz4Rbx+8YsK+f+L\n" +
                        "qsRvfroxP+OWMfHoTuUN4Pm8mjGaUUSur7bHvN+4xgmBaB2+rV1luQ==";
                String decodestr=null;
                try {
                    str_desc = DESCoder.Encrypt(str);
                    decodestr=DESCoder.Decrypt(encodestr);

                } catch (Exception e) {
                    e.printStackTrace();
                }                tv_display.setText(
                        "加密测试-->原文："+str+"\n"+"密文："+str_desc+"\n\n\n\n"+
                        "解密测试-->密文："+encodestr+"\n"+"原文："+decodestr+"\n");
                System.out.printf("encrpt code :%s",str_desc);
            }
        });

    }

    private void synjssales() {
        HashMap devicelist = new HashMap<String, String>();
        PosTabInfo posTabInfo = new PosTabInfo(PosInit.this);
        devicelist.put("deviceid", posTabInfo.getDeviceid());
        devicelist.put("posno", posTabInfo.getPosMachine());

        SyncJsSaleData task = new SyncJsSaleData(PosInit.this, devicelist);
        task.execute();
        posTabInfo.setLastUploadDate(new Date());
    }


    private void popupHeadWindowcll() {
        mPopupHeadViewy = View.inflate(this, R.layout.adminlogin, null);
        tetle = (TextView) mPopupHeadViewy.findViewById(R.id.tetle);
//        textdz = (TextView) mPopupHeadViewy.findViewById(R.id.textdz);
        adminpass = mPopupHeadViewy.findViewById(R.id.adminpasswordinput);
        textwzdl = (TextView) mPopupHeadViewy.findViewById(R.id.textwzdl);
        textckxq = (TextView) mPopupHeadViewy.findViewById(R.id.textckxq);
//        mHeadPopupclly = new PopupWindow(mPopupHeadViewy, AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT, true);
        mHeadPopupclly = new PopupWindow(mPopupHeadViewy, (int) (getScreenWidth() * 0.5), -2, true);
        // 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mHeadPopupclly.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mHeadPopupclly.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mHeadPopupclly.setBackgroundDrawable(new BitmapDrawable());
        mHeadPopupclly.setOutsideTouchable(true);
        mHeadPopupclly.showAsDropDown(bt_syncdata, 0, 0);
        textwzdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ADMINPASSWORD.equals(adminpass.getText().toString())) {
                    Toast.makeText(context, "管理员正确", Toast.LENGTH_LONG).show();
                    CleanLocalSales();
                    mHeadPopupclly.dismiss();
                } else {
                    Toast.makeText(context, "管理员密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
        textckxq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeadPopupclly.dismiss();
                Toast.makeText(PosInit.this, "取消", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void popupDownloadDb() {
        mPopupHeadViewy = View.inflate(this, R.layout.adminlogin, null);
        tetle = (TextView) mPopupHeadViewy.findViewById(R.id.tetle);
        TextView msg = (TextView) mPopupHeadViewy.findViewById(R.id.msg_display);
        msg.setText( "此操作会清除本地交易记录！");

//        textdz = (TextView) mPopupHeadViewy.findViewById(R.id.textdz);
        adminpass = mPopupHeadViewy.findViewById(R.id.adminpasswordinput);
        textwzdl = (TextView) mPopupHeadViewy.findViewById(R.id.textwzdl);
        textckxq = (TextView) mPopupHeadViewy.findViewById(R.id.textckxq);
//        mHeadPopupclly = new PopupWindow(mPopupHeadViewy, AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT, true);
        mHeadPopupclly = new PopupWindow(mPopupHeadViewy, (int) (getScreenWidth() * 0.5), -2, true);
        // 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mHeadPopupclly.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mHeadPopupclly.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mHeadPopupclly.setBackgroundDrawable(new BitmapDrawable());
        mHeadPopupclly.setOutsideTouchable(true);
        mHeadPopupclly.showAsDropDown(bt_syncdata, 0, 0);
        textwzdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ADMINPASSWORD.equals(adminpass.getText().toString())) {
                    Toast.makeText(context, "管理员正确", Toast.LENGTH_LONG).show();
                    UpgradeUI ugui = new UpgradeUI(PosInit.this);
                    ugui.dn_db_template();
                    mHeadPopupclly.dismiss();
                } else {
                    Toast.makeText(context, "管理员密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
        textckxq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeadPopupclly.dismiss();
                Toast.makeText(PosInit.this, "取消", Toast.LENGTH_LONG).show();
            }
        });
    }



    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = PosInit.this.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
