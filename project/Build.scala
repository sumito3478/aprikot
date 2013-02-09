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
        "ch.qos.logback" % "logback-classic" % "1.0.+",
        "net.java.dev.jna" % "jna" % "3.5.+"
        ),
      version := "0.2.0-SNAPSHOT"
    ): _*
  )

}

