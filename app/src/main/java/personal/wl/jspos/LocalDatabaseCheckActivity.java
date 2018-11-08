package personal.wl.jspos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import personal.wl.jspos.method.PosHandleDB;

public class LocalDatabaseCheckActivity extends AppCompatActivity {
    private TextView tv_diplay;
    private String st_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_database_check);
        tv_diplay = findViewById(R.id.localdatadisplay);
        localinfo();
        tv_diplay.setText(st_display);
    }


    private void localinfo() {
        String txt_product = "本地产品Product记录数：" + PosHandleDB.getRecordLocalProduct().toString() + "\n";
        String txt_productBarcode = "本地条码ProductBarcode记录数：" + PosHandleDB.getRecordLocalProductBarcode().toString() + "\n";
        String txt_productBranchRel = "本地产品价格ProductBrachRel记录数：" + PosHandleDB.getRecordLocalProductBranchRel().toString() + "\n";
        String txt_saledaily = "本地交易记录数：" + PosHandleDB.getRecordLocalSaleDaily().toString() + "\n";
        String txt_pmdmbransh = "本地DM促销记录数：" + PosHandleDB.getRecordLocalPmtDMBranch().toString() + "\n";
        st_display = txt_product + txt_productBarcode + txt_productBranchRel + txt_pmdmbransh+txt_saledaily;


    }
}
