package com.sunquan.zmqproto.spuborder;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.TradingProxyClass.OrderInfo;
import com.sunquan.zmqproto.TradingProxyClass.OrderUpdates;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.BuySell;


public class OrderOperator {
	
	public static String stockaccountSH = "A1299122";
	public static String stockaccountSZ = "075511906";
	public static String acct= "mingcemoni";
 
	private static void hintinput(){
		System.out.println("请输入命令...\n1 n(new-order)\n2 d(deal)\n3 r(Reject)");
	}
	public static void main(String[] args) throws InvalidProtocolBufferException {

		MsgCarrier.Builder mc = MsgCarrierClass.MsgCarrier.newBuilder()
				.setMsgid(1)
				.setType(MsgType.PUB_ORDERUPDATE);
				//.setMessage(data.toByteString());

		ZMQ.Context context = ZMQ.context(1);
		System.out.println("Connecting ...");

		ZMQ.Socket socket = context.socket(ZMQ.REQ);
		socket.connect("tcp://127.0.0.1:5559");
		Scanner input= new Scanner(System.in);
		OrderInfo order = null;
		OrderUpdates orderupdate;
		OrderBean bean= new OrderBean();
		hintinput();
		String code="";
		BuySell side = BuySell.BS_BUY;
		while(input.hasNext()) {
			String cmd = input.nextLine();
			if(null== cmd || cmd.equals("exit"))break;
			
			if(cmd.startsWith("n") || cmd.startsWith("d") ||  cmd.startsWith("r")){
				if(cmd.startsWith("n")){
					System.out.println("输入代码:");
					code = input.nextLine();
				    side = BuySell.BS_BUY;
					System.out.println("输入买卖方向:(buy/sell)");
					String tmp=input.nextLine();
					if(tmp.startsWith("s"))side=BuySell.BS_SELL;
				}
				System.out.println("输入数量：");
				int amt= input.nextInt();
				System.out.println("输入价格：");
				double  price =  input.nextDouble();
				input.nextLine();//吃掉一个回车
				order = bean.Change(cmd, code,side, amt, price);
				if(order !=null){
					System.out.println("准备发送消息...");
					orderupdate = OrderUpdates.newBuilder().addItems(order).build();
					mc.setMessage(orderupdate.toByteString());
					socket.send(mc.build().toByteArray());
				    System.out.println("send one order successfull!\n"+order);
				    byte[] reply = socket.recv();
				    try {
						System.out.println("Received Msg=["  +new String(reply,"utf-8") +"]" );
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} //do nothing
			else{
				System.err.println("输入命令不合法,重新输入!");
			}
			hintinput();
		}
		System.out.println("退出!");
		input.close();
		socket.close();
		context.term();
	}

}
