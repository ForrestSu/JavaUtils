package com.sq.demo.examples;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class AckCallback implements Callback {

    private final long startNs;
    private final long key;
    private final String message;

    public AckCallback(long key, String message) {
        this.startNs = System.nanoTime();
        this.key = key;
        this.message = message;
    }

    private long getElapseTime() {
        return System.nanoTime() - startNs;
    }

    @Override
    public void onCompletion(RecordMetadata meta, Exception e) {
        double elapsedMs = getElapseTime() / 1000000.0;
        if (e != null) {
            e.printStackTrace();
            return;
        }
        // broker报告-消息已提交
        String msg = " Publish message(" + key + ", " + message + ") sent to partition(" + meta.partition() +
            "), " + "offset(" + meta.offset() + ") in " + elapsedMs + " ms";
        System.out.println("tid: " + Thread.currentThread().getId() + msg);
    }
}
