package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class Fields {

    private int privateProperty = 41;

    int publicProperty = 42;

    static int staticProperty = 43;
}
