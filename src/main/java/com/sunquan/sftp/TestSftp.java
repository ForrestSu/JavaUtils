package com.sunquan.sftp;

import java.util.Vector;

import com.jcraft.jsch.SftpException;

public class TestSftp 
{
    private static String IP_ADDRESS = "192.168.0.15";
    private static int SFTP_IPORT = 22;
    private static String USER_NAME = "username";
    private static String PASSWORD = null;
    private static String RSA_PRIVATE_KEY = "config/sftp";
    
    /*
     * Description: SFTP 测试单个文件的上传/下载
     *  usage: 如果需要上传文件夹,递归遍历上传即可
     */
    public static boolean testSFTP(String fileName) {
        boolean bRet = true;
        SftpUtil sftp = new SftpUtil(IP_ADDRESS, SFTP_IPORT, USER_NAME, PASSWORD, RSA_PRIVATE_KEY);
        sftp.login();  
        try {
            // 0 测试建立远程文件夹
            // sftp.mkdirs("/home/opadm/sunquan/upload/sftp/123");
            // 1 上传单个文件
            sftp.upload(fileName, "/home/opadm/sunquan/upload/sftp/123", "test_sftp.jpg");
            System.out.println("upload ok..");

            // 2 下载单个文件
            // sftp.downloadFile("/home/opadm/sunquan/upload/sftp/123", "test_sftp.jpg", "config/");
            // System.out.println("download ok..");

            // 3 列出远程文件列表
            Vector<?> lists = sftp.listFiles("/home/opadm/sunquan");
            for (int k = 0; k < lists.size(); ++k) {
                System.out.println(lists.get(k));
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } 
        sftp.logout();
        return bRet;
    }
    
    
    public static void main(String[] args )
    {
        boolean bRet = false;
        String fileName = "config/test.jpg";
        bRet = testSFTP(fileName);
        System.out.println( "Test ==> "+ bRet );
    }
}
