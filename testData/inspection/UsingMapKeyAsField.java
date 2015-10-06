package inspection;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import java.util.Map;

@RunWith(ConcordionRunner.class)
public class UsingMapKeyAsField {

    public Outer outer = new Outer();
    public Map<String, String> properties = null;

    public Map<String, String> properties() {
        return null;
    }

    public Outer outer() {
        return new Outer();
    }

    public static final class Outer {

        public String inner;
    }
}
