package com.test;

public class InheritedAnnotation extends AnnotatedParent {

    public String data() {
        return "data";
    }

    public String assignHere() {
        return "invalid";
    }

    public Iterable<String> rows() {
        return Arrays.asList("data1", "data2", "data3");
    }

    public ComplexExpressions makeItComplex() {
        return new ComplexExpressions();
    }
}
