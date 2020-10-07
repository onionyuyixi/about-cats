package com.example.aboutcats.cats.functor


sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  def branch[A](leaf: Tree[A], right: Tree[A]): Tree[A] = Branch(leaf, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)
}


object ExerciseFunctor extends App {

  import cats.Functor

  implicit val treeFunctor = new Functor[Tree] {
    override def map[A, B](tree: Tree[A])(f: A => B): Tree[B] =
      tree match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(value) => Leaf(f(value))
      }
  }

  println(Functor[Tree].map(Branch(Leaf(10), Leaf(20)))(_ * 2))

  import cats.instances._
  import cats.syntax.functor._

  //无法编译通过
  //原因是 The compiler can find a Functor instance for Tree but not for Branch or Leaf
  //Branch(Leaf(10), Leaf(20)).map(_ * 2)
  //这种搭配 也不错啊 隐式class 拓展新方法
  implicit class TreeMap[A](src: Tree[A]) {
    implicit def map[B](f: A => B): Tree[B] = treeFunctor.map(src)(f)
  }

  println(Branch(Leaf(10), Leaf(20)).map(_ * 2))
  println(Leaf(20).map(_ * 2))

}

class ExerciseFunctor {

}



