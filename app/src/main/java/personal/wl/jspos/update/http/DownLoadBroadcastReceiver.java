package personal.wl.jspos.update.http;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.yanzhenjie.permission.FileProvider;

import java.io.File;

public class DownLoadBroadcastReceiver extends BroadcastReceiver {
    private long completeId;
    private DownloadManager manager;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        SharedPreferences sPreferences = context.getSharedPreferences(
                "downloadapk", 0);
        long Id = sPreferences.getLong("apk", 0);
        if (Id == completeId) {
            this.manager =
                    (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            File apkFile = queryDownloadedApk();
            if (apkFile.exists()) {
                try {
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    installIntent.setDataAndType(getFileUrl(apkFile), "application/vnd.android.package-archive");
                    context.startActivity(installIntent);
                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    public File queryDownloadedApk() {
        File targetApkFile = null;
        if (this.completeId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(this.completeId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = this.manager.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!uriString.isEmpty()) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    public Uri getFileUrl(File targetapkfile) {
        Uri contentUri = null;
        contentUri = FileProvider.getUriForFile(this.context, this.context.getApplicationContext().getPackageName() + ".provider", targetapkfile);
        return contentUri;

    }
}