package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Collections;

public class PosTabInfo {
    private static SharedPreferences pre;


    public PosTabInfo(Context context) {
        pre = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public  String getPosMachine() {
        return pre.getString("pos_machine", "0");
    }

    public  String getBranchCode() {
        return pre.getString("branch_selected", "0");
    }

    public  void setDeviceId(String s) {
        SharedPreferences.Editor editor =pre.edit();
        editor.putString("pos_device_id",s);
        editor.commit();
    }

    public  String getDeviceid() {
        return pre.getString("pos_device_id", "0");
    }


}
