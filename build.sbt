ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "task4-scala-covid-api",
    idePackagePrefix := Some("com.innowise")
  )

val AkkaVersion = "2.8.0"
val AkkaHttpVersion = "10.5.1"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
)

val http4sVersion = "1.0.0-M39"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-circe"        % http4sVersion,

)

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

val ironVersion = "2.0.0"

libraryDependencies += "io.github.iltotore" %% "iron" % ironVersion
//"io.github.iltotore::iron-string:1.1-0.1.0",
//ivy"io.github.iltotore::iron-cats:1.1-0.1.0",
//ivy"io.github.iltotore::iron-circe:1.1-0.1.0"