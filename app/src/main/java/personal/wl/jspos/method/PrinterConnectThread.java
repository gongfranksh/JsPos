package personal.wl.jspos.method;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import personal.wl.jspos.R;

import static android.support.constraint.Constraints.TAG;

public class PrinterConnectThread extends Thread {
    private String address;
    private Context context;
    private BluetoothDevice mmDevice;
    private BluetoothSocket socket;


    public PrinterConnectThread(String mac,BluetoothDevice mmDevice,  Context context) {
        this.mmDevice = mmDevice;
        this.address = mac;
        this.context = context;
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
        super.run();
        try {
            Log.i(TAG, "run(MainActivity.java:367)--->> " + "连接socket");
            if (socket.isConnected()) {
                Log.i(TAG, "run(MainActivity.java:369)--->> " + "已经连接过了");

//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.buynow_barcode);
                PrintUtil.printTest(socket, bitmap);
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
    }
}
