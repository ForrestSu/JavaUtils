package com.sunquan.zmqproto.reqrep;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.DemoProtos;
import com.sunquan.zmqproto.DemoProtos.HelloMessage;

public class MainReq {
	public static void main (String[] args) throws InvalidProtocolBufferException{
		
		HelloMessage helloMessage = DemoProtos.HelloMessage.newBuilder()
			.setMessageText("Hello from Client")
			.build();
		
		
        ZMQ.Context context = ZMQ.context(1);
        System.out.println("Connecting to hello world server");

        ZMQ.Socket socket = context.socket(ZMQ.REQ);
        socket.connect ("tcp://192.168.4.184:8120");

        for(int requestNbr = 0; requestNbr != 10; requestNbr++) {                      
            socket.send(helloMessage.toByteArray(), 0);

            byte[] reply = socket.recv(0);
            HelloMessage helloMessageResponse = DemoProtos.HelloMessage.parseFrom(reply);
            System.out.println("Received "+ helloMessageResponse.getMessageText()+" " + requestNbr);
        }
        
        socket.close();
        context.term();
    }
}
