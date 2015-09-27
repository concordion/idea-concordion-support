package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.String;

@RunWith(ConcordionRunner.class)
public class NestedMembersOfVariable {

    public Nested create() {return new Nested();};

    public static final class Nested {

        public String field;
        public String method() {return "";}
    }
}
