package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.String;

@RunWith(ConcordionRunner.class)
public class ChainFromVariable {

    public String data() {
        return "hello";
    }
}
