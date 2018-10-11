package personal.wl.jspos.update.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FTPToolkit {

    private FTPToolkit() {
    }

    /**
     * 创建FTP连接
     *
     * @param host     主机名或IP
     * @param port     ftp端口
     * @param username ftp用户名
     * @param password ftp密码
     * @return 一个客户端
     * @throws Exception
     */
    public static FTPClient makeFtpConnection(String host, int port,
                                              String username, String password) throws Exception {
        FTPClient client = new FTPClient();
        int reply;
        try {
            client.connect(host, port);

            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                // 断开连接
                client.disconnect();
                throw new IOException("connect fail: " + reply);
            }


            if (username != null && password != null) {
                client.login(username, password);
                client.enterLocalPassiveMode();
                client.setFileType(FTP.BINARY_FILE_TYPE);
//                client.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
            }

            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                // 断开连接
                client.disconnect();
                throw new IOException("connect fail: " + reply);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return client;
    }

    /**
     * FTP下载文件到本地一个文件夹,如果本地文件夹不存在，则创建必要的目录结构
     *
     * @param client         FTP客户端
     * @param remoteFileName FTP文件
     * @param localPath      存的本地文件路径或目录
     * @throws Exception
     */
    public static void download(FTPClient client, String remoteFileName,
                                String localPath) throws Exception {

        String localfilepath = localPath;
        int x = isExist(client, remoteFileName);
        File localFile = new File(localPath);
        if (localFile.isDirectory()) {
            if (!localFile.exists())
                localFile.mkdirs();
            localfilepath = PathToolkit.formatPath4File(localPath
                    + File.separator + new File(remoteFileName).getName());
        }

        if (x == FTPFile.FILE_TYPE) {
            try {
//                File file = new File(Context.getFilesDir().getPath() + "test.txt");
                FileOutputStream fileOutputStream;
                fileOutputStream = new FileOutputStream(localFile);
//                        new FileOutputStream(localFile);
                BufferedOutputStream outStream = new BufferedOutputStream(fileOutputStream);
                boolean b = client.retrieveFile(remoteFileName, outStream);

            } catch (Exception e) {
                throw new Exception(e);
            }
        } else {
            throw new Exception("the target " + remoteFileName + "not exist");
        }
    }


    public static int getfilesize(FTPClient client, String remoteFileName
    ) throws Exception {


        int x = isExist(client, remoteFileName);

        if (x == FTPFile.FILE_TYPE) {
            try {
                FTPFile[] list = null;
                list = client.listFiles(remoteFileName);
                FTPFile f = list[0];

                return (int) f.getSize();

            } catch (Exception e) {
                throw new Exception(e);
            }
        } else {
            throw new Exception("the target " + remoteFileName + "not exist");
        }
    }

    /**
     * FTP上传本地文件到FTP的一个目录下
     *
     * @param client           FTP客户端
     * @param localfile        本地文件
     * @param remoteFolderPath FTP上传目录
     * @throws Exception
     */
    public static void upload(FTPClient client, File localfile,
                              String remoteFolderPath) throws Exception {
        remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
        try {
            client.changeWorkingDirectory(remoteFolderPath);
            if (!localfile.exists())
                throw new Exception("the upload FTP file"
                        + localfile.getPath() + "not exist!");
            if (!localfile.isFile())
                throw new Exception("the upload FTP file"
                        + localfile.getPath() + "is a folder!");


        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * FTP上传本地文件到FTP的一个目录下
     *
     * @param client           FTP客户端
     * @param localfilepath    本地文件路径
     * @param remoteFolderPath FTP上传目录
     * @throws Exception
     */
    public static void upload(FTPClient client, String localfilepath,
                              String remoteFolderPath) throws Exception {
        File localfile = new File(localfilepath);
        upload(client, localfile, remoteFolderPath);
    }

    /**
     * 批量上传本地文件到FTP指定目录上
     *
     * @param client
     *            FTP客户端
     * @param localFilePaths
     *            本地文件路径列表
     * @param remoteFolderPath
     *            FTP上传目录
     * @throws Exception
     */
//        public static void uploadListPath(FTPClient client,
//                                          List<String> localFilePaths, String remoteFolderPath, MyFtpListener listener) throws Exception {
//            remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
//            try {
//                client.changeWorkingDirectory(remoteFolderPath);
//                for (String path : localFilePaths) {
//                    File file = new File(path);
//                    if (!file.exists())
//                        throw new Exception("the upload FTP file" + path + "not exist!");
//                    if (!file.isFile())
//                        throw new Exception("the upload FTP file" + path
//                                + "is a folder!");
//                    if (listener != null)
//                        client.upload(file, listener);
//                    else
//                        client.upload(file);
//                }
//                client.changeWorkingDirectory("/");
//            } catch (Exception e) {
//                throw new Exception(e);
//            }
//        }

    /**
     * 批量上传本地文件到FTP指定目录上
     *
     * @param client
     *            FTP客户端
     * @param localFiles
     *            本地文件列表
     * @param remoteFolderPath
     *            FTP上传目录
     * @throws Exception
     */
//        public static void uploadListFile(FTPClient client, List<File> localFiles,
//                                          String remoteFolderPath, MyFtpListener listener) throws Exception {
//            try {
//                client.changeWorkingDirectory(remoteFolderPath);
//                remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
//                for (File file : localFiles) {
//                    if (!file.exists())
//                        throw new Exception("the upload FTP file" + file.getPath()
//                                + "not exist!");
//                    if (!file.isFile())
//                        throw new Exception("the upload FTP file" + file.getPath()
//                                + "is a folder!");
//                    if (listener != null)
//                        client.upload(file, listener);
//                    else
//                        client.upload(file);
//                }
//                client.changeWorkingDirectory("/");
//            } catch (Exception e) {
//                throw new Exception(e);
//            }
//        }

    /**
     * 判断一个FTP路径是否存在，如果存在返回类型(FTPFile.TYPE_DIRECTORY=1、FTPFile.TYPE_FILE=0、
     * FTPFile.TYPE_LINK=2) 如果文件不存在，则返回一个-1
     *
     * @param client     FTP客户端
     * @param remotePath FTP文件或文件夹路径
     * @return 存在时候返回类型值(文件0 ， 文件夹1 ， 连接2)，不存在则返回-1
     */
    public static int isExist(FTPClient client, String remotePath) throws IOException {
        remotePath = PathToolkit.formatPath4FTP(remotePath);
//            client.changeWorkingDirectory("/");

        FTPFile[] list = null;
        try {
//                list = client.listFiles();
            list = client.listFiles(remotePath);

        } catch (Exception e) {
            return -1;
        }
        if (list.length > 1)
            return FTPFile.DIRECTORY_TYPE;
        else if (list.length == 1) {
            FTPFile f = list[0];
            if (f.getType() == FTPFile.DIRECTORY_TYPE)
                return FTPFile.DIRECTORY_TYPE;

            if (f.getType() == FTPFile.FILE_TYPE)
                return FTPFile.FILE_TYPE;

//             假设推理判断
            String _path = remotePath + "/" + f.getName();
            try {
                int y = client.listFiles(_path).length;
                if (y == 1)
                    return FTPFile.DIRECTORY_TYPE;
                else
                    return FTPFile.FILE_TYPE;
            } catch (Exception e) {
                return FTPFile.FILE_TYPE;
            }
        } else {
            try {
                client.changeWorkingDirectory(remotePath);
                return FTPFile.DIRECTORY_TYPE;
            } catch (Exception e) {
                return -1;
            }
        }
    }

    public static long getFileLength(FTPClient client, String remotePath) throws Exception {
        String remoteFormatPath = PathToolkit.formatPath4FTP(remotePath);
        if (isExist(client, remotePath) == 0) {
            FTPFile[] files = client.listFiles(remoteFormatPath);
            return files[0].getSize();

        } else {
            throw new Exception("get remote file length error!");
        }
    }

    /**
     * 关闭FTP连接，关闭时候像服务器发送一条关闭命令
     *
     * @param client FTP客户端
     * @return 关闭成功，或者链接已断开，或者链接为null时候返回true，通过两次关闭都失败时候返回false
     */

    public static boolean closeConnection(FTPClient client) {
        if (client == null)
            return true;
        if (client.isConnected()) {
            try {
                client.disconnect();
                return true;
            } catch (Exception e) {
                try {
                    client.disconnect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }


    public static void downloadmethod(FTPClient client, String remoteFileName,
                                      String localPath) throws Exception {
        try {
            File file = new File(localPath);
            OutputStream out = new FileOutputStream(file);
            InputStream inputfile = client.retrieveFileStream(remoteFileName);
            long fileLength = FTPToolkit.getFileLength(client, remoteFileName);
            byte[] data = new byte[1024];
            int total = 0;
            int count;
            while ((count = inputfile.read(data)) != -1) {
                total += count;
                if (fileLength > 0) // only if total length is known
                    out.write(data, 0, count);
            }
            out.flush();
            out.close();
            inputfile.close();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
