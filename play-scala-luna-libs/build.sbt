name := """play-scala-luna-libs"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += jdbc
// https://mvnrepository.com/artifact/org.postgresql/postgresql
//libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1200-jdbc41"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "1.1.0"
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0"
