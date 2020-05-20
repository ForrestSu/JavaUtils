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

package com.sunquan.zmqproto.proxy;


import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.util.Scanner;

import org.zeromq.ZMQ;

public class RouteProxy_Inproc
{
    
    static private String SADDRESS = "inproc://lat_test" ; 

    static class Worker implements Runnable
    {
        private ZMQ.Context ctx;
        private ZMQ.Socket frontend;
        private ZMQ.Socket backend;
        
        Worker(ZMQ.Context ctx)
        {
            this.ctx = ctx;
            frontend = ctx.socket(ZMQ.ROUTER);
            backend = ctx.socket(ZMQ.DEALER);
        }

        @Override
        public void run()
        {
            if (frontend == null) {
                System.out.println("error in zmq_socket: ");
                return;
            }

            boolean rc1 = frontend.bind(SADDRESS);
            if (!rc1) {
                System.out.println("error in zmq_bind: ");
                return;
            }
        
            if (backend == null) {
                System.out.println("error in zmq_socket: %s\n");
                exit(1);
            }
   
            boolean rc = backend.connect("tcp://127.0.0.1:8046");
            if (!rc) {
                System.out.println("error in zmq_connect: %s\n");
                exit(1);
            }
            System.out.println("begin..!");
            
            ZMQ.proxy(frontend, backend, null);
             
            System.out.println("OK!");
        }

        public void exit(int i)
        {
            System.out.println("exit...");
            frontend.close();
            backend.close();
            ctx.close();
            ctx.term();
            System.out.println("exit ok.");
        }
    }
    
    static class Client implements Runnable
    {
        private ZMQ.Context ctx;
        private String msg;
        private ZMQ.Socket m_socket;
        
        Client(ZMQ.Context ctx,  String msg)
        {
            this.ctx = ctx;
            this.msg = msg;
            m_socket = ctx.socket(ZMQ.DEALER);
        }

        @Override
        public void run()
        {
            boolean rc = m_socket.connect(SADDRESS);
            if (!rc) {
                System.out.println("error in zmq_connect: %s\n");
                return ;
            }
            m_socket.send(msg);
            do {
               byte[] data = m_socket.recv();
               try {
                    System.out.println("tid:"+ Thread.currentThread().getName() +", Recv <==" + new String(data, "utf-8"));
               } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
               }
            }while(true);
        }
    }

    public static final void getProcessID() {
        System.out.println( ManagementFactory.getRuntimeMXBean().getName());
    }
    
    public static void main(String[] argv1) throws Exception
    {
        
        ZMQ.Context ctx =  ZMQ.context(1);
        if (ctx == null) {
            System.out.println("error in zmq_init:");
            return;
        }

        getProcessID();

        Worker wk= new Worker(ctx);
        Thread localThread = new Thread(wk);
        localThread.start();
        
        Thread client1 = new Thread(new Client(ctx, "sunquan"));
        Thread client2 = new Thread(new Client(ctx, "lisi"));
        
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            
            int a = in.nextInt();
            if(a == 1){
               System.out.println("start client!");
               client1.start();
               client2.start();
            }else if(a ==9) {
                wk.exit(0);
            }
        }
        
        in.close();
        client1.join();
        client2.join();
        localThread.join();

        System.out.println("average latency: ok!\n");

        ctx.close();
        ctx.term();
    }
 
 
}
