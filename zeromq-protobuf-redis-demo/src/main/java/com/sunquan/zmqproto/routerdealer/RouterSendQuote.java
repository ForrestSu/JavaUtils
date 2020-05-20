package com.sunquan.zmqproto.routerdealer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sunquan.zmqproto.MsgCarrierClass;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier;
import com.sunquan.zmqproto.MsgCarrierClass.MsgCarrier.MsgType;
import com.sunquan.zmqproto.QuoteClass.QueryQuoteRep;
import com.sunquan.zmqproto.QuoteClass.QueryQuoteRep.Builder;
import com.sunquan.zmqproto.QuoteClass.SnapShot;
import com.sunquan.zmqproto.TypeDefClass.TypeDef.Exchange;

public class RouterSendQuote {
	
	
	
	
	public static QueryQuoteRep  DealOneline(String oneline,int ilinenumber)
	{
		String[] arr = oneline.split(",");
		if(arr.length == 53)
		{
			 SnapShot.Builder  sst= SnapShot.newBuilder()
					 .setDate( Integer.valueOf(arr[2]))
					 .setTime( Integer.valueOf(arr[3]) )
					 .setExchange(Exchange.EX_SH)
					 .setCode(arr[1])
					 .setLastprice( Double.valueOf(arr[5]))
					 .setPrevclose( Double.valueOf(arr[6]))
					 .setOpen(  Double.valueOf(arr[7]))
					 .setHigh(  Double.valueOf(arr[8]))
					 //.setLow(  Double.valueOf(arr[8]))
					 .setVolume( Long.valueOf(arr[9]) )
					 .setValue( Double.valueOf(arr[10]) )
					 .setHighlimited( Double.valueOf(arr[11]) )
					 .setLowlimited(Double.valueOf(arr[12]));
			 for(int i=13;i<23;i++)
				 sst.addBidprices( Double.valueOf(arr[i]));
			 for(int i=23;i<33;i++)
				 sst.addBidvolumes( Long.valueOf(arr[i]));
			  for(int i=33;i<43;i++)
				 sst.addAskprices( Double.valueOf(arr[i]));
			 for(int i=43;i<53;i++)
				 sst.addAskvolumes( Long.valueOf(arr[i]));
			 
			QueryQuoteRep rep =QueryQuoteRep.newBuilder()
					.setReqid(100)
					.setStatus(0)
					.setErrorno(0)
					.setErrormsg("")
					.addSnapshots(sst.build())
					.build();
			//
		    return rep;
		}else
		{
			System.err.println("Line  "+ ilinenumber +"error! size=" +arr.length );
			return null;
		}
	}
	
	public static List<QueryQuoteRep> ReadQuote()
	{
		List<QueryQuoteRep> quote = new ArrayList<QueryQuoteRep>();
		String myfile ="C:\\Users\\toptrade\\Desktop\\data.csv";
		try {
                String encoding="utf-8";
                File file=new File(myfile);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = "" ;
                    int ilinenumber = 0;
                    while((lineTxt = bufferedReader.readLine()) != null)
                    {
                    	ilinenumber ++;
                    	lineTxt =lineTxt.trim();
                    	if( (ilinenumber>1) && lineTxt.length()>10)
                    	{
                    		QueryQuoteRep rep= DealOneline(lineTxt,ilinenumber);
                    		if(rep!=null)
                    			quote.add(rep);
                    	}
                    	else
                    		System.err.println("第 "+ ilinenumber +"行数据不处理！");
                    }
                    
                   
                    
                    read.close();
        }else{
            System.out.println("找不到指定的文件"+myfile);
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return quote;
	}
	public static void main (String[] args) throws InvalidProtocolBufferException, InterruptedException, UnsupportedEncodingException  {
		
		List<QueryQuoteRep> quote = ReadQuote();
		
		//System.out.println(quote.get(0).toString() );
		System.out.println("共加载行情 "+quote.size()+" 条");
		
                  
        //  Prepare our context and publisher
        ZMQ.Context context = ZMQ.context(1);
        Socket socket = context.socket(zmq.ZMQ.ZMQ_ROUTER);
        socket.bind ("tcp://*:8045");
        
       
        socket.close ();
        context.term ();
    }
}
