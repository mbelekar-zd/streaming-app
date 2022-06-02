package streamingapp

import io.circe.parser._
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

object Main {
  def main(args: Array[String]): Unit = {
    val stream: Observable[Page] = Extractor.extractPages("/Users/manasi.belekar/project/practice/streaming-app/data/01.json")
    val persons = Transformer.transform(stream).take(4)
    val pageList = persons.toListL.runSyncUnsafe()
    println(pageList)

  }
}
