package com.sq.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class TcpClient {

    public static  String saddr = "192.168.4.41";
    public static  int  iport = 9081;
    //
    InetSocketAddress endpoint = null ;
    Socket socket = null;
    PrintWriter write = null;
    BufferedReader in = null;
    
    public TcpClient() {
        Init();
    }
    public void Init() {
        int conn_timeout = 5 * 1000; //5 seconds
        InetSocketAddress endpoint = new InetSocketAddress(saddr, iport);
        socket = new Socket();
        
        try {
            socket.setTcpNoDelay(true);
            // socket.setKeepAlive(true);
            socket.setSoTimeout(20 * 1000); // 20 seconds
            socket.connect(endpoint, conn_timeout);
            System.out.println("connect ok!");
            // 1 由Socket对象得到输出流，并构造PrintWriter对象
            write = new PrintWriter(socket.getOutputStream());
            // 2 由Socket对象得到输入流，并构造相应的BufferedReader对象
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "gb2312"));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void  Close() {
        write.close(); // 关闭Socket输出流
        try {
            in.close(); // 关闭Socket输入流
            socket.close();
            System.out.println("Close Stream!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Reconnect() {
       Close();
       Init();
    }

    public void PrintMsg(String data) {
        System.out.println(data);
    }

    public String SendDataRecv(String sendmsg, int iCount) {
        
        PrintMsg("Send ==> [" + sendmsg + "]");
        boolean bflag = true;
        write.println(sendmsg); // 发送数据
        write.flush();
        try {
            int batch_recv_bytes = 200;
            // 从输出流读取数据
            StringBuffer buffer = new StringBuffer();
            while (bflag) {
                char chbuf[] = new char[batch_recv_bytes];
                int nread = in.read(chbuf);
                if (nread == -1) {
                    System.out.println("[-1] recv fail!");
                    break;
                } else {
                    PrintMsg("recv bytes = " + nread);
                    buffer.append(chbuf, 0, nread);
                    if(nread < batch_recv_bytes) {
                        return buffer.toString();
                    }  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[Empty!]";
    }
    
    public static void main(String[] args) {
        String LoginMsg = "R||100001|61|174|0||1740047208|600837|WSWT||1|";
        
        TcpClient obj = new TcpClient();
        Scanner in = new Scanner(System.in);
        System.out.println("Please input type: Login(1), Reconnect(8), Quit(9)\n");
        while (in.hasNext()) {
            int cmd = in.nextInt();
            if (cmd == 1) {
                // login
                String resp = obj.SendDataRecv(LoginMsg, 0);
                resp = "LoginResp <== [" +  resp +"]";
                obj.PrintMsg(resp); 
            }else if(cmd == 8) {
               obj.Reconnect();
            }
            else if(cmd == 9) {
                System.out.println("exit!");
                break;
            }
            System.out.println("Please input type: Login(1), Reconnect(8), Quit(9)\n");
        }
        in.close();
    }
}
