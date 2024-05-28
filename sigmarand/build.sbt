ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"

lazy val root = (project in file("."))
  .settings(
    name := "sigmarand"
  )

resolvers += "Maven Central Server" at "https://repo1.maven.org/maven2"

libraryDependencies += "org.ergoplatform" %% "ergo-appkit" % "5.0.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.4"
