package com.daiyanping.cms.scala

/**
 * TraitApp
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object TraitApp {

  def main(args: Array[String]): Unit = {

  }

}

// 相当于java的接口
trait A {

}

// 相当于java的接口
trait B {

}

// 多继承
class C extends A with B {

}