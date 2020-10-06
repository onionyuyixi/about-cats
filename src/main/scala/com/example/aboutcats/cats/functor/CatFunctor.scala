package com.example.aboutcats.cats.functor

import cats.Functor


class CatFunctor {

}

object CatFunctor extends App {

  //  import cats.instances.function._

  import cats.syntax.functor._

  val func1: Int => Double = (x: Int) => x.toDouble

  val func2 = (y: Double) => y * 2

  (func1 map func2) (1)
  (func1 andThen func2) (1)
  func2(func1(1))

  val func = ((x: Int) => x.toDouble)
    .map(_ * 2)
    .map(n => s"${n}")

  print(func(12))


  // Declare F using underscores:
  //  def myMethod[F[_]] = {
  // Reference F without underscores:
  //    val functor = Functor.apply[F]
  //  }

  // Declare f specifying parameter types
  def f(x: Int): Int =
  // Reference x without type
    x * 2

  //上面的两段代码相似  对于参数我们的使用方式 都是先申明 再直接调用其名即可
  //所谓type constructor 便是申明type 变量


  val list1 = List(1, 2, 3)
  println(Functor[List].map(list1)(_ * 2))
  list1.map(_ * 2)

  val option1 = Option(123)
  val op2 = Functor[Option].map(option1)(_.toString)
  val unLiftedFunc = (x: Int) => x.toString
  //lift func
  val liftedFunc: Option[Int] => Option[String] = Functor[Option].lift(unLiftedFunc)
  println(liftedFunc(option1) == op2) //结果一致 true

  //as func
  println(Functor[List].as(List(1, 2, 3), "xi") == List("xi", "xi", "xi")) //true 意在覆盖 替代

  //以下部分 使用functor ---type constructor实例
  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] = functor.map(start)(_+2)

  //def doMath方法 非常类似type class 中的 implicit method 
  implicit class DoMath[F[_], A](src: F[A]) {
    def doMath[B](func: A => B)(implicit functor: Functor[F]): F[B] = functor.map(src)(func)
  }


  doMath(Option(12))

  //  doMath(Set(1,2))

  import cats.instances.list._

  doMath(List(1, 2))
  //只能匹配Int类型
  //doMath(List("1", "2"))

  //无效 表明set 没有functor instance

  import cats.instances.set._

  implicit val setFunctorInstance = new Functor[Set] {
    override def map[A, B](fa: Set[A])(f: A => B): Set[B] = fa map f
  }
  println(Set(3, 4, 5).doMath(_ + 1))

  implicit val myIntListFunctor = new Functor[MyIntList] {
    override def map[A, B](fa: MyIntList[A])(f: A => B): MyIntList[B] = MyIntList(fa.ints.map(f))
  }


  //MyIntList 有了新方法哇
  println(MyIntList(List(1, 2, 3)).doMath(_ * 2) == MyIntList(List(2, 4, 6)))


  println(Functor[MyIntList].map(MyIntList(List(1, 5, 3)))(_ * 2))

  import cats.instances.vector._

  println(Vector(1, 2, 3).doMath(_ * 3))


  implicit val myStrList = new Functor[MyStrList] {
    override def map[A, B](fa: MyStrList[A])(f: A => B): MyStrList[B] =
      MyStrList(fa.strs.map(f))
  }
  println(MyStrList(List("4", "5", "6")).doMath(_.toInt + 1))


  import scala.concurrent.{Future, ExecutionContext}

  implicit def futureFunctor(implicit ex: ExecutionContext): Functor[Future] = new Functor[Future] {
    override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa map f
  }


}

trait MyList[A] {
  def getStr(list: List[A]): String = list.toString
}

case class MyIntList[Int](ints: List[Int]) extends MyList[Int] {
}

case class MyStrList[String](strs: List[String]) extends MyList[String] {
}