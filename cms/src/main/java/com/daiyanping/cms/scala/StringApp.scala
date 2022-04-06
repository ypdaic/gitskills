package com.daiyanping.cms.scala

/**
 * StringApp
 *
 * @author daiyanping
 * @date 2022-04-06
 * @description ${description}
 */
object StringApp extends App {

  val s = "Hello:"
  val name = "PK"
  val team = "AC Milan"
  println(s + name)

  // 字符串插值
  println(s"Hello:$name")

  println(s"Hello:$name, Welcome to $team")

  val b =
    """
      |这是一个多行字符串
      |hello
      |world
      |PK
      |""".stripMargin

  println(b)
}
