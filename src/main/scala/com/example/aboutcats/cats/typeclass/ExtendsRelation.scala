package com.example.aboutcats.cats.typeclass

class ExtendsRelation {

}

trait CanFoo[A] {
  def foos(x: A): String
}

trait Vehicle {

}

case class Automobile() extends Vehicle

object Vehicle {
  implicit val vehicleAutomobileFoo = new CanFoo[Automobile] {
    override def foos(x: Automobile): String = "companionAutomobileFoo" + x.toString
  }
}

object Automobile {
  implicit val companionAutomobileFoo = new CanFoo[Automobile] {
    override def foos(x: Automobile): String = "companionAutomobileFoo" + x.toString
  }
}

object Main extends App {

  /*
  由于继承关系并不适应于companion obj 所以import只有下面的那一个有用
   */
  import Vehicle._
  import Automobile._

  def test(): String = foo(Automobile())

  def foo[A: CanFoo](a: A): String = implicitly[CanFoo[A]].foos(a)

  println(test) //companionAutomobileFooAutomobile()


}