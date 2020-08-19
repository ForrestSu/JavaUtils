package com.sq.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Consumer {
    private final KafkaConsumer<Integer, byte[]> consumer;
    private final List<String> topics;

    public Consumer(String topic, Properties props) {

        // Properties props = prop_default;
        // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        // KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        // props.put("client.id", "avro_data");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        consumer = new KafkaConsumer<Integer, byte[]>(props);
        this.topics = Collections.singletonList(topic);
        System.out.println("subscribe topic = <" + topic + ">");
    }


    public void run() {
        consumer.subscribe(topics);
        Duration timeout = Duration.ofSeconds(3);
        while (true) {
            try {
                ConsumerRecords<Integer, byte[]> records = consumer.poll(timeout);
                for (ConsumerRecord<Integer, byte[]> record : records) {
                    final String msg = new String(record.value(), StandardCharsets.UTF_8);
                    System.out.println("Received: (" + record.key() + ", msg: " + msg + ") at offset " + record.offset());
                    // JSONObject jsonObject = JSONObject.parseObject(record.value());
                    // System.out.println("Content: "+JSONObject.toJSONString(jsonObject) );
                }
                // 手动提交 offset
                consumer.commitAsync();
                System.out.println("poll...");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
            }
        }
    }
}
