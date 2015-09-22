package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.Properties;

@RunWith(ConcordionRunner.class)
public class ResolvingFields {

    public int resolvedProperty = 42;

    public Properties properties = new Properties();
}
