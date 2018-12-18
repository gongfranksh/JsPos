package personal.wl.jspos.method;

import android.content.Context;
import android.net.ParseException;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import personal.wl.jspos.pos.SalePayMode;
import personal.wl.jspos.update.utils.FtpInfo;

import static personal.wl.jspos.update.utils.FtpInfo.FTP_IP;

public class DeviceUtils {
    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }

    public static String jsonreplace(String str) {
        String dest = null;
        if (str == null) {
            return dest;
        } else {
//            String regEx=regEx="[\\[\\]]";
//            Pattern p = Pattern.compile(regEx);
//            Matcher m = p.matcher(str);
//            dest = m.replaceAll("").trim();
            dest = str.substring(1, str.length() - 1);

            return dest;
        }
    }

    public static Map<String, Object> getVersion_JSON(Context context) {
        try {
            InputStream jsonversion = new FileInputStream(context.getFilesDir().getPath() + "/" + FtpInfo.UPGRADE_JSON_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(jsonversion, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();


            CommonJSONParser commonJSONParser = new CommonJSONParser();
            Map<String, Object> checkversion = commonJSONParser.parse(jsonreplace(builder.toString()));
            return checkversion;
//
//            JSONObject testjson = new JSONObject();
//            testjson.getJSONObject(builder.toString());
////            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
//            //直接传入JSONObject来构造一个实例
//            JSONArray array = testjson.getJSONArray("role");         //从JSONObject中取出数组对象
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject role = array.getJSONObject(i);    //取出数组中的对象
//            }//

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersion_Readme(Context context) {
        try {
            InputStream versionreadme = new FileInputStream(context.getFilesDir().getPath() + "/" + FtpInfo.UPGRADE_JSON_FILE_NAME_README);
            InputStreamReader isr = new InputStreamReader(versionreadme, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line + '\n');
            }
            br.close();
            isr.close();

            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Boolean CheckResponse(String returnMsg) {
        if (returnMsg.indexOf("100% loss") != -1) return false;
        if (returnMsg.indexOf("100% packet loss") != -1) return false;
        if (returnMsg.length() == 0) return false;
        return true;
    }


    public static String GetTransInnerID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().trim();
    }


    public static String GetTimeStamp() {
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat
                .format(" yyyyMMddHHmmss", sysTime);
        return sysTimeStr.toString().trim();
    }

    public static String GetCurrentTime() {
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat
                .format(" yyyy-MM-dd HH:mm:ss", sysTime);
        return sysTimeStr.toString().trim();
    }

    public static String GetSaleDateYYMMDD(Date tmp_date) {
        CharSequence sysTimeStr = DateFormat
                .format(" yyyy-MM-dd", tmp_date);
        return sysTimeStr.toString().trim();
    }


    public static double ToolsGetSaleAmt(List<SalePayMode> salePayModeList) {
        double tmp_saleamt = 0.00;
        for (int i = 0; i < salePayModeList.size(); i++) {

            tmp_saleamt += salePayModeList.get(i).getPayMoney();
        }
        return tmp_saleamt;
    }


    public static Boolean CheckDB2MSSQLConnect(String IP) {
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 -w 1 " + IP);
            InputStreamReader r = new InputStreamReader(process.getInputStream());
            LineNumberReader returnData = new LineNumberReader(r);
            String rt_msg = "";
            String line = "";
            while ((line = returnData.readLine()) != null) {
                System.out.println(line);
                rt_msg += line;
            }
            return CheckResponse(rt_msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean CheckFtpServerConnect() {
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 -w 1 " + FTP_IP);
            InputStreamReader r = new InputStreamReader(process.getInputStream());
            LineNumberReader returnData = new LineNumberReader(r);
            String rt_msg = "";
            String line = "";
            while ((line = returnData.readLine()) != null) {
                System.out.println(line);
                rt_msg += line;
            }
            return CheckResponse(rt_msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;

    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime); // 把String类型转换为Date类型
        return date;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }
}
