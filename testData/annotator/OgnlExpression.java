package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Collections;

@RunWith(ConcordionRunner.class)
public class OgnlExpression {

    public String property = "value";

    public String method() {
        return "value";
    }

    public DataHolder holder() {
        return new DataHolder();
    }

    public static class DataHolder {
        public Collection<String> data() {
            return Collections.emptyList();
        }
    }
}
