package com.sq.network.udp_multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TestServer {

	public static void main(String[] args){
		//接受组播和发送组播的数据报服务都要把组播地址添加进来
		String host = "224.0.2.22";//多播地址
		int port = 9998;
		int length = 1024;
		byte[] buf = new byte[length];
		DatagramPacket dp = null;
		StringBuffer sbuf = new StringBuffer();
		try(MulticastSocket ms = new MulticastSocket(port)) {
			dp = new DatagramPacket(buf, length);
			//加入多播地址
			InetAddress group = InetAddress.getByName(host);
			ms.joinGroup(group);

			System.out.println("Listen： "+ host +" "+ port);
			ms.receive(dp);
            for (int i = 0; i < 1024; i++){
				if(buf[i] == 0){
					break;
				}
				sbuf.append((char) buf[i]);
			}
			System.out.println("收到多播消息：" + sbuf.toString());
			DatagramPacket dps = new DatagramPacket("ok".getBytes(), "ok".length(), dp.getAddress(), dp.getPort());
			ms.send(dps);


		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}