package com.sq;

import java.util.Properties;

import com.sq.consumer.Consumer;

public class KafkaConsumerDemo {

    /**
     * java -cp target/kafka-protobuf-1.0.jar com.sq.demo.KafkaConsumerDemo ./acl.properties
     */
    private static final String topic = "test";
    private static final String servers = "test-ogg-02:9092";
    private static final String groupId = "group01";

    public static void main(String[] args) {

        System.out.println("start ...");
        Properties props = new Properties();
        props.put("topic", topic);
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);

        Consumer consumer = new Consumer(topic, props);
        consumer.run();

        System.out.println("exit...");
    }
}
