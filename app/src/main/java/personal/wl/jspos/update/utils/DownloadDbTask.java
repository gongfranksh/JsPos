package personal.wl.jspos.update.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import personal.wl.jspos.Config.Beans.SystemFtpInfo;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.Config.Beans.SystemSettingConstant.SETTING_LOCAL_DB_NAME;

public class DownloadDbTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = "DownloadDbTask";
    private Context context;
    private final String DB_NAME = "jsPos.db";
    private String REMOTE_DB_FILENAME;
    private CommonProgressDialog pbar;
    private PowerManager.WakeLock mWakeLock;
    private FTPClient mFtpClient;
    private PosTabInfo posTabInfo;
    private HashMap device = new HashMap<String, String>();
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

    public DownloadDbTask(CommonProgressDialog pbar, Context context) {
        this.context = context;
        this.pbar = pbar;
        this.posTabInfo = new PosTabInfo(context);
        systemFtpInfo = new SystemFtpInfo(context);
        REMOTE_DB_FILENAME = systemFtpInfo.getFtp_path() + posTabInfo.getBranchCode().trim() + SETTING_LOCAL_DB_NAME;
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
            Boolean changeDirResult = mFtpClient.changeWorkingDirectory(systemFtpInfo.getFtp_path());

            if (!changeDirResult) throw new Exception("remote path do not exist");
            FTPFile[] getremotefiles = mFtpClient.listFiles(REMOTE_DB_FILENAME);

            if (getremotefiles.length == 0) {
                throw new Exception("remote db file do not exist");
            }

            long fileLength = getremotefiles[0].getSize();
            long step = fileLength / 100;
            long process = 0;
            long currentSize = 0;

            mFtpClient.enterLocalPassiveMode();
            OutputStream out = new FileOutputStream(locadbfile);
            InputStream inputfile = mFtpClient.retrieveFileStream(REMOTE_DB_FILENAME);

            byte[] data = new byte[1024];
            int length = 0;
            int total = 0;
            while ((length = inputfile.read(data)) != -1) {
                if (isCancelled()) {
                    inputfile.close();
                    return null;
                }
                out.write(data, 0, length);
                currentSize = currentSize + length;

                process = currentSize / step;

                Log.i(TAG, "total -->" + total
                        + "   filelength-->" + fileLength
                        + "---processbar--->" + (int) process);

                if (process % 10 == 0) {
                    publishProgress((int) (process));
                }
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
