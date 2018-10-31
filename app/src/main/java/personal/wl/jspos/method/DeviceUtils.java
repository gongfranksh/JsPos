package personal.wl.jspos.method;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import personal.wl.jspos.update.utils.FtpInfo;

import static personal.wl.jspos.db.DBC2Jspot.IP;

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

    public static Boolean CheckDB2MSSQLConnect() {
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


}
