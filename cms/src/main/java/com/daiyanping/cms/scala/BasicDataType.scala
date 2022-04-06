package com.daiyanping.cms.scala

/**
 * BasicDataType
 * scala 数据类型
 * @author daiyanping
 * @date 2022-03-28
 * @description ${description}
 */
object BasicDataType {

  def main(array: Array[String]): Unit = {
    // int 类型
    val a:Int = 10;
    println(a)
    // boolean类型
    val c:Boolean = false;
    println(c)
    // Double类型
    val d:Double = 1.1;
    println(d)
    // Float类型
    val b:Float = 1.1f;
    println(b)
    // 字符串类型
    var e:String = "sss";
    println(e)

    val g = a.asInstanceOf[Double];
    println(g)

    var result = 10.isInstanceOf[Int];
    println(result)
  }
}
