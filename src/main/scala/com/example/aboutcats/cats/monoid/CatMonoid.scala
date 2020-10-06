package com.example.aboutcats.cats.monoid

class CatMonoid {

}

import cats.Monoid
import cats.instances.string._

object CatMonoid extends App {

  println(Monoid[String].combine("Hi", "onion") == Monoid[String].combine("onion", "Hi"))
  println(Monoid[String].empty + "--")

  import cats.instances.option._

  val a = Option(22)

  val b = Option(20)

  println(Monoid[Option[Int]].combine(a, b))

  //上面是去用Monoid去调用combine方法 接下来采用enrichment type
  //可以更简洁 直接采用Ops操作

  import cats.syntax.semigroup._

  val stringResult = "Hi" |+| "there" |+| Monoid[String].empty
  println(stringResult)


  import cats.instances.int._


  println(1 |+| 2 |+| Monoid[Int].empty)


  println(Map("1" -> 1) |+| Map("2" -> 2))





}
