package com.example.aboutcats.cats.typeclass.exercise

final case class Cat(name: String, age: Int, color: String)


final case object CatPrintable extends Printable[Cat] {
  override def format(cat: Cat): String = s"${cat.name} is a ${cat.age} year-old ${cat.color} cat"
}

trait Printable[A] {


  def format(input: A): String
}

trait Printable1[A] {


  def format(): String
}

object Printable extends App {

  def apply[A](implicit printable: Printable[A]) = printable

  implicit val stringPrint = new Printable[String] {
    override def format(input: String): String = input
  }

  implicit val intPrint = new Printable[Int] {
    override def format(input: Int): String = input.toString
  }

  implicit val catPrint = CatPrintable
  //  implicit val catPrint1 = CatPrintable1
  //catPrint  vs  catPrint1 得知需要的是Companion中的no args constructor 正应对着 new 关键字

  def formatInput[A](a: A)(implicit printable: Printable[A]) = printable.format(a)

  def print[A](a: A)(implicit printable: Printable[A]) = println(formatInput(a))

  formatInput(123)
  formatInput("123")
  print("str123")
  print(123)

  println(formatInput(Cat("ketty", 12, "blue")))
  print(Cat("ketty", 12, "blue"))

  import PrintableSyntax._
  //将object Printable修改成 object PrintableXx 会出现implicit scope无法找到的错误
  //这既是伴生对象的好处
  Cat("123", 23, "white").print


}


final case class CatPrintable1(cat: Cat) extends Printable1[Cat] {
  override def format: String = s"${cat.name} is a ${cat.age} year-old ${cat.color} cat"
}


object PrintableSyntax extends App {

  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String =
      p.format(value)

    def print(implicit p: Printable[A]): Unit =
      println(format(p))
  }


}