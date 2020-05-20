package com.sunquan.zmqproto.pubsub;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.DemoProtos;
import com.sunquan.zmqproto.DemoProtos.Alert;

public class MainSub {
	public static void main (String[] args) throws InvalidProtocolBufferException {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting messages from alerts server");
        ZMQ.Socket subscriber = context.socket(ZMQ.XSUB);
        subscriber.connect("tcp://localhost:5556");

       
        
    	subscriber.subscribe("alerts".getBytes());

        int messageCount;
        long totalSeverity = 0;
        for (messageCount = 0; messageCount < 5; messageCount++) {
        	String subject = subscriber.recvStr(); //主题
        	
        	byte[] alertData = subscriber.recv();
        	Alert alert = DemoProtos.Alert.parseFrom(alertData);
        	totalSeverity += alert.getSeverity();
        	System.out.println("subject=" + subject +"\n"+alert);
        }
        System.out.println("Average severity for zipcode was " + (int) (totalSeverity / messageCount));
        
        subscriber.close();
        context.term();
    }
}
