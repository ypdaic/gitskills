package com.daiyanping.cms.scala

/**
 * ApplyApp
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object ApplyApp {

  def main(args: Array[String]): Unit = {
    for(i <- 1 to 10) {
      // object类型的可以直接调用

      ApplyTest.incr
    }
    // 结果是10, 说明oject是一个单例对象
    println(ApplyTest.count)

    val b = ApplyTest() // 调用的 Object.apply

    val c = new ApplyTest()
    println(c)
    c() // 调用的 class.apply
  }

}

// 伴生类和伴生对象
// class ApplyTest 为 object ApplyTest 伴生对象
// 如果还有一个class,还有一个与class同名的object
// 那么就称这个object是class的伴生对象，class是object的伴生类
class ApplyTest {

  def apply(): Unit = {
    println("class ApplyTest apply....")
    new ApplyTest
  }
}

// object ApplyTest 为 class ApplyTest 伴生类
object ApplyTest {

  var count = 0

  def incr = {
    count = count + 1
  }

  // 最佳实践：在Object的apply方法中去new Class
  def apply(): ApplyTest = {
    println("Object ApplyTest apply....")
    new ApplyTest
  }
}
