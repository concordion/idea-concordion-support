package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class Methods {

    private int privateMethod() {
        return 41;
    }

    int publicMethod() {
        return 42;
    }

    static int staticMethod() {
        return 43;
    }
}
