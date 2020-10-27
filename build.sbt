enablePlugins(GitVersioning)

organization := "com.ctc"
name := "daffodil-schematron-validator"
git.useGitDescribe := true
scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "com.helger" % "ph-schematron" % "5.6.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

lazy val ref="git://github.com/jw3/incubator-daffodil.git#validator_spi/impl"
dependsOn(
  ProjectRef(uri(ref), "daffodil-lib"),
  ProjectRef(uri(ref), "daffodil-cli").%(conf = "compile->test")
)

assemblyMergeStrategy in assembly := {
  case "module-info.class"                                 => MergeStrategy.discard
  case PathList("META-INF", "services", _*)                => MergeStrategy.concat
  case PathList("META-INF", _*)                            => MergeStrategy.discard
  case PathList("org", "fusesource", _*)                   => MergeStrategy.first
  case PathList("org", "apache", "daffodil", _*)           => MergeStrategy.discard
  case PathList("edu", "illinois", "ncsa", "daffodil", _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
