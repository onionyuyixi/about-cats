package com.example.aboutcats.cats.monoid

trait OnionMonoid[A] {

  def combine(a: A, b: A): A

  def empty: A

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: OnionMonoid[A]): Boolean = {
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
  }

  def identityLaw[A](x: A)(implicit m: OnionMonoid[A]): Boolean = {
    (m.combine(x, m.empty) == x) && (m.combine(m.empty, x) == x)
  }


}


