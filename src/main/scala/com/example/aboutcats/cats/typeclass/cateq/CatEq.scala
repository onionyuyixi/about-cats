package com.example.aboutcats.cats.typeclass.cateq

import java.util.Date

import com.example.aboutcats.cats.typeclass.catshow.Cat

class CatEq {

}


object CatEq extends App {


  import cats.Eq

  val eqInt = Eq[Int]
  println(eqInt.eqv(1, 2))

  import cats.syntax.eq._

  /*
  package cats.syntax
  final class EqOps
   */
  println(123.eqv(345))


  import cats.instances.int._
  import cats.instances.option._

  println((Some(1): Option[Int]) =!= (None: Option[Int]))


  //Comparing Custom Types
  implicit val dateEq = Eq.instance[Date] {
    (date1, date2) => date1.getTime === date2.getTime
  }

  val x = new Date()

  val y = new Date()

  println(x === x)

  println(x === y)


  implicit val catEq: Eq[Cat] = Eq.instance[Cat]((a, b) => {
    a.name === b.name && a.age === b.age && a.color === b.color
  })

  println("catEq result ---->" + (Cat("1", 1, "2") === Cat("1", 1, "2")))

  val optionCat = Option(Cat("1", 1, "2"))
  val emptyCat = Option.empty[Cat]

  println("opt result ------>" + (optionCat === emptyCat))


}


trait Shape {
  def getDesc: String
}

case class Circle(val desc: String, r: Double) extends Shape {
  override def getDesc: String = desc + "半径" + r
}

object TestCovariance extends App {

  //covariance 类似多态
  val circles: List[Circle] = List(Circle("a", 1), Circle("b", 2))
  println(circles)
  val shapes: List[Shape] = circles
  println(shapes)


  def write[A](value: A, writer: StringWriter[A]) = writer.write(value)

  //
  println(write(Circle("001", 2), StringShapeWriter()))

}

//contravariance 即颠倒之意 子可为父 恰如后来居上
// 该接口的两个实现类 StringShapeWriter  StringCircleWriter
// 由于Circle是shape的子类 那么StringShapeWriter则是StringCircleWriter的子类
trait StringWriter[-A] {
  def write(value: A): String
}

case class StringShapeWriter() extends StringWriter[Shape] {
  override def write(shape: Shape): String = shape.getDesc
}

case class StringCircleWriter() extends StringWriter[Circle] {
  override def write(value: Circle): String = value.getDesc
}
