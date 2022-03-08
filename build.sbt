ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "streaming-app"
  )
libraryDependencies ++= Seq(
  "io.monix" %% "monix" % "3.4.0",
  "io.circe" %% "circe-core" % "0.15.0-M1",
  "io.circe" %% "circe-parser" % "0.15.0-M1"
)