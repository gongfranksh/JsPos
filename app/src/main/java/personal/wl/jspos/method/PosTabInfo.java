package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static personal.wl.jspos.method.PosHandleDB.getSalerName;

public class PosTabInfo {
    public static final String NOBODY_LOGIN = "00000";
    private  SharedPreferences pre;
    private  Context context;


    public PosTabInfo(Context context) {
        pre = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
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

    public String getLastSaleId() {
        String tmpd = pre.getString("pos_last_sale_id", "00000");
         return pre.getString("pos_last_sale_id", "00000");
    }


    public void setLastSaleId(String s) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("pos_last_sale_id", s);
        editor.commit();
    }


    public String getLastUploadDate() {
        String tmpd = pre.getString("pos_last_upload_date", "00000");
        return pre.getString("pos_last_upload_date", "00000");
    }


    public void setLastUploadDate(Date date) {
        SharedPreferences.Editor editor = pre.edit();
        SimpleDateFormat sd =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sd.format(date);
        editor.putString("pos_last_upload_date", sd.format(date));
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

    public int getPackageCode() {
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

    public  String getPackageName() {
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
