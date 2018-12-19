package personal.wl.jspos.Config.Beans;

public class SystemSettingConstant {

    public static String DEFAULT_QR_PREX = "http://fapiao.buynow.com.cn:8090/invoice/qr?saleid=saleid&braid=braid&saledate=saledate";
    public static String DEFAULT_UPGRADE_URL = "http://192.168.168.169/apk/";

    public static String DEFAULT_MSSQL_DB_IP = "192.168.72.5";
    public static String DEFAULT_MSSQL_DB_NAME = "headquarters";
    public static String DEFAULT_MSSQL_ACCOUNT = "syzy";
    public static String DEFAULT_MSSQL_ACCOUNT_PASSWORD = "7fad69fa0c";

    public static String DEFAULT_FTP_IP = "192.168.72.220";
    public static String DEFAULT_FTP_ACCOUNT = "sy";
    public static String DEFAULT_FTP_ACCOUNT_PASSWORD = "buynow";
    public static String DEFAULT_FTP_PATH = "/posapp/";

    public static String DEFAULT_README_FILE = "readme.txt";
    public static String DEFAULT_JOSON_FILE = "output.json";

    public final static String SETTING_APP_APK_FIlE = "app-release.apk";
    public final static String SETTING_LOCAL_DB_NAME = "jsPos.db";
    public final static String SETTING_UPGRADE_SQL_FILE = "upgrade.sql";
    public static final String TEST_OK_STATUS= "TESTOK";

    public static final String SETTING_DB_CONF_NAME="DBSetting";
    public static final String SETTING_DB_IP_ADDRESS="SETTING_DB_IP_ADDRESS";
    public static final String SETTING_DB_NAME="SETTING_DB_NAME";
    public static final String SETTING_DB_ACCOUNT="SETTING_DB_ACCOUNT";
    public static final String SETTING_DB_PASSWORD="SETTING_DB_PASSWORD";

    public static final String SETTING_FTP_CONF_NAME="FTPSetting";
    public final static String SETTING_FTP_IP_ADDRESS = "SETTING_FTP_IP_ADDRESS";
    public final static String SETTING_FTP_LOGIN_ACCOUNT = "SETTING_FTP_LOGIN_ACCOUNT";
    public final static String SETTING_FTP_LOGIN_PASSWORD = "SETTING_FTP_LOGIN_PASSWORD";
    public final static String SETTING_FTP_UPGRADE_JSON_FILE_NAME = "SETTING_FTP_UPGRADE_JSON_FILE_NAME";
    public final static String SETTING_FTP_UPGRADE_JSON_FILE_NAME_README = "SETTING_FTP_UPGRADE_JSON_FILE_NAME_README";
    public final static String SETTING_FTP_UPLOAD_FILE_ADDRESS = "SETTING_FTP_UPLOAD_FILE_ADDRESS";

    public static final String SETTING_HTTP_CONF_NAME="HTTPSetting";
    public final static String SETTING_HTTP_URL = "SETTING_HTTP_URL";
    public final static String SETTING_HTTP_QR_URL = "SETTING_HTTP_QR_URL";

    public final static int FTP_DOWNLOAD_OVER = 9100;
    public final static int FTP_DOWNLOAD_UPDATE = 9110;
    public final static int FTP_DOWNLOAD_DB_TEMPLATE = 9170;
    public final static int FTP_UPLOAD_UPDATE = 9120;




}
