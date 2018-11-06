package personal.wl.jspos.method;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import personal.wl.jspos.R;
import personal.wl.jspos.adapter.PrintOrderStatusChange;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static android.support.constraint.Constraints.TAG;

public class PrinterConnectThread extends Thread {
    private String address;
    private Context context;
    private BluetoothDevice mmDevice;
    private BluetoothSocket socket;
    private Boolean nottest;
    private List<SaleDaily> saleDailyList;
    private List<SalePayMode> salePayModeList;
    private Message msg;


    public PrinterConnectThread(String mac, BluetoothDevice mmDevice, Context context, Boolean NotTest, List<SaleDaily> orderlist, List<SalePayMode> orderpayment) {
        this.mmDevice = mmDevice;
        this.address = mac;
        this.context = context;
        this.nottest = NotTest;
        this.saleDailyList = orderlist;
        this.salePayModeList = orderpayment;
        try {
            if (socket == null) {
                socket = BluetoothUtil.connectDevice(mmDevice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void run() {
//        super.run();
//        do {
            try {
                Log.i(TAG, "run(MainActivity.java:367)--->> " + "连接socket");
//                msg = new Message();
//                msg.what = 555;
                if (socket.isConnected()) {
                    Log.i(TAG, "run(MainActivity.java:369)--->> " + "已经连接过了");
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
                    process_print();
                }
            } catch (Exception connectException) {
                Log.i(TAG, "run(MainActivity.java:402)--->> " + "连接失败");
                try {
                    if (socket != null) {

                        socket = null;
                    }
                } catch (Exception closeException) {

                }

            }
//        } while (true);
    }

    private void process_print() {
        if (!nottest) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.buynow_barcode);
            PrintUtil.printTest(socket, bitmap);
        } else {
            List<SalePayMode> tt = this.salePayModeList;
            List<SaleDaily> mm = this.saleDailyList;
         //   PrintUtil.print_weight_56mm(socket, null, this.saleDailyList, this.salePayModeList);
            PrintOrderStatusChange.getInstance().notifyDataChange(true);
//            mHandler.sendMessage(msg);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 555:
                    Toast.makeText(context, "printok", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };


}
