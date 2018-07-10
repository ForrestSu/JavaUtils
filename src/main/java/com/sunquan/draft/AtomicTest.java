package com.sunquan.draft;

import java.sql.Date;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicTest implements Runnable{

    private final AtomicReference<Thread> thread = new AtomicReference<>();

    @Override
    public void run() {

        if(!thread.compareAndSet(null, Thread.currentThread()) ){
            System.err.println("thread has started!  exit");
            return;
        }
        System.out.println("normal start!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         System.out.println("normal exit!");
    }

    public Thread thread()
    {
        return thread.get();
    }

    public static void main(String[] args) {

        long time= System.currentTimeMillis();
        Date dt= new Date(time);
        System.out.println(dt.toLocaleString());

        //1529459537290
        //1529459501651
        AtomicTest test = new AtomicTest();
        Thread th = new Thread(test);
        th.start();
        // th.start();

        System.out.println(test.thread());
    }



}