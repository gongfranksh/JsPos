package personal.wl.jspos.Config.Beans;

import android.content.Context;
import android.content.SharedPreferences;

import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_FTP_ACCOUNT;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_FTP_ACCOUNT_PASSWORD;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_FTP_IP;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_FTP_PATH;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_JOSON_FILE;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.DEFAULT_README_FILE;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_CONF_NAME;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_IP_ADDRESS;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_LOGIN_ACCOUNT;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_LOGIN_PASSWORD;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_UPGRADE_JSON_FILE_NAME;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_UPGRADE_JSON_FILE_NAME_README;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_FTP_UPLOAD_FILE_ADDRESS;

public class SystemFtpInfo {
    private SharedPreferences sPre;
    private String ftp_ip_address;
    private String ftp_name;
    private String ftp_iaccount;
    private String ftp_ipassword;
    private String ftp_path;
    private String ftp_joson_file;
    private String ftp_readme_file;

    private Context context;

    public String getFtp_joson_file() {
         ftp_joson_file=sPre.getString(SETTING_FTP_UPGRADE_JSON_FILE_NAME, DEFAULT_JOSON_FILE);
        return ftp_joson_file;
    }

    public void setFtp_joson_file(String ftp_joson_file) {
        sPre.edit().putString(SETTING_FTP_UPGRADE_JSON_FILE_NAME, ftp_joson_file).commit();
    }

    public String getFtp_readme_file() {
        ftp_readme_file=sPre.getString(SETTING_FTP_UPGRADE_JSON_FILE_NAME_README, DEFAULT_README_FILE);
        return ftp_readme_file;
    }

    public void setFtp_readme_file(String ftp_readme_file) {
        sPre.edit().putString(SETTING_FTP_UPGRADE_JSON_FILE_NAME_README, ftp_readme_file).commit();
    }

    public String getFtp_path() {
        ftp_path=sPre.getString(SETTING_FTP_UPLOAD_FILE_ADDRESS, DEFAULT_FTP_PATH);
        return ftp_path;
    }

    public void setFtp_path(String ftp_path) {
        sPre.edit().putString(SETTING_FTP_UPLOAD_FILE_ADDRESS, ftp_path).commit();
    }



    public String getFtp_ip_address() {
        ftp_ip_address=sPre.getString(SETTING_FTP_IP_ADDRESS, DEFAULT_FTP_IP);
        return ftp_ip_address;
    }

    public void setFtp_ip_address(String ftp_ip_address) {
        sPre.edit().putString(SETTING_FTP_IP_ADDRESS, ftp_ip_address).commit();
    }

    public String getFtp_name() {
        return ftp_name;
    }

    public void setFtp_name(String ftp_name) {
        this.ftp_name = ftp_name;
    }

    public String getFtp_iaccount() {
        ftp_iaccount=sPre.getString(SETTING_FTP_LOGIN_ACCOUNT, DEFAULT_FTP_ACCOUNT);
        return ftp_iaccount;
    }

    public void setFtp_iaccount(String ftp_iaccount) {
        sPre.edit().putString(SETTING_FTP_LOGIN_ACCOUNT, ftp_iaccount).commit();
    }

    public String getFtp_ipassword() {
        ftp_ipassword=sPre.getString(SETTING_FTP_LOGIN_PASSWORD, DEFAULT_FTP_ACCOUNT_PASSWORD);
        return ftp_ipassword;
    }

    public void setFtp_ipassword(String ftp_ipassword) {
        sPre.edit().putString(SETTING_FTP_LOGIN_PASSWORD, ftp_ipassword).commit();
    }



    public SystemFtpInfo(Context context) {
        this.context = context;
        sPre = context.getSharedPreferences(
                SETTING_FTP_CONF_NAME, 0);
    }

}
