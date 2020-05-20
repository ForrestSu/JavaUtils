package com.sunquan.zmqproto.spuborder;


import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.TradingProxyClass.OrderInfo;

public class PubOrderInfo {
	
	
	static String acct= "mingcemoni";
	public static void main (String[] args) throws Exception {
	
        //  Prepare our context and publisher
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:8034"); //绑定一个端口用于发布
        String subject = acct+".order";
        //
        Socket socket = context.socket(ZMQ.REP);
        socket.bind ("tcp://*:5559");
        
        boolean flag = true;
        MsgCarrier recv;
        //OrderInfo order;
        while (flag) {
        	//发送消息
            byte[]  reply = socket.recv();
            recv = MsgCarrier.parseFrom(reply);
            //order = OrderInfo.parseFrom(recv.getMessage());
            System.out.println(recv);
            socket.send("hell world!");
            
        	publisher.sendMore(subject);
            publisher.send(recv.toByteArray());
            Thread.sleep(500);
        }

        publisher.close ();
        context.term ();
    }
}
