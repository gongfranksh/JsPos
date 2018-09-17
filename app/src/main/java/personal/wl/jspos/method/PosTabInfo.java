package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

public class PosTabInfo {
    private  static SharedPreferences pre;

    public PosTabInfo(Context context) {
        pre = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getPosMachine(){
        return pre.getString("pos_machine", "0");
    }

    public static  String getBranchCode(){
        return pre.getString("branch_selected", "0");
    }



}
