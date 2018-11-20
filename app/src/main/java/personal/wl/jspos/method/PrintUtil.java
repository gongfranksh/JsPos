package personal.wl.jspos.method;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.format.DateFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static personal.wl.jspos.method.PosHandleDB.QueryProductByCode;

//import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 蓝牙打印工具类
 */
public class PrintUtil {

    private OutputStreamWriter mWriter = null;
    private OutputStream mOutputStream = null;

    public final static int WIDTH_PIXEL = 384;
    public final static int IMAGE_SIZE = 320;
//    public final static int WIDTH_PIXEL = 200;
//    public final static int IMAGE_SIZE = 320;


    /**
     * 初始化Pos实例
     *
     * @param encoding 编码
     * @throws IOException
     */
    public PrintUtil(OutputStream outputStream, String encoding) throws IOException {
        mWriter = new OutputStreamWriter(outputStream, encoding);
        mOutputStream = outputStream;
        initPrinter();
    }

    public void print(byte[] bs) throws IOException {
        mOutputStream.write(bs);
    }

    public void printRawBytes(byte[] bytes) throws IOException {
        mOutputStream.write(bytes);
        mOutputStream.flush();
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    public void initPrinter() throws IOException {
        mWriter.write(0x1B);
        mWriter.write(0x40);
        mWriter.flush();
    }

    /**
     * 打印换行
     *
     * @return length 需要打印的空行数
     * @throws IOException
     */
    public void printLine(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            mWriter.write("\n");
        }
        mWriter.flush();
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    public void printLine() throws IOException {
        printLine(1);
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    public void printTabSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            mWriter.write("\t");
        }
        mWriter.flush();
    }

