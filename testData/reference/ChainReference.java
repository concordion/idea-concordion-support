package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class ChainReference {

    public Next chainStart = new Next();

    public class Next {
        public int chainNext() {
            return 42;
        }
    }
}
