package com.test;

import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@FullOGNL
public class OgnlExpressionWithAnnotation {

    public String property = "value";

    public String method() {
        return "value";
    }

    public DataHolder holder() {
        return new DataHolder();
    }

    public static class DataHolder {
        public Collection<String> data() {
            return Collections.emptyList();
        }
    }
}
