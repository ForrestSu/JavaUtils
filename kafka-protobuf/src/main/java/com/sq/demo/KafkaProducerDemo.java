package com.sq.demo;

import java.util.Properties;

import com.sq.demo.examples.Producer;
import com.sq.demo.utils.KafkaPropertiesUtil;

public class KafkaProducerDemo {

    /**
     * java -cp target/kafka-protobuf-1.0.jar com.sq.demo.KafkaProducerDemo ./acl.properties
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(">> Usage: java -jar app.jar /etc/uvframe/acl.properties");
            return;
        }
        Properties props = KafkaPropertiesUtil.LoadProperties(args[0]);

        boolean isAsync = true;
        String topic = props.getProperty("topic", "test");
        System.out.println("publish topic is :" + topic);
        Producer producerThread = new Producer(topic, props, isAsync);
        producerThread.start();
    }
}