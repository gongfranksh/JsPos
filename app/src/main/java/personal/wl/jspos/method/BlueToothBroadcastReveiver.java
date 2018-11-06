package personal.wl.jspos.method;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import personal.wl.jspos.R;


public class BlueToothBroadcastReveiver extends BroadcastReceiver {

    private BluetoothAdapter adapter;//蓝牙适配器
    private List<BluetoothBean> mBluetoothList;//搜索的蓝牙设备
    private List<BluetoothBean> mBluetoothList2;//去重的蓝牙设备
    private ProgressDialog pdSearch;//搜索时
    private ProgressDialog pdConnect;//连接时
    private String TAG = "BlueToothBroadcastReveiver";
    private PopupWindow pw;
    private Context context;
    private DeviceBlueToothsAdapter myBluetoothAdapter;


    public BlueToothBroadcastReveiver(Context context, ProgressDialog pdSearch) {
        this.context = context;
        this.pdSearch = pdSearch;
        adapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothList = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        this.context = context;

        //找到设备,有可能重复搜索同一设备,可在结束后做去重操作
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device == null) {
                return;
            }
            if (device.getName() == null) {
                return;
            }

            BluetoothBean bluetoothBean = new BluetoothBean();
            bluetoothBean.mBluetoothName = device.getName();
            bluetoothBean.mBluetoothAddress = device.getAddress();
            bluetoothBean.mBluetoothDevice = adapter.getRemoteDevice(bluetoothBean.mBluetoothAddress);
            mBluetoothList.add(bluetoothBean);

            Log.i(TAG, "onReceive(MainActivity.java:184)--->> " + device.getName());
            Log.i(TAG, "onReceive(MainActivity.java:185)--->> " + mBluetoothList.size());

//                if (device.getName().startsWith("Printer_29D0")) {
//                    //取消搜索
//                    adapter.cancelDiscovery();
//                    deviceAddress = device.getAddress();
//                    mBluetoothDevice = adapter.getRemoteDevice(deviceAddress);
//                    connectState = device.getBondState();
//                    switch (connectState) {
//                        // 未配对
//                        case BluetoothDevice.BOND_NONE:
//                            // 配对
//                            try {
//                                Method createBondMethod = mBluetoothDevice.getClass().getMethod("createBond");
//                                createBondMethod.invoke(mBluetoothDevice);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        // 已配对
//                        case BluetoothDevice.BOND_BONDED:
//                            if (device.getName().startsWith("Printer_29D0")) {
//                                connect(deviceAddress, mBluetoothDevice);
//                            }
//                            break;
//                    }
//                }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.i(TAG, "onReceive(MainActivity.java:213)--->> " + "搜索完成");
//            pdSearch.dismiss();
            if (0 == mBluetoothList.size())
                Toast.makeText(context, "搜索不到蓝牙设备", Toast.LENGTH_SHORT).show();
            else {
                //去重HashSet add会返回一个boolean值，插入的值已经存在就会返回false 所以true就是不重复的
                HashSet<BluetoothBean> set = new HashSet<>();
                mBluetoothList2 = new ArrayList<>();
                for (BluetoothBean bean : mBluetoothList) {
                    boolean add = set.add(bean);
                    if (add) {
                        mBluetoothList2.add(bean);
                    }
                }
                showBluetoothPop(mBluetoothList2);
            }
//            unregisterReceiver(receiver);
        }
    }

    private void showBluetoothPop(final List<BluetoothBean> bluetoothList) {
        pdSearch.dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bluetooth, null);
        ListView mListView = view.findViewById(R.id.lv_bluetooth);
        DeviceBlueToothsAdapter myBluetoothAdapter = new DeviceBlueToothsAdapter(bluetoothList, this.context);
        mListView.setAdapter(myBluetoothAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (0 != mBluetoothList2.size()) {
                    closePopupWindow();
                    pdConnect = ProgressDialog.show(context, "", "保存打印机设置", true, true);
                    pdConnect.setCanceledOnTouchOutside(false);
                    pdConnect.show();
                    registerprinter(bluetoothList.get(position));
                    pdConnect.dismiss();
//                    connect(bluetoothList.get(position).mBluetoothAddress, bluetoothList.get(position).mBluetoothDevice);
                }
            }
        });
        myBluetoothAdapter.notifyDataSetChanged();
        pw = new PopupWindow(view, (int) (getScreenWidth() * 0.5), -2);
        closePopupWindow();
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
//        WindowManager.LayoutParams lp =context.getResources().
//        lp.alpha = 0.7f;
//        getWindow().setAttributes(lp);
//        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
//        pw.setAnimationStyle(R.style.PopAnim);
        //显示
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void closePopupWindow() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
            pw = null;
        }
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    private void registerprinter(BluetoothBean bluetoothBean) {
        PosTabInfo posTabInfo = new PosTabInfo(context);
        posTabInfo.setBlueToothPrinterName(bluetoothBean.mBluetoothName);
        posTabInfo.setBlueToothPrinterAddress(bluetoothBean.mBluetoothAddress);
        Toast.makeText(context,"注册打印机成功",Toast.LENGTH_LONG).show();
    }

}
