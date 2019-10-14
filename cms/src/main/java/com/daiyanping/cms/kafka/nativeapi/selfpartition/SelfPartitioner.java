package com.daiyanping.cms.kafka.nativeapi.selfpartition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * 自定义分区器
 */
public class SelfPartitioner implements Partitioner {
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {
        //拿到分区信息
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        //TODO 分区数
        int num = partitionInfos.size();
        //TODO 根据value与分区数求余的方式得到分区ID
        int parId = ((String)value).hashCode()%num;
        return parId;
    }

    public void close() {
        //do nothing
    }

    public void configure(Map<String, ?> configs) {
        //do nothing
    }

}
