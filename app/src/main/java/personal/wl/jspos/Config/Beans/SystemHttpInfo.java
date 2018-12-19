package personal.wl.jspos.Config.Beans;

import android.content.Context;
import android.content.SharedPreferences;

import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_HTTP_CONF_NAME;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_HTTP_QR_URL;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_HTTP_URL;

public class SystemHttpInfo {
    private SharedPreferences sPre;
    private Context context;

    public SystemHttpInfo(Context context) {
        this.context = context;
        sPre = context.getSharedPreferences(
                SETTING_HTTP_CONF_NAME, 0);
    }

    private String URL;

    public String getQR_URL() {
        this.QR_URL = sPre.getString(SETTING_HTTP_QR_URL, "http://fapiao.buynow.com.cn:8090/invoice/qr?saleid=saleid&braid=braid&saledate=saledate");
        return QR_URL;
    }

    public void setQR_URL(String QR_URL) {
        sPre.edit().putString(SETTING_HTTP_QR_URL, QR_URL).commit();
    }

    private String QR_URL;

    public String getURL() {
        this.URL = sPre.getString(SETTING_HTTP_URL, "http://192.168.168.169/apk/");
        return URL;
    }

    public void setURL(String URL) {
        sPre.edit().putString(SETTING_HTTP_URL, URL).commit();
    }
}
