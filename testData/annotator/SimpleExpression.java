package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class SimpleExpression {

    public String property = "value";

    public String method() {
        return "value";
    }

    public Collection<String> data() {
        return Collections.emptyList();
    }
}
