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
        copyTestFixtureToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutSimpleOgnlExpression() {
        copyTestFixtureToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlTestFixture() {
        copyTestFixtureToConcordionProject("ComplexExpressions.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ComplexExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlParentOfTestFixture() {

        copyTestFixtureToConcordionProject("AnnotatedParent.java");
        copyTestFixtureToConcordionProject("InheritedAnnotation.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("InheritedAnnotation.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("\"buildString().toString()\"").withDescription("Complex expression with fixture not annotated with @FullOGNL"));
    }
}