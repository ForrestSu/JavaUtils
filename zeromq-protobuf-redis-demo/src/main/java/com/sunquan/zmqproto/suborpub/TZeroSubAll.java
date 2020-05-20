package com.sunquan.zmqproto.suborpub;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.AcctmgmtClass.PubPositions;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.TradingProxyClass.AccountBusiness;

public class TZeroSubAll {
	public static void main (String[] args) throws InvalidProtocolBufferException {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting messages from alerts server");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://192.168.4.43:8046");

    	subscriber.subscribe("26.1.601398".getBytes());
    	subscriber.subscribe("94.1.601398".getBytes());
    	boolean flag=true;
    	while(flag) {
          	String subject = subscriber.recvStr();
          	byte[] alertData = subscriber.recv();
          	//System.out.println(alertData);
          	
          	MsgCarrier  msg = MsgCarrier.parseFrom(alertData);
          //	allpos = PubPositions.parseFrom(msg.getMessage());
          	
          	System.out.println("subject=["+subject+"]");
          	System.out.println(alertData.length);
    	}
        
        subscriber.close();
        context.term();
    }
}
