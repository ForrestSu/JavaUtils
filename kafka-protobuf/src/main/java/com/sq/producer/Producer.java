package com.sq.producer;

import com.sq.interceptor.MyProducerInterceptor;
import com.sq.partition.KeyOrderingPartitionStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isAsync;

    public Producer(String topic, Properties props, Boolean isAsync) {
        // props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" +
        // KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, "5"); //发送失败事, 最大重试次数
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 指定生产者写入-分区策略
        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KeyOrderingPartitionStrategy.class.getName());
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MyProducerInterceptor.class.getName());
        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    public void run() {
        System.out.println("tid: " + Thread.currentThread().getId());
        int key = 0;
        while (true) {
            key++;
            String message = "sunquan-" + key;
            // Send asynchronously
            if (isAsync) {
                AckCallback cb = new AckCallback(key, message);
                producer.send(new ProducerRecord<>(topic, key, message), cb);
            } else {
                try {
                    Future<RecordMetadata> fut = producer.send(new ProducerRecord<>(topic, key, message));
                    // 阻塞等待 wait response blocked
                    RecordMetadata metadata = fut.get();
                    System.out.println("Sync sent message: (" + key + ", " + message + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //just for test sleep 1s
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

