package com.sunquan.zmqproto.testcase;

import org.zeromq.ZMQ;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.T0tradingClass.AcctT0Stock;
import com.sunquan.zmqproto.T0tradingClass.UserT0Stock;
import com.sunquan.zmqproto.TradingProxyClass.AccountBusiness;
import com.sunquan.zmqproto.TradingProxyClass.AccountInfo;
import com.sunquan.zmqproto.TradingProxyClass.AdminInfo;
import com.sunquan.zmqproto.TradingProxyClass.PositionInfo;
import com.sunquan.zmqproto.TypeDefClass.TypeDef;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.AuthType;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.SecurityProviderEnum;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.SecurityType;

public class TestCaseDiv1 {
	
	public static String stockaccountSH = "A1299122";
	public static String stockaccountSZ = "075511906";
	public static String acct= "mingcemoni";
		
	static AcctT0Stock AddData()
    {
        AdminInfo admin =AdminInfo.newBuilder()
        		.setAdminname("admin")
        		.setIp("127.0.0.1")
        		.setMac("ADFS89912")
        		.build();
        
        AccountInfo  account= AccountInfo.newBuilder()
        		.setAccount(acct)
        		.setPassword("mingcemoni")
        		.setBranchno("1234")
        		.setSecurityprovider(SecurityProviderEnum.PROVIDER_HANDSUN)
        		.setSecuritytype(SecurityType.SECURITY_STOCK)
        		.setStockaccountSh(stockaccountSH)
        		.setStockaccountSz(stockaccountSZ)
        		.setAccountname("孙权")
        		.setAdmininfo(admin)
        		.setAuthtype(AuthType.NORMAL)
        		.build();
        
        UserT0Stock divdata = GetAcct_UserT0Stock();
        UserT0Stock subdivdata = GetUserT0Stock();
       
        AcctT0Stock divstock = AcctT0Stock.newBuilder()
        		.setReqid(100)
        		.setAccountinfo(account) //设置账户信息
        		.addItems(divdata) //主账号分券
        		.addItems(subdivdata) //子账号分券
        		.build();
        return divstock;
    }
	//发送分券数据
	public static void main(String[] args) throws InvalidProtocolBufferException {

		AcctT0Stock  data = AddData();
		MsgCarrier mc = MsgCarrierClass.MsgCarrier.newBuilder()
				.setMsgid(1)
				.setType(MsgType.SAVE_T0STOCK_REQ)
				.setMessage(data.toByteString())
				.build();

		ZMQ.Context context = ZMQ.context(1);
		System.out.println("Connecting ...");

		ZMQ.Socket socket = context.socket(ZMQ.DEALER);
		socket.connect("tcp://192.168.4.184:8120");

		for (int i = 0; i < 1; i++) {
			System.out.println(mc.toString());
			socket.send(mc.toByteArray());
			System.out.println("发送分券数据完成...");
			byte[] reply = socket.recv();

			MsgCarrier pos = MsgCarrier.parseFrom(reply);
			System.out.println("Received reply:\n["  + pos +"]" );
		}

		socket.close();
		context.term();
	}
	
	//获取主账号的分券记录
	static UserT0Stock GetAcct_UserT0Stock()
	{
        AccountBusiness user_asset = AccountBusiness.newBuilder()
        		.setAccount(acct)
        		.setSecuritytype(SecurityType.SECURITY_STOCK)
        		.setCurrbalance(500000.99)//当前余额
        		.setAvailable(500000.99)//可用余额
        		.setT0Available(500000.99) //首次划分金额
        		.setMktval(900000.995) //持仓市值
        		.setAsset(4000000.99)  //账户总资产
                .setTdgain(0.0)
                .setPositiongain(100.12)
                .setClosegain(0.0)
                .setPoscount(2)
                .setAlpha(0.618)
                .setBeta(0.382)
        		.build();
        PositionInfo user_pos1= PositionInfo.newBuilder()
        		.setSecurityid("600570")
        		.setExchange(TypeDef.Exchange.EX_SH)
        		.setAccount(acct)
        		.setPosition(3000)     //持仓数量
        		.setAvailable(3000)    //持仓可用
        		.setT0Available(2700)  //T0初始划分数量
        		.setPositionprice(36.78) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("HS恒生电子"))
        		.setStockaccount(stockaccountSH)
        		.build();
        PositionInfo user_pos2= PositionInfo.newBuilder()
        		.setSecurityid("000001")
        		.setExchange(TypeDef.Exchange.EX_SZ)
        		.setAccount(acct)
        		.setPosition(2000)     //持仓数量
        		.setAvailable(2000)    //持仓可用
        		.setT0Available(1800)   //分券数量
        		.setPositionprice(8.1) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("PA平安银行"))
        		.setStockaccount(stockaccountSZ)
        		.build();
        PositionInfo user_pos3= PositionInfo.newBuilder()
        		.setSecurityid("000031")
        		.setExchange(TypeDef.Exchange.EX_SZ)
        		.setAccount(acct)
        		.setPosition(500)     //持仓数量
        		.setAvailable(500)    //持仓可用
        		.setT0Available(500)   //分券数量
        		.setPositionprice(7) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("ZL中粮地产"))
        		.setStockaccount(stockaccountSZ)
        		.build();
        PositionInfo user_pos4= PositionInfo.newBuilder()
        		.setSecurityid("601211")
        		.setExchange(TypeDef.Exchange.EX_SH)
        		.setAccount(acct)
        		.setPosition(1000)     //持仓数量
        		.setAvailable(1000)    //持仓可用
        		.setT0Available(900)   //分券数量
        		.setPositionprice(12.1) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("GT国泰君安"))
        		.setStockaccount(stockaccountSH)
        		.build();
        
        //主账号
        UserT0Stock subdivdata = UserT0Stock.newBuilder()
        		.setUser("")
        		.setAsset(user_asset)
        		.addPositems(user_pos1)
        		.addPositems(user_pos2)
        		.addPositems(user_pos3)
        		.addPositems(user_pos4)
        		.build();
        return subdivdata;
	}
	//构造一条子账号分券记录
	static UserT0Stock GetUserT0Stock()
	{
		 //子账号分券
        AccountBusiness user_asset = AccountBusiness.newBuilder()
        		.setAccount(acct)
        		.setSecuritytype(SecurityType.SECURITY_STOCK)
        		.setCurrbalance(500000.99)//当前余额
        		.setAvailable(500000.99)//可用余额
        		.setT0Available(500000.99) //首次划分金额
        		.setMktval(0) //持仓市值（后续计算会更新）
        		.setAsset(400000.99)  //账户总资产
                .setTdgain(0.0)
                .setPositiongain(100.12)
                .setClosegain(0.0)
                .setPoscount(2)
                .setAlpha(0.618)
                .setBeta(0.382)
        		.build();
        PositionInfo user_pos1= PositionInfo.newBuilder()
        		.setSecurityid("600570")
        		.setExchange(TypeDef.Exchange.EX_SH)
        		.setAccount(acct)
        		.setPosition(1000)     //持仓数量
        		.setAvailable(1000)    //持仓可用
        		.setT0Available(1000)  //T0初始划分数量
        		.setPositionprice(50) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("HS恒生电子"))
        		.setStockaccount(stockaccountSH)
        		.build();
        PositionInfo user_pos2= PositionInfo.newBuilder()
        		.setSecurityid("000001")
        		.setExchange(TypeDef.Exchange.EX_SZ)
        		.setAccount(acct)
        		.setPosition(700)     //持仓数量
        		.setAvailable(700)    //持仓可用
        		.setT0Available(700)   //分券数量
        		.setPositionprice(13) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("PA平安银行"))
        		.setStockaccount(stockaccountSZ)
        		.build();
        PositionInfo user_pos3= PositionInfo.newBuilder()
        		.setSecurityid("000031")
        		.setExchange(TypeDef.Exchange.EX_SZ)
        		.setAccount(acct)
        		.setPosition(500)     //持仓数量
        		.setAvailable(500)    //持仓可用
        		.setT0Available(500)   //分券数量
        		.setPositionprice(7) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("ZL中粮地产"))
        		.setStockaccount(stockaccountSZ)
        		.build();
       PositionInfo user_pos4= PositionInfo.newBuilder()
        		.setSecurityid("601211")
        		.setExchange(TypeDef.Exchange.EX_SH)
        		.setAccount(acct)
        		.setPosition(300)     //持仓数量
        		.setAvailable(300)    //持仓可用
        		.setT0Available(300)   //分券数量
        		.setPositionprice(19) //持仓价
        		.setSecurityname(ByteString.copyFromUtf8("GT国泰君安"))
        		.setStockaccount(stockaccountSH)
        		.build();
        //一条分券数据
        UserT0Stock subdivdata = UserT0Stock.newBuilder()
        		.setUser("sunquan")
        		.setAsset(user_asset)
        		.addPositems(user_pos1)
        		.addPositems(user_pos2)
        		.addPositems(user_pos3)
        		.addPositems(user_pos4)
        		.build();
        return subdivdata;
	}
}
