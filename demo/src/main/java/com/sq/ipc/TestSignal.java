package com.sq.ipc;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestSignal {

    private static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        // System.out.println(runtimeMXBean.getName());
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
    }

    public static void main(String[] args) {

        final AtomicBoolean running = new AtomicBoolean(true);
        System.out.println("pid: " + getProcessID());
        SigTerm.register(() -> {
            System.out.println("recv sig term");
            running.set(false);
        });
        SigInt.register(() -> {
            System.out.println("recv sig int");
            running.set(false);
        });


        System.out.println("start");
        while (running.get()) {
            try {
                System.out.println("running... ");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("exit!");
    }
}

