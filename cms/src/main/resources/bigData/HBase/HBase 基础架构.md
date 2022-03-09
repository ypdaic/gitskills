![](.HBase基础架构_images/426a38f4.png)

架构角色：

    1）Region Server

        Region Server为 Region的管理者，其实现类为HRegionServer，主要作用如下:
    
        对于数据的操作：get, put, delete；
        对于Region的操作：splitRegion、compactRegion。

    2）Master

        Master是所有Region Server的管理者，其实现类为HMaster，主要作用如下：
        对于表的操作：create, delete, alter
        对于RegionServer的操作：分配regions到每个RegionServer，监控每个RegionServer的状态，负载均衡和故障转移。

    3）Zookeeper

        HBase通过Zookeeper来做master的高可用、RegionServer的监控、元数据的入口以及集群配置的维护等工作。

    4）HDFS

        HDFS为Hbase提供最终的底层数据存储服务，同时为HBase提供高可用的支持(HDFS有多个副本)。