package com.sq.draft;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sq.utils.DateUtils;

public class TimerDemo {

    public static void main(String[] args) {
        /*
         * Timer timer = new Timer(); 
         * Task task = new Task();
         * timer.schedule(task, new Date(), 1000);
         */
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        final long timeSpanSec = 5;
        final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            EmbeddedRecording.flag = false;
            System.out.println("resubmit worker...");
            System.out.println("isShutdown: " + executor.isShutdown() + " isTerminated: " + executor.isTerminated());
            executor.submit(() -> {
                try (EmbeddedRecording record = new EmbeddedRecording();) {
                    record.makeRecording("zhangsan" + System.currentTimeMillis());
                    System.out.println("work done!");
                }
            });
        }, timeSpanSec, 5, TimeUnit.SECONDS);

        executor.submit(() -> {
            System.out.println("[first] submit record work...");
            try (EmbeddedRecording record = new EmbeddedRecording();) {
                record.makeRecording("sunquan");
                System.out.println("[first] work done!");
            }

        });

        System.out.println("thread_id is " + Thread.currentThread().getId());
    }
}

class EmbeddedRecording implements AutoCloseable {
    public static boolean flag = true;

    public void makeRecording(String tag) {
        flag = true;
        while (flag) {
            try {
                System.out.println(tag + " ==> " + Thread.currentThread().getId() + "  " + DateUtils.CurTime());
                Thread.sleep(3000);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("error!");
                break;
            }
        }
    }

    @Override
    public void close() {
        System.out.println("close...");
    }
}
/*
 * class Task extends TimerTask {
 *    @Override 
 *    public void run() {
 *       System.out.println("Do work..."); 
 *    } 
 * }
 */
