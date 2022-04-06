package com.daiyanping.cms.scala

/**
 * SimpleObjectApp
 *
 * @author daiyanping
 * @date 2022-03-28
 * @description ${description}
 */
object SimpleObjectApp {

  def main(args: Array[String]): Unit = {
    val people = new People();
    people.name = "Messi"
    people.age = 30
    people.watchFootball("china")
    println(people.eat())
    people.printInfo()
  }


}

class People {

  // 定义属性
  var name:String = ""
  // _ 表示占位符
//  var name:String = _
  var age:Int = 10

  // 私有属性
  private [this] var gender = "male"

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
