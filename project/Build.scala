import sbt._
import Keys._

import info.sumito3478.aprikot.sbt._

object build extends Build {

  lazy val project = Project(
    id = "aprikot",
    base = file(".")
  ).settings(StandardProject.newSettings :_*
  ).settings(
    Seq(
      libraryDependencies ++= Seq(
        "org.json4s" %% "json4s-jackson" % "3.1.+",
        "org.json4s" %% "json4s-ext" % "3.1.+",
        "commons-io" % "commons-io" % "2.0.+",
        "com.typesafe.slick" %% "slick" % "1.0.+",
        "com.h2database" % "h2" % "1.3.+",
        "ch.qos.logback" % "logback-classic" % "1.0.+",
        "com.nativelibs4java" % "bridj" % "0.6.2"
        ),
      version := "0.4.0-SNAPSHOT"
    ): _*
  )

}

