package personal.wl.jspos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

import personal.wl.jspos.method.DeviceUtils;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.sync.SyncJsSaleData;
import personal.wl.jspos.update.http.HttpToolsKits;
import personal.wl.jspos.update.utils.UpgradeUI;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.db.DBC2Jspot.IP;
import static personal.wl.jspos.method.PosHandleDB.CleanLocalSales;

public class PosInit extends AppCompatActivity {
    private Button bt_cleanLocalSaledata;
    private Button bt_uploadLocalSaledata;
    private Button bt_syncdata;
    private Button bt_getDeviceId;
    private Button bt_checkversion;
    private Button bt_checkversion_http;
    private Button bt_checknetwork;
    private TextView tv_display;
    private EditText device_diplay;
    private CommonProgressDialog pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_init);
        bt_cleanLocalSaledata = findViewById(R.id.sync_clean_local);
        bt_getDeviceId = findViewById(R.id.getdeviceid);
        bt_uploadLocalSaledata = findViewById(R.id.sync_upload_sales);
        bt_checkversion = findViewById(R.id.check_apk);
        bt_checkversion_http = findViewById(R.id.check_apk_http);
        tv_display = findViewById(R.id.initdisplay);
        device_diplay = findViewById(R.id.deviceno_display);
        device_diplay.setVisibility(View.INVISIBLE);

        bt_checknetwork = findViewById(R.id.checknetwork);


        bt_checknetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_display.setVisibility(View.VISIBLE);
                if (DeviceUtils.CheckDB2MSSQLConnect()) {
                    tv_display.setText(IP + "服务器连接成功！！！！！");

                } else {
                    tv_display.setText(IP + "服务器连接失败");
                }

//                try {
//                    Process process = Runtime.getRuntime().exec("ping -c 1 -w 1 " + IP);
//                    InputStreamReader r = new InputStreamReader(process.getInputStream());
//                    LineNumberReader returnData = new LineNumberReader(r);
//                    String returnMsg = "";
//                    String line = "";
//                    while ((line = returnData.readLine()) != null) {
//                        System.out.println(line);
//                        returnMsg += line;
//                    }
//
//                    tv_display.setVisibility(View.VISIBLE);
//                    if (DeviceUtils.CheckDBConnect(returnMsg)) {
//                        tv_display.setText(IP + "服务器连接成功！！！！！");
//                        //                        System.out.println("与 " +address +" 连接不畅通.");
//                    } else {
//                        tv_display.setText(IP + "服务器连接失败");
////                        System.out.println("与 " +address +" 连接畅通.");
//                    }
//
//                } catch (IOException e) {
//                    Toast.makeText(PosInit.this, e.toString(), Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
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
                    CleanLocalSales();
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

        bt_checkversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpgradeUI ugui = new UpgradeUI(PosInit.this);
                ugui.getversion();

            }
        });


        bt_checkversion_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpToolsKits httpToolsKits = new HttpToolsKits(PosInit.this, v);
                httpToolsKits.downloadVersionFile();

//                try {
////                    String uriString = "file:///storage/emulated/0/Android/data/personal.wl.jspos/files/Download/app-release-1.apk";
////                    String uriString = PosInit.this.getFilesDir().getPath() + "/" + APK_FIlE;
////                    File targetApkFile = new File(uriString);
////                    Uri contentUri = FileProvider.getUriForFile(PosInit.this, PosInit.this.getApplicationContext().getPackageName() + ".provider", targetApkFile);
//
//                    String uriString = "/storage/emulated/0/Android/data/personal.wl.jspos/files/Download/app-release-1.apk";
//                    File targetApkFile = new File(uriString);
//                    Uri contentUri = FileProvider.getUriForFile(PosInit.this, PosInit.this.getApplicationContext().getPackageName() + ".provider", targetApkFile);
//
//
//                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
//                    installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//
//
//
//                    startActivity(installIntent);
//
//
//                } catch (Exception e) {
//                    Toast.makeText(PosInit.this,e.toString(),Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//
//                }
//
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


}
