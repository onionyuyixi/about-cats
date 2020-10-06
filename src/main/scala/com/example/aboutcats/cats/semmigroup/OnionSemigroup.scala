package com.example.aboutcats.cats.semmigroup

trait OnionSemigroup[A] {

  def combine(a: A, b: A): A
}

trait NewMonoid[A] extends OnionSemigroup[A] {
  def empty: A
}

object NewMonoid {
  def apply[A](implicit monoid: NewMonoid[A]) = monoid
}

case object BooleanAndMonoid extends NewMonoid[Boolean] {
  override def empty: Boolean = true

  override def combine(a: Boolean, b: Boolean): Boolean = a && b
}

case object BooleanOrMonoid extends NewMonoid[Boolean] {
  override def empty: Boolean = false

  override def combine(a: Boolean, b: Boolean): Boolean = a || b
}


case class SetUnionMonoid[A]() extends NewMonoid[Set[A]] {
  override def empty: Set[A] = Set.empty[A]

  override def combine(a: Set[A], b: Set[A]): Set[A] = a union b
}


case class SetIntersectionSemiGroup[A]() extends OnionSemigroup[Set[A]] {
  override def combine(a: Set[A], b: Set[A]): Set[A] = a intersect b
}

object Main extends App {

  implicit val intSetUnionMonoid = new SetUnionMonoid[Int]
  implicit val strSetUnionMonoid = new SetUnionMonoid[String]

  val intSetMonoid: NewMonoid[Set[Int]] = NewMonoid[Set[Int]]
  val strSetMonoid: NewMonoid[Set[String]] = NewMonoid[Set[String]]

  println(intSetMonoid.combine(Set(1, 2, 3, 4), Set(3, 4, 5, 6)))
  println(strSetMonoid.combine(Set("j", "k", "L"), Set("k", "q")))


}


/*
If we define a Monoid for a type A, we get a Semigroup for free.
Similarly, if a method requires a parameter of type Semigroup[B], we can pass a Monoid[B] instead
 */
