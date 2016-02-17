package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class OverloadedMethodArgumentsType {

    public A createA() {
        return new A();
    }

    public B createB() {
        return new B();
    }

    public int methodToResolve(A a, int i, B b) {
        return 42;
    }

    public int methodToResolve(B b, int i, A a) {
        return 42;
    }

    public static final class A {}
    public static final class B {}
}
