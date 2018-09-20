package personal.wl.jspos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static personal.wl.jspos.method.PosHandleDB.CleanLocalSales;

public class PosInit extends AppCompatActivity {
    private Button bt_cleanLocalSaledata;
    private Button bt_uploadLocalSaledata;
    private Button bt_syncdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_init);
        bt_cleanLocalSaledata = findViewById(R.id.sync_clean_local);

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

    }
}
