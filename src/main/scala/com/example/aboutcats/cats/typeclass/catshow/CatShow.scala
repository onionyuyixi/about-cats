package com.example.aboutcats.cats.typeclass.catshow

import java.util.Date

class CatShow {

}

case class Person(name: String, age: Int)

object CatShow extends App {

  import cats.Show

  // def apply[A](implicit instance : cats.Show[A]) : cats.Show[A]
  // 这就是伴身对象中apply的一种定义 是interface object的一种用巧
  val showInt = Show.apply[Int]
  println(showInt.show(123))

  //编译不通过 无法通过 没有相应的 instance
  //Show.apply[Person]

  //import cats.instances //package object instances 这之中有许多的已经实现的object


  //Importing Interface Syntax -----enrichment type

  import cats.syntax.show._ //package object syntax  没有这个import 下面的ObjectXx.show都无法成立
  println(123.show) //这种比上面的那种更简洁
  println("Importing Interface Syntax".show)


  //import cats._ //package object cats  imports all of Cats’ type classes in one go;
  //import cats.implicits._ // imports all of the standard type class instances and all of the syntax in one go.

  //Defining Custom Instances
  implicit val personShow = new Show[Person] {
    override def show(person: Person): String = s"姓名：${person.name}----年龄：${person.age}"
  }

  println(Person("onion", 27).show)

  implicit val dateShow: Show[Date] = Show.show(date => s"现在是${date.getTime}ms ")

  println(new Date().show)

  implicit val implicitCatShow = Show.show[Cat] { cat =>
    val name = cat.name.show
    val age = cat.age.show
    val color = cat.color.show
    s"$name is a $age year-old $color cat."
  }

  println(Cat("hello kitty", 10, "yellow").show)

}

final case class Cat(name: String, age: Int, color: String)


