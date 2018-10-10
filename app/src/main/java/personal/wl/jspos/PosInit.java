package personal.wl.jspos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

import personal.wl.jspos.method.DeviceUtils;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.sync.SyncJsSaleData;
import personal.wl.jspos.update.utils.UpgradeUI;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.method.PosHandleDB.CleanLocalSales;

public class PosInit extends AppCompatActivity {
    private Button bt_cleanLocalSaledata;
    private Button bt_uploadLocalSaledata;
    private Button bt_syncdata;
    private Button bt_getDeviceId;
    private Button bt_checkversion;
    private TextView tv_display;
    private CommonProgressDialog pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_init);
        bt_cleanLocalSaledata = findViewById(R.id.sync_clean_local);
        bt_getDeviceId = findViewById(R.id.getdeviceid);
        bt_uploadLocalSaledata = findViewById(R.id.sync_upload_sales);
        bt_checkversion = findViewById(R.id.check_apk);
        tv_display = findViewById(R.id.initdisplay);


        bt_getDeviceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp_deviceid = DeviceUtils.getUniqueId(PosInit.this);
                tv_display.setText(tmp_deviceid);

                PosTabInfo posTabInfo = new PosTabInfo(PosInit.this);
                posTabInfo.setDeviceId(tmp_deviceid);

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
