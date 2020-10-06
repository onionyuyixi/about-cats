package com.example.aboutcats.typeclass

import java.util.Date

class TypeClassTest {

}

trait HtmlWriteable {
  def toHtml: String
}

final case class Person(name: String, email: String) extends HtmlWriteable {
  override def toHtml: String = s"<span>$name&lt;$name&gt;<span>"
}

// 没有使用Type class 不易拓展  转而使用pattern match
//object HtmlWriter {
//  def write(in: Any): String = in match {
//    case Person(_, _) => {
//      println(in);
//      in.toString
//    }
//    case Date => {
//      println(in); in.toString
//    }
//    case _ => throw new Exception(s"Can't render ${in} to Html")
//  }
//}

/*
Type Class Pattern
A type class is a trait with at least one type variable.
The type variables specify the concrete types the
type class instances are defined for.
Methods in the trait usually use the type variables.
 */
trait HtmlWriter[A] {
  def write(a: A): String
}

/*
Type Class Interface Pattern
If the desired interface to a type class TypeClass is exactly the methods defined on the type class trait,
define an interface on the companion object using a no-argument apply method like
object TypeClass {
def apply[A](implicit instance: TypeClass[A]): TypeClass[A] =
instance
}
 */
object HtmlWriter {
  def apply[A](implicit writer: HtmlWriter[A]): HtmlWriter[A] = writer
}

object PersonWriter extends HtmlWriter[Person] {
  override def write(person: Person): String = s"<span>${person.name} &lt;${person.email}&gt;</span>"
}

object DateWriter extends HtmlWriter[Date] {
  override def write(in: Date): String = s"<span>${in.toString}</span>"
}


trait Equal[A] {
  def equalsByFiled(a: A, b: A): Boolean
}

object Equal {
  def apply[A](a: A, b: A)(implicit equal: Equal[A]): Boolean =
    equal.equalsByFiled(a, b)
}


object PersonEmailEqual extends Equal[Person] {
  override def equalsByFiled(a: Person, b: Person): Boolean = a.email == b.email
}

object PersonEqual extends Equal[Person] {
  override def equalsByFiled(a: Person, b: Person): Boolean = (a.email == b.email) && (a.name == b.name)
}


object NameAndEmailImplicit {

  implicit object NameEmailEqual extends Equal[Person] {
    override def equalsByFiled(a: Person, b: Person): Boolean =
      (a.name == b.name) && (a.email == b.email)
  }

}

object EmailImplicit {

  implicit object EmailEqual extends Equal[Person] {
    override def equalsByFiled(a: Person, b: Person): Boolean =
      a.email == b.email
  }

}

object TypeClassTest extends App {
  println(Person("John", "john@example.com").toHtml)
  println(PersonWriter.write(Person("John", "john@example.com")))
  println(DateWriter.write(new Date))
  println(HtmlUtil.htmlify(Person("John", "john@example.com"))(PersonWriter))

  implicit object ApproximationWriter extends HtmlWriter[Int] {
    override def write(in: Int): String = s"It's definitely less than ${((in / 10) + 1) * 10}"
  }

  //隐式空间参数列表 神似match case
  println(HtmlUtil.htmlify(2)(ApproximationWriter) == HtmlUtil.htmlify(2))

  def testA: Unit = {
    import NameAndEmailImplicit._
    //如果没有companion obj则无法直接使用 static方法
    println(Equal(Person("1", "2"), Person("1", "2")))
  }

  def testB: Unit = {
    import EmailImplicit._
    println(Equal(Person("1", "2"), Person("1", "2")))
  }

  testA
  testB

  /*
  When the compiler processes our call to numberOfVowels, it interprets it as a type error because there is no
such method in String. Rather than give up, the compiler attempts to fix the error by searching for an implicit
class that provides the method and can be constructed from a String. It finds ExtraStringMethods. The
compiler then inserts an invisible constructor call, and our code type checks correctly.
   */
  //但是只能带一个参数的构造器  赋予String type 新的api 即所谓 Type enrichment
  implicit class ExtraStringMethod(string: String) {
    val vowels = Seq('a', 'e', 'i', 'o', 'u')

    def numbersOfVowels = string.toList.count(vowels contains _)
  }

  println("aeiou".numbersOfVowels)

  implicit class NumAndStr(num: Int) {
    def xx() = 2 * num
  }

  println(4.xx)


  implicit class HtmlOps[T](data: T) {
    def toHtml(implicit writer: HtmlWriter[T]) = writer.write(data)
  }

  println(Person("yuyixi", "yuyixi@gamil.com").toHtml)


  import IntImplicits._

  2.yeah

  3.times(i => println(s"Look - it's the number $i!"))

  implicit class EqualString(str: String) {
    def ===(string: String): Boolean = if (str.compareToIgnoreCase(string) == 0) true else false
  }


  println("a1w2e3".===("A1W2E3"))

  import IntImplicits1._
  6.times(println)

}


object HtmlUtil {
  /*
  The implicit keyword applies to the whole parameter list, not just an individual parameter. This makes the
parameter list optional—when we call HtmlUtil.htmlify we can either specify the list as normal
   */
  def htmlify[A](data: A)(implicit writer: HtmlWriter[A]): String =
    writer.write(data)
}


object IntImplicits {

  implicit class IntString(num: Int) {
    def yeah = (0 until num).foreach(a => println("Oh yeah!"))

    def times(f: Int => Unit) = (0 until num).foreach(f)
  }

}

object IntImplicits1 {

  class IntString(num: Int) {
    def yeah = (0 until num).foreach(a => println("Oh yeah!"))

    def times(f: Int => Unit) = (0 until num).foreach(f)
  }

  implicit def intToIntString(value: Int) = new IntString(value)

}

object ContextTypeClass {

  case class Example(name: String)

  implicit val implicitExample = Example("implicit")
}


/*
Implicit classes are actually just syntactic sugar for the combination of a regular class and an implicit conversion.
With an implicit class we have to define a new type as a target for the conversion; with an implicit method we
can convert from any type to any other type as long as an implicit is available in scope
 */


