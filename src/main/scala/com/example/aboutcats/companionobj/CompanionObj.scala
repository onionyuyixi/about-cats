package com.example.aboutcats.companionobj

class CompanionObj {

}

//多个参数的构造函数 由companion object的apply 方法来完成


class A(a: Int, b: Int, c: Int) {

  def sum = a+b+c
}

object A {

  def apply(a: Int, b: Int): A = new A(a, b, 1)

  def apply(a: Int): A = new A(a, 1, 1)
}

/*
Scala has two namespaces: a space of type names and a space of value names. This separation
allows us to name our class and companion object the same thing without conflict.

scala 有两种空间  a--->类名空间(class)    b->值空间(companion obj)


It is important to note that the companion object is not an instance of the class—
it is a singleton object with its own type

Companion objects replace Java’static methods.





 */
object TestCompanion extends App {


  val a = A(1)

  println(a.sum)

  val b = A(1,2)

  println(b.sum)


}

