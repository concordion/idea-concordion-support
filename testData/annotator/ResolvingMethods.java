package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ResolvingMethods {

    public int resolvedMethod() {
        return 42;
    }

    public int resolvedMethodWithArg(Object arg1) {
        return 42;
    }

    public int resolvedMethodWithArgs(Object arg1, Object arg2, Object arg3) {
        return 42;
    }
}