    public void printSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            mWriter.write(" ");
        }
        mWriter.flush();
    }

    /**
     * 绝对打印位置
     *
     * @return
     * @throws IOException
     */
    public byte[] setLocation(int offset) throws IOException {
        byte[] bs = new byte[4];
        bs[0] = 0x1B;
        bs[1] = 0x24;
        bs[2] = (byte) (offset % 256);
        bs[3] = (byte) (offset / 256);
        return bs;
    }

    public byte[] getGbk(String stText) throws IOException {
        byte[] returnText = stText.getBytes("GBK"); // 必须放在try内才可以
        return returnText;
    }

    private int getStringPixLength(String str) {
        int pixLength = 0;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
//            if (Pinyin.isChinese(c)) {
//                pixLength += 24;
//            } else {
//                pixLength += 12;
//            }
        }
        return pixLength;
    }

    public int getOffset(String str) {
        return WIDTH_PIXEL - getStringPixLength(str);
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    public void printText(String text) throws IOException {
        mWriter.write(text);
        mWriter.flush();
    }

    /**
     * 对齐0:左对齐，1：居中，2：右对齐
     */
    public void printAlignment(int alignment) throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x61);
        mWriter.write(alignment);
    }

    public void printLargeText(String text) throws IOException {

        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(48);

        mWriter.write(text);

        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(0);

        mWriter.flush();
    }

    public void printTwoColumn(String title, String content) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[100];
        byte[] tmp;

        tmp = getGbk(title);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(content));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(content);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }

    public void printThreeColumn(String left, String middle, String right) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[200];
        byte[] tmp = new byte[0];

        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(left);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        int pixLength = getStringPixLength(left) % WIDTH_PIXEL;
        if (pixLength > WIDTH_PIXEL / 2 || pixLength == 0) {
            middle = "\n\t\t" + middle;
        }

        tmp = setLocation(192);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(middle);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(right));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(right);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }


    public void printOrderLine(String productid, String productname, String saleqty, String saleamount, String saleprice) throws IOException {
        int iNum = 0;
//        每行37个字符；
        int perline = 32;

        byte[] byteBuffer = new byte[200];
        byte[] tmp = new byte[0];
//        第一行产品名称
        byte[] proname = new byte[32];
//        第二行产品编码、价格、数量、小计

        byte[] proid = new byte[6];
        byte[] qty = new byte[6];
        byte[] price = new byte[10];
        byte[] amount = new byte[10];

        tmp = getGbk(productname);
        for (int i = 0; i < proname.length; i++) {
            if (i < tmp.length) {
                proname[i] = tmp[i];
            } else {
                break;
            }
        }

        proname = getPrintContent(proname);
        System.arraycopy(proname, 0, byteBuffer, iNum, proname.length);
        iNum += proname.length;
        tmp = getGbk("\n");
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;
        print(byteBuffer);


        byteBuffer = new byte[200];
        iNum = 0;
        tmp = getGbk(productid);
        for (int i = 0; i < productid.length() - 7; i++) {
            proid[i] = tmp[i + 7];
        }
        System.arraycopy(proid, 0, byteBuffer, iNum, proid.length);
        iNum += proid.length;


        tmp = getGbk(saleqty);
        for (int i = 0; i < qty.length; i++) {
            if (i < tmp.length) {
                qty[qty.length - tmp.length + i] = tmp[i];
            } else {
                break;
            }
        }
        qty = getPrintContent(qty);
        System.arraycopy(qty, 0, byteBuffer, iNum, qty.length);
        iNum += qty.length;
//        tmp = getGbk(" ");
//        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
//        iNum += tmp.length;

        tmp = getGbk(saleprice);
        for (int i = 0; i < price.length; i++) {
            if (i < tmp.length) {
                price[price.length - tmp.length + i] = tmp[i];
            } else {
                break;
            }
        }
        price = getPrintContent(price);
        System.arraycopy(price, 0, byteBuffer, iNum, price.length);
        iNum += price.length;

        tmp = getGbk(saleamount);
        for (int i = 0; i < amount.length; i++) {
            if (i < tmp.length) {
                amount[amount.length - tmp.length + i] = tmp[i];
            } else {
                break;
            }
        }
        amount = getPrintContent(amount);
        System.arraycopy(amount, 0, byteBuffer, iNum, amount.length);
        iNum += amount.length;

        print(byteBuffer);
    }

    public void printPayLine(String paycode, String paymentmoney) throws IOException {
        int iNum = 0;
//        每行37个字符；
        int perline = 32;
        byte[] byteBuffer = new byte[200];
        byte[] tmp = new byte[0];
        byte[] payname = new byte[22];
        byte[] paymoney = new byte[10];
        String tmp_payname;


        tmp = getGbk(paycode);
        tmp_payname = PosPayMent.getPayMentName((char) tmp[0]);
        //PAYMENT_CASH_CODE


        tmp = getGbk(tmp_payname);
        for (int i = 0; i < payname.length; i++) {
            if (i < tmp.length) {
                payname[i] = tmp[i];
            } else {
                break;
            }
        }
        payname = getPrintContent(payname);
        System.arraycopy(payname, 0, byteBuffer, iNum, payname.length);
        iNum += payname.length;


        tmp = getGbk(paymentmoney);
        for (int i = 0; i < paymoney.length; i++) {
            if (i < tmp.length) {
                paymoney[i] = tmp[i];
            } else {
                break;
            }
        }
        paymoney = getPrintContent(paymoney);
        System.arraycopy(paymoney, 0, byteBuffer, iNum, paymoney.length);
        iNum += paymoney.length;
        tmp = getGbk("\n");
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;


        print(byteBuffer);
    }

    public void printOrderTotal(String subtotal) throws IOException {
        int iNum = 0;
//        每行37个字符；
        int perline = 32;
        byte[] byteBuffer = new byte[200];
        byte[] tmp = new byte[0];
        byte[] subtotalname = new byte[22];
        byte[] subtotalmoney = new byte[10];
        String tmp_payname;
        tmp_payname = "合计：";
        tmp = getGbk(tmp_payname);
        for (int i = 0; i < subtotalname.length; i++) {
            if (i < tmp.length) {
                subtotalname[i] = tmp[i];
            } else {
                break;
            }
        }
        subtotalname = getPrintContent(subtotalname);
        System.arraycopy(subtotalname, 0, byteBuffer, iNum, subtotalname.length);
        iNum += subtotalname.length;


        tmp = getGbk(subtotal);
        for (int i = 0; i < subtotalmoney.length; i++) {
            if (i < tmp.length) {
                subtotalmoney[subtotalmoney.length - tmp.length + i] = tmp[i];
            } else {
                break;
            }
        }
        subtotalmoney = getPrintContent(subtotalmoney);
        System.arraycopy(subtotalmoney, 0, byteBuffer, iNum, subtotalmoney.length);
        iNum += subtotalmoney.length;
        tmp = getGbk("\n");
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;


        print(byteBuffer);
    }


    public void printMyTwoColumn(String title, String content) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[100];
        byte[] tmp;

        tmp = getGbk(title);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

//        tmp = setLocation(getOffset(content));
        tmp = getGbk(content);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

//        tmp = getGbk(content);
//        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }


    public void printDashLine() throws IOException {
        printText("--------------------------------");
    }

    public void printBitmap(Bitmap bmp) throws IOException {
        bmp = compressPic(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        printRawBytes(bmpByteArray);
    }


    public byte[] getPrintContent(byte[] input) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == 0) {
                input[i] = 32;
//                input[i] = 48;
            }
        }
        return input;
    }

    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    private byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;

        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    private byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
        return gray;
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    private Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = IMAGE_SIZE;
        int newHeight = IMAGE_SIZE;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    public static void printTest(BluetoothSocket bluetoothSocket, Bitmap bitmap) {

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            // 店铺名 居中 放大
            pUtil.printAlignment(1);
            pUtil.printLargeText("乐之店");
            pUtil.printLine();
            pUtil.printAlignment(0);
            pUtil.printLine();

            pUtil.printTwoColumn("时间:", "2017-05-09 15:50:41");
            pUtil.printLine();

            pUtil.printTwoColumn("订单号:", System.currentTimeMillis() + "");
            pUtil.printLine();

            pUtil.printTwoColumn("付款人:", "VitaminChen");
            pUtil.printLine();

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            //打印商品列表
            pUtil.printText("商品");
            pUtil.printSpace(2);
            pUtil.printText("数量");
            pUtil.printSpace(2);
            pUtil.printText("    单价");
            pUtil.printLine();

            pUtil.printThreeColumn("iphone6", "1", "4999.00");
            pUtil.printThreeColumn("测试一个超长名字的产品看看打印出来会怎么样哈哈哈哈哈哈", "1", "4999.00");

            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("订单金额:", "9998.00");
            pUtil.printLine();

            pUtil.printTwoColumn("实收金额:", "10000.00");
            pUtil.printLine();

            pUtil.printTwoColumn("找零:", "2.00");
            pUtil.printLine();

            pUtil.printDashLine();

            pUtil.printBitmap(bitmap);

            pUtil.printLine(4);

        } catch (IOException e) {

        }
    }


    public static void print_weight_56mm(BluetoothSocket bluetoothSocket, Bitmap bitmap, List<SaleDaily> saleDailyList, List<SalePayMode> salePayModeList, PosTabInfo posTabInfo, int printertimes, boolean is1st) {

        try {
            Double sub_total_order = 0.00;

            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            // 店铺名 居中 放大
            pUtil.printAlignment(1);
            pUtil.printLargeText(posTabInfo.getBranchName());
            pUtil.printLine();
            if (!is1st) {
                pUtil.printLargeText("--重打印--");
                pUtil.printLine();
            }
            pUtil.printAlignment(0);
            pUtil.printLine();
            if (saleDailyList.size() != 0) {
                CharSequence sysTimeStr = DateFormat
                        .format(" yyyy-MM-dd HH:mm:ss", saleDailyList.get(0).getSaleDate());
                pUtil.printMyTwoColumn("交易时间:", (String) sysTimeStr);
                pUtil.printLine();
                pUtil.printMyTwoColumn("订单号:", saleDailyList.get(0).getSaleId() + "");
                pUtil.printLine();
                pUtil.printText("营业员:");
                pUtil.printText(saleDailyList.get(0).getSaleMan());
                pUtil.printSpace(10);
                pUtil.printText("第" + printertimes + "联");
                pUtil.printLine();
            }


            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printText("商品");
            pUtil.printSpace(4);
            pUtil.printText("数量");
            pUtil.printSpace(6);
            pUtil.printText("单价");
            pUtil.printSpace(6);
            pUtil.printText("金额");
            pUtil.printLine();
            pUtil.printDashLine();
//
//
            for (int i = 0; i < saleDailyList.size(); i++) {

                List<Product> tmp_products = QueryProductByCode(saleDailyList.get(i).getProId());
                if (tmp_products.size() != 0) {
                    sub_total_order += saleDailyList.get(i).getSaleAmt();
                    pUtil.printOrderLine(saleDailyList.get(i).getProId(),
                            tmp_products.get(0).getProSName(),
                            saleDailyList.get(i).getSaleQty().toString(),
                            saleDailyList.get(i).getSaleAmt().toString(),
                            saleDailyList.get(i).getCurPrice().toString());
                    pUtil.printLine();
                }
            }
            pUtil.printDashLine();
            pUtil.printOrderTotal(sub_total_order.toString());
            pUtil.printDashLine();
            pUtil.printText("实际付款:");
            pUtil.printLine();
            for (int i = 0; i < salePayModeList.size(); i++) {
                pUtil.printPayLine(salePayModeList.get(i).getPayModeId().toString(), salePayModeList.get(0).getPayMoney().toString());
                pUtil.printLine();

            }

            pUtil.printLine(1);
            if (bitmap != null) {
                pUtil.printBitmap(bitmap);
                pUtil.printText("注：隔日扫码，可以开具电子发票！" );
            }
            pUtil.printLine(2);
            pUtil.printText("打印时间:" + DeviceUtils.GetCurrentTime());
            pUtil.printLine(1);
            pUtil.printText("Android");
            pUtil.printLine(2);

        } catch (IOException e) {

        }
    }


    private void print_title() {
    }


}