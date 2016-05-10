package org.concordion.plugin.idea.lang;

import com.intellij.testFramework.ParsingTestCase;

public class ConcordionParserTest extends ParsingTestCase {

    public ConcordionParserTest() {
        super("", ConcordionFileType.INSTANCE.getDefaultExtension(), new ConcordionParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "testData/parser";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testIterateExpression() {
        doTest(true);
    }

    public void testOgnlChain() {
        doTest(true);
    }

    public void testOgnlField() {
        doTest(true);
    }

    public void testOgnlList() {
        doTest(true);
    }

    public void testOgnlMap() {
        doTest(true);
    }

    public void testOgnlMethod() {
        doTest(true);
    }

    public void testOgnlMethodWithArguments() {
        doTest(true);
    }

    public void testOgnlNumericLiteral1() {
        doTest(true);
    }

    public void testOgnlNumericLiteral2() {
        doTest(true);
    }

    public void testOgnlStringLiteral() {
        doTest(true);
    }

    public void testOgnlVariable() {
        doTest(true);
    }

    public void testSetExpression() {
        doTest(true);
    }

    public void testEmbeddedCommand() {
        doTest(true);
    }

    public void testShortEmbeddedCommand() {
        doTest(true);
    }

    public void testDictionaryValue() {
        doTest(true);
    }
}
