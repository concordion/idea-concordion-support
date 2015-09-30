package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(ConcordionRunner.class)
public class VariableDefinedInVerifyRows {

    public Collection<String> data() {
        return Arrays.asList("a","b","c");
    }
}
