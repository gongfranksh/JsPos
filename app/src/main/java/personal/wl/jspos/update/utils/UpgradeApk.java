package personal.wl.jspos.update.utils;

import android.content.Context;

import java.util.Map;

import static personal.wl.jspos.method.DeviceUtils.getVersion_JSON;

public class UpgradeApk {

    private Context context;
    private Map<String, Object> upgradeversion;
    private Map<String, Object> upgradeapkinfo;

    private int versioncode;
    private String versionname;
    private String path;

    public int getVersioncode() {
        versioncode = (int) this.upgradeapkinfo.get("versionCode");
        return versioncode;
    }

    public String getVersionname() {
        versionname = (String) this.upgradeapkinfo.get("versionName");
        return versionname;
    }

    public String getPath() {
        path = (String) this.upgradeversion.get("path");
        return path;
    }

    public UpgradeApk(Context context) {
        this.context = context;
        this.upgradeversion = getVersion_JSON(this.context);
        this.upgradeapkinfo = (Map<String, Object>) this.upgradeversion.get("apkInfo");
    }


}
