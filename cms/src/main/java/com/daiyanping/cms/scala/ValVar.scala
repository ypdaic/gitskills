package com.daiyanping.cms.scala

/**
 * ValVar
 *
 * @author daiyanping
 * @date 2022-03-28
 * @description ${description}
 */
object ValVar {

  def main(arg: Array[String]): Unit = {
    // val 相当于java的final
    val money: Int = 100;
    // 下面是不允许的
//    money = 200;

    // var 定义的就是变量
    var string: String = "test1";
    println(string);
    string = "test2";
    println(string)

  }
}
