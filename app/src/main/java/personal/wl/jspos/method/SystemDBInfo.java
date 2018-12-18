package personal.wl.jspos.method;

import android.content.Context;
import android.content.SharedPreferences;

import static personal.wl.jspos.method.SystemSettingConstant.SETTING_DB_ACCOUNT;
import static personal.wl.jspos.method.SystemSettingConstant.SETTING_DB_CONF_NAME;
import static personal.wl.jspos.method.SystemSettingConstant.SETTING_DB_IP_ADDRESS;
import static personal.wl.jspos.method.SystemSettingConstant.SETTING_DB_NAME;
import static personal.wl.jspos.method.SystemSettingConstant.SETTING_DB_PASSWORD;

public class SystemDBInfo {
    private SharedPreferences sPre;
    private String db_ip_address;
    private String db_name;
    private String db_account;
    private String db_password;
    private Context context;


    public String get_Db_IP_Address() {
        db_ip_address=sPre.getString(SETTING_DB_IP_ADDRESS, "0.0.0.0");
        return db_ip_address;
    }

    public void set_Db_Ip_Address(String db_ip_address) {
        sPre.edit().putString(SETTING_DB_IP_ADDRESS, db_ip_address).commit();
    }

    public String get_Db_Name() {
        this.db_name=sPre.getString(SETTING_DB_NAME, "headquarters");
        return this.db_name;
    }

    public void set_Db_Name(String db_name) {
        sPre.edit().putString(SETTING_DB_NAME, db_name).commit();
    }

    public String get_Db_Account() {
        this.db_account=sPre.getString(SETTING_DB_ACCOUNT, "syzy");
        return this.db_account;
    }

    public void set_Db_Account(String db_account) {
        sPre.edit().putString(SETTING_DB_ACCOUNT, db_account).commit();
    }

    public String get_Db_Password() {
        this.db_password=sPre.getString(SETTING_DB_PASSWORD, "7fad69fa0c");
        return this.db_password;
    }

    public void set_Db_Password(String db_password) {
        sPre.edit().putString(SETTING_DB_PASSWORD, db_password).commit();
    }



    public SystemDBInfo(Context context) {
        this.context = context;
        sPre = context.getSharedPreferences(
                SETTING_DB_CONF_NAME, 0);
    }

}
