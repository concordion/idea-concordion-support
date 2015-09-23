package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class AnnotateFullOgnlExpressionsTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testDummy() {}

    public void ignoreTestShouldNotCreateErrorOnSimpleExpression() {

        copyJavaRunnerToConcordionProject("SimpleExpression.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("SimpleExpression.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }

    public void ignoreTestShouldCreateErrorOnComplexExpression() {

        copyJavaRunnerToConcordionProject("OgnlExpression.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("OgnlExpression.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }

    //Can not find annotation since concordion library is not present in testing test classpath
    public void ignoredTestShouldNotCreateErrorOnComplexAnnotatedExpression() {

        copyJavaRunnerToConcordionProject("OgnlExpressionWithAnnotation.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("OgnlExpressionWithAnnotation.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }
}
