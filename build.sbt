import Dependencies._
import sbt.Def

/** COMMAND SETTINGS
  */

addCommandAlias("dev", "; tpolecatDevMode")
addCommandAlias("ci", "; tpolecatCiMode")

def toCmd(cmds: Seq[String]): String = s";${cmds.mkString(";")}"

lazy val scalafixCmds = Seq(
  "scalafixAll ExplicitResultTypes", // run ExplictResultTypes first and separately to avoid conflicts
  "scalafixAll"
)

addCommandAlias("fix", toCmd(scalafixCmds))

lazy val scalafmtCmds = Seq(
  "scalafmtAll",
  "scalafmtSbt"
)

addCommandAlias("fmt", toCmd(scalafmtCmds))

lazy val ciPrepCmds = Seq(
  "tpolecatCiMode",
  "compile"
)

lazy val scalastyleCmds = Seq("scalastyle")

lazy val lintCmds = Seq(
  "tpolecatCiMode",
  "compile",
  "scalafixAll ExplicitResultTypes", // run ExplictResultTypes first and separately to avoid conflicts
  "scalafixAll",
  "scalafmtAll",
  "scalafmtSbt",
  "scalastyle"
)

addCommandAlias("lint", toCmd(lintCmds))

lazy val preCommitCmds = Seq(
  "tpolecatCiMode",
  "compile",
  "scalafixAll ExplicitResultTypes --check", // run ExplictResultTypes first and separately to avoid conflicts
  "scalafixAll --check",
  "scalafmtCheckAll",
  "scalafmtSbtCheck",
  "scalastyle"
)

// DO NOT CHANGE THE NAME OF THIS COMMAND; the pre-commit git hook greps for it
addCommandAlias("preCommit", toCmd(preCommitCmds))

/** BUILD SETTINGS
  */

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full)

lazy val buildSettings = inThisBuild(
  Seq(
    scalaVersion := Versions.scala213.full,
    organization := "ly.analogical",
    onChangedBuildSource := ReloadOnSourceChanges,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    bloopExportJarClassifiers := Some(Set("sources")),
    scalafixScalaBinaryVersion := Versions.scala213.binary,
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"
  )
)

lazy val coverageSettings = Seq(
  Test / test / coverageEnabled := true,
  coverageMinimumStmtTotal := 100,
  coverageFailOnMinimum := true
)

lazy val lintingSettings = Seq(
  Compile / compile / wartremoverErrors ++= Warts
    .allBut(Wart.Var, Wart.ImplicitParameter, Wart.DefaultArguments, Wart.Overloading),
  scalastyleFailOnWarning := true
)

lazy val testSettings = Seq(
  testFrameworks += new TestFramework("munit.Framework"),
  Test / javaOptions ++= List("-Xss64m", "-Xmx2048m"),
  Test / fork := true,
  Test / parallelExecution := true
)

lazy val baseWithoutTestSettings = buildSettings ++ lintingSettings
lazy val baseSettings = baseWithoutTestSettings ++ coverageSettings ++ testSettings

lazy val root = (project in file("."))
  .settings(baseSettings)
  .settings(
    name := "project name here",
    description := "description here"
  )
  .aggregate(
    core
  )

lazy val core = (project in file("core"))
  .settings(baseSettings)
  .settings(
    name := "core",
    description := "Something",
    libraryDependencies ++= coreDependencies
  )
