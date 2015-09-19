package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

public class Parent {

    public String inheritedField = "inheritedField";

    public String inheritedMethod() {
        return "inheritedMethod";
    }
}
