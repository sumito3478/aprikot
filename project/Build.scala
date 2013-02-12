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
        "commons-io" % "commons-io" % "2.0.+",
        "com.typesafe.slick" %% "slick" % "1.0.+",
        "com.h2database" % "h2" % "1.3.+",
        "ch.qos.logback" % "logback-classic" % "1.0.+",
        "net.java.dev.jna" % "jna" % "3.5.+"
        ),
      version := "0.2.1-SNAPSHOT"
    ): _*
  )

}

