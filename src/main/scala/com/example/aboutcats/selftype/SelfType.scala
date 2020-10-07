package com.example.aboutcats.selftype

class SelfType {

}


trait A {
  def aPrint(x: Any) = println(x)
}

class B(value: String) {
  a: A =>

  def outputValue = a.aPrint(value)

}

//if not  mix trait A ,compiler will promote a warn
//C 继承了B 但B中混入了A 别名做a 所以B的子类必须混入A
class C(val value: String, desc: String) extends B(value: String) with A {
  override def outputValue: Unit = aPrint(value + "------" + desc)
}

////或者直接混入也行
class D(val value: String, desc: String) extends B(value: String) {
  a1: A =>
  override def outputValue: Unit = a1.aPrint(value + "------" + desc)
}

class E(e: String) {
  self =>
  def whatIsContent = println(e)
}

object SelfType extends App {

  val serviceB = new B("B") with A
  serviceB.outputValue
  new C("c", "c").outputValue

  val e = new E("extend")
  e.whatIsContent
}