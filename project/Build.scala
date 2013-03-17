import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "jumpstart"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.mindrot" % "jbcrypt" % "0.3m",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
