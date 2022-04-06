package com.daiyanping.cms.scala

/**
 * AbstractApp
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object AbstractApp {

  def main(args: Array[String]): Unit = {
    val student = new Student2();
    student.speak
  }
}

abstract class Person3 {

  def speak

  val name:String
  val age:Int
}

class Student2 extends Person3 {

  // 必须实现
  override def speak: Unit = {
    println("sss")
  }

  // 必须实现
  override val name: String = "PK"
  // 必须实现
  override val age: Int = 18
}
