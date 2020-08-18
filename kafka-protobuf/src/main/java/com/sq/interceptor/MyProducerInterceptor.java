package com.sq.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MyProducerInterceptor implements ProducerInterceptor<Integer, String> {

    /**
     * 在发送之前
     */
    @Override
    public ProducerRecord<Integer, String> onSend(ProducerRecord<Integer, String> record) {
        System.out.println("onSend:" +  record.key());
        return record;
    }

    /**
     * 发送消息后,收到broker端的确认，但是在Ack之前
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
