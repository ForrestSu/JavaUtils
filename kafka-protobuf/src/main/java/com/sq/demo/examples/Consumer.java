/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sq.demo.examples;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer extends ShutdownableThread {
    private final KafkaConsumer<Integer, byte[]> consumer;
    private final String topic;

    public Consumer(String topic, Properties props) {
        super("KafkaConsumerExample", false);

        // Properties props = prop_default;
        // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        // KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        // props.put("client.id", "avro_data");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        consumer = new KafkaConsumer<Integer, byte[]>(props);
        this.topic = topic;
        System.out.println("topic = <" + topic + ">");
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(this.topic));
        while (true) {
            try {
                ConsumerRecords<Integer, byte[]> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<Integer, byte[]> record : records) {
                    System.out.println("Received message: (" + record.key() + ", length is " + record.value().length
                            + ") at offset " + record.offset());
                    // JSONObject jsonObject = JSONObject.parseObject(record.value());
                    // System.out.println("Content: "+JSONObject.toJSONString(jsonObject) );

                    // System.out.println("record.value() = " + new String(record.value(), Charset.forName("UTF-8")));
                    // String value = record.value();
                    // ProtoBufUtil.UnPackMsg(value.getBytes(Charset.forName("UTF-8")));
                    // ProtoBufUtil.UnPackMsg(record.value());
                    // m_oraclewt.ExecuteSql(record.value());
                }
                System.out.println("poll...");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("e");
            }
        }
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }
}
