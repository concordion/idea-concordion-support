package com.test;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.Map;

@RunWith(ConcordionRunner.class)
public class ResolvingTypeOfExpression {

    public String stringField;
    public String[] arrayField;
    public List<String> listField;
    public List nonGenericListField;
    public Map<Character, String> mapField;
    public Nested userTypeField;

    public String stringMethod() {return null;}
    public String[] arrayMethod() {return null;}
    public List<String> listMethod() {return null;}
    public List nonGenericListMethod() {return null;}
    public Map<Character, String> mapMethod() {return null;}
    public Nested userTypeMethod() {return null;}

    public String getStringProperty() {return null;}
    public String[] getArrayProperty() {return null;}
    public List<String> getListProperty() {return null;}
    public List getNonGenericListProperty() {return null;}
    public Map<Character, String> getMapProperty() {return null;}
    public Nested getUserTypeProperty() {return null;}

    public static final class Nested {

        public String nestedStringField;

        public String nestedStringMethod() {return null;}
    }
}
