package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static personal.wl.jspos.method.PosHandleDB.getSalerName;

public class PosTabInfo {
    public static final String NOBODY_LOGIN = "00000";
    private SharedPreferences pre;
    private Context context;
    private String BlueToothPrinterName;
    private String BlueToothPrinterAddress;

    public String getBlueToothPrinterName() {
        return pre.getString("BlueTooth_Printer_Name", "0");
    }

    public void setBlueToothPrinterName(String blueToothPrinterName) {
        SharedPreferences.Editor editor = pre.edit();
        BlueToothPrinterName = blueToothPrinterName;
        editor.putString("BlueTooth_Printer_Name", blueToothPrinterName);
        editor.commit();
    }

    public String getBlueToothPrinterAddress() {

        return pre.getString("BlueTooth_Printer_Address", "0");
    }

    public void setBlueToothPrinterAddress(String blueToothPrinterAddress) {
        SharedPreferences.Editor editor = pre.edit();
        BlueToothPrinterAddress = blueToothPrinterAddress;
        editor.putString("BlueTooth_Printer_Address", blueToothPrinterAddress);
        editor.commit();
    }

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
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sd.format(date);
        editor.putString("pos_last_upload_date", sd.format(date));
        editor.commit();
    }

    public String getSalerId() {
        String tmpd = pre.getString("pos_cur_saler", "00000");
        return pre.getString("pos_cur_saler", "00000");
    }

    public String getShowLogon() {
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

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    public String getPackageName() {
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

    public boolean isConnectingToInternet() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE); //获取系统服务
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {  //判断网络是否已连接
                        return true;  //网络已连接
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean HadSetPrinter() {
        if (this.getBlueToothPrinterName().length() != 0 && this.getBlueToothPrinterName() != "0")
            return true;
        return false;
    }

}
