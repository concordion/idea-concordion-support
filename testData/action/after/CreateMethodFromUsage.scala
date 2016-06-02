package com.test

import org.concordion.integration.junit4.ConcordionRunner
import org.junit.runner.RunWith

@RunWith(classOf[ConcordionRunner])
class CreateMethodFromUsage {

  def getField: A = null

  def method: A = null

  class A {
  }

  def createMe(arg: String, field: A, method: A, param3: String, param4: String, param5: String): String = ???
}
