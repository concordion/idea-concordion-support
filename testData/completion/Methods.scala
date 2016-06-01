package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(classOf[ConcordionRunner])
class Methods() {

    private def privateMethod() = 41
    protected def protectedMethod() = 41
    def publicMethod() = 41
}
object Methods {
    def staticMethod() = 41
}
