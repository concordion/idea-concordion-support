package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.String;

@RunWith(ConcordionRunner.class)
public class ConcordionFields {

    private int privateProperty = 41;

    public int publicProperty = 42;

    public static int staticProperty = 43;
}
