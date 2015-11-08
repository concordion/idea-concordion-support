package reference;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class MethodWithVarArg {

    public int number(int n) {
        return n;
    }

    public double sum(double... numbers) {
        double d = 0;
        for (double number : numbers) {
            d += number
        }
        return d;
    }
}
