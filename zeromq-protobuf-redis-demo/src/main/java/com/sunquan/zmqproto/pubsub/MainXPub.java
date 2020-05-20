package com.sunquan.zmqproto.pubsub;

import java.util.Scanner;

import org.zeromq.ZMQ;

import com.sunquan.zmqproto.DemoProtos;
import com.sunquan.zmqproto.DemoProtos.Alert;

public class MainXPub extends Thread {

	private ZMQ.Context context;
	private ZMQ.Socket publisher;
	private boolean bRunning;

	public MainXPub() {
		bRunning = true;
		context = ZMQ.context(1);
		publisher = context.socket(ZMQ.XPUB);
		publisher.connect("tcp://127.0.0.1:5556");
	}

    public void Work(int id) {
        // publisher.bind("tcp://*:5556");

        byte[] data = new byte[8192];
        for (int i = 0; i < data.length; ++i)
            data[i] = (byte) i;

        publisher.sendMore(data);
        publisher.send(data);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	public void Close(int id) {
		bRunning = false;
		if (publisher != null) {
			System.out.println("before closing"+id+"...");
			//publisher.disconnect(addr)
			publisher.close();
			publisher = null;
			System.out.println("closed." + id);
		}
		if (context != null)
			context.term();
	}

	@Override
	public void run() {
		int id = (int) (Math.random() * 1000 + 1000);
		while (bRunning) {
			Work(id);
		}
	}

	public static void main(String[] args) {
		// Prepare our context and publisher
		/*
		 * MainXPub[] worker =new MainXPub[5]; for(int i=0;i< 5;++i) {
		 * worker[i].start(); }
		 */
		MainXPub worker1 = new MainXPub();
		worker1.start();
		MainXPub worker2 = new MainXPub();
		worker2.start();
		System.out.println("worker3 ...");
		MainXPub worker3 = new MainXPub();
		worker3.start();
		MainXPub worker4 = new MainXPub();
		worker4.start();
		System.out.println("worker4 ...");
		Scanner scan = new Scanner(System.in);
		int flag=1;
		while(flag!=0)
		{
			System.out.print("please input:");
			flag = scan.nextInt();
			
			if(flag >0 && flag <5)
			{
				if(flag==1)worker1.Close(flag);
				if(flag==2)worker2.Close(flag);
				if(flag==3)worker3.Close(flag);
				if(flag==4)worker4.Close(flag);
			}
			
		}
		scan.close();
	}
}
