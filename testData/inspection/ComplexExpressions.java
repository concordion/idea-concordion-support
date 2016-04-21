package com.test;

import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import java.util.Arrays

@RunWith(ConcordionRunner.class)
@FullOGNL
public class ComplexExpressions {

    public String data() {
        return "data";
    }

    public String assignHere() {
        return "invalid";
    }

    public Iterable<String> rows() {
        return Arrays.asList("data1", "data2", "data3");
    }

    public ComplexExpressions makeItComplex() {
        return new ComplexExpressions();
    }
}
