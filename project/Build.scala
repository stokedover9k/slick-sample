import sbt._
import Keys._

object Build extends Build {

  val appName         = "slick-sample"
  val appVersion      = "0.0.1-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.slick" %% "slick" % "3.0.0",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.h2database" % "h2" % "1.4.181",
    "com.typesafe.play" %% "play-slick" % "0.5.0.2-SNAPSHOT",
    "mysql" % "mysql-connector-java" % "latest.release"
  )

  val main =
    play.Project(appName, appVersion)
      .settings(
        libraryDependencies ++= appDependencies,
        resolvers ++= Seq(
          Resolver.sonatypeRepo("releases"),
          "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
        )
      )
}
