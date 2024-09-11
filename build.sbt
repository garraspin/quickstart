val Http4sVersion = "0.23.27"
val CirceVersion = "0.14.9"
val Log4j = "2.23.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "quickstart",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "3.3.3",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,

      "org.apache.logging.log4j" % "log4j-api" % Log4j,
      "org.apache.logging.log4j" % "log4j-core" % Log4j,
      "org.apache.logging.log4j" % "log4j-slf4j2-impl" % Log4j,
      "org.apache.logging.log4j" % "log4j-layout-template-json" % Log4j,

      "io.kamon" %% "kamon-bundle" % "2.7.3",
      "io.kamon" %% "kamon-prometheus" % "2.7.3",
      "io.kamon" %% "kamon-http4s-0.23" % "2.6.1",
    ),
  )
