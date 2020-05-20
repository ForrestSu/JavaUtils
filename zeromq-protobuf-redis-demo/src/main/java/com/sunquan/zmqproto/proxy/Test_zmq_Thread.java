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

import org.zeromq.ZMQ;

public class Test_zmq_Thread
{
    
    public static final void getProcessID() {
        System.out.println( ManagementFactory.getRuntimeMXBean().getName());
    }
    
    //测试 zeromq 每增加一个socket, 线程数是否增加
    public static void main(String[] argv1) throws Exception
    {
        getProcessID();
        
        String SADDRESS = "tcp://192.168.4.204:8046"; 
        
        ZMQ.Context ctx =  ZMQ.context(1);
        if (ctx == null) {
            System.out.println("error in zmq_init:");
            return;
        }
        
        ZMQ.Socket  frontend = ctx.socket(ZMQ.ROUTER); //enable recv sub msg
        ZMQ.Socket  backend = ctx.socket(ZMQ.DEALER);
        
        frontend.bind("tcp://127.0.0.1:8046");
        backend.connect(SADDRESS);
        
        ZMQ.Socket  backend1 = ctx.socket(ZMQ.DEALER); //enable recv sub msg
        ZMQ.Socket  backend2 = ctx.socket(ZMQ.DEALER);
        
        
        backend1.connect(SADDRESS);
        backend2.connect(SADDRESS);

        //close socket
        backend1.close();
        backend2.close();
        frontend.close();
        backend.close();
        
        ctx.close();
        System.out.println("ok!\n");
    }
 
 
}
