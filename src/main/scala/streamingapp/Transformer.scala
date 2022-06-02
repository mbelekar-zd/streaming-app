package streamingapp

import monix.eval.Task
import monix.reactive.Observable

object Transformer {
  def transform(pages: Observable[Page]): Observable[Person] = {
//    pages.map(_.items).map(Observable.fromIterable).flatten
    pages.flatMap { page =>
      if (page.items.isEmpty) {
        Observable.fromTask(Task(print("Found empty items"))).flatMap(_ => Observable.empty) // Observable.empty[Person]
      } else {
        Observable.fromIterable(page.items)
      }
    }.mapEval(person => Task(println(person.name)).as(person))
  }
}
