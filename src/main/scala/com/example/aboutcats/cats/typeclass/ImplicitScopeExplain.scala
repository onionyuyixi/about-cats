package com.example.aboutcats.cats.typeclass

import java.util.Date

class ImplicitScopeExplain {

}


// 1 何为implicit scope
// The places where the compiler searches for candidate instances is known as the implicit scope
// implicit scope 有三种形式
// The implicit scope which roughly consists of:
//• local or inherited definitions;  本地或者继承
//• imported definitions; 导入
//• definitions in the companion object of the type class or the parameter
//type (in this case JsonWriter or String) type class 或者 parameter type 的伴生对象
//常见的四种wrapper type class instance 方式
//1. by placing them in an object    使用时 采用import
//2. by placing them in a trait;     使用时  采用继承
//3. by placing them in the companion object of the type class;
//4. by placing them in the companion object of the parameter type.
// 3和4 始终存放在implicit scope中 无论我们在哪里使用type class instance

// 2 伴生对象对implicit scope有奇效
/*
Placing instances in a companion object to the type
class has special significance in Scala
because it plays into something called implicit scope.
将type class instances 放到 trait的伴生对象中  从而对implicit scope做处理影响
 */

//3 定义type class instance 的两种方式
//1. by defining concrete instances as implicit vals of the required type;
//2. by defining implicit methods to construct instances from other type class instances.

// by defining implicit methods to construct instances from other type class instances.
// 可以参考com.example.aboutcats.cats.typeclass.JsonWriterInstances.implicitMethod
// 从而可以尽可能的复用已经定义的instance

object ImplicitMethod extends App {

  import JsonWriterInstances._

  //Recursive Implicit Resolution 递归的隐式处理
  println(JsonOp.toJson(Option("13456"))(new Date()))
  //上一句代码 编译时的处理大致如下
  //JsonOp.toJson(Option("13456"))(new Date())(implicitOptionWriter[String])----> 然后找到对应数据类型的type class instance 作为参数
  //JsonOp.toJson(Option("13456"))(new Date())(implicitOptionWriter(stringWriter))----> 这即所谓的递归
  println(JsonOp.toJson(Person("13456", "aaaaaaaaa@"))(new Date()))

  //这里因为没有Int类型的type class instance 所以无法通过编译
  //println(JsonOp.toJson(Option(123456))(new Date()))

  //by defining implicit methods to construct instances from other type class instances.
  //也可以用一下Interface Syntax(enrichment type)来实现
  //可以说二者在功能上是等同的
  implicit class OptJsonWriterOps[A](option: Option[A]) {
    def toJson(implicit writer: JsonWriter[A]): Json = option match {
      case Some(value) => writer.write(value)
      case None => JsNull
    }
  }


  println(Option(Person("11111", "@@@@")).toJson)
  println(Option("123").toJson)
  //同样是因为无法找到JsonWriter[Int]出现编译错误
  //println(Option(123).toJson)


}


trait TestCompanionImplicitScope[A] {
  def getA: A
}


case class StringTest(value: String) extends TestCompanionImplicitScope[String] {
  override def getA: String = value
}

case class IntTest(value: Int) extends TestCompanionImplicitScope[Int] {
  override def getA: Int = value
}

object TestCompanionImplicitScope {

  def apply[A](implicit implicitScope: TestCompanionImplicitScope[A]) = implicitScope

  implicit val strTest = StringTest // 案 这里调用的是companion obj中的no args constructor

  implicit val intTest = IntTest


}

object TestImplicitScope extends App {
  //伴生对象对implicit scope的影响 如下 完全不需要import操作
  println(TestCompanionImplicitScope(12))
  println(TestCompanionImplicitScope("12"))

}
