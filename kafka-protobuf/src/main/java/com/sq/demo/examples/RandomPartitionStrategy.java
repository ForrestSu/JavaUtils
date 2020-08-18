package com.sq.demo.examples;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 自定义：生产者的分区策略
 */

public class RandomPartitionStrategy implements Partitioner {

    /**
     * 随机分区策略
     *  TODO： 注意-使用多线程安全的随机数生成器！
     *  eg: concurrent 包下面的 ThreadLocalRandom
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        return ThreadLocalRandom.current().nextInt(partitions.size());
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
