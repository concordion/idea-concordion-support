package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class PairedSingleQuotes {

    public String field = "field";

    public String method() {
        return "method()";
    }
}
