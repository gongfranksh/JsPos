package personal.wl.jspos.method;

public final class PosPayMent {
    public static final int PAYMENT_CASH = 0;
    public static final int PAYMENT_ALIPAY = 1;
    public static final int PAYMENT_WEIXIN = 2;
    public static final char PAYMENT_CASH_CODE = '1';
    public static final char PAYMENT_ALIPAY_CODE = '7';
    public static final char PAYMENT_WEIXIN_CODE = 'w';

    public static final String PAYMENT_CASH_NAME = "现金";
    public static final String PAYMENT_ALIPAY_NAME = "支付宝";
    public static final String PAYMENT_WEIXIN_NAME = "微信";


    public static char getPayMentCode(int payment) {
        char tmp_paymodecode;
        switch (payment) {
            case PAYMENT_CASH:
                tmp_paymodecode = PAYMENT_CASH_CODE;
                break;
            case PAYMENT_ALIPAY:
                tmp_paymodecode = PAYMENT_ALIPAY_CODE;
                break;
            case PAYMENT_WEIXIN:
                tmp_paymodecode = PAYMENT_WEIXIN_CODE;
                break;
            default:
                tmp_paymodecode = PAYMENT_CASH_CODE;
                break;

        }
        return tmp_paymodecode;
    }

}
