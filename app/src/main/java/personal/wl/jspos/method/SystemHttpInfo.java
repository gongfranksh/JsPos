package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;

import static personal.wl.jspos.method.SystemSettingConstant.SETTING_HTTP_CONF_NAME;
import static personal.wl.jspos.method.SystemSettingConstant.SETTING_HTTP_URL;

public class SystemHttpInfo {
    private SharedPreferences sPre;
    private Context context;

    public SystemHttpInfo(Context context) {
        this.context = context;
        sPre = context.getSharedPreferences(
                SETTING_HTTP_CONF_NAME, 0);
    }

    private String URL;

    public String getURL() {
        this.URL = sPre.getString(SETTING_HTTP_URL, "http://192.168.168.169/apk/");
        return URL;
    }

    public void setURL(String URL) {
        sPre.edit().putString(SETTING_HTTP_URL, URL).commit();
    }
}
