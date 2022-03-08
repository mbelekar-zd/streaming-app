package streamingapp

import io.circe.Decoder

case class Person (name: String, age: Int)

object Person {
  implicit val decoder: Decoder[Person] = Decoder.forProduct2("name", "age")((name, age) => Person(name, age))
}
