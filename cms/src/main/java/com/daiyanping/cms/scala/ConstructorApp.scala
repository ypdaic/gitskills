package com.daiyanping.cms.scala

/**
 * ConstructorApp
 *
 * @author daiyanping
 * @date 2022-03-28
 * @description ${description}
 */
object ConstructorApp {

  def main(args: Array[String]): Unit = {
    val student = new Student("李四", 18, "Math")
    println(student.name + ":" + student.age + " : " + student.major)
    println(student)
  }


}

// 主构造器
class People2(val name:String, val age:Int) {

  println("Person Constructor enter...")

  val school = "ustc"

  println("Person Constructor leave...")

  // 私有属性
  private [this] var gender:String = _

  // 附属构造器，第一行代码必须调用主构造器
  def this(name:String, age:Int, gender:String) {
    this(name, age)
    this.gender = gender
    println(this.gender)
  }

  def eat():String = {
    name + "eat....."
  }

  def watchFootball(teamName: String): Unit = {
    println(name + "is watching match of " + teamName)
  }

  def printInfo(): Unit = {
    println(this.gender)
  }
}

// name:String, age:Int 继承自People2可以不用var val 修饰，major你要想通过Student对象访问到必须有var,val
class Student(name:String, age:Int, val major:String) extends People2(name, age) {
  println("Person Student enter.....")

  println("Person Student leave.....")

  // 重写
  override val school = "peking"

  // 重写
  override def toString: String = {
    return "toString"
  }
}
