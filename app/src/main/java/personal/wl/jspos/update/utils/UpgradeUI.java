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
import personal.wl.jspos.Config.Beans.SystemFtpInfo;
import personal.wl.jspos.update.view.CommonProgressDialog;

import static personal.wl.jspos.Config.Beans.SystemSettingConstant.FTP_DOWNLOAD_DB_TEMPLATE;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.FTP_DOWNLOAD_OVER;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.FTP_DOWNLOAD_UPDATE;
import static personal.wl.jspos.Config.Beans.SystemSettingConstant.FTP_UPLOAD_UPDATE;


public class UpgradeUI {
    private Context context;
    private CommonProgressDialog pBar;
    private FTPClient mFtpClient;
    private File getversionfilejson;
    private File getversionfilejsonreadme;
    private File getapkfile;
    private DownloadTask downloadTask;

    private UploadDbTask uploadDbTask;
    private DownloadDbTask downloadDbTask;
    private SystemFtpInfo systemFtpInfo;

    public UpgradeUI(Context context) {
        this.context = context;
        systemFtpInfo= new SystemFtpInfo(context);

    }

    public void getdonwload() {
        new getversionftpjson().start();
    }

    public void upload() {
        new uploaddb2ftp().start();
//        ShowDialog("上传本地数据库", true);
    }

    public void dn_db_template() {
        new download_db_templdate().start();
    }


    private void ShowDialog(String content, final Boolean isDownload, final Boolean isDB
    ) {
        new android.app.AlertDialog.Builder(this.context)
                .setTitle("数据传输")
                .setMessage(content)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(context);
                        pBar.setCanceledOnTouchOutside(false);

                        if (isDownload) {
                            pBar.setTitle("正在下载");
                            pBar.setMessage("正在下载");
                        } else {
                            pBar.setTitle("正在上传本地数据库");
                            pBar.setMessage("正在上传");
                        }


                        pBar.setCustomTitle(LayoutInflater.from(
                                context).inflate(
                                R.layout.title_dialog, null));
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(true);

                        if (isDownload) {
                            if (!isDB) {
                                downloadTask = new DownloadTask(pBar, context);
                                downloadTask.execute();
                            } else {
                                downloadDbTask = new DownloadDbTask(pBar, context);
                                downloadDbTask.execute();
                            }

                        } else {
                            uploadDbTask = new UploadDbTask(pBar, context);
                            uploadDbTask.execute();
                        }


                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                if (isDownload) {
                                    if (!isDB) {
                                        downloadTask.cancel(true);
                                    } else {
                                        downloadDbTask.cancel(true);
                                    }


                                } else {
                                    uploadDbTask.cancel(true);
                                }


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
                        .makeFtpConnection(systemFtpInfo.getFtp_ip_address(), 21,
                                systemFtpInfo.getFtp_iaccount(), systemFtpInfo.getFtp_ipassword());

                getversionfilejson = new File(context.getFilesDir().getPath() + "/" + systemFtpInfo.getFtp_joson_file());
                getversionfilejsonreadme = new File(context.getFilesDir().getPath() + "/" + systemFtpInfo.getFtp_readme_file());
                FTPToolkit.download(mFtpClient, systemFtpInfo.getFtp_path()+'/'+systemFtpInfo.getFtp_joson_file(), systemFtpInfo.getFtp_joson_file());
                FTPToolkit.download(mFtpClient, systemFtpInfo.getFtp_path()+'/'+systemFtpInfo.getFtp_readme_file(),systemFtpInfo.getFtp_readme_file());
                Message msg = new Message();
                msg.what = FTP_DOWNLOAD_UPDATE;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class download_db_templdate extends Thread {
        @Override
        public void run() {
            try {
//                mFtpClient = FTPToolkit
//                        .makeFtpConnection(FtpInfo.IP, FtpInfo.PORT,
//                                FtpInfo.LOGIN_ACCOUNT, FtpInfo.LOGIN_PASSWORD);
//                getversionfilejson = new File(context.getFilesDir().getPath() + "/" + UPGRADE_JSON_FILE_NAME);
                Message msg = new Message();
                msg.what = FTP_DOWNLOAD_DB_TEMPLATE;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class uploaddb2ftp extends Thread {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                msg.what = FTP_UPLOAD_UPDATE;
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
                case FTP_DOWNLOAD_UPDATE:
                    download_update_proc();
                    break;

                case FTP_DOWNLOAD_OVER:
//                    download_installApk();
                    break;
                case FTP_UPLOAD_UPDATE:
                    upload_localdb_proc();
                    break;
                case FTP_DOWNLOAD_DB_TEMPLATE:
                    download_db_template_proc();
                    break;

                default:
                    break;
            }
        }
    };


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
                + versionreadme;
        System.out.println(newversion + "v" + vision + ",,"
                + cc);

        //检查版本是否需要更新
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(newversion + "v"
                        + vision);
                // 版本号不同
                ShowDialog(content, true,false);
//                download_installApk();
            }
        } else {
            Toast.makeText(this.context, "当前无更新", Toast.LENGTH_LONG).show();
        }
    }

    private void upload_localdb_proc() {
        String content = "\n" +
                "---------------------\n" +
                "上传本地数据库";
        ShowDialog(content, false,true);
    }

    private void download_db_template_proc() {
        String content = "\n" +
                "---------------------\n" +
                "下载初始数据库";
        ShowDialog(content,  true,true);
    }

}
