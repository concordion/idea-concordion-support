package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class ChainReferenceWithArray {

    public Next[] chainStart = new Next[0];

    public class Next {
        public int chainNext() {
            return 42;
        }
    }
}
