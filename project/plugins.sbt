resolvers := Seq(
  "sumito3478 Maven Repository (pull)" at "http://maven.sumito3478.info/",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots",
  "Maven Repository Mirror" at "http://uk.maven.org/maven2")

externalResolvers <<= resolvers map {
  rs =>
    Resolver.withDefaultResolvers(rs, mavenCentral = false)
}

addSbtPlugin("info.sumito3478" %% "aprikot-sbt" % "0.0.6")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.2.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0")

