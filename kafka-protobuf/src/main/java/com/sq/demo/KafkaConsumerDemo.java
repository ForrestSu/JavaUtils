package com.sq.demo;

import java.util.Properties;

import com.sq.demo.examples.Consumer;
import com.sq.demo.utils.KafkaPropertiesUtil;

public class KafkaConsumerDemo {

    /**
     * java -cp target/kafka-protobuf-1.0.jar com.sq.demo.KafkaConsumerDemo ./acl.properties
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(">> Usage: java -jar app.jar /etc/uvframe/acl.properties");
            return;
        }
        Properties props = KafkaPropertiesUtil.LoadProperties(args[0]);

        System.out.println("props.size() " + props.size());
        // must be add into system
        /*
         * String kers_key = "java.security.auth.login.config"; System.setProperty(kers_key,
         * props.getProperty(kers_key)); System.out.println("load properties, load: " + props.size());
         * props.remove(kers_key);
         */


        System.out.println("start ...");
        String topic = props.getProperty("topic", "storage-1");
        System.out.println("subscribe topic is:" + topic);
        Consumer consumerThread = new Consumer(topic, props);
        consumerThread.start();

        boolean flag = true;
        while (flag) {
            try {
                Thread.sleep(1000);
                // System.out.println("xxx");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // System.out.println("main thread is exit!");
    }
}
