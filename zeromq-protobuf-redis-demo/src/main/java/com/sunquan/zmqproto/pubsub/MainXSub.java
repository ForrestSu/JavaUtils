package com.sunquan.zmqproto.pubsub;

import org.zeromq.ZMQ;


public class MainXSub {
	public static void main (String[] args)  {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting messages from alerts server");
        ZMQ.Socket subscriber = context.socket(ZMQ.XSUB);
        subscriber.bind("tcp://*:5556");
       // subscriber.connect("tcp://localhost:5556");

       
        
    	subscriber.send("\1".getBytes());
    	System.out.println("sub topic..");
        int messageCount=1;
       while(messageCount == 1) {
        	String subject = subscriber.recvStr(); //主题
        	
        	byte[] content = subscriber.recv();
        	//Alert alert = DemoProtos.Alert.parseFrom(alertData);
        	System.out.println("subject=" + subject.length());
        }
        
        subscriber.close();
        context.term();
    }
}
