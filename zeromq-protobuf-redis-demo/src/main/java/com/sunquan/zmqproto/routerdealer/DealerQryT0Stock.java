package com.sunquan.zmqproto.routerdealer;



import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.T0tradingClass.AcctT0Stock;
import com.sunquan.zmqproto.T0tradingClass.QueryT0Stock;
import com.sunquan.zmqproto.TradingProxyClass;
import com.sunquan.zmqproto.TradingProxyClass.*;

public class DealerQryT0Stock {
	public static void main(String[] args) throws InvalidProtocolBufferException {

		AccountInfo acct = TradingProxyClass.AccountInfo.newBuilder().setAccount("66880000").build();

		QueryT0Stock qt= QueryT0Stock.newBuilder()
				.setAccountinfo(acct)
				.setUser("")
				.build();
		MsgCarrier mc = MsgCarrierClass.MsgCarrier.newBuilder()
				.setMsgid(2000)
				.setType(MsgType.QUERY_T0STOCK_REQ)
				.setMessage(qt.toByteString())
				.build();
 
		ZMQ.Context context = ZMQ.context(1);
		System.out.println("Connecting ...");

		ZMQ.Socket socket = context.socket(ZMQ.DEALER);
		//socket.setIdentity(new String("client-999887").getBytes());
		socket.connect("tcp://10.0.1.27:8120");
		
		MsgCarrier recv;
		System.out.println(System.currentTimeMillis());
		for (int i = 0; i < 1; i++) {
			socket.sendMore("\0");
			//System.out.println(mc.toString());
			socket.send(mc.toByteArray());
			System.out.println("等待接收数据...");
			byte[] reply = socket.recv();
			//System.out.println("len(reply)="+reply.length+",reply[0]="+reply[0]);
			reply = socket.recv();
			
			recv = MsgCarrier.parseFrom(reply);
			AcctT0Stock obj = AcctT0Stock.parseFrom(recv.getMessage());
			System.out.println("Received reply:\n["  + obj.toString() +"]" );
		}

		socket.close();
		context.term();
	}
}
