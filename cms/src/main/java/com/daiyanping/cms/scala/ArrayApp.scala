package com.daiyanping.cms.scala

/**
 * ArrayApp
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object ArrayApp extends App {

    // 定长数组
    val a = new Array[String](5)

    a.length
    a(0) = "hello"

  // 这种方式声明数组也是定长的
  val b = Array("hadoop", "scala", "storm")

  val c = Array(2,3,4,5,6,7,8,9)
  c.sum
  c.min
  // 转成字符串
  c.mkString(",")

  // 可变数组
  val d = scala.collection.mutable.ArrayBuffer[Int]()
  // 添加数据
  d +=1
  d +=2
  d +=(3,4,5)
  d ++=Array(6,7,8)
  d.insert(0, 3)
  d.remove(0)
  d.remove(0)
  println(d)

  // 正序
  for (i <- 0 until d.length) {
    println(d(i))
  }

  // 逆序
  for (i <- (0 until d.length).reverse) {
    println(d(i))
  }
}
