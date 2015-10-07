package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static com.gman.idea.plugin.concordion.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class FullOgnlRequiredTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    public void testErrorOutComplexOgnlExpression() {
        copyJavaRunnerToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.enableInspections(FullOgnlRequired.class);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutSimpleOgnlExpression() {
        copyJavaRunnerToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.enableInspections(FullOgnlRequired.class);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));

    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlTestFixture() {
        copyJavaRunnerToConcordionProject("ComplexExpressions.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ComplexExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.enableInspections(FullOgnlRequired.class);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));

    }
}