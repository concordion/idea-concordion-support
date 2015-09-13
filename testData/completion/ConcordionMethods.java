package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ConcordionMethods {

    private int privateMethod() {
        return 41;
    }

    public int publicMethod() {
        return 42;
    }

    public static int staticMethod() {
        return 43;
    }
}
