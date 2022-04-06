package com.daiyanping.cms.scala

import com.redislabs.provider.redis.rdd.RedisKeysRDD
import com.redislabs.provider.redis.{RedisConfig, RedisEndpoint, toRedisContext}
import org.apache.spark.sql.SparkSession

/**
 * RedisApp
 *
 * @author daiyanping
 * @date 2022-03-31
 * @description ${description}
 */
object RedisApp {



  def main(args: Array[String]): Unit = {
    val Redis_prefix = "cdp_dataservice_metadata_"
    val Redis_suffix = "_tag_metadata"

    val redisServerDnsAddress = "home.minquiers.com"
    val redisPortNumber = 26502
    val redisPassword = "123456"
    val redisConfig = new RedisConfig(new RedisEndpoint(redisServerDnsAddress, redisPortNumber, redisPassword))
    implicit val spark = SparkSession.builder().getOrCreate();
    val sc = spark.sparkContext;

    val redisKeysRDD:RedisKeysRDD = sc.fromRedisKeys(Array("cdp_dataservice_metadata_61c28e0e4e20b0e45ed3a60a_tag_metadata"),1)(redisConfig)
    val rdd = redisKeysRDD.getKV().first();
    println(rdd)
  }
}
