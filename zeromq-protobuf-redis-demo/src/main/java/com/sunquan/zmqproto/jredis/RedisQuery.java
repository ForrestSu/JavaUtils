package com.sunquan.zmqproto.jredis;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.T0tradingClass.AcctT0Stock;
import com.sunquan.zmqproto.TradingProxyClass.OrderInfo;
import com.sunquan.zmqproto.TradingProxyClass.TradeInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisQuery {

	private Jedis jedis;

	public boolean ConnRedis(String IP, int port) {
		jedis = new Jedis(IP, port);
		try {
			jedis.connect();
			System.out.println("connect ok!");
			return true;
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void Close() {
		jedis.quit();
	}

	public void GetHashSet(String qrykey) {
		try {
			byte[] qrybyte = qrykey.getBytes("UTF-8");

			Map<byte[], byte[]> result = jedis.hgetAll(qrybyte);
			int i = 0;
			for (Map.Entry<byte[], byte[]> entry : result.entrySet()) {
				String skey = new String(entry.getKey(), "UTF-8");
				// byte[] value = entry.getValue();

				String value = new String(entry.getValue(), "UTF-8");
				
			 if(skey.startsWith("sn1802"))
			    System.out.println("i==" + i + " ,Key = " + skey + ", Value = [" + value + "]");

			    //jedis.hdel(qrykey, skey);
				i++;

				// long ret= jedis.hset(newkey, skey, value);
				// AcctT0Stock obj =
				// AcctT0Stock.parseFrom(ByteString.copyFrom(value));
				// TradeInfo obj =
				// TradeInfo.parseFrom(ByteString.copyFrom(value));

				// System.out.println("Received reply:[\n" + obj.toString() +
				// "]");
			}
			/*String Filed= "ru1801:40";
			String value = "20170907:0.11268015:0.01820198:-0.01658373:-0.02461062:10.81744593:-2.72905535:-0.17191310:-0.05381138:0.52591227:1.74655961:1.48629818:144506";
			jedis.hset(qrykey, Filed, value);
			Filed= "ru1801:60";
			value = "20170907:0.11143432:0.01576121:-0.01780670:-0.02713580:11.21384183:-2.47086476:-0.04636035:0.06880904:0.45923193:1.78216909:1.51393912:144511";
			jedis.hset(qrykey, Filed, value);
			
			Filed= "ru1801:120";
			value ="20170907:0.10942351:0.01594880:-0.01688494:-0.02652986:11.46120858:-2.29207270:0.13943733:0.42180092:0.59379239:1.86229021:1.60078998:144517";
			jedis.hset(qrykey, Filed, value);
			
			Filed= "ru1801:240";
			value = "20170907:0.09511352:0.00940638:-0.02527023:-0.03667107:11.64125431:-2.02785712:0.44872834:0.94275428:0.85469331:1.98999843:1.74817675:144523";
			jedis.hset(qrykey, Filed, value);
			*/
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String IP = "192.168.4.193";
		int port = 6380;

		String qrykey ="stock_daily_preclose";

		RedisQuery qry = new RedisQuery();
		if (qry.ConnRedis(IP, port)) {
			qry.GetHashSet(qrykey);
		}
		qry.Close();
	}

	
}
