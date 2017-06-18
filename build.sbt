name := """yuitter"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.27",
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.springframework.security" % "spring-security-web" % "4.2.3.RELEASE",
  cache,
  filters
)

// code generation task
slick <<= slickCodeGenTask

lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = "./app"
  val username = "root"
  val password = ""
  val url = "jdbc:mysql://localhost/yuitter_development"
  val jdbcDriver = "com.mysql.jdbc.Driver"
  val slickDriver = "slick.driver.MySQLDriver"
  val pkg = "models"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, username, password), s.log))
  val fname = outputDir + "/models/Tables.scala"
  Seq(file(fname))
}
