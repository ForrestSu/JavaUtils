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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zeromq.ZMQ;

public class StreamZmq
{

    public static void main(String[] argv1) throws Exception
    {
        ZMQ.Context ctx =  ZMQ.context(1);
        if (ctx == null) {
            System.out.println("error in zmq_init:");
            return;
        }
        ZMQ.Socket skt = ctx.socket(ZMQ.STREAM);
        skt.setConnectRid("connectRid");
        skt.setLinger(0);
        skt.bind("tcp://127.0.0.1:8046");
        System.out.println("recv...");
        boolean brun = true;
     
        while(brun) 
        {
            List<byte[]> frames = new ArrayList<>();
            boolean more = false;
            //recv from dealer
            do {
                byte[] data = skt.recv();
                frames.add(data);
                more = skt.hasReceiveMore();
                System.out.println("more: " + more +", size = "+ data.length +", data = ["+ new String(data,"utf-8" )+ "]");
            }while(more);
            System.out.println("frame size:" + frames.size());
            
            // send response
            
            String respmsg = new String(frames.get(frames.size()-1), "utf-8");
            
            System.out.println("Server:[" + respmsg + "]");
            //skt.send(respmsg);
        }
 
        skt.close();
        ctx.close();
        ctx.term();
    }
 
 
}
