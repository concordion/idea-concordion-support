package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class OverloadedMethodArgumentsNumber2 {

    public int methodToResolve(Object arg1, Object arg2, Object arg3, Object arg4) {
        return 42;
    }

    public int methodToResolve(Object arg1, Object arg2) {
        return 42;
    }
}
