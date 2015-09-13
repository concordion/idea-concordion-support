package com.gman.concordion.play;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class PlaySpec {

    public int num = 42;
    public String str = "PlaySpec.str";

    public int buildNum() {
        return 4242;
    }

    public String buildStr() {
        return "PlaySpec.buildStr()";
    }

    public Parameters buildSpecial(String name) {
        Parameters parameters = new Parameters();
        parameters.str = name;
        return parameters;
    }

    public Parameters parameters = new Parameters();

    public Parameters buildParameters() {
        return new Parameters();
    }

    public static final class Parameters {

        public int num = 424242;
        public String str = "PlaySpec.Parameters.str";

        public int createNum() {
            return 42424242;
        }

        public String createStr() {
            return "PlaySpec.Parameters.createStr()";
        }

        public Parameters copy() {
            return this;
        }
    }
}
