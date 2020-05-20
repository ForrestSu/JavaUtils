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

import zmq.Ctx;
import zmq.Msg;
import zmq.SocketBase;
import zmq.ZMQ;

public class InprocReqResp
{
    private InprocReqResp()
    {
    }

    static class Worker implements Runnable
    {
        private Ctx ctx;
        private int roundtripCount;
        Worker(Ctx ctx, int roundtripCount)
        {
            this.ctx = ctx;
            this.roundtripCount = roundtripCount;
        }

        @Override
        public void run()
        {
            SocketBase s = ZMQ.socket(ctx, ZMQ.ZMQ_REP);
            if (s == null) {
                printf("error in zmq_socket: %s\n");
                exit(1);
            }

            boolean rc = ZMQ.connect(s, "inproc://lat_test");
            if (!rc) {
                printf("error in zmq_connect: %s\n");
                exit(1);
            }

            Msg msg;

            for (int i = 0; i != roundtripCount; i++) {
                msg = ZMQ.recvMsg(s, 0);
                if (msg == null) {
                    printf("error in zmq_recvmsg: %s\n");
                    exit(1);
                }
                printf("recv_inner==> " + new String(msg.buf().array()));
                msg.put('1');
                int r = ZMQ.sendMsg(s, msg, 0);
                if (r < 0) {
                    printf("error in zmq_sendmsg: %s\n");
                    exit(1);
                }
            }

            ZMQ.close(s);
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

        int messageSize = atoi(argv [0]);
        int roundtripCount = atoi(argv [1]);

        
        String sendmsg = "abcdefghij";
        messageSize = sendmsg.length();
        
        
        Ctx ctx = ZMQ.init(1);
        if (ctx == null) {
            printf("error in zmq_init:");
            return;
        }

        SocketBase s = ZMQ.socket(ctx, ZMQ.ZMQ_REQ);
        if (s == null) {
            printf("error in zmq_socket: ");
            return;
        }

        boolean rc = ZMQ.bind(s, "inproc://lat_test");
        if (!rc) {
            printf("error in zmq_bind: ");
            return;
        }
       getProcessID();

        Thread localThread = new Thread(new Worker(ctx, roundtripCount));
        localThread.start();

        Msg smsg = new Msg(sendmsg.getBytes());

        printf("message size: %d [B]\n", messageSize);
        printf("roundtrip count: %d\n", roundtripCount);

        long watch = ZMQ.startStopwatch();

        for (int i = 0; i != roundtripCount; i++) {
            int r = ZMQ.sendMsg(s, smsg, 0);
            if (r < 0) {
                printf("error in zmq_sendmsg: %s\n");
                return;
            }
            Msg msg = ZMQ.recvMsg(s, 0);
            if (msg == null) {
                printf("error in zmq_recvmsg: %s\n");
                return;
            }
            if (ZMQ.msgSize(msg) != messageSize) {
                printf("message of incorrect size received\n");
                return;
            }
            
            printf("接收到数据：" + new String(msg.buf().array()));
            Thread.sleep(1000);
        }

        long elapsed = ZMQ.stopStopwatch(watch);

        double latency = (double) elapsed / (roundtripCount * 2);

        localThread.join();

        printf("average latency: %.3f [us]\n", latency);

       // ZMQ.zmq_close(s);

       // ZMQ.zmq_term(ctx);
    }

    private static int atoi(String string)
    {
        return Integer.parseInt(string);
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
