package streamingapp

import io.circe
import monix.eval.Task
import monix.reactive.Observable

import scala.io.Source
import io.circe.parser._

object Extractor {
  def extractPages(filename: String): Observable[Page] = {
    val x: Task[Page] = readFile(filename).map(stringToPage).flatMap(Task.fromEither(_))
    val y: Task[Observable[Page]] = x.map(generateStream(_, Observable.empty))
    Observable.fromTask(y).flatten
  }

  private def generateStream(page: Page, pages: Observable[Page]): Observable[Page] = {
    page.next match {
      case Some(value) =>
       val readNext: Task[Page] = readFile(value).map(stringToPage).flatMap(Task.fromEither(_))
        val nextPage: Observable[Page] = Observable.fromTask(readNext)
        pages ++ nextPage
      case None => Observable.empty[Page]
    }
  }

  // Wrapped in a task to make it re-usable
  private def readFile(filename: String): Task[String] = Task {
    val json = Source.fromFile(filename)
    json.getLines().mkString("")
  }

  private def stringToPage(str: String): Either[circe.Error, Page] = {
    decode[Page](str)
  }
}
