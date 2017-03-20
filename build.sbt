name := "Trumpeteer"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= {
  val akkaV = "2.4.3"
  Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.3",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.3",
    "org.twitter4j" % "twitter4j-core" % "4.0.4",
    //"org.scalatest" % "scalatest_2.12" % "3.0.1" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.mockito" % "mockito-core" % "1.8.5"
  )
}
    