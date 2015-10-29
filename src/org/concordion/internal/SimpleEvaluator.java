package org.concordion.internal;

//This class is copied from concordion library so that I do not need to make 1mb dependency on it
public class SimpleEvaluator {

    private static final String METHOD_NAME_PATTERN = "[a-z][a-zA-Z0-9_]*";
    private static final String PROPERTY_NAME_PATTERN = "[a-z][a-zA-Z0-9_]*";
    private static final String STRING_PATTERN = "'[^']+'";
    private static final String LHS_VARIABLE_PATTERN = "#" + METHOD_NAME_PATTERN;
    private static final String RHS_VARIABLE_PATTERN = "(" + LHS_VARIABLE_PATTERN + "|#TEXT|#HREF|#LEVEL)";
    private static final String METHOD_CALL_PARAMS = METHOD_NAME_PATTERN + " *\\( *" + RHS_VARIABLE_PATTERN + "(, *" + RHS_VARIABLE_PATTERN + " *)*\\)";
    private static final String METHOD_CALL_NO_PARAMS = METHOD_NAME_PATTERN + " *\\( *\\)";
    private static final String TERNARY_STRING_RESULT = " \\? " + STRING_PATTERN + " : " + STRING_PATTERN;

    public static final MultiPattern EVALUATION_PATTERNS = MultiPattern.fromRegularExpressions(
            PROPERTY_NAME_PATTERN,
            METHOD_CALL_NO_PARAMS,
            METHOD_CALL_PARAMS,
            RHS_VARIABLE_PATTERN,
            LHS_VARIABLE_PATTERN + "(\\." + PROPERTY_NAME_PATTERN + ")+",
            LHS_VARIABLE_PATTERN + " *= *" + PROPERTY_NAME_PATTERN,
            LHS_VARIABLE_PATTERN + " *= *" + METHOD_CALL_NO_PARAMS,
            LHS_VARIABLE_PATTERN + " *= *" + METHOD_CALL_PARAMS,
            LHS_VARIABLE_PATTERN + TERNARY_STRING_RESULT,
            PROPERTY_NAME_PATTERN + TERNARY_STRING_RESULT,
            METHOD_CALL_NO_PARAMS + TERNARY_STRING_RESULT,
            METHOD_CALL_PARAMS + TERNARY_STRING_RESULT,
            LHS_VARIABLE_PATTERN + "\\." + METHOD_CALL_NO_PARAMS,
            LHS_VARIABLE_PATTERN + "\\." + METHOD_CALL_PARAMS);

    // #var                         VARIABLE
    // #var = myProp                VARIABLE = PROPERTY
    // #var = myMethod()            VARIABLE = METHOD
    // #var = myMethod(var1)        VARIABLE = METHOD_WITH_PARAM
    // #var = myMethod(var1, var2)  VARIABLE = METHOD_WITH_MULTIPLE_PARAMS
    public static final MultiPattern SET_VARIABLE_PATTERNS = MultiPattern.fromRegularExpressions(
            RHS_VARIABLE_PATTERN,
            LHS_VARIABLE_PATTERN + "\\." + PROPERTY_NAME_PATTERN,
            LHS_VARIABLE_PATTERN + " *= *" + PROPERTY_NAME_PATTERN,
            LHS_VARIABLE_PATTERN + " *= *" + METHOD_NAME_PATTERN + " *\\( *\\)",
            LHS_VARIABLE_PATTERN + " *= *" + METHOD_NAME_PATTERN + " *\\( *" + RHS_VARIABLE_PATTERN + "(, *" + RHS_VARIABLE_PATTERN + " *)*\\)");
}
