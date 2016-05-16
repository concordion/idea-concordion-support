package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import java.util.Arrays;

@RunWith(ConcordionRunner.class)
class SimpleExpressions {

    def String data() {
        return "data";
    }

    def String assignHere() {
        return "invalid";
    }

    def Iterable<String> rows() {
        return Arrays.asList("data1", "data2", "data3");
    }

    def SimpleExpressions makeItComplex() {
        return new SimpleExpressions();
    }
}
