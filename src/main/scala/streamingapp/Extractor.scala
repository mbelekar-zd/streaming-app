package streamingapp

import io.circe
import monix.eval.Task
import monix.reactive.Observable

import scala.io.Source
import io.circe.parser._

object Extractor {
  def extractPages(filename: String): Observable[Page] = {
    val x: Observable[Page] = Observable.fromTask(readFile(filename).map(stringToPage).flatMap(Task.fromEither(_)))
    x.flatMap(generateStream(_, Observable.empty))
  }

  private def generateStream(page: Page, pages: Observable[Page]): Observable[Page] = {
    page.next match {
      case Some(value) =>
        val readNext: Task[Page] = readFile(value).map(stringToPage).flatMap(Task.fromEither(_))
        val nextPage: Observable[Page] = Observable.fromTask(readNext)
        nextPage.flatMap(p => generateStream(p, pages.append(page)))
      case None => pages.append(page)
    }
  }

  // Wrapped in a task to make it re-usable
  private def readFile(filename: String): Task[String] = Task {
    println(s"Reading file ${filename}")
    val json = Source.fromFile(filename)
    json.getLines().mkString("")
  }

  private def stringToPage(str: String): Either[circe.Error, Page] = {
    decode[Page](str)
  }
}
