package cgta.otest
package runner

import sbt.testing.{Fingerprint, Runner}


//////////////////////////////////////////////////////////////
// Copyright (c) 2014 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 5/28/14 4:33 PM
//////////////////////////////////////////////////////////////

class OtestSbtFramework extends sbt.testing.Framework {
  override def name(): String = "otest-jvm"
  override def fingerprints(): Array[Fingerprint] = FrameworkHelp.fingerprints()

  override def runner(args: Array[String],
    remoteArgs: Array[String],
    testClassLoader: ClassLoader): Runner = {
    new OtestRunner(args, remoteArgs, testClassLoader)
  }

  def slaveRunner(args: Array[String],
    remoteArgs: Array[String],
    testClassLoader: ClassLoader,
    send: String => Unit): Runner = {
    runner(args, remoteArgs, testClassLoader)
  }

}