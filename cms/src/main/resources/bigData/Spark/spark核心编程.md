1: 核心编程
    
    Spark计算框架为了能够进行高并发和高吞吐的数据处理，封装了三大数据结构，用于处理不同的应用场景。三大数据结构分别是：
    RDD : 弹性分布式数据集
    累加器：分布式共享只写变量
    广播变量：分布式共享只读变量
    接下来我们一起看看这三大数据结构是如何在数据处理中使用的

2: RDD
        
    RDD（Resilient Distributed Dataset）叫做弹性分布式数据集，是Spark中最基本的数据处理模型。
    代码中是一个抽象类，它代表一个弹性的、不可变、可分区、里面的元素可并行计算的集合。

    弹性
    存储的弹性：内存与磁盘的自动切换；
    容错的弹性：数据丢失可以自动恢复；
    计算的弹性：计算出错重试机制；
    分片的弹性：可根据需要重新分片。
    分布式：数据存储在大数据集群不同节点上
    数据集：RDD封装了计算逻辑，并不保存数据
    数据抽象：RDD是一个抽象类，需要子类具体实现
    不可变：RDD封装了计算逻辑，是不可以改变的，想要改变，只能产生新的RDD，在新的RDD里面封装计算逻辑(就是RDD的相关计算函数)
    可分区、并行计算
    
    1：核心属性
        
        分区列表
        
        RDD数据结构中存在分区列表，用于执行任务时并行计算，是实现分布式计算的重要属性。
        protected def getPartitions: Array[Partition]

        分区计算函数

        Spark在计算时，是使用分区函数对每一个分区进行计算
        def compute(split: Partition, context: TaskContext): Iterator[T]

        RDD之间的依赖关系
        
        RDD是计算模型的封装，当需求中需要将多个计算模型进行组合时，就需要将多个RDD建立依赖关系
        protected def getDependencies: Seq[Dependency[_]] = deps

        分区器（可选）

        当数据为KV类型数据时，可以通过设定分区器自定义数据的分区
        /** Optionally overridden by subclasses to specify how they are partitioned. */
        @transient val partitioner: Option[Partitioner] = None

        首选位置（可选）
        计算数据时，可以根据计算节点的状态选择不同的节点位置进行计算

        /**
        * Optionally overridden by subclasses to specify placement preferences.
        * /
        protected def getPreferredLocations(split: Partition): Seq[String] = Nil


