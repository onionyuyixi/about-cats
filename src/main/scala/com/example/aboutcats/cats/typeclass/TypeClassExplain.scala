package com.example.aboutcats.cats.typeclass

import java.util.Date

class TypeClassExplain {

}

/*
何为Type class ----->至少有一个type parameter 的 trait
In Scala a type class is represented by a trait with at least one
type parameter
 */

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

final case object JsNull extends Json

//type class
trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)

//type class instance with implicit values which is arranged in a object
object JsonWriterInstances {
  implicit val stringWriter = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val personWriter = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))
  }

  //相较于普通的instance 这里采用了match pattern进行了进一层的数据抽象
  implicit def implicitOptionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
    (option: Option[A]) => option match {
      case Some(value) => writer.write(value)
      case None => JsNull
    }

}

//type class use
// which means use type class instances as  parameters with implicit
//and there are two ways to complete this goal
// 1 interface objects  2 Interface Syntax

//1 interface (which could use a type class trait)companion object
object JsonOp extends App { //对应sealed trait Json

  //这样我们就能import trait （type class instance）简化代码
  //自动匹配对应的implicit list
  def toJson[A](value: A)(date: Date)(implicit writer: JsonWriter[A]): Json =
    writer.write(value)


  import JsonWriterInstances._

  //JsObject(Map(name -> JsString(onion), email -> JsString(onion@qq.com)))
  println(JsonOp.toJson(Person("onion", "onion@qq.com"))(new Date()))
  //JsString(123)
  println(JsonOp.toJson("123")(new Date()))

}

//2 Interface Syntax  alias type enrichment
// 通过class 构造器传参 将value 与 implicit 参数 分离开来
// but 构造器只能带一个参数 这就不如interface objects 灵活了
object JsonSyntax extends App {

  //interface object的参数分离版本 可以增加type parameter所对应的class的方法
  //这种灵活性偏向于class 故而才定义了class级别的implicit
  //不过从class的层面去添加特性 要比interface object 更为稳定 编译更轻松 匹配更直接
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit writer: JsonWriter[A]): Json =
      writer.write(value)
  }


  import JsonWriterInstances._

  println(Person("onion", "onion@email").toJson)
  println("onion".toJson)


}

object ImplicitlyCase extends App {

  import JsonWriterInstances._

  //另外还可以采用implicitly修饰 相当于import  加上 自动匹配
  // 因此可以用来debug程序 是否则正确匹配到了implicit 参数
  private val personWriter1: JsonWriter[Person] = implicitly[JsonWriter[Person]]

  println(personWriter1.write(Person("1", "@")))

  //这种方法是两行代码的简写
  def write[A: JsonWriter](x: A): Json = implicitly[JsonWriter[A]].write(x)

  println(implicitly[JsonWriter[Person]].write(Person("1", "@")))

  println(write(Person("aaaaa", "!!!!!")))
}

//trait companion obj with apply method carried with Type class alias
//Type Class Interface Pattern
//这样采用伴生对象的默认方法 就更加简洁了
//这其实是Interface obj的一种特定变形 可以参考object JsonOp
object Json extends App {

  def apply[A](value: A)(implicit writer: JsonWriter[A]): Json = writer.write(value)

  import JsonWriterInstances._

  println(Json(Person("1", "@")))
}