package personal.wl.jspos.Config.Beans;

import android.content.Context;
import android.content.SharedPreferences;

import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_QR_PREX;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_UPGRADE_URL;
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
        this.QR_URL = sPre.getString(SETTING_HTTP_QR_URL, DEFAULT_QR_PREX);
        return QR_URL;
    }

    public void setQR_URL(String QR_URL) {
        sPre.edit().putString(SETTING_HTTP_QR_URL, QR_URL).commit();
    }

    private String QR_URL;

    public String getURL() {
        this.URL = sPre.getString(SETTING_HTTP_URL, DEFAULT_UPGRADE_URL);
        return URL;
    }

    public void setURL(String URL) {
        sPre.edit().putString(SETTING_HTTP_URL, URL).commit();
    }
}
