package com.sunquan.ipc;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestSingal {

    public static void main(String[] args) {

        final AtomicBoolean running = new AtomicBoolean(true);
        SigInt.register(() -> {
            System.out.println("recv sig int");
            running.set(false);
        });

        System.out.println("start");
        while (running.get()) {
            try {
                Thread.sleep(1000);
                System.out.println("running... ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("exit!");
    }
}
