package com.sq.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * 自定义：生产者的分区策略
 */

public class KeyOrderingPartitionStrategy implements Partitioner {

    /**
     * 根据 key的 hashCode 来分区
     * 保证同一个 key 的所有消息，落在同一个分区上
     *
     * TODO: 这个 key 可以是股票代码, 资金账号, etc.
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        return Math.abs(key.hashCode()) % partitions.size();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
