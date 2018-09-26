package personal.wl.jspos.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;

import personal.wl.jspos.db.DBC2Jspot;
import personal.wl.jspos.db.Utils;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static personal.wl.jspos.method.PosHandleDB.CheckDeviceByLocal;
import static personal.wl.jspos.method.PosHandleDB.InsertDeviceByLocal;
import static personal.wl.jspos.method.PosHandleDB.getSalesDailyUpload;
import static personal.wl.jspos.method.PosHandleDB.getSalesPaymentForUpload;

public class SyncJsSaleData extends AsyncTask<HashMap, Integer, Integer>
        implements DialogInterface.OnCancelListener {
    private ProgressDialog pd = null;
    private Context context = null;
    private HashMap device = new HashMap<String, String>();


    private static final String TAG = "SyncJsSaleData";

    public SyncJsSaleData(Context context, HashMap device) {
        this.context = context;
        this.device = device;
    }


    @Override
    protected Integer doInBackground(HashMap... hashMaps) {

        int num = 5;
        int i = 0;

        i = i + 1;
        DBC2Jspot js = new DBC2Jspot();
        //Check Device id
        if (js.CheckDeviceByServer(device)) {
            List devicelist = js.getCheckDeviceThisFromServer(device);
            if (!CheckDeviceByLocal(device)) {
                InsertDeviceByLocal(devicelist);
            } else {
                //已经插入本地数据库;
            }
            this.device.put("sourceid", (Integer) ((HashMap) devicelist.get(0)).get("sourceid"));

        } else {
            //授权失败推出不下载数据
            return 10;
        }

        //上传付款数据
        i = i + 1;
        long maxnumber = js.getLastUploadTranscations(device);
        device.put("max", maxnumber);
        List<SalePayMode> needupdatesalepaymode = getSalesPaymentForUpload(device);
        List<SaleDaily> needuploadsalesdailylist = getSalesDailyUpload(needupdatesalepaymode);


        Utils.sleepForSecs(2);
        publishProgress(i);

        i = i + 1;
        js.LastUploadTranscations(needupdatesalepaymode);
        Utils.sleepForSecs(2);
        publishProgress(i);

        i = i + 1;
        js.InSertMobileSaleDaily(needuploadsalesdailylist);
        Utils.sleepForSecs(2);
        publishProgress(i);

        i = i + 1;
        js.UploadMobileDeviceLogId(needupdatesalepaymode);
        Utils.sleepForSecs(2);
        publishProgress(i);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        Utils.logThreadSignature(tag);
        pd = new ProgressDialog(context);
        pd.setTitle("上传销售资料数据");
        pd.setMessage("In progressing");
        pd.setCancelable(true);
        pd.setOnCancelListener(this);
        pd.setIndeterminate(false);//
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(5);
        pd.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        Integer i = values[0];
//        report.reportBack(tag, "Progress: i = " + i);
        switch (i) {
            case 0:
                pd.setMessage("开始同步");
                break;


            case 1:
                pd.setMessage("检查设备是否授权");
                break;

            case 2:
                pd.setMessage("上传PayMode....");
                break;

            case 3:
                pd.setMessage("上传SalesDaily....");
                break;

            case 4:
                pd.setMessage("更新设备....");
                break;

            default:
                break;
        }

        pd.setProgress(i + 1);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
