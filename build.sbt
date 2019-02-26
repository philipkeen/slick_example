name := """slick_example"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.6", "2.11.12")

libraryDependencies += guice
libraryDependencies += evolutions
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0"
)
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
