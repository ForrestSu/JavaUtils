package com.sunquan.zmqproto.suborpub;

import java.io.UnsupportedEncodingException;

import org.zeromq.ZMQ;

public class TestRep {
	public static void main(String[] args) throws InterruptedException {
		ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);

        socket.bind ("tcp://*:5555");

        while (!Thread.currentThread ().isInterrupted ()) {

            byte[] request = socket.recv(0);
            String msg="";
			try {
				msg = new String(request, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("recvd:"+msg);
            socket.send("world", 0);
            Thread.sleep(1000); //  Do some 'work'
        }
        
        socket.close();
        context.term();
	}
}
