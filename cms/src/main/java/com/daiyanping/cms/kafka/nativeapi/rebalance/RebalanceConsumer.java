package com.daiyanping.cms.kafka.nativeapi.rebalance;

import com.daiyanping.cms.kafka.nativeapi.BusiConst;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明：设置了再均衡监听器的消费者
 */
public class RebalanceConsumer {

    public static final String GROUP_ID = "rebalanceconsumer";

    private static ExecutorService executorService
            = Executors.newFixedThreadPool(
            BusiConst.CONCURRENT_PARTITIONS_COUNT);


    /**
     * 13-onPartitionsRevoked参数值为：[]
     * 14-onPartitionsRevoked参数值为：[]
     * 13-服务器准备分区再均衡，提交偏移量。当前偏移量为：{}
     * 14-服务器准备分区再均衡，提交偏移量。当前偏移量为：{}
     * 分区偏移量表中：{}
     * 分区偏移量表中：{}
     * 14-再均衡完成，onPartitionsAssigned参数值为：[rebalance-topic-three-part-2]
     * 分区偏移量表中：{}
     * 13-再均衡完成，onPartitionsAssigned参数值为：[rebalance-topic-three-part-1, rebalance-topic-three-part-0]
     * 14-topicPartitionrebalance-topic-three-part-2
     * 分区偏移量表中：{}
     * 13-topicPartitionrebalance-topic-three-part-1
     * 13-topicPartitionrebalance-topic-three-part-0
     * 17-onPartitionsRevoked参数值为：[]
     * 17-服务器准备分区再均衡，提交偏移量。当前偏移量为：{}
     * 分区偏移量表中：{}
     * 13-onPartitionsRevoked参数值为：[rebalance-topic-three-part-1, rebalance-topic-three-part-0]
     * 13-服务器准备分区再均衡，提交偏移量。当前偏移量为：{}
     * 分区偏移量表中：{}
     * 14-onPartitionsRevoked参数值为：[rebalance-topic-three-part-2]
     * 14-服务器准备分区再均衡，提交偏移量。当前偏移量为：{}
     * 分区偏移量表中：{}
     * 17-再均衡完成，onPartitionsAssigned参数值为：[rebalance-topic-three-part-2]
     * 分区偏移量表中：{}
     * 14-再均衡完成，onPartitionsAssigned参数值为：[rebalance-topic-three-part-1]
     * 分区偏移量表中：{}
     * 13-再均衡完成，onPartitionsAssigned参数值为：[rebalance-topic-three-part-0]
     * 分区偏移量表中：{}
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i<BusiConst.CONCURRENT_PARTITIONS_COUNT; i++){
            executorService.submit(new ConsumerWorker(false));
        }
        Thread.sleep(5000);
        //用来被停止，观察保持运行的消费者情况
        new Thread(new ConsumerWorker(true)).start();
    }
}
