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
                .has(fullOgnlRequired().withText("makeItComplex().data()"))
                .has(fullOgnlRequired().withText("assignHere()"))
                .has(fullOgnlRequired().withText("#var = makeItComplex().data()"), 2)
                .has(fullOgnlRequired().withText("makeItComplex().rows()"));
    }

    public void testDoesNotErrorOutSimpleOgnlExpression() {
        copyTestFixtureToConcordionProject("SimpleExpressions.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SimpleExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(fullOgnlRequired().withText("data()"))
                .hasNo(fullOgnlRequired().withText("#var"))
                .hasNo(fullOgnlRequired().withText("#var = data()"))
                .hasNo(fullOgnlRequired().withText("rows()"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlTestFixture() {
        copyTestFixtureToConcordionProject("ComplexExpressions.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ComplexExpressions.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(fullOgnlRequired().withText("makeItComplex().data()"))
                .hasNo(fullOgnlRequired().withText("assignHere()"))
                .hasNo(fullOgnlRequired().withText("#var = makeItComplex().data()"))
                .hasNo(fullOgnlRequired().withText("makeItComplex().rows()"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlParentOfTestFixture() {

        copyTestFixtureToConcordionProject("AnnotatedParent.java");
        copyTestFixtureToConcordionProject("InheritedAnnotation.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("InheritedAnnotation.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(fullOgnlRequired().withText("makeItComplex().data()"))
                .hasNo(fullOgnlRequired().withText("assignHere()"))
                .hasNo(fullOgnlRequired().withText("#var = makeItComplex().data()"))
                .hasNo(fullOgnlRequired().withText("makeItComplex().rows()"));
    }

    private Info fullOgnlRequired() {
        return anInfo().withSeverity(ERROR).withDescription("Too complex expression");
    }
}