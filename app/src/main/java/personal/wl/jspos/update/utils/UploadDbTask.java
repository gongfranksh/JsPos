package personal.wl.jspos.update.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.Config.Beans.SystemFtpInfo;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.method.DeviceUtils.GetTimeStamp;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_LOCAL_DB_NAME;

public class UploadDbTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private String REMOTE_DB_FILENAME;
    private CommonProgressDialog pbar;
    private PowerManager.WakeLock mWakeLock;
    private FTPClient mFtpClient;
    private PosTabInfo posTabInfo;

    private SystemFtpInfo systemFtpInfo;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pbar.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pbar.setIndeterminate(false);
        pbar.setMax(100);
        pbar.setProgress(values[0]);
    }

    public UploadDbTask(CommonProgressDialog pbar, Context context) {
        this.context = context;
        this.pbar = pbar;
        this.posTabInfo = new PosTabInfo(context);
        systemFtpInfo = new SystemFtpInfo(context);
        REMOTE_DB_FILENAME = systemFtpInfo.getFtp_path() + posTabInfo.getPosMachine().trim()
                + GetTimeStamp() + "-" + SETTING_LOCAL_DB_NAME;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream input = null;
        OutputStream output = null;
        try {
            mFtpClient = FTPToolkit
                    .makeFtpConnection(systemFtpInfo.getFtp_ip_address(), 21,
                            systemFtpInfo.getFtp_iaccount(), systemFtpInfo.getFtp_ipassword());

            File locadbfile = new File(context.getDatabasePath(SETTING_LOCAL_DB_NAME).getPath());

            if (!locadbfile.exists())
                throw new Exception("local database file not exist!==>" + locadbfile.getPath());
            FileInputStream fis = new FileInputStream(locadbfile);
            long fileLength = fis.available();
            Boolean changeDirResult = mFtpClient.changeWorkingDirectory(systemFtpInfo.getFtp_path());
            if (!changeDirResult) throw new Exception("remote path do not exist");
            FTPFile[] getremotefiles = mFtpClient.listFiles(systemFtpInfo.getFtp_path());

            mFtpClient.enterLocalPassiveMode();
            FileInputStream inputfile = new FileInputStream(locadbfile);
            OutputStream out = mFtpClient.storeFileStream(REMOTE_DB_FILENAME);

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
            mFtpClient.isConnected();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pbar.show();
    }
}
