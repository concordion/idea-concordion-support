package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.lang.Object;

@RunWith(ConcordionRunner.class)
public class MethodWithVarArg {

    public double[] numbers(int i1, int i2, int i3) {
        return new double[]{i1, i2, i3};
    }

    public double sum(double... numbers) {
        double d = 0;
        for (double number : numbers) {
            d += number
        }
        return d;
    }
}
