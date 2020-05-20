package com.sunquan.zmqproto.suborpub;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.AcctmgmtClass.AvailableInfo;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;

public class SubOnepos {
	public static void main (String[] args) throws InvalidProtocolBufferException {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting messages from alerts server");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://192.168.4.184:8121");

       // 
    	subscriber.subscribe("mingcemoni|sunquan.onepos".getBytes());
    	boolean flag=true;
    	AvailableInfo old=null;
    	while(flag) {
          	String address = subscriber.recvStr();
          	byte[] alertData = subscriber.recv();
          	//System.out.println(alertData);
          	MsgCarrier  msg = MsgCarrier.parseFrom(alertData);
          	AvailableInfo asset = AvailableInfo.parseFrom(msg.getMessage());
          	if( old==null || (Double.compare(old.getPosition(), asset.getPosition()))!=0
          	    || (Double.compare(old.getAvailable(), asset.getAvailable()))!=0
          	  
          	  )
          	{
          		if (old==null)
          			System.out.println("接收到主推数据:\n"+asset.toString());
          		else
          		{
          			System.out.println("\n当前持仓："+(asset.getPosition()-old.getPosition()));
          			System.out.println("持仓变更："+(asset.getAvailable()-old.getAvailable()));
          			System.out.println("================\n"+asset.toString());
          		}
          		old = asset;
              }
          	
          }
        
        subscriber.close();
        context.term();
    }
}
