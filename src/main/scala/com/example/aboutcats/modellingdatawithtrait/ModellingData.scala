package com.example.aboutcats.modellingdatawithtrait

import java.util.Date

trait ModellingData {

}


case class User(id: String, email: String, createdAt: Date = new Date())

case class Anonymous(id: String, createdAt: Date = new Date())

trait Visitor {
  def id: String

  def createdAt: Date

  def age: Long = new Date().getTime - createdAt.getTime
}

case class AnonymousWithTrait(id: String, createdAt: Date) extends Visitor

case class UserWithTrait(id: String, email: String, createdAt: Date) extends Visitor


sealed trait Visitor1 {
  def id: String

  def createdAt: Date
}


case class User1(id: String, createdAt: Date = new Date()) extends Visitor1
case class User2(id: String, createdAt: Date = new Date()) extends Visitor1


object TestTrait extends App {

  def testTrait(x: Visitor1) = x match {
    case User1(_, _) => println(x.id + "-------"+x.createdAt)
  }

  testTrait(User1("id"))


}

//Product Type pattern   has-a  and
//case class A(b:B,c:C)
//trait A {
//  def b:B
//  def c:C
//}

//Sum Type Pattern      is-a     or
sealed trait A
final case class B() extends A
final case class C() extends A