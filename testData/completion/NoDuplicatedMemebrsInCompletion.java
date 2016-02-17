package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.String;
import java.util.ArrayList;

@RunWith(ConcordionRunner.class)
public class NoDuplicatedMemebrsInCompletion {

    public MyClass3 myMembers() {
        return new MyClass3();
    }

    public static class MyClass1 {
        public String field = "f1";
        public String method() {return "method";}
    }

    public static class MyClass2 extends MyClass1 {
        public String field = "f2";
        public String method() {return "method";}
    }

    public static class MyClass3 extends MyClass2 {
        public String field = "f3";
        public String method() {return "method";}
    }
}
