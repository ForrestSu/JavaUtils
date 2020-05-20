package com.sunquan.zmqproto.suborpub;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.QuoteClass.SnapShot;

public class SubQuote {
	public static void main (String[] args) throws InvalidProtocolBufferException {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting messages from alerts server");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://192.168.4.21:8046");

       // 
    	subscriber.subscribe("26.3".getBytes());
    	boolean flag=true;
    	while(flag) {
          	String address = subscriber.recvStr();
          	byte[] alertData = subscriber.recv();
          	//System.out.println(alertData);
          	MsgCarrier  msg = MsgCarrier.parseFrom(alertData);
          	if(msg.getType() == MsgType.SNAPSHOT)
          	{
          		SnapShot  sst= SnapShot.parseFrom(msg.getMessage());
          		System.out.println(sst.toString());
          	}else
          	{
          		System.err.println(msg.toString());
          	}
          	
          	 
          }
        
        subscriber.close();
        context.term();
    }
}
