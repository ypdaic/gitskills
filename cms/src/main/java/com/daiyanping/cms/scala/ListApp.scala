package com.daiyanping.cms.scala

/**
 * ListApp
 *
 * @author daiyanping
 * @date 2022-03-29
 * @description ${description}
 */
object ListApp extends App{

  val l = List(1,2,3,4,5);

  println(l.head)
  println(l.tail)

  // 相当于List(1)
  val l2 = 1 :: Nil
  println(l2)

  val l3 = 2 :: l2
  println(l3)

  val l4 = 1 :: 2 :: 3 :: l3
  println(l4)

  // 非定长集合
  val l5 = scala.collection.mutable.ListBuffer[Int]()

  l5 += 2
  l5 += (3,4,5)
  println(l5)

  l5 -= 2
  l5 -= 3
  println(l5)

  def sum(nums:Int*):Int = {
    if (nums.length == 0) {
      0
    } else {
      nums.head + sum(nums.tail:_*)
    }
  }

  println(sum(1,2,3,4))
}
