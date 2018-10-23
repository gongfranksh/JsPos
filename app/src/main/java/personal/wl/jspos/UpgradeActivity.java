package personal.wl.jspos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.update.http.HttpToolsKits;

public class UpgradeActivity extends AppCompatActivity {
    private TextView upgrade_readme;
    private String displaystr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        PosTabInfo posTabInfo = new PosTabInfo(UpgradeActivity.this);
        displaystr = "当前系统版本为:" + posTabInfo.getPackageName();
        upgrade_readme = findViewById(R.id.upgrade_readme);
        upgrade_readme.setText(displaystr);
//        UpgradeUI ugui = new UpgradeUI(UpgradeActivity.this);
//        ugui.getversion();

        HttpToolsKits httpToolsKits = new HttpToolsKits(UpgradeActivity.this, getWindow().getDecorView());
        httpToolsKits.downloadVersionFile();


    }
}
