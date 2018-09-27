package personal.wl.jspos.db;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.InverseBindingAdapter;
import android.text.format.DateUtils;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.String.valueOf;
import static personal.wl.jspos.method.PosHandleDB.getSalerName;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY_NAME;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH_NAME;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN_NAME;

public class InverseMethodTools {
    @BindingAdapter("android:text")
    public static void setText(TextView view, double value) {
        if (view.getText() != null
                ) {
            view.setText(Double.toString(value));
        }

    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Double getText(TextView view) {
        if (!view.getText().toString().isEmpty()) {
            String s = view.getText().toString().trim();
            s = s.replaceAll("\r|\n|\t", "");
            return Double.parseDouble(s);
        } else return 1.00;
    }


    public static String DateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.CHINA);
        return sdf.format(date);
    }


    public static String PaymentToDisplay(String paycode) {
        char[] paychars = paycode.toCharArray();

        switch (paychars[0]) {
            case PAYMENT_CASH_CODE:
                return PAYMENT_CASH_NAME;

            case PAYMENT_ALIPAY_CODE:
                return PAYMENT_ALIPAY_NAME;
            case PAYMENT_WEIXIN_CODE:
                return PAYMENT_WEIXIN_NAME;
            default:
                return "错误";
        }

    }

    public static String SalerIdToDisplayName(String salerid) {
        return getSalerName(salerid);
        }

}




