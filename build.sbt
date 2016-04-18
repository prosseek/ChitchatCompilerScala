name := ""

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
target in Compile in doc := baseDirectory.value / "doc/api"

// ScalaTest additional setup
// http://www.scalatest.org/install
// libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6" 
// If you need to add classpath directory
// <http://stackoverflow.com/questions/23357490/add-directory-to-classpath-in-build-scala-of-sbt>
// (fullClasspath in Test) += Attributed.blank(file("./src/test/resources/util/file/"))

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2",
  "chitchattype" %% "chitchattype" % "0.1",
  "bloomierfilter" %% "bloomierfilter" % "0.1",
  "chitchatsummary" %% "chitchatsummary" % "0.1"
)

// META-INF discarding
/*mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
}

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)*/