import sbt._

object Dependencies {

  object Versions {

    object scala213 {
      lazy val binary = "2.13"
      lazy val full = "2.13.8"
    }

    lazy val cats = "2.7.0"
    lazy val catsEffect = "3.3.12"
    lazy val fs2 = "3.2.7"
    lazy val http4s = "0.23.12"
    lazy val munit = "1.0.0-M1"
  }

  lazy val catsCore: Seq[ModuleID] = Seq("org.typelevel" %% "cats-core" % Versions.cats)
  lazy val catsEffect: Seq[ModuleID] = Seq("org.typelevel" %% "cats-effect" % Versions.catsEffect)

  lazy val fs2: Seq[ModuleID] = Seq(
    "co.fs2" %% "fs2-core"
  ).map(_ % Versions.fs2)

  lazy val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-ember-client"
  ).map(_ % Versions.http4s)

  lazy val baseDependencies: Seq[ModuleID] =
    catsCore ++ catsEffect

  lazy val munit = "org.scalameta" %% "munit" % Versions.munit

  lazy val testDependencies = Seq(
    munit
  ).map(_ % Test)

  /* ------- MODULES --------- */

  lazy val coreDependencies = baseDependencies ++ fs2 ++ http4s ++ testDependencies
}
