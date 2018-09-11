package personal.wl.jspos.db;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static  boolean isLoginAccount(String str){
        if (str.length()!=5) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }


        return true;

    }

    public static void sleepForSecs(int secs){
        try{
            Thread.sleep(secs*1000);
        }catch(InterruptedException x){
            throw new RuntimeException("interrupted",x);
        }
    }

    public static String getThreadSignature(){
        Thread t = Thread.currentThread();
        return t.getName() + ": id = " + t.getId() + " priority = " + t.getPriority()
                + " group = " + t.getThreadGroup().getName();
    }

    public static void logThreadSignature(String tag){
        Log.v(tag, getThreadSignature());
    }
}
