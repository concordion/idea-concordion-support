package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(ConcordionRunner.class)
public class NestedMembersOfVerifyRowsLoopVariable {

    public Data data() {return new Data();};

    public static final class Data extends ArrayList<DataItem> {

    }

    public static final class DataItem {

        public String method() {
            return "hello";
        }
    }
}
