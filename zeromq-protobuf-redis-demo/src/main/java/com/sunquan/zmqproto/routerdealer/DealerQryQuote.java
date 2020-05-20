package com.sunquan.zmqproto.routerdealer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.QuoteClass.QueryQuote;
import com.sunquan.zmqproto.QuoteClass.QueryQuoteRep;
import com.sunquan.zmqproto.util.MyFileWriter;

public class DealerQryQuote {
	
	
	static HashMap<String, Integer> map = new HashMap<>();
	static String outFileString="C:\\Users\\toptrade\\Desktop\\result.txt";
	static MyFileWriter fw = new MyFileWriter(outFileString);
	
	private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>  
    {  
        public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)  
        {  
            return n.getValue()-m.getValue();  
        }  
    }  
	public static void main(String[] args) throws InvalidProtocolBufferException, UnsupportedEncodingException {

		 //String stockcodes="000040;000301;000404;000517;000715;002076";
		String stockcodes = "";
		
		
		QueryQuote qp = QueryQuote.newBuilder()
				.setReqid(System.currentTimeMillis() % 1000000)
				.setRequest("snapshot")
				.setExchanges("CF")
				.setCodes(stockcodes)
				.setDate(20170707)
				.setStarttime(91500000)
				.setEndtime( 100000000)
				.build();
		MsgCarrier mc = MsgCarrierClass.MsgCarrier.newBuilder()
				.setMsgid(1)
				.setType(MsgType.QueryQuote)
				.setMessage(qp.toByteString())
				.build();
		

		ZMQ.Context context = ZMQ.context(1);
		System.out.println("Connecting ...");

		ZMQ.Socket socket = context.socket(ZMQ.DEALER);
		socket.connect("tcp://192.168.4.21:8045");

		MsgCarrier recv;

		int count = 1;
		long starttime= System.currentTimeMillis();
		fw.writeln("开始时间:"+starttime);
		
		socket.sendMore("\0");
		//System.out.println(mc.toString());
		socket.send(mc.toByteArray());
		System.out.println("同步接收数据...");
		while (count > 0) {
			  byte[] reply = socket.recv();
			//System.out.println("收到数据1");
			reply = socket.recv();
			System.out.println("收到数据2");
			recv = MsgCarrier.parseFrom(reply);

			QueryQuoteRep pos = QueryQuoteRep.parseFrom(recv.getMessage());
			// 查询结束 返回END
			if (pos.getErrorno() != 0 || pos.getErrormsg().toUpperCase().equals("END")) {
				System.err.println(pos.getErrorno()+ "==="+pos.getErrormsg());
				System.err.println("Total query HQ count = " + count);
				fw.writeln("Total query HQ count = " + count);
				break;
			} else {
				for (int i = 0; i < pos.getSnapshotsCount(); i++) {
					String code = pos.getSnapshots(i).getCode();

					Integer value = map.get(code);
					if (value == null) {
						map.put(code, 1); 
					} else
						map.put(code, value+1); 
				}

		  System.out.println("Received reply "+count + ":\n[" +pos.toString() + "]" );
				count++;
			}
		}
		long endtime=System.currentTimeMillis();
		fw.writeln("结束时间:"+endtime);
		fw.writeln( "Costms="+ (endtime - starttime));
		Show();
		
		System.out.println("OK");
		socket.close();
		context.term();
	}
	
	public static void Show(){
		
		List<Map.Entry<String,Integer>> list=new ArrayList<>();  
        list.addAll(map.entrySet());  
        DealerQryQuote.ValueComparator vc=new ValueComparator();  
        Collections.sort(list,vc);  
        int cnter=0;
        for(Iterator<Map.Entry<String,Integer>> it=list.iterator();it.hasNext();)  
        {  
            fw.writeln(it.next().toString());  
            cnter ++;
            //if(cnter>99)break;
        }  
         fw.writeln("STKCOUNT="+cnter); 
	}
}
