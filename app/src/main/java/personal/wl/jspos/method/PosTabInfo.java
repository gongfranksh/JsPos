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

    public static String getPosMachine() {
        return pre.getString("pos_machine", "0");
    }

    public static String getBranchCode() {
        return pre.getString("branch_selected", "0");
    }

    public static void setDeviceId(String s) {
        SharedPreferences.Editor editor =pre.edit();
        editor.putString("pos_device_id",s);
        editor.commit();
    }

    public static String getDeviceid() {
        return pre.getString("pos_device_id", "0");
    }


}
