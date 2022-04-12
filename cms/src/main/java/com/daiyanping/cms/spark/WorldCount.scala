package com.daiyanping.cms.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * WorldCount
 *
 * @author daiyanping
 * @date 2022-04-06
 * @description ${description}
 */
object WorldCount extends App {

  // 创建Spark运行配置对象,local 表示本地环境，local后面不加任何内容表示单线程，想多线程可以设置
  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")

  // 创建Spark上下文环境对象（连接对象）
  val sc : SparkContext = new SparkContext(sparkConf)

  // 读取文件数据(可以单一文件，也可以是目录)
  val fileRDD: RDD[String] = sc.textFile("/Users/daiyanping/code/gitskills/cms/src/main/java/com/daiyanping/cms/spark/word.txt")

  // 将文件中的数据扁平花处理后进行分词
  val wordRDD: RDD[String] = fileRDD.flatMap( _.split(" ") )

  // 先分组
  val groupByKey: RDD[(String, Iterable[String])] = wordRDD.groupBy(key => key)
  println(groupByKey)
  val rdd: RDD[(String, Int)] = groupByKey.map((rdd) => {
    (rdd._1, rdd._2.size)
  })
  rdd.collect().foreach(println)

  // 转换数据结构 word => (word, 1)
  val word2OneRDD: RDD[(String, Int)] = wordRDD.map((_,1))

  // 将转换结构后的数据按照相同的单词进行分组聚合(spark 提供的reduceByKey方法)
  val word2CountRDD: RDD[(String, Int)] = word2OneRDD.reduceByKey(_+_)

  // 将数据聚合结果采集到内存中
  val word2Count: Array[(String, Int)] = word2CountRDD.collect()

  // 打印结果
  word2Count.foreach(println)

  //关闭Spark连接
  sc.stop()

}
