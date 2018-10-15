package personal.wl.jspos.update.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;

import personal.wl.jspos.R;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.update.utils.FtpInfo.DOWNLOAD_OVER;
import static personal.wl.jspos.update.utils.FtpInfo.DOWNLOAD_UPDATE;
import static personal.wl.jspos.update.utils.FtpInfo.UPGRADE_JSON_FILE_ADDRESS;
import static personal.wl.jspos.update.utils.FtpInfo.UPGRADE_JSON_FILE_NAME;
import static personal.wl.jspos.update.utils.FtpInfo.UPGRADE_JSON_FILE_NAME_README;
import static personal.wl.jspos.update.utils.FtpInfo.UPGRADE_README_FILE_ADDRESS;


public class UpgradeUI {
    private Context context;
    private CommonProgressDialog pBar;
    private FTPClient mFtpClient;
    private File getversionfilejson;
    private File getversionfilejsonreadme;
    private File getapkfile;

    public UpgradeUI(Context context) {
        this.context = context;
    }

    public void getversion() {
        new getversionftpjson().start();
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
                        final DownloadTask downloadTask = new DownloadTask(pBar, context);
                        downloadTask.execute();
                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
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

    private class getversionftpjson extends Thread {
        @Override
        public void run() {
            try {
                mFtpClient = FTPToolkit
                        .makeFtpConnection(FtpInfo.IP, FtpInfo.PORT,
                                FtpInfo.LOGIN_ACCOUNT, FtpInfo.LOGIN_PASSWORD);
                getversionfilejson = new File(context.getFilesDir().getPath() + "/" + UPGRADE_JSON_FILE_NAME);
                getversionfilejsonreadme = new File(context.getFilesDir().getPath() + "/" + UPGRADE_JSON_FILE_NAME_README);
                FTPToolkit.download(mFtpClient, UPGRADE_JSON_FILE_ADDRESS, getversionfilejson.getPath());
                FTPToolkit.download(mFtpClient, UPGRADE_README_FILE_ADDRESS, getversionfilejsonreadme.getPath());
                Message msg = new Message();
                msg.what = DOWNLOAD_UPDATE;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_UPDATE:
                    download_update_proc();
                    break;

                case DOWNLOAD_OVER:
//                    download_installApk();
                    break;

                default:
                    break;
            }
        }
    };

//    private void download_installApk() {
//        String ss = BuildConfig.APPLICATION_ID;
//        getapkfile=new File(context.getFilesDir().getPath() + "/" + APK_FIlE);
//        Intent install = new Intent(Intent.ACTION_VIEW);
//        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri contentUri = FileProvider.getUriForFile(this.context, context.getApplicationContext().getPackageName() + ".provider", getapkfile);
//        install.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        this.context.startActivity(install);
//
//
//    }

    private void download_update_proc() {
        int vision = Tools.getVersion(context);
        UpgradeApk upgradeApk = new UpgradeApk(this.context);
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
                +versionreadme;
        System.out.println(newversion + "v" + vision + ",,"
                + cc);

        //检查版本是否需要更新
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(newversion + "v"
                        + vision);
                // 版本号不同
                ShowDialog(vision, newversion, content);
//                download_installApk();
            }
        } else {
            Toast.makeText(this.context, "当前无更新", Toast.LENGTH_LONG).show();
        }


    }

}
