package com.sq;

import java.util.Properties;
import com.sq.producer.Producer;

public class KafkaProducerDemo {

    /**
     * java -cp target/kafka-protobuf-1.0.jar com.sq.demo.KafkaProducerDemo ./acl.properties
     */

    private static final String topic = "test";
    private static final String servers = "test-ogg-02:9092";

    public static void main(String[] args) {

        System.out.println("start ...");
        Properties props = new Properties();
        props.put("topic", topic);
        props.put("bootstrap.servers", servers);

        boolean isAsync = true;
        System.out.println("publish topic is :" + topic);
        Producer producer = new Producer(topic, props, isAsync);
        producer.run();

        System.out.println("exit...");
    }
}
