package org.concordion.plugin.idea.inspection;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.HighlightingAssert;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static org.concordion.plugin.idea.HighlightingAssert.*;

//TODO unstable because of "java.lang.AssertionError: Cannot restore"
public class FullOgnlRequiredInMdTest extends ConcordionCodeInsightFixtureTestCase {

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
        VirtualFile mdSpec = copySpecToConcordionProject("SimpleExpressions.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .has(fullOgnlRequired().withText("makeItComplex().data()"))
                .has(fullOgnlRequired().withText("assignHere()"))
                .has(fullOgnlRequired().withText("#var = makeItComplex().data()"), 2);
    }

    public void testDoesNotErrorOutSimpleOgnlExpression() {
        copyTestFixtureToConcordionProject("SimpleExpressions.java");
        VirtualFile mdSpec = copySpecToConcordionProject("SimpleExpressions.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(fullOgnlRequired().withText("data()"))
                .hasNo(fullOgnlRequired().withText("#var"))
                .hasNo(fullOgnlRequired().withText("#var = data()"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlTestFixture() {
        copyTestFixtureToConcordionProject("ComplexExpressions.java");
        VirtualFile mdSpec = copySpecToConcordionProject("ComplexExpressions.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(fullOgnlRequired().withText("makeItComplex().data()"))
                .hasNo(fullOgnlRequired().withText("assignHere()"))
                .hasNo(fullOgnlRequired().withText("#var = makeItComplex().data()"));
    }

    public void testDoesNotErrorOutComplexExpressionWithFullOgnlParentOfTestFixture() {

        copyTestFixtureToConcordionProject("AnnotatedParent.java");
        copyTestFixtureToConcordionProject("InheritedAnnotation.java");
        VirtualFile mdSpec = copySpecToConcordionProject("InheritedAnnotation.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(fullOgnlRequired().withText("makeItComplex().data()"))
                .hasNo(fullOgnlRequired().withText("assignHere()"))
                .hasNo(fullOgnlRequired().withText("#var = makeItComplex().data()"));
    }

    private Info fullOgnlRequired() {
        return anInfo().withSeverity(ERROR).withDescription("Too complex expression");
    }
}