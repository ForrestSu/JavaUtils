/*
    Copyright (c) 2007-2014 Contributors as noted in the AUTHORS file

    This file is part of 0MQ.

    0MQ is free software; you can redistribute it and/or modify it under
    the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    0MQ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sunquan.zmqproto.inproc;

import java.lang.management.ManagementFactory;

import org.zeromq.ZMQ;

public class InprocRouter
{
    private InprocRouter()
    {
    }

    static class Worker implements Runnable
    {
        private ZMQ.Context ctx;
        private int roundtripCount;
        Worker(ZMQ.Context ctx, int roundtripCount)
        {
            this.ctx = ctx;
            this.roundtripCount = roundtripCount;
        }

        @Override
        public void run()
        {
            ZMQ.Socket skt = ctx.socket(ZMQ.DEALER);
            if (skt == null) {
                printf("error in zmq_socket: %s\n");
                exit(1);
            }

            boolean rc =  skt.connect("inproc://lat_test");
            if (!rc) {
                printf("error in zmq_connect: %s\n");
                exit(1);
            }

            for (int i = 0; i != roundtripCount; i++) {
                
                String msgdata = "abcdefghij"+i;
                boolean r = skt.send(msgdata, ZMQ.DONTWAIT);
                if (r == false) {
                    printf("error in zmq_sendmsg: %s\n");
                    exit(1);
                }
                
              /*  msg = ZMQ.recvMsg(s, 0);
                if (msg == null) {
                    printf("error in zmq_recvmsg: %s\n");
                    exit(1);
                }
                printf("recv_inner==> " + new String(msg.buf().array()));
                msg.put('1');*/
                
            }

            skt.close();
        }

        private void exit(int i)
        {
            // TODO Auto-generated method stub
        }
    }

    public static final void getProcessID() {
        System.out.println( ManagementFactory.getRuntimeMXBean().getName());
    }
    
    public static void main(String[] argv1) throws Exception
    {
        String[] argv = {"10", "10"};
      
        if (argv.length != 2) {
            printf("usage: inproc_lat <message-size> <roundtrip-count>\n");
            return;
        }

        int messageSize = Integer.valueOf(argv [0]);
        int roundtripCount = Integer.valueOf(argv [1]);

        
        String sendmsg = "abcdefghij";
        messageSize = sendmsg.length();
        
        
        ZMQ.Context ctx =  ZMQ.context(1);
        if (ctx == null) {
            printf("error in zmq_init:");
            return;
        }

        ZMQ.Socket skt = ctx.socket(ZMQ.ROUTER);
        if (skt == null) {
            printf("error in zmq_socket: ");
            return;
        }

        boolean rc = skt.bind("inproc://lat_test");
        if (!rc) {
            printf("error in zmq_bind: ");
            return;
        }
        getProcessID();

        Thread localThread = new Thread(new Worker(ctx, roundtripCount));
        localThread.start();


        printf("message size: %d [B]\n", messageSize);
        printf("roundtrip count: %d\n", roundtripCount);


        long begin = System.currentTimeMillis();
        
        for (int i = 0; i != roundtripCount; i++) {
          
            byte[] data = skt.recv();
            if (data == null) {
                printf("error in zmq_recvmsg: %s\n");
                return;
            }
            
            printf("接收到数据：" + new String(data, "utf-8"));
            
            boolean r= skt.send("abc", ZMQ.DONTWAIT);
            if (r == false) {
                printf("error in zmq_sendmsg: %s\n");
                return;
            }
            //Thread.sleep(1000);
        }

        long elapsed = (System.currentTimeMillis() - begin)*1000;

        double latency = (double) elapsed / (roundtripCount * 2);

        localThread.join();

        printf("average latency: %.3f [us]\n", latency);

       // ZMQ.zmq_close(s);

       // ZMQ.zmq_term(ctx);
    }
 

    private static void printf(String string)
    {
        System.out.println(string);
    }

    private static void printf(String string, Object ... args)
    {
        System.out.println(String.format(string, args));
    }
}
