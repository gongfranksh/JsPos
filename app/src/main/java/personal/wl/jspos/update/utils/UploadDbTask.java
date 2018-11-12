package personal.wl.jspos.update.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.text.format.DateFormat;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.update.utils.FtpInfo.UPLOAD_FILE_ADDRESS;

public class UploadDbTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private final String DB_NAME = "jsPos.db";
    private String REMOTE_DB_FILENAME;
    private CommonProgressDialog pbar;
    private PowerManager.WakeLock mWakeLock;
    private FTPClient mFtpClient;
    private PosTabInfo posTabInfo;

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
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat
                .format(" yyyyMMddHHmmss", sysTime);
        REMOTE_DB_FILENAME = UPLOAD_FILE_ADDRESS + posTabInfo.getPosMachine().trim()
                +sysTimeStr.toString().trim() +"-"+ DB_NAME;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream input = null;
        OutputStream output = null;
        try {
            mFtpClient = FTPToolkit
                    .makeFtpConnection(FtpInfo.IP, FtpInfo.PORT,
                            FtpInfo.LOGIN_ACCOUNT, FtpInfo.LOGIN_PASSWORD);

            File locadbfile = new File(context.getDatabasePath(DB_NAME).getPath());

            if (!locadbfile.exists())
                throw new Exception("local database file not exist!==>" + locadbfile.getPath());
            FileInputStream fis = new FileInputStream(locadbfile);
            long fileLength = fis.available();
//            long fileLength = FTPToolkit.getFileLength(mFtpClient,locadbfile.getPath());

            Boolean changeDirResult = mFtpClient.changeWorkingDirectory(UPLOAD_FILE_ADDRESS);
            if (!changeDirResult) throw new Exception("remote path do not exist");
            FTPFile[] getremotefiles = mFtpClient.listFiles(UPLOAD_FILE_ADDRESS);
            //            if (getremotefiles != null) {
//                if (getremotefiles.length != 0) {
//                    mFtpClient.deleteFile(REMOTE_DB_FILENAME);
//                }
//            }
//            mFtpClient.enterLocalPassiveMode();
//            mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
//            mFtpClient.completePendingCommand();

            mFtpClient.enterLocalPassiveMode();
            FileInputStream inputfile = new FileInputStream(locadbfile);
//            boolean store = mFtpClient.storeFile(DB_NAME, inputfile);
//            OutputStream out = mFtpClient.appendFileStream(REMOTE_DB_FILENAME);
            OutputStream out = mFtpClient.storeFileStream(REMOTE_DB_FILENAME);

//            InputStream inputfile = mFtpClient.retrieveFileStream(FtpInfo.remoteFile);
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
