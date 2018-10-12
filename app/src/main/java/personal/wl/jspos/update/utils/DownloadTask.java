package personal.wl.jspos.update.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import personal.wl.jspos.update.view.CommonProgressDialog;

public class DownloadTask extends AsyncTask<String, Integer, String> {

    public DownloadTask(CommonProgressDialog pbar, Context context) {
        this.context = context;
        this.pbar = pbar;
    }

    private Context context;
    private CommonProgressDialog pbar;
    private PowerManager.WakeLock mWakeLock;
    private FTPClient mFtpClient;

    @Override
    protected String doInBackground(String... strings) {
        InputStream input = null;
        OutputStream output = null;
        try {
            mFtpClient = FTPToolkit
                    .makeFtpConnection(FtpInfo.IP, FtpInfo.PORT,
                            FtpInfo.LOGIN_ACCOUNT, FtpInfo.LOGIN_PASSWORD);

            File getversionfilejson = new File(context.getFilesDir().getPath() + "/"+FtpInfo.UPGRADE_JSON_FILE_NAME);
            FTPToolkit.download(mFtpClient, FtpInfo.UPGRADE_JSON_FILE_ADDRESS, getversionfilejson.getPath());

            long fileLength = FTPToolkit.getFileLength(mFtpClient, FtpInfo.remoteFile);

            File file = new File(context.getFilesDir().getPath() + "/"+FtpInfo.APK_FIlE);
            OutputStream out = new FileOutputStream(file);
            InputStream inputfile = mFtpClient.retrieveFileStream(FtpInfo.remoteFile);
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
//            finish();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pbar.setIndeterminate(false);
        pbar.setMax(100);
        pbar.setProgress(values[0]);
    }
}