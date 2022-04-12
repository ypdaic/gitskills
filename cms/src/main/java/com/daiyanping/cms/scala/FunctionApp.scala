package com.daiyanping.cms.scala

/**
 * FunctionApp
 *
 * @author daiyanping
 * @date 2022-03-28
 * @description ${description}
 */
object FunctionApp {

  def add(x:Int, y:Int):Int = {
    x+y
  }

  def main(args:Array[String]): Unit = {
    println(add(1,2))
    println(three())
    // 函数没有入参的话，可以不用使用()
    println(three)
    sayHello()
    sayHello("test")
    sayName()
    sayName("haha   ")
    println(speed(100, 10))
    // 命名参数，我们可以给的参数和定义的顺序不一致，根据名称来的
    println(speed(distance = 100, 10))
    println(speed(time = 10, distance = 100))
    println(sum(1,2))
    // 可变参数
    println(sum(1,2,2,2,2,2))
    if_()
    to_()
    list_()
  }

  def three() = 1 + 2

  def sayHello(): Unit = {
    println("Say hello...")
  }

  def sayHello(name: String): Unit = {
    println("Say " + name)
  }

  // = "pk" 表示参数默认值
  def sayName(name:String = "pk"): Unit = {
    println(name)
  }

  def speed(distance:Float, time:Float):Float = {
    distance/time
  }

  def sum(a:Int, b:Int):Int = {
    a + b
  }

  // 定义可变参数
  def sum(a:Int*):Int = {
    var result = 0
    for(number <- a) {
      result += number
    }
    result
  }

  // if 使用
  def if_(): Unit = {
    val a = 1
    if (1 > 0) {
      println("true")
    } else {
      println("false")
    }
  }

  def for_(): Unit = {

  }

  // to遍历
  def to_(): Unit = {
    for (i <- 1 to 10) {
      println(i)
    }

    for (i <- 1 to 10 if i % 2 == 0) {
      println(i)
    }

  }

  // 数组遍历
  def list_(): Unit = {
    val courses = Array("Hadoop", "Spark SQL", "Spark Streaming", "Storm")
    for (course <- courses) {
      println(course)
    }

    courses.foreach(course => println(course))
  }


  def sayHello2(name:String): Unit = {
    println("Hi:" + name)
  }

  sayHello2("PK")

  /*
  匿名函数
   */
  val sayHello3 = (name:String) => println("he" + name)

  sayHello3("ddd")

  // 将原来参数分开定义
//  def sum(a:Int)(b:Int) = a + b
//  println(sum(2)(3))

  // map 函数，逐个去操作集合中的每个元素
  val l = List(1,2,3,4,5,6,7)
  l.map((x:Int)=> x+1)

  l.map(x => x * 2)
  l.map(x => (x,1))
  l.map(_ * 2)

  l.map(_ * 2).foreach(println)

  l.map(_ * 2).filter(_ > 8).foreach(println)
  // 前4个
  l.take(4)

  // 两两相加 1+ 2 3+4 5+6
  l.reduce(_+_)
  // 两两相减
  l.reduce(_-_)
  l.reduceLeft(_-_)
  l.reduceRight(_-_)
  l.fold(0)(_)

  val f = List(List(1,2),List(3,4),List(5,6))
  // 压平
  f.flatten

  f.map(_.map(_*2))
  // 压平后再处理
  f.flatMap(_.map(_* 2))

}
