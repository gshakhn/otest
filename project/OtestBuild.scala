import sbt._
import sbt.Keys._

import scala.scalajs.sbtplugin.env.nodejs.NodeJSEnv
import scala.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin._
import scala.scalajs.sbtplugin.testing.JSClasspathLoader




object OtestBuild extends Build {
  import Common._
  lazy val otestX = xprojects("otest")
    .settingsShared(macroSettings: _*)
    .settingsShared(libraryDependencies ++= Libs.sbtTestInterface)
    .settingsSjs(addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.0-M3"))

  lazy val otest    = otestX.shared
  lazy val otestJvm = otestX.jvm
  lazy val otestSjs = otestX.sjs

  lazy val root = Project("root", file("."))
    .aggregate(otestJvm, otestSjs)
    .settings(basicSettings: _*)
    .settings(publish := {})
}

object OtestSamplesBuild extends Build {
  import Common._

  val otestFrameworkJvm = new TestFramework("cgta.otest.runner.OtestSbtFrameworkJvm")
  val otestFrameworkSjs = new TestFramework("cgta.otest.runner.OtestSbtFrameworkSjs")

  lazy val osampletestsX = xprojects("osampletests")
    .settingsShared(libraryDependencies += "biz.cgta" %% "otest-jvm" % (version in ThisBuild).value,
      testFrameworks += otestFrameworkJvm)
    .settingsJvm(libraryDependencies += "biz.cgta" %% "otest-jvm" % (version in ThisBuild).value,
      testFrameworks += otestFrameworkJvm)
    .settingsSjs(
      libraryDependencies += "biz.cgta" %%% "otest-sjs" % (version in ThisBuild).value,
      (loadedTestFrameworks in Test) := {
        import cgta.otest.runner.OtestSbtFrameworkSjs
        (loadedTestFrameworks in Test).value.updated(
          sbt.TestFramework(classOf[OtestSbtFrameworkSjs].getName),
          new OtestSbtFrameworkSjs(env = (ScalaJSKeys.jsEnv in Test).value)
        )
      },
      (ScalaJSKeys.jsEnv in Test) := new NodeJSEnv,
      testLoader := JSClasspathLoader((ScalaJSKeys.execClasspath in Compile).value),
      testFrameworks += otestFrameworkSjs
    )



  lazy val osampletests    = osampletestsX.shared
  lazy val osampletestsJvm = osampletestsX.jvm
  lazy val osampletestsSjs = osampletestsX.sjs
}

