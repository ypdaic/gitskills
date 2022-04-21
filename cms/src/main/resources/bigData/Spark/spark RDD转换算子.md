1:  RDD转换算子

    RDD根据数据处理方式的不同将算子整体上分为Value类型、双Value类型和Key-Value类型
    转换算子不会触发具体的逻辑执行，collect方法才会触发作业的执行，也叫行动算子
    
    1: Value类型
        
        1: map
        函数签名
        def map[U: ClassTag](f: T => U): RDD[U]
        将处理的数据逐条进行映射转换，这里的转换可以是类型的转换，也可以是值的转换。
        val dataRDD: RDD[Int] = sparkContext.makeRDD(List(1,2,3,4))
        val dataRDD1: RDD[Int] = dataRDD.map(
            num => {
            num * 2
            }
        )

        val dataRDD2: RDD[String] = dataRDD1.map(
            num => {
                "" + num
            }
        )
        
        2: mapPartitions
        函数签名
        def mapPartitions[U: ClassTag](f: Iterator[T] => Iterator[U],preservesPartitioning: Boolean = false): RDD[U]
        函数说明
        将待处理的数据以分区为单位发送到计算节点进行处理，这里的处理是指可以进行任意的处理，哪怕是过滤数据。
        val dataRDD1: RDD[Int] = dataRDD.mapPartitions(
            datas => {
                datas.filter(_==2)
            }
        )

        3: mapPartitionsWithIndex
        函数签名
        def mapPartitionsWithIndex[U: ClassTag](
        f: (Int, Iterator[T]) => Iterator[U],
        preservesPartitioning: Boolean = false): RDD[U]
        函数说明
        将待处理的数据以分区为单位发送到计算节点进行处理，这里的处理是指可以进行任意的处理，哪怕是过滤数据，在处理时同时可以获取当前分区索引。
        val dataRDD1 = dataRDD.mapPartitionsWithIndex(
            (index, datas) => {
                 datas.map(index, _)
            }
        )

        4: flatMap
        函数签名
        def flatMap[U: ClassTag](f: T => TraversableOnce[U]): RDD[U]
        函数说明
        将处理的数据进行扁平化后再进行映射处理，所以算子也称之为扁平映射
        val dataRDD = sparkContext.makeRDD(List(
             List(1,2),List(3,4)
        ),1)
        val dataRDD1 = dataRDD.flatMap(
        list => list
        )
        
        5: glom
        函数签名
        def glom(): RDD[Array[T]]
        函数说明
        将同一个分区的数据直接转换为相同类型的内存数组进行处理，分区不变
        val dataRDD = sparkContext.makeRDD(List(
            1,2,3,4
        ),1)
        val dataRDD1:RDD[Array[Int]] = dataRDD.glom()
        
        6: groupBy
        函数签名
        def groupBy[K](f: T => K)(implicit kt: ClassTag[K]): RDD[(K, Iterable[T])]
        函数说明
        将数据根据指定的规则进行分组, 分区默认不变，但是数据会被打乱重新组合，我们将这样的操作称之为shuffle。
        极限情况下，数据可能被分在同一个分区中
        一个组的数据在一个分区中，但是并不是说一个分区中只有一个组

        7: filter
        函数签名
        def filter(f: T => Boolean): RDD[T]
        函数说明
        将数据根据指定的规则进行筛选过滤，符合规则的数据保留，不符合规则的数据丢弃。
        当数据进行筛选过滤后，分区不变，但是分区内的数据可能不均衡，生产环境下，可能会出现数据倾斜。
        val dataRDD = sparkContext.makeRDD(List(
            1,2,3,4
        ),1)
        val dataRDD1 = dataRDD.filter(_%2 == 0)

        8: sample
        函数签名
        def sample(
            withReplacement: Boolean,
            fraction: Double,
            seed: Long = Utils.random.nextLong): RDD[T]
        函数说明
        根据指定的规则从数据集中抽取数据
        val dataRDD = sparkContext.makeRDD(List(
            1,2,3,4
        ),1)
        // 抽取数据不放回（伯努利算法）
        // 伯努利算法：又叫0、1分布。例如扔硬币，要么正面，要么反面。
        // 具体实现：根据种子和随机算法算出一个数和第二个参数设置几率比较，小于第二个参数要，大于不要
        // 第一个参数：抽取的数据是否放回，false：不放回
        // 第二个参数：抽取的几率，范围在[0,1]之间,0：全不取；1：全取；
        // 第三个参数：随机数种子
        val dataRDD1 = dataRDD.sample(false, 0.5)
        // 抽取数据放回（泊松算法）
        // 第一个参数：抽取的数据是否放回，true：放回；false：不放回
        // 第二个参数：重复数据的几率，范围大于等于0.表示每一个元素被期望抽取到的次数
        // 第三个参数：随机数种子
        val dataRDD2 = dataRDD.sample(true, 2)
        
        
        9: distinct
        函数签名
        def distinct()(implicit ord: Ordering[T] = null): RDD[T]
        def distinct(numPartitions: Int)(implicit ord: Ordering[T] = null): RDD[T]
        将数据集中重复的数据去重
        val dataRDD = sparkContext.makeRDD(List(
            1,2,3,4,1,2
        ),1)
        val dataRDD1 = dataRDD.distinct()
        
        val dataRDD2 = dataRDD.distinct(2)

        10: coalesce
        函数签名
        def coalesce(numPartitions: Int, shuffle: Boolean = false,
           partitionCoalescer: Option[PartitionCoalescer] = Option.empty)
          (implicit ord: Ordering[T] = null): RDD[T]
        函数说明
        根据数据量缩减分区，用于大数据集过滤后，提高小数据集的执行效率当spark程序中，存在过多的小任务的时候，可以通过coalesce方法，收缩合并分区，减少分区的个数，减小任务调度成本
        
        10: repartition
        函数签名
        def repartition(numPartitions: Int)(implicit ord: Ordering[T] = null): RDD[T]
        函数说明
        该操作内部其实执行的是coalesce操作，参数shuffle的默认值为true。无论是将分区数多的RDD转换为分区数少的RDD，还是将分区数少的RDD转换为分区数多的RDD，repartition操作都可以完成，因为无论如何都会经shuffle过程。
        val dataRDD = sparkContext.makeRDD(List(
            1,2,3,4,1,2
        ),2)
        
        val dataRDD1 = dataRDD.repartition(4)

        11: sortBy
        函数签名
        def sortBy[K](f: (T) => K,
        ascending: Boolean = true,
        numPartitions: Int = this.partitions.length)
        (implicit ord: Ordering[K], ctag: ClassTag[K]): RDD[T]
        函数说明
        该操作用于排序数据。在排序之前，可以将数据通过f函数进行处理，之后按照f函数处理的结果进行排序，默认为正序排列。排序后新产生的RDD的分区数与原RDD的分区数一致。
        val dataRDD = sparkContext.makeRDD(List(
        1,2,3,4,1,2
        ),2)
        
        val dataRDD1 = dataRDD.sortBy(num=>num, false, 4)

        12: pipe
        函数签名
        def pipe(command: String): RDD[String]
        函数说明
        管道，针对每个分区，都调用一次shell脚本，返回输出的RDD。
        注意：shell脚本需要放在计算节点可以访问到的位置
        1) 编写一个脚本，并增加执行权限
        [root@linux1 data]# vim pipe.sh
        #!/bin/sh
        echo "Start"
        while read LINE; do
        echo ">>>"${LINE}
        done
        
        [root@linux1 data]# chmod 777 pipe.sh
        2) 命令行工具中创建一个只有一个分区的RDD
        scala> val rdd = sc.makeRDD(List("hi","Hello","how","are","you"), 1)
        3) 将脚本作用该RDD并打印
        scala> rdd.pipe("/opt/module/spark/pipe.sh").collect()
        res18: Array[String] = Array(Start, >>>hi, >>>Hello, >>>how, >>>are, >>>you)

    2: 双Value类型
        
        1: intersection
        函数签名
        def intersection(other: RDD[T]): RDD[T]
        函数说明
        对源RDD和参数RDD求交集后返回一个新的RDD
        val dataRDD1 = sparkContext.makeRDD(List(1,2,3,4))
        val dataRDD2 = sparkContext.makeRDD(List(3,4,5,6))
        val dataRDD = dataRDD1.intersection(dataRDD2)
        
        2: union
        函数签名
        def union(other: RDD[T]): RDD[T]
        函数说明
        对源RDD和参数RDD求并集后返回一个新的RDD
        val dataRDD1 = sparkContext.makeRDD(List(1,2,3,4))
        val dataRDD2 = sparkContext.makeRDD(List(3,4,5,6))
        val dataRDD = dataRDD1.union(dataRDD2)
        
        3: subtract
        函数签名
        def subtract(other: RDD[T]): RDD[T]
        函数说明
        以一个RDD元素为主，去除两个RDD中重复元素，将其他元素保留下来。求差集
        val dataRDD1 = sparkContext.makeRDD(List(1,2,3,4))
        val dataRDD2 = sparkContext.makeRDD(List(3,4,5,6))
        val dataRDD = dataRDD1.subtract(dataRDD2)
        
        4: zip
        函数签名
        def zip[U: ClassTag](other: RDD[U]): RDD[(T, U)]
        函数说明
        将两个RDD中的元素，以键值对的形式进行合并。其中，键值对中的Key为第1个RDD中的元素，Value为第2个RDD中的元素。
        val dataRDD1 = sparkContext.makeRDD(List(1,2,3,4))
        val dataRDD2 = sparkContext.makeRDD(List(3,4,5,6))
        val dataRDD = dataRDD1.zip(dataRDD2)

    3: Key - Value类型

        1: partitionBy
        函数签名
        def partitionBy(partitioner: Partitioner): RDD[(K, V)]
        函数说明
        将数据按照指定Partitioner重新进行分区。Spark默认的分区器是HashPartitioner
        val rdd: RDD[(Int, String)] =
        sc.makeRDD(Array((1,"aaa"),(2,"bbb"),(3,"ccc")),3)
        import org.apache.spark.HashPartitioner
        val rdd2: RDD[(Int, String)] =
        rdd.partitionBy(new HashPartitioner(2))

        2: reduceByKey
        函数签名
        def reduceByKey(func: (V, V) => V): RDD[(K, V)]
        def reduceByKey(func: (V, V) => V, numPartitions: Int): RDD[(K, V)]
        函数说明
        可以将数据按照相同的Key对Value进行聚合
        val dataRDD1 = sparkContext.makeRDD(List(("a",1),("b",2),("c",3)))
        val dataRDD2 = dataRDD1.reduceByKey(_+_)
        val dataRDD3 = dataRDD1.reduceByKey(_+_, 2)

        3：groupByKey
        函数签名
        def groupByKey(): RDD[(K, Iterable[V])]
        def groupByKey(numPartitions: Int): RDD[(K, Iterable[V])]
        def groupByKey(partitioner: Partitioner): RDD[(K, Iterable[V])]
        函数说明
        将分区的数据直接转换为相同类型的内存数组进行后续处理
        
        4: aggregateByKey
        函数签名
        def aggregateByKey[U: ClassTag](zeroValue: U)(seqOp: (U, V) => U, combOp: (U, U) => U): RDD[(K, U)]
        函数说明
        将数据根据不同的规则进行分区内计算和分区间计算
        val dataRDD1 =
            sparkContext.makeRDD(List(("a",1),("b",2),("c",3)))
        val dataRDD2 =
        dataRDD1.aggregateByKey(0)(_+_,_+_)
        取出每个分区内相同key的最大值然后分区间相加














    
    










        

        









