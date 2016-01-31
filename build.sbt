name := "exercise-dispatch"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.apache.commons" % "commons-email" % "1.4",

  // tests
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.jvnet.mock-javamail" % "mock-javamail" % "1.9"
)