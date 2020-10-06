package com.example.aboutcats.tocase

/*
Whenever we declare a case class, Scala automatically generates a class and companion object

case class 乃是 class 与其 companion obj的简写

另外 带上了 hashcode equals toString copy方法
 */
class ToCase {

}


case class Person(firstName: String, lastName: String)


object ToCaseTest extends App {
  val person = Person("onion", "yuyixi")
  val person1 = new Person("onion", "yuyixi")
  println(person == person1) // true
  println(person) // true
  println(person1) // true

  println(person.copy("yangcong")) //可选择的复制
  println(person.copy(firstName = "yangcong")) //可选择的复制
  println(person.copy(lastName = "yangcong")) //可选择的复制


}

/*
A final note. If you find yourself defining a case class with no constructor arguments you can instead a define
a case object. A case object is defined just like a case class and has the same default methods as a case class
 */
case object Citizen {
  def firstName = "John"

  def lastName = "Doe"

  def name = firstName + " " + lastName
}

//case object相当于

class Citizen1 {

}

object Citizen1 extends Citizen1 {

  def firstName = "John"

  def lastName = "Doe"

  def name = firstName + " " + lastName

}