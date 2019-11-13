name := "scala-midi"

version := "0.1"

scalaVersion := "2.13.1"


lazy val root = (project in file("."))
  .settings(
    name := "notation-structure",
    fork in run := true,
    connectInput in run := true,
  )