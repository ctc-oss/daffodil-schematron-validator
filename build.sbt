organization := "com.ctc"
name := "daffodil-schematron-validator"
version := "0.1"
scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "com.helger" % "ph-schematron" % "5.6.1"
)

lazy val ref="git://github.com/jw3/incubator-daffodil.git#validator_spi/impl"
dependsOn(
  ProjectRef(uri(ref), "daffodil-lib"),
  ProjectRef(uri(ref), "daffodil-cli").%(conf = "compile->test")
)
