package reference;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class LengthOfArray {

    public Data[] dataProperty = new Data[0];

    public Data[] dataMethod() {
        return new Data[0];
    }

    public static final class Data {

        public String field;
        public String method() {return null;}
    }
}
