package personal.wl.jspos.method;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import personal.wl.jspos.R;
import personal.wl.jspos.adapter.PrintOrderStatusChange;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static android.support.constraint.Constraints.TAG;

public class PrinterConnectAsyc extends AsyncTask {
    private String address;
    private Context context;


    private BluetoothDevice mmDevice;
    private BluetoothSocket socket;
    private Boolean nottest;
    private List<SaleDaily> saleDailyList;
    private List<SalePayMode> salePayModeList;
    private PosTabInfo posTabInfo;

    public PrinterConnectAsyc(Context context, BluetoothDevice mmDevice, Boolean NotTest, List<SaleDaily> saleDailyList, List<SalePayMode> salePayModeList) {
        this.context = context;
        this.mmDevice = mmDevice;
        this.saleDailyList = saleDailyList;
        this.nottest = NotTest;
        this.salePayModeList = salePayModeList;
        this.posTabInfo = new PosTabInfo(context);
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
            PrintUtil.print_weight_56mm(socket, null, this.saleDailyList, this.salePayModeList,posTabInfo);

        }
    }

}
