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


import java.lang.management.ManagementFactory;
import java.util.Scanner;

import org.zeromq.ZMQ;

public class PubProxy_Inproc
{
    
    static private String G_Bind_Iproc = "inproc://lat_test"; 
    static private String G_Server_Address = "tcp://127.0.0.1:8046";

    static class Worker implements Runnable
    {
        private ZMQ.Socket frontend;
        private ZMQ.Socket backend;
        
        Worker(ZMQ.Context ctx)
        {
            frontend = ctx.socket(ZMQ.XPUB); //enable recv sub msg
            backend = ctx.socket(ZMQ.XSUB);
        }

        @Override
        public void run()
        {
            if (frontend == null) {
                System.out.println("error in zmq_socket: ");
                return;
            }

            boolean rc1 = frontend.bind(G_Bind_Iproc);
            if (!rc1) {
                System.out.println("error in zmq_bind: ");
                return;
            }
        
            if (backend == null) {
                System.out.println("error in zmq_socket: %s\n");
                exit(1);
                return ;
            }
   
            boolean rc = backend.connect(G_Server_Address);
            if (!rc) {
                System.out.println("error in zmq_connect: %s\n");
                exit(1);
                return ;
            }
            System.out.println("begin..!");
            
            //wait
            boolean bret = ZMQ.proxy(frontend, backend, null, null);
            System.out.println("ZMQ.proxy() return: " + bret);
            frontend.close();
            backend.close();
        }

        public void exit(int i)
        {
            
            System.out.println("exit!");
        }
    }
    
    static class Client implements Runnable
    {
        private String topic;
        private ZMQ.Socket m_socket;
        private boolean brunning;
        
        Client(ZMQ.Context ctx,  String msg)
        {
            this.topic = msg;
            m_socket = ctx.socket(ZMQ.SUB);
            m_socket.setReceiveTimeOut(5000);
            brunning = true;
        }

        @Override
        public void run()
        {
            boolean rc = m_socket.connect(G_Bind_Iproc);
            if (!rc) {
                System.out.println("error in zmq_connect: %s\n");
                return ;
            }
            
            System.out.println("conn " + rc );
            
            m_socket.subscribe(topic);
            do {
               String topicstr = m_socket.recvStr();
               boolean more = m_socket.hasReceiveMore();
               
               System.out.println("tid:"+ Thread.currentThread().getName() +", topic <==" + topicstr);
               if(more) {
                   byte[] data = m_socket.recv();
                   //System.out.println("tid: "+ Thread.currentThread().getName());
               }
            }while(brunning);
            System.out.println("tid: "+ Thread.currentThread().getName() +" exit.");
            m_socket.close();
        }
        public void stop() {
            brunning = false;
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
        
        Client client1 = new Client(ctx, "Mr.Bob");
        Thread t1 = new Thread(client1);
        
        Client client2 = new Client(ctx, "Mrs.Alice");
        Thread t2 = new Thread(client2);
        
        Client client3 = new Client(ctx, "Mr");
        Thread t3 = new Thread(client3);
        
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            
            int a = in.nextInt();
            if(a == 1){
               System.out.println("start client!");
               t1.start();
               t2.start();
               t3.start();
            }else if(a ==9) {
                //wk.exit(0);
                client1.stop();
                client2.stop();
                client3.stop();
                System.out.println("exit1");
                break;
            }
        }
        System.out.println("exit2");
        in.close();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("exit3");
        
        
        System.out.println("average latency: ok!\n");
        wk.exit(0);
        ctx.close();//zmq_proxy will return 
        
        localThread.join();
       
        ctx.term();
        System.out.println("ok!\n");
    }
 
 
}
