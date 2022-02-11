name := "akka-streams"

version := "0.1"

scalaVersion := "2.13.5"

val AkkaVersion = "2.6.14"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)