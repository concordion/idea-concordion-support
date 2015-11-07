package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class MethodWithArgumentsReference {

    public String field;

    public String method() {
        return null;
    }

    public int methodToResolve(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
        return 42;
    }
}
