package com.sunquan.zmqproto.proxy;

import org.zeromq.ZMQ;

public class Pub_Server
{

    public static void main(String[] argv1) throws Exception
    {
        ZMQ.Context ctx =  ZMQ.context(1);
        if (ctx == null) {
            System.out.println("error in zmq_init:");
            return;
        }
        ZMQ.Socket skt = ctx.socket(ZMQ.PUB);
        skt.bind("tcp://127.0.0.1:8046");
        System.out.println("pub...");
        boolean brun = true;
     
        String [] topic = new String[] {"Mr.Bob", "Mrs.Alice", "Mr.BobAlice"};
        while(brun) 
        {
            Thread.sleep(2000);
            for(String onemsg : topic) {
              System.out.println("pub:[" + onemsg + "]");
              skt.sendMore(onemsg);  
              skt.send("msg_content");  
            }
        }
        skt.close();
        ctx.close();
        ctx.term();
    }
}
