package com.sq.sftp;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
/** 
* 类说明 sftp工具类
*/
public class SftpUtil {
    private ChannelSftp sftp;
        
    private Session session;
    /** SFTP 服务器地址IP地址*/
    private String host;
    /** SFTP 端口*/
    private int port;
    /** SFTP 登录用户名*/
    private String username;
    /** SFTP 登录密码 */
    private String password = null;
    /** 私钥*/
    private String privateKey = null;
    
    /**
     * Create 密码认证的sftp对象
     *   new SFTPUtil("ip地址", 22, "用户名", "密码", null)
     * Create 秘钥认证的sftp对象
     *   new SFTPUtil("ip地址", 22, "用户名", null, "私钥")
     */
    public SftpUtil(String host, int port, String username, String password, String privateKey) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.privateKey = privateKey;
    }
    
    public SftpUtil() {
    }
    
    /** 
     * 连接sftp服务器
     * @return if login success return true
     */
    public boolean login() {
        boolean bRet = false;
        try {
            JSch jsch = new JSch();
            if (privateKey != null && privateKey.length() > 0) {
                jsch.addIdentity(privateKey);// 优先设置私钥
            } else if (password != null && password.length() > 0) {
                session.setPassword(password); // 否则设置密码
            } else {
                System.err.println("error: please config: password or private_key!");
                return false;
            }
            session = jsch.getSession(username, host, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.setTimeout(5000); // set timeout ms
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            bRet = channel.isConnected();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return bRet;
    }
    
    /**
     * 断开连接
     */
    public void logout() {
        if (sftp != null) {
            sftp.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * @param path 相对路径 或者 绝对路径
     * @throws if fail throw SftpException
     */
    public void mkdirs(String path) throws SftpException {
        try {
            sftp.cd(path);
            // System.out.println("path: [" + path + "] already exists!");
        } catch (SftpException e) {
            if (path.startsWith("/")) {
                try {
                    sftp.cd("/");
                } catch (SftpException e1) {
                    e1.printStackTrace();
                }
                path = path.substring(1);
            }
            String[] dirs = path.split("/");
            for (String dir : dirs) {
                try {
                    sftp.cd(dir);
                } catch (SftpException e1) {
                    sftp.mkdir(dir);
                    sftp.cd(dir);
                }
                // System.out.println("pwd: " + sftp.pwd());
            }
        }
    }

    /**
     * 上传单个文件到服务器
     *
     * @param srcFile     本地文件(可带全路径)
     * @param dstDir      上传到该目录
     * @param dstFileName 服务器端文件名
     */
    public void upload(String srcFile, String dstDir, String dstFileName) throws SftpException {
        try {
            sftp.cd(dstDir);
            // System.out.println("path: [" + path + "] already exists!");
        } catch (SftpException e) {
            mkdirs(dstDir);
        }
        // 上传文件
        sftp.put(srcFile, dstFileName);
    }
    /**
     * 下载单个文件到本地
     * @param srcDir      服务端目录
     * @param srcFileName 下载服务端的文件名
     * @param dstDir      存在本地的路径
     */
    public void downloadFile(String srcDir, String srcFileName, String dstDir) throws SftpException {
        if (srcDir != null && (srcDir.length() > 0)) {
            sftp.cd(srcDir);
        }
        sftp.get(srcFileName, dstDir);
    }
    
    // List source (remote, sftp) directory and create a local copy of it - method for every single directory.
    private void lsFolderCopy(String sourcePath, String destPath) throws SftpException {
        Vector<ChannelSftp.LsEntry> list = sftp.ls(sourcePath); // List source directory structure.
        for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the list to get file/folder names.
            if (!oListItem.getAttrs().isDir()) { // If it is a file (not a directory).
                if (!(new File(destPath + "/" + oListItem.getFilename())).exists() || (oListItem.getAttrs().getMTime() > Long.valueOf(new File(destPath + "/" + oListItem.getFilename()).lastModified() / (long) 1000).intValue())) { // Download only if changed later.
                    new File(destPath + "/" + oListItem.getFilename());
                    // Grab file from source ([source filename], [destination filename]).
                    sftp.get(sourcePath + "/" + oListItem.getFilename(), destPath + "/" + oListItem.getFilename());
                }
            } else {
                if(".".equals(oListItem.getFilename()) || "..".equals(oListItem.getFilename())) {
                    // ignore 
                } else {
                    // Empty folder copy.
                    new File(destPath + "/" + oListItem.getFilename()).mkdirs();
                    // Enter found folder on server to read its contents and create locally.
                    lsFolderCopy(sourcePath + "/" + oListItem.getFilename(), destPath + "/" + oListItem.getFilename());
                }
            }
        }
    }
    public void downloadDir(String sourcePath, String destPath) throws SftpException { // With subfolders and all files.
        // Create local folders if absent.
        try {
            new File(destPath).mkdirs();
        } catch (Exception e) {
            System.out.println("Error at : " + destPath);
        }
        sftp.lcd(destPath);
        // Copy remote folders one by one.
        lsFolderCopy(sourcePath, destPath); // Separated because loops itself inside for subfolders.
    }

    /**
     * 3 删除文件
     * 
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException{  
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }
    
    
    /**
     * 4 列出目录下的文件
     * @param directory 要列出的目录
     * @param sftp
     */
    public Vector<?> listFiles(String directory) throws SftpException {  
        return sftp.ls(directory);
    }
    //上传文件测试
    public static void main(String[] args) throws SftpException, IOException {
        SftpUtil sftp = new SftpUtil("ip地址", 22, "用户名", "密码", null);
        sftp.login();
        sftp.upload("文件路径", "test_sftp.jpg", "D:\\picture\\logo.jpg");
        sftp.logout();
    }
}
 