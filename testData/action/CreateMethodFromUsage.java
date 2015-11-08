package action;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class CreateMethodFromUsage {

    public A field;

    public A method() {
        return null;
    }

    public static final class A {

    }
}
