package com.sunquan.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 给server发送一个，接收一个（数据包）就关闭。
 *
 */
public class UDPClient {

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        try {
            // 发送数据
            clientSocket = new DatagramSocket();// 构造“邮局”
            InetAddress address = InetAddress.getByName("localhost");
            byte[] receive = new byte[1024];
            byte[] sendData = new byte[1024];

            String data = "hello!";
            sendData = data.getBytes();
            // 构建一个数据包[一个封装好的信封]，发送到address的指定端口
            DatagramPacket dp = new DatagramPacket(sendData, sendData.length, address, 8899);
            clientSocket.send(dp);

            // 接收数据，提前准备一个空信封DatagramPacket
            DatagramPacket dp1 = new DatagramPacket(receive, receive.length);

            clientSocket.receive(dp1);// 接收数据，封装放到dp1数据包
            String data1 = new String(dp1.getData());
            System.out.println("Client's receive:" + data1);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientSocket.close();
        }

    }

}
