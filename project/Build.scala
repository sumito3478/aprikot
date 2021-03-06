import sbt._
import Keys._

import info.sumito3478.aprikot.sbt._

object build extends Build {
  lazy val simpleDist = TaskKey[Unit]("simple-dist")

  def simpleDistTask = simpleDist <<= (update, crossTarget, scalaVersion) map {
    (updateReport, out, scalaVer) =>
      updateReport.allFiles foreach {
        srcPath =>
          val destPath = out / "dist" / srcPath.getName
          IO.copyFile(srcPath, destPath, preserveLastModified = true)
      }
  }

  lazy val project = Project(
    id = "aprikot",
    base = file(".")
  ).settings(StandardProject.newSettings :_*
  ).settings(
    Seq(
      simpleDistTask,
      libraryDependencies ++= Seq(
        "org.mongodb" %% "casbah" % "2.5.+",
        "org.scribe" % "scribe" % "1.3.+",
        "org.slf4j" % "slf4j-api" % "[1.7.5,1.7.1000]",
        "com.typesafe.akka" %% "akka-actor" % "[2.1.2,2.1.1000]",
        "org.json4s" %% "json4s-jackson" % "3.1.+",
        "org.json4s" %% "json4s-ext" % "3.1.+",
        "commons-io" % "commons-io" % "[2.4,2.4.1000]",
        "com.typesafe.slick" %% "slick" % "1.0.+",
        "com.h2database" % "h2" % "1.3.+",
        "ch.qos.logback" % "logback-classic" % "1.0.+",
        "com.nativelibs4java" % "bridj" % "0.6.2"
        ),
      version := "0.5.0"
    ): _*
  )

}

