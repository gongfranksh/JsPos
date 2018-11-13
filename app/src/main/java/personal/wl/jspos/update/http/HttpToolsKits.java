package personal.wl.jspos.update.http;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import personal.wl.jspos.R;
import personal.wl.jspos.update.utils.Tools;
import personal.wl.jspos.update.utils.UpgradeApk;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static personal.wl.jspos.method.PosHandleDB.UpGradeSqlScript;
import static personal.wl.jspos.update.http.ReleaseWebInfo.APK_FILE_NAME;
import static personal.wl.jspos.update.http.ReleaseWebInfo.APK_URL;
import static personal.wl.jspos.update.http.ReleaseWebInfo.README_FILE_NAME;
import static personal.wl.jspos.update.http.ReleaseWebInfo.README_URL;
import static personal.wl.jspos.update.http.ReleaseWebInfo.UPGRADE_FILE_NAME;
import static personal.wl.jspos.update.http.ReleaseWebInfo.UPGRADE_URL;
import static personal.wl.jspos.update.http.ReleaseWebInfo.VERSION_FILE_NAME;
import static personal.wl.jspos.update.http.ReleaseWebInfo.VERSION_URL;

public class HttpToolsKits {

    private Context context;
    private View v;
    private CommonProgressDialog pBar;

    public HttpToolsKits(Context context, View v) {
        this.context = context;
        this.v = v;
    }

    public void downloadVersionFile() {
        //下载output.json版本信息文件
        OkHttpUtils
                .get()
                .url(VERSION_URL)
                .build()//
                .execute(new FileCallBack(context.getFilesDir().getPath(), VERSION_FILE_NAME)//
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "下载" + VERSION_FILE_NAME + "失败" + e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Call call, File file) {
//                        Toast.makeText(context, "下载" + VERSION_FILE_NAME + "成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void inProgress(float v, long l) {
                    }
                });


        //下载Readme.txt 发版说明文件
        OkHttpUtils
                .get()
                .url(README_URL)
                .build()//
                .execute(new FileCallBack(context.getFilesDir().getPath(), README_FILE_NAME)//
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "下载" + README_FILE_NAME + "失败" + e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Call call, File file) {
//                        Toast.makeText(context, "下载" + README_FILE_NAME + "成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void inProgress(float v, long l) {
                    }
                });
        checkdownload();

    }


    public void downloadUpgradeSqlfilie() {
        //下载Readme.txt 发版说明文件
        OkHttpUtils
                .get()
                .url(UPGRADE_URL)
                .build()//
                .execute(new FileCallBack(context.getFilesDir().getPath(), UPGRADE_FILE_NAME)//
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "下载" + UPGRADE_FILE_NAME + "失败" + e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Call call, File file) {
                        Toast.makeText(context, "下载" + UPGRADE_FILE_NAME + "成功", Toast.LENGTH_LONG).show();
                        try {
                            FileInputStream f = new FileInputStream(file);
                            InputStreamReader fis = new InputStreamReader(f);
                            BufferedReader buffreader = new BufferedReader(fis);
                            String line;
                            while (( line = buffreader.readLine()) != null) {
                                String content = line + "\n";
                                UpGradeSqlScript(content);
                            }
                            buffreader.close();
                            fis.close();
                            f.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void inProgress(float v, long l) {
                    }
                });

    }

    public void checkdownload() {
        int vision = Tools.getVersion(context);
        UpgradeApk upgradeApk = new UpgradeApk(context);
        int cc = upgradeApk.getVersioncode();
        String newversion = upgradeApk.getVersionname();
        String versionreadme = upgradeApk.getVersionreadme();
        String versionpath = upgradeApk.getPath();


        String content = "\n" +
                "---------------------\n" +
                "本次升级版到：" +
                upgradeApk.getVersionname() +
                "\n" +
                "---------------------\n"
                + versionreadme;
        System.out.println(newversion + "v" + vision + ",,"
                + cc);

        //检查版本是否需要更新
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(newversion + "v"
                        + vision);
                // 版本号不同
//                downloadapk(context);
                ShowDialog(vision, newversion, content);
            }
        } else {
            Toast.makeText(context, "当前无更新", Toast.LENGTH_LONG).show();
        }
    }

    public static void downloadapk(Context context) {
        DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_URL));
        request.setDescription("下载中");
        request.setTitle("新版本下载");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,APK_FILE_NAME);
        request.setDestinationInExternalFilesDir(context, DIRECTORY_DOWNLOADS, APK_FILE_NAME);


        long downloadid = manager.enqueue(request);
        SharedPreferences sPreferences = context.getSharedPreferences(
                "downloadapk", 0);

        sPreferences.edit().putLong("apk", downloadid).commit();//保存此次下载ID
        sPreferences.edit().putString("apk_url", DIRECTORY_DOWNLOADS).commit();//保存此次下载ID


    }

    private void ShowDialog(int vision, String newversion, String content
    ) {

        new android.app.AlertDialog.Builder(this.context)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(context);
                        pBar.setCanceledOnTouchOutside(false);
                        pBar.setTitle("正在下载");
                        pBar.setCustomTitle(LayoutInflater.from(
                                context).inflate(
                                R.layout.title_dialog, null));
                        pBar.setMessage("正在下载");
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(true);
                        downloadapk(context);
                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
//                                downloadTask.cancel(true);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();


    }

}
