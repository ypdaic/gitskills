1: RDD创建
    
    1: 从集合（内存）中创建RDD
    
        从集合中创建RDD，Spark主要提供了两个方法：parallelize和makeRDD
        
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("spark")
        val sparkContext = new SparkContext(sparkConf)
        val rdd1 = sparkContext.parallelize(
        List(1,2,3,4)
        )
        val rdd2 = sparkContext.makeRDD(
        List(1,2,3,4)
        )
        rdd1.collect().foreach(println)
        rdd2.collect().foreach(println)
        sparkContext.stop()
        
        从底层代码实现来讲，makeRDD方法其实就是parallelize方法

    2: 从外部存储（文件）创建RDD
        
        由外部存储系统的数据集创建RDD包括：本地的文件系统，所有Hadoop支持的数据集，比如HDFS、HBase等

        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("spark")
        val sparkContext = new SparkContext(sparkConf)
        val fileRDD: RDD[String] = sparkContext.textFile("input")
        fileRDD.collect().foreach(println)
        sparkContext.stop()

    3: 从其他RDD创建

        主要是通过一个RDD运算完后，再产生新的RDD。详情请参考后续章节

    4: 直接创建RDD（new）
    
        使用new的方式直接构造RDD，一般由Spark框架自身使用。

