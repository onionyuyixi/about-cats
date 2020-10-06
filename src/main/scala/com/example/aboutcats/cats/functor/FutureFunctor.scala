package com.example.aboutcats.cats.functor

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class FutureFunctor()

object FutureFunctor extends App {

   val future: Future[String] = Future(123)
    .map(_ + 1).map(_ * 2)
    .map(n => s"${n}!")

  println(Await.result(future, 1.second))
}