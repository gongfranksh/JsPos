package personal.wl.jspos.method;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import personal.wl.jspos.Config.Beans.SystemHttpInfo;
import personal.wl.jspos.R;
import personal.wl.jspos.adapter.PrintOrderStatusChange;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static android.content.ContentValues.TAG;
import static personal.wl.jspos.method.QRCodeUtil.createQRCodeBitmap;
import static personal.wl.jspos.method.QRCodeUtil.getQRcodeContent;

public class PrinterConnectAsyc extends AsyncTask {
    private String address;
    private Context context;


    private BluetoothDevice mmDevice;
    private BluetoothSocket socket;
    private Boolean nottest;
    private Boolean isReturn=false;
    private List<SaleDaily> saleDailyList;
    private List<SalePayMode> salePayModeList;
    private PosTabInfo posTabInfo;
    private Bitmap bitmapQRcode=null;
    boolean is1st;
    private SystemHttpInfo systemHttpInfo;

    public PrinterConnectAsyc(Context context, BluetoothDevice mmDevice, Boolean NotTest, List<SaleDaily> saleDailyList, List<SalePayMode> salePayModeList, Boolean is1st) {
        this.context = context;
        this.mmDevice = mmDevice;
        this.saleDailyList = saleDailyList;
        this.nottest = NotTest;
        this.salePayModeList = salePayModeList;
        this.posTabInfo = new PosTabInfo(context);
        this.systemHttpInfo = new SystemHttpInfo(context);
        this.is1st = is1st;
        try {
            if (socket == null) {
                socket = BluetoothUtil.connectDevice(mmDevice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrinterConnectAsyc(Context context, BluetoothDevice mmDevice, Boolean NotTest, List<SaleDaily> saleDailyList, List<SalePayMode> salePayModeList, Boolean is1st,Boolean isReturn) {
        this.context = context;
        this.mmDevice = mmDevice;
        this.saleDailyList = saleDailyList;
        this.nottest = NotTest;
        this.salePayModeList = salePayModeList;
        this.posTabInfo = new PosTabInfo(context);
        this.is1st = is1st;
        this.isReturn=isReturn;
        try {
            if (socket == null) {
                socket = BluetoothUtil.connectDevice(mmDevice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Log.i(TAG, "run(PrinterConnectAsyc.java:58)--->> " + "连接socket");
            if (socket.isConnected()) {
                Log.i(TAG, "run(PrinterConnectAsyc.java:62)--->> " + "已经连接过了");
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
                process_print();
            }
        } catch (Exception connectException) {
            Log.i(TAG, "run(PrinterConnectAsyc.java:67)--->> " + connectException.toString());
            try {
                if (socket != null) {

                    socket = null;
                }
            } catch (Exception closeException) {

            }

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        PrintOrderStatusChange.getInstance().notifyDataChange(true);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    private void process_print() {
        if (!nottest) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.buynow_barcode);
            PrintUtil.printTest(socket, bitmap);
        } else {

            List<SalePayMode> tt = this.salePayModeList;
            List<SaleDaily> mm = this.saleDailyList;
            Log.i(TAG, "run(PrinterConnectAsyc.java:89)---process_print---传递进入打印--收款明细>>" + this.salePayModeList.toString());
            Log.i(TAG, "run(PrinterConnectAsyc.java:90)---process_print---传递进入打印--交易明细>> " + this.saleDailyList.toString());
            int ii = posTabInfo.getPrinterTimes();
            for (int i = 0; i < posTabInfo.getPrinterTimes(); i++) {
                if (i == 0 & posTabInfo.getNeedQRcode() &(!isReturn)) {
                    String printqrstr = getQRcodeContent(this.salePayModeList, this.saleDailyList,systemHttpInfo.getQR_URL());
                    bitmapQRcode= createQRCodeBitmap(printqrstr, 200, 200,"UTF-8","H", "1", Color.BLACK, Color.WHITE);
                    PrintUtil.print_weight_56mm(socket, bitmapQRcode, this.saleDailyList, this.salePayModeList, posTabInfo, i + 1, is1st);
                } else {
                    PrintUtil.print_weight_56mm(socket, null, this.saleDailyList, this.salePayModeList, posTabInfo, i + 1, is1st);
                }
            }

        }
    }

}
