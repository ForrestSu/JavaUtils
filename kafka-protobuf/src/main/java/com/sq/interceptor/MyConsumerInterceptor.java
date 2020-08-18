package com.sq.interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class MyConsumerInterceptor implements ConsumerInterceptor<Integer, byte[]> {

    /**
     * 在消费之前
     * @param records
     * @return
     */
    @Override
    public ConsumerRecords<Integer, byte[]> onConsume(ConsumerRecords<Integer, byte[]> records) {
        // TODO
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
