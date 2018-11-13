package personal.wl.jspos.method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.List;
import java.util.Set;

import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

public class PosPrinter {
    private PosTabInfo posTabInfo;
    private Context context;
    private Boolean isfirsttimes;
    private PrinterConnectThread mThread;//连接的蓝牙线程

    public PosPrinter(Context context,Boolean isFirstTimes) {
        PosTabInfo posTabInfo = new PosTabInfo(context);
        this.context = context;
        this.posTabInfo = posTabInfo;
        this.isfirsttimes=isFirstTimes;
    }


    public BluetoothDevice getPosPrinter() {
        BluetoothDevice printerdevice;
        Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        int count = 0;
        for (BluetoothDevice pairedDevice : devices) {
            if (pairedDevice.getName() == null) {
                return null;
            } else if (pairedDevice.getName().startsWith(posTabInfo.getBlueToothPrinterName())) {
                count++;
                String deviceAddress = pairedDevice.getAddress();
                printerdevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                return printerdevice;
            }
        }
        return null;
    }

    public synchronized void connect(BluetoothDevice device, List<SaleDaily> saleDailyList, List<SalePayMode> salePayModeList,Boolean is1st) {


        PrinterConnectAsyc printerConnectAsyc = new PrinterConnectAsyc(context,device,true,saleDailyList,salePayModeList,is1st);
        printerConnectAsyc.execute();
//        if (mThread != null) {
//            mThread.interrupt();
//            mThread = null;
//        }
//        mThread = new PrinterConnectThread(device.getAddress(), device, context, true, saleDailyList, salePayModeList);
//        mThread.start();
    }


}


