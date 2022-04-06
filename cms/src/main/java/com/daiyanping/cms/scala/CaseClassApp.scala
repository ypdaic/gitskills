package com.daiyanping.cms.scala

/**
 * CaseClass
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object CaseClassApp {

  def main(args: Array[String]): Unit = {
    println(Dog("wangcai").name)
    caseClassMatch(CTO("pk", "22"))
  }

  def caseClassMatch(person:Person): Unit = {
    person match {
      case CTO(name,floor) => println("CTO name is:" + name + " , floor is: " + floor)
      case Employee(name,floor) => println("Employee name is:" + name + " , floor is: " + floor)
      case _ => println("other")
    }
  }
}

// case class 不用new
case class Dog(name:String)

class Person
case class CTO(name:String, floor:String) extends Person
case class Employee(name:String, floor:String) extends Person
case class CEO(name:String, floor:String) extends Person


