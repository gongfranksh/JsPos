package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Collections;

import static personal.wl.jspos.method.PosHandleDB.getSalerName;

public class PosTabInfo {
    public static final String NOBODY_LOGIN = "00000";
    private  SharedPreferences pre;
    private  Context context;


    public PosTabInfo(Context context) {
        pre = PreferenceManager.getDefaultSharedPreferences(context);
        context = context;
    }

    public String getPosMachine() {
        return pre.getString("pos_machine", "0");
    }

    public String getBranchCode() {
        return pre.getString("branch_selected", "0");
    }

    public void setDeviceId(String s) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("pos_device_id", s);
        editor.commit();
    }

    public String getDeviceid() {

        return pre.getString("pos_device_id", "0");
    }

    public void setSalerid(String s) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("pos_cur_saler", s);
        editor.commit();
    }

    public String getSalerId() {
        String tmpd = pre.getString("pos_cur_saler", "00000");
         return pre.getString("pos_cur_saler", "00000");
    }

    public String getShowLogon(){
        String str_showlogon = "当前登陆者\n 编号:" + this.getSalerId() + "\t" +
                "姓名:" + getSalerName(this.getSalerId());
        return str_showlogon;
    }

    public int packageCode() {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public  String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }
}
