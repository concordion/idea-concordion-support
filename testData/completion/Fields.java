package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class Fields {

    private int privateProperty = 41;
    protected int proptectedProperty = 41;
    int packagePrivateProperty = 41;
    public int publicProperty = 42;
    public static int staticProperty = 43;
}
