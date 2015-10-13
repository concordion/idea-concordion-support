package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class FullOgnlRequiredTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(FullOgnlRequired.class);
    }

    public void testErrorOutComplexOgnlExpression() {
        copyJavaRunnerToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutSimpleOgnlExpression() {
        copyJavaRunnerToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlTestFixture() {
        copyJavaRunnerToConcordionProject("ComplexExpressions.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ComplexExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlParentOfTestFixture() {

        copyJavaRunnerToConcordionProject("AnnotatedParent.java");
        copyJavaRunnerToConcordionProject("InheritedAnnotation.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("InheritedAnnotation.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }
}