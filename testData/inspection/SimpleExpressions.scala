package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import java.util.Arrays;

@RunWith(classOf[ConcordionRunner])
class SimpleExpressions {

    def data() = "data"

    def assignHere() = "invalid"

    def rows(): Iterable[String] = {
        Arrays.asList("data1", "data2", "data3")
    }

    def makeItComplex(): SimpleExpressions = {
        new SimpleExpressions()
    }
}
