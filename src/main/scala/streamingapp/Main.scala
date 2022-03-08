package streamingapp

import io.circe.parser._

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val json = Source.fromFile("/Users/manasi.belekar/project/practice/streaming-app/data/01.json")
    val jsonString = json.getLines().mkString("")
    println(decode[Page](jsonString))
  }
}
