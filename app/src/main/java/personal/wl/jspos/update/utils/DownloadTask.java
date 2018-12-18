package personal.wl.jspos.update.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.content.FileProvider;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import personal.wl.jspos.BuildConfig;
import personal.wl.jspos.method.SystemFtpInfo;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.method.SystemSettingConstant.SETTING_APP_APK_FIlE;

public class DownloadTask extends AsyncTask<String, Integer, String> {

    public DownloadTask(CommonProgressDialog pbar, Context context) {
        this.context = context;
        this.pbar = pbar;
        systemFtpInfo = new SystemFtpInfo(context);
    }

    private Context context;
    private CommonProgressDialog pbar;
    private PowerManager.WakeLock mWakeLock;
    private FTPClient mFtpClient;
    private SystemFtpInfo systemFtpInfo;

    @Override
    protected String doInBackground(String... strings) {
        InputStream input = null;
        OutputStream output = null;
        try {
            mFtpClient = FTPToolkit
                    .makeFtpConnection(systemFtpInfo.getFtp_ip_address(), 21,
                            systemFtpInfo.getFtp_iaccount(), systemFtpInfo.getFtp_ipassword());

            File getversionfilejson = new File(context.getFilesDir().getPath() + "/"+systemFtpInfo.getFtp_joson_file());
            FTPToolkit.download(mFtpClient, systemFtpInfo.getFtp_path()+systemFtpInfo.getFtp_joson_file(), getversionfilejson.getPath());

            long fileLength = FTPToolkit.getFileLength(mFtpClient, systemFtpInfo.getFtp_path()+SETTING_APP_APK_FIlE);

            File file = new File(context.getFilesDir().getPath() + "/"+SETTING_APP_APK_FIlE);
            OutputStream out = new FileOutputStream(file);
            InputStream inputfile = mFtpClient.retrieveFileStream(systemFtpInfo.getFtp_path()+SETTING_APP_APK_FIlE);
            byte[] data = new byte[1024];
            int total = 0;
            int count;
            while ((count = inputfile.read(data)) != -1) {

                if (isCancelled()) {
                    inputfile.close();
                    return null;
                }

                total += count;
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                out.write(data, 0, count);

            }
            out.flush();
            out.close();
            inputfile.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mFtpClient.isConnected();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pbar.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pbar.dismiss();
        download_installApk();
//            finish();
    }

    @Override
    protected void onProgressUpdate(Integer ... values) {
        super.onProgressUpdate(values);
        pbar.setIndeterminate(false);
        pbar.setMax(100);
        pbar.setProgress(values[0]);
    }


    private void download_installApk() {
        String ss = BuildConfig.APPLICATION_ID;
        File getapkfile = new File(context.getFilesDir().getPath() + "/" + SETTING_APP_APK_FIlE);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri contentUri = FileProvider.getUriForFile(this.context, context.getApplicationContext().getPackageName() + ".provider", getapkfile);
        install.setDataAndType(contentUri, "application/vnd.android.package-archive");
        this.context.startActivity(install);


    }
}