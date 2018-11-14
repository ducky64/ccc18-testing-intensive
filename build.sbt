name := "ccc18-testing-intensive"

version := "0.1"

scalaVersion := "2.12.4"

// Needed to support anonymous Bundle definitions under Scala >2.11
scalacOptions := Seq("-deprecation", "-feature", "-Xsource:2.11", "-language:reflectiveCalls")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.2-SNAPSHOT"
libraryDependencies += "edu.berkeley.cs" %% "chisel-testers2" % "0.1-SNAPSHOT"
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.2+"
