package com.sunquan.zmqproto.spuborder;

import com.sunquan.zmqproto.TradingProxyClass.OrderInfo;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.BuySell;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.Exchange;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.OrderStatus;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.SecurityType;

public class OrderBean {
	
	
	private OrderInfo.Builder order;
	static String acct= "mingcemoni";
	public OrderBean()
	{
		order = OrderInfo.newBuilder()
				.setAccount(acct)
				.setSecuritytype(SecurityType.SECURITY_STOCK)
				.setBuyselldir(BuySell.BS_BUY)
				.setExchangeid(Exchange.EX_SH)
				.setTag("sunquan");
	}
	private long genUUID()
	{
		return  System.currentTimeMillis();
	}
	private OrderInfo NewOrder(String code,BuySell side, int volume,double price)
	{
		long orderid = genUUID();
		System.out.println("新订单..."+String.valueOf(orderid));
		Exchange exchtype = Exchange.EX_SH;
		if(code.startsWith("6"))exchtype=Exchange.EX_SH;
		else if (code.startsWith("0"))exchtype=Exchange.EX_SZ;
		else exchtype=Exchange.EX_SH;
		
		order.setOrderid(orderid)
		.setExchangeid(exchtype)
		.setSecurityid(code)
		.setBuyselldir(side)
		.setVolume(volume)
		.setPrice(price)
		.setTradevolume(0)
		.setTradeprice(0)
		.setOrderstatus(OrderStatus.PENDING);
		return order.build();
	}
	/**
	 * @param volume 本次成交量
	 * @param price
	 * @return
	 */
	private OrderInfo PartialFill(int volume,double price)
	{
		int tradevol= order.getTradevolume();
		int vol = order.getVolume();
		OrderStatus status=OrderStatus.ALLTRADED; 
		if(tradevol + volume <vol){
			tradevol = tradevol + volume;
			status = OrderStatus.PARTTRADED;
		}else if(tradevol + volume == vol)
		{
			tradevol = vol;
			status = OrderStatus.ALLTRADED;
		}else {
			System.err.println("已经全部成交!!");
			return null;
		}
		System.out.println("成交..."+String.valueOf(order.getOrderid()) );
		return order.setTradevolume(tradevol)
					.setTradeprice(price)
					.setOrderstatus(status)
					.build();
	}
	private OrderInfo Reject()
	{
		return order.setOrderstatus(OrderStatus.REJECTED).build();
	}
	private OrderInfo CancelAccept()
	{
		return order.setOrderstatus(OrderStatus.CANCELLED).build();
	}
	public OrderInfo Change(String type , String code,BuySell side, int volume, double price)
	{ 
		type = type.toLowerCase();
		if(type.startsWith("n")) //"neworder"
		{  
			return NewOrder(code,side,volume,price);
		}
		else if(type.startsWith("d")) //"deal" 
		{
			return PartialFill(volume, price);
		}else if(type.startsWith("c")) //"cancelaccept"
		{
			System.out.println("撤单成交...");
			return CancelAccept();
		}else if(type.startsWith("r")) //"reject"
		{
			System.out.println("废单...");
		    return Reject();
		}else
		{
			System.err.println("输入的命令有误！");
			return null;
		}
	}
}
