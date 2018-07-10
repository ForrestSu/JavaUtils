package com.sunquan.net.udp_multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Test {

    public static void main(String[] args) {
        String host = "225.0.0.1";// 多播地址
        int port = 9998;
        String message = "hello,multicastSocket!";
        try(MulticastSocket skt = new MulticastSocket(port))
        {
            InetAddress group = InetAddress.getByName(host);
            skt.joinGroup(group); // 加入多播组
            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), group, port);
            skt.send(dp);

            try {
                System.out.println("sleep 3s ...");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            byte[] buf = new byte[1024];
            DatagramPacket dpc = new DatagramPacket(buf, buf.length);
            skt.receive(dpc);
            System.out.println("收到多播信息：" + new String(buf));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}