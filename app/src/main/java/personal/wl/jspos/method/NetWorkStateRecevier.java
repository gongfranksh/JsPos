package personal.wl.jspos.method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkStateRecevier extends BroadcastReceiver {
    APPNetwork appNetwork = new APPNetwork();

    @Override
    public void onReceive(Context context, Intent intent) {


        System.out.println("网络状态发生变化");
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }
//API大于23时使用下面的方式进行网络监听
        } else {

            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected()) {

                    appNetwork.setConnected(true);
                    //通知观察者网络状态已改变
                    NetworkChange.getInstance().notifyDataChange(appNetwork);
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                        appNetwork.setWifi(true);
                        //通知观察者网络状态已改变
                        NetworkChange.getInstance().notifyDataChange(appNetwork);
                        Toast.makeText(context, "当前wifi连接可用", Toast.LENGTH_SHORT).show();
                    } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                        appNetwork.setMobile(true);
                        //通知观察者网络状态已改变
                        NetworkChange.getInstance().notifyDataChange(appNetwork);
                        Toast.makeText(context, "当前移动网络连接可用", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    appNetwork.setConnected(false);
                    //通知观察者网络状态已改变
                    NetworkChange.getInstance().notifyDataChange(appNetwork);
                    Toast.makeText(context, "当前没有网络连接，请确保你已经打开网络", Toast.LENGTH_SHORT).show();
                }
            } else {
                appNetwork.setWifi(false);
                appNetwork.setMobile(false);
                appNetwork.setConnected(false);
                //通知观察者网络状态已改变
                NetworkChange.getInstance().notifyDataChange(appNetwork);
                Toast.makeText(context, "当前没有网络连接，请确保你已经打开网络", Toast.LENGTH_SHORT).show();
            }


//            //获取所有网络连接的信息
//            Network[] networks = connMgr.getAllNetworks();
//            //用于存放网络连接信息
//            StringBuilder sb = new StringBuilder();
//            //通过循环将网络信息逐个取出来
//            for (int i = 0; i < networks.length; i++) {
//                //获取ConnectivityManager对象对应的NetworkInfo对象
//                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
//                sb.append(networkInfo.getTypeName() + " 使用 " + networkInfo.getExtraInfo() + " 状态为:" + networkInfo.isConnected());
//            }
//
//            if (networks.length > 0) {
//                Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "离线offline状态", Toast.LENGTH_SHORT).show();
//            }

        }
    }
}

