lazy val root = (project in file(".")).
  settings(
    name := "adventofcode2018",
    version := "1.0",
    scalaVersion := "2.12.7"
  )

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
