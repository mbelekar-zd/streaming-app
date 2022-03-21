package streamingapp

import io.circe.parser._
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

object Main {
  def main(args: Array[String]): Unit = {
    val stream: Observable[Page] = Extractor.extractPages("/Users/manasi.belekar/project/practice/streaming-app/data/01.json").head
    val pageList = stream.toListL.runSyncUnsafe()
    println(pageList)
  }
}
