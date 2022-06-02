package streamingapp

import io.circe
import io.circe.parser._
import monix.eval.Task
import monix.reactive.Observable

import scala.io.Source

object Extractor {
//  def paginateEval[S, A](seed: => S)(f: S => Task[(A, Option[S])]): Observable[A] =
//    new PaginateEvalObservable(seed, f)

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

//  def extractPages(filename: String): Observable[Page] = {
//    Observable.paginateEval(filename){ filename =>
//      val readPage: Task[Page] = readFile(filename).map(stringToPage).flatMap(Task.fromEither(_))
//      readPage.map (page =>(page, page.next))
//    }
//  }

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
