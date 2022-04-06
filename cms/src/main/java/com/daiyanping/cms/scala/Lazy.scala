package com.daiyanping.cms.scala

/**
 * Lazy
 *
 * @author daiyanping
 * @date 2022-03-28
 * @description ${description}
 */
object Lazy {

  def main(args: Array[String]): Unit = {

    // 不会立马加载，只有第一次使用的时候才会去加载
    lazy val a = 1;
  }
}
