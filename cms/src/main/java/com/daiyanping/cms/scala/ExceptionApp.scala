package com.daiyanping.cms.scala

/**
 * ExceptionApp
 *
 * @author daiyanping
 * @date 2022-04-06
 * @description ${description}
 */
class ExceptionApp extends App {

    try {
      val i = 10 /0
      println(i)
    } catch {
      case e:ArithmeticException => println("除数不能为0..")
      case e:Exception => println(e.getMessage)
    } finally {
      // 释放资源
    }

}
