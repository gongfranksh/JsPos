package personal.wl.jspos;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import personal.wl.jspos.method.BlueToothBroadcastReveiver;
import personal.wl.jspos.method.BluetoothBean;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.method.PrinterConnectThread;

public class BlueToothDeviceActivity extends AppCompatActivity {
    private Button bt_printersetting;
    private Button bt_printertesting;
    private Context context;
    private BlueToothBroadcastReveiver receiver;//蓝牙搜索的广播
    private String TAG = "BlueToothDeviceActivity";
    private BluetoothDevice printerdevice;
    private ProgressDialog pdSearch;//搜索时
    private List<BluetoothBean> mBluetoothList;//搜索的蓝牙设备
    private List<BluetoothBean> mBluetoothList2;//去重的蓝牙设备
    private BluetoothAdapter adapter;//蓝牙适配器
    private TextView tv_display;
    private PosTabInfo posTabInfo;

    private PrinterConnectThread mThread;//连接的蓝牙线程

    private BluetoothSocket socket;//蓝牙socket

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = BlueToothDeviceActivity.this;
        posTabInfo = new PosTabInfo(context);
        adapter = BluetoothAdapter.getDefaultAdapter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_device);
        bt_printersetting = findViewById(R.id.setting_bluetoothprinter);
        bt_printertesting = findViewById(R.id.Tesing_bluetoothprinter);
        tv_display = findViewById(R.id.print_setting_display);
        bt_printersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBlueToothDevice();
            }
        });
        bt_printertesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findprinter();
            }
        });
        displayprinter();
    }

    private void displayprinter() {
        tv_display.setText("当前打印机名称:" + posTabInfo.getBlueToothPrinterName()
                + "\n" + "当前打印机地址:" + posTabInfo.getBlueToothPrinterAddress());
    }

    public void searchBlueToothDevice() {
        pdSearch = ProgressDialog.show(this.context, "", "连接中", true, true);
        pdSearch.setCanceledOnTouchOutside(false);
        pdSearch.show();
        mBluetoothList = new ArrayList<>();
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        // 如果蓝牙已经关闭就打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
            return;
        }
//        // 获取已配对的蓝牙设备
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
//        int count = 0;
//        for (BluetoothDevice pairedDevice : devices) {
//            if (pairedDevice.getName() == null) {
//                return;
//            } else if (pairedDevice.getName().startsWith("MPT-II")) {
//                count++;
//                String deviceAddress = pairedDevice.getAddress();
//                printerdevice = adapter.getRemoteDevice(deviceAddress);
//                Log.i(TAG, "找到打印机 " + pairedDevice.getName());
//                pdSearch.dismiss();
//                connect(printerdevice.getAddress(), printerdevice);
//                break;
//
//            }
//
//        }

        // 遍历
//        int count = 0;
//        for (BluetoothDevice pairedDevice : devices) {
//            Log.i(TAG, "searchBlueToothDevice(MainActivity.java:137)--->> " + pairedDevice.getName());
//            if (pairedDevice.getName() == null) {
//                return;
//            } else if (pairedDevice.getName().startsWith("Printer_29D0")) {
//                count++;
//                deviceAddress = pairedDevice.getAddress();
//                mBluetoothDevice = adapter.getRemoteDevice(deviceAddress);
//                connect(deviceAddress, mBluetoothDevice);
//                break;
//            }
//        }


        //----自动搜索蓝牙
        if (adapter.isEnabled()) {
            //开始搜索
            adapter.startDiscovery();

            // 设置广播信息过滤
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            // 注册广播接收器，接收并处理搜索结果
            receiver = new BlueToothBroadcastReveiver(this.context, pdSearch);
            registerReceiver(receiver, intentFilter);
        }


    }

    private void findprinter() {
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        int count = 0;
        for (BluetoothDevice pairedDevice : devices) {
            if (pairedDevice.getName() == null) {
                return;
            } else if (pairedDevice.getName().startsWith(posTabInfo.getBlueToothPrinterName())) {
                count++;
                String deviceAddress = pairedDevice.getAddress();
                printerdevice = adapter.getRemoteDevice(deviceAddress);
                connect(printerdevice.getAddress(), printerdevice);
                break;

            }
        }

    }

    public synchronized void connect(String macAddress, BluetoothDevice device) {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
        if (socket != null) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;
        }
        mThread = new PrinterConnectThread(macAddress, device, context);
        mThread.start();
    }
}
