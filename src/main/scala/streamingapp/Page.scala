package streamingapp

import io.circe.Decoder

case class Page (items: List[Person], next: Option[String])

object Page {
  implicit val decoder: Decoder[Page] = Decoder.forProduct2("items", "next")((items, next) => Page(items, next))
}