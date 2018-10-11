package personal.wl.jspos.update.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;

import personal.wl.jspos.R;
import personal.wl.jspos.update.view.CommonProgressDialog;

public class UpgradeUI {
    private Context context;
    private CommonProgressDialog pBar;
    private FTPClient mFtpClient;
    private File getversionfilejson;

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


                getversionfilejson = new File(context.getFilesDir().getPath() + "/" + "output.json");
                FTPToolkit.download(mFtpClient, "/posapp/output.json", getversionfilejson.getPath());

                Message msg = new Message();
                msg.what = 8888;
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
                case 8888:
                    proc();
                    break;
                default:
                    break;
            }
        }
    };

    private void proc() {
        int vision = Tools.getVersion(context);
        UpgradeApk upgradeApk = new UpgradeApk(this.context);
        int cc = upgradeApk.getVersioncode();
        String newversion = upgradeApk.getVersionname();
        String content = "\n" +
                "---------------------\n" +
                "本次升级版到：" +
                upgradeApk.getVersionname() +
                "\n" +
                "---------------------\n";

        System.out.println(newversion + "v" + vision + ",,"
                + cc);
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(newversion + "v"
                        + vision);
                // 版本号不同
                ShowDialog(vision, newversion, content);
            }
        }


    }

}
