package com.sunquan.zmqproto.routerdealer;


import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.T0tradingClass.AcctT0Stock;
import com.sunquan.zmqproto.TradingProxyClass;
import com.sunquan.zmqproto.TradingProxyClass.*;

public class DealerQryAcctPos {
	public static void main(String[] args) throws InvalidProtocolBufferException {

		AccountInfo acct = TradingProxyClass.AccountInfo.newBuilder()
						   .setAccount("66880000")
						   .build();

		QueryPosition qp = TradingProxyClass.QueryPosition.newBuilder()
				.setReqid(109)
				.setUserid("")  //为空表示查找主账号
				.setAccountinfo(acct).build();
		MsgCarrier mc = MsgCarrierClass.MsgCarrier.newBuilder()
				.setMsgid(1)
				.setType(MsgType.QUERYPOSITION)
				.setMessage(qp.toByteString())
				.build();
 
		ZMQ.Context context = ZMQ.context(1);
		System.out.println("Connecting ...");

		ZMQ.Socket socket = context.socket(ZMQ.DEALER);
		socket.connect("tcp://10.0.1.27:8020");
		
		MsgCarrier recv;
		for (int i = 0; i < 1; i++) {
			System.out.println(mc.toString());
			
			socket.sendMore("\0");
			socket.send(mc.toByteArray());
			System.out.println("等待接收数据...");
			byte[] reply = socket.recv();
			 reply = socket.recv();
			recv = MsgCarrier.parseFrom(reply);
			
	        QueryPositionRep pos = QueryPositionRep.parseFrom(recv.getMessage());
			System.out.println("Received reply:\n["  + pos.toString() +"]" );
		}

		socket.close();
		context.term();
	}
}
