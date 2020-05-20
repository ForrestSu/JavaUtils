package com.sunquan.zmqproto.reqrepbroker;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Broker {
	public static void main (String[] args) {
        //  Prepare our context and sockets
        Context context = ZMQ.context(1);

        Socket frontend = context.socket(ZMQ.ROUTER);
        Socket backend  = context.socket(ZMQ.DEALER);
        frontend.bind("tcp://*:5559");
        backend.bind("tcp://*:5560");

        System.out.println("launch and connect broker.");

        //  Initialize poll set. //old api: new ZMQ.Poller(context,2);
        ZMQ.Poller items = context.poller(2); 
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);

        boolean more = false;
        byte[] message;

        //  Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {            
            //  poll and memorize multipart detection
            items.poll();

            if (items.pollin(0)) {
                while (true) {
                    // receive message
                    message = frontend.recv(0);
                    more = frontend.hasReceiveMore();

                    // Broker it
                    backend.send(message, more ? ZMQ.SNDMORE : 0);
                    if(!more){
                        break;
                    }
                }
            }
            if (items.pollin(1)) {
                while (true) {
                    // receive message
                    message = backend.recv(0);
                    more = backend.hasReceiveMore();
                    // Broker it
                    frontend.send(message,  more ? ZMQ.SNDMORE : 0);
                    if(!more){
                        break;
                    }
                }
            }
        }
        //  We never get here but clean up anyhow
        frontend.close();
        backend.close();
        context.term();
    }
}
