package com.test

import org.concordion.integration.junit4.ConcordionRunner
import org.junit.runner.RunWith

@RunWith(ConcordionRunner.class)
class CreateMethodFromUsage {

    public A field

    def A method() {
        null
    }

    java.lang.String createMe(java.lang.String arg, com.test.CreateMethodFromUsage.A field, com.test.CreateMethodFromUsage.A method, java.lang.String param3, java.lang.String param4, java.lang.String param5) {
    }

    class A {

    }
}
