package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class CreateMethodFromUsage {

    public A field;

    public A method() {
        return null;
    }

    public String createMe(String arg, A field, A method, String param3, String param4, String param5) {
        return null;
    }

    public static final class A {

    }
}
