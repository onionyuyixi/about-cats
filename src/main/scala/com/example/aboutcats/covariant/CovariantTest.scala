package com.example.aboutcats.covariant

class CovariantTest {

}


sealed trait Maybe[+A]

final case class Full[A](value: A) extends Maybe[A]

final case class Empty[A]() extends Maybe[A]

//Objects can’t have type parameters
final case object Empty extends Maybe[Nothing]


case class Box[+A](value: A) {
  //Error:(9, 11) covariant type A occurs in contravariant position in type A of value a
  //  def set(a:A):Box[A] = Box(a)
  //Remember that functions, and hence methods, which are just like functions, are contravariant in their input parameters.
  //方法和函数 对于input params是逆向协变的 即往上追溯可以找到Input type params
  //AA >: A 表示AA是A的父类  A 即是class type parameter

  def set[AA >: A](a: AA): Box[AA] = Box(a)
}


object CovariantTest extends App {
  val possible: Maybe[Int] = Empty()
  //Error:(28, 33) type mismatch;
  // found   : com.example.aboutcats.covariant.Empty.type
  // required: com.example.aboutcats.covariant.Maybe[Int]
  // The problem here is that Empty is a Maybe[Nothing] and a Maybe[Nothing] is not a subtype of Maybe[Int]
  //  val possible1: Maybe[Int] = Empty

  /*
  加入协变（+A）以后 上面的不成立通过编译
   */
  val possible1: Maybe[Int] = Empty


}

sealed trait Sum[+A, +B] {
  def flatMap[AA >: A, C](f: B => Sum[AA, C]): Sum[AA, C] =
    this match {
      case Success(b) => f(b)
      case Failure(a) => Failure(a)
    }
}

final case class Failure[A](value: A) extends Sum[A, Nothing]

final case class Success[B](value: B) extends Sum[Nothing, B]