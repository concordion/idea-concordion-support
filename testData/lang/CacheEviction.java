package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class CacheEviction {

    public Nested field;

    public Nested method() {
        return null;
    }

    public static final class Nested {

        public String chained;
    }
}
