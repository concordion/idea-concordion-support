package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ResolvingMethod {

    public int resolvedMethod() {
        return 42;
    }
}