package com.daiyanping.cms.scala

import scala.util.Random

/**
 * MatchApp
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object MatchApp extends App{

  val names = Array("Akiho Yoshizawa", "YuiHatano", "Aoi Sola")
  val name = names(Random.nextInt(names.length))

  name match {
    case "Akiho Yoshizawa" => println("吉老师...")
    case "YuiHatano" => println("波老师...")
    case _ => println("真不知道你们在说什么")
  }


  def judgeGrade(name:String, grade:String): Unit = {
    grade match {
      case "A" => println("Excellent")
      case "B" => println("Good...")
      case "C" => println("Just so so...")
      // 加条件匹配
      case _ if(name == "lisi") => println(name + ", you are a good boy, but...")
      case _ => println("You need work harder...")
    }
  }

  judgeGrade("zhangsan", "A")
  judgeGrade("lisi", "B")
  judgeGrade("haha", "C")

  def greeting(array:Array[String]): Unit = {
    array match {
      // 只有一个张三
      case Array("zhansan") => println("Hi:zhangsan")
      // 只有两个元素
      case Array(x,y) => println("Hi:" + x + " , " + y)
      // 以张三开头
      case Array("zhangsan", _*) => println("Hi:zhangsan and other fridends...")
      case _ => println("Hi: everybody...")
    }
  }

  greeting(Array("xxxx", "uuuu"))
  greeting(Array("zhansan"))
  greeting(Array("zhangsan", "test1", "test2"))
  greeting(Array("ttt","yyy","ddd"))
}
