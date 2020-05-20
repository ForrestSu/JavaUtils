package com.sunquan.zmqproto.suborpub;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.TradingProxyClass.AccountBusiness;

public class SubAsset {
	public static void main (String[] args) throws InvalidProtocolBufferException {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting messages from alerts server");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://192.168.4.184:8121");

        
    	subscriber.subscribe("mingcemoni|sunquan.acctinfo".getBytes());
    	boolean flag=true;
    	AccountBusiness old = null;
        //  Process 100 updates
        while(flag) {
        	String subject = subscriber.recvStr();
        	byte[] reply = subscriber.recv();
        	MsgCarrier  msg = MsgCarrier.parseFrom(reply);
        	AccountBusiness asset = AccountBusiness.parseFrom(msg.getMessage());
        	if( old==null || (Double.compare(old.getCurrbalance(), asset.getCurrbalance()))!=0
        	    || (Double.compare(old.getAvailable(), asset.getAvailable()))!=0
        	    || (Double.compare(old.getAsset(), asset.getAsset()))!=0 //账户总资产
        	  )
        	{
        		if (old==null)
        		{
        			System.out.println("\nsubject:"+subject);
        			System.out.println("接收到主推数据:\n"+asset.toString());
        		}
        		else
        		{
        			System.out.println("\nsubject:"+subject);
        			System.out.println("当前资金："+(asset.getCurrbalance()-old.getCurrbalance()));
        			System.out.println("可用变更："+(asset.getAvailable()-old.getAvailable()));
        			System.out.println("总资产："+(asset.getAsset()-old.getAsset()));
        			System.out.println("================\n"+asset.toString());
        		}
        		old = asset;
            }
        	
        }
        
        subscriber.close();
        context.term();
    }
}
