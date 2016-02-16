package org.concordion.plugin.idea.annotator;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static org.concordion.plugin.idea.HighlightingAssert.*;

public class UnresolvedMethodAnnotatorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testErrorOutUnresolvedMethods() {

        copyTestFixtureToConcordionProject("ResolvingMethods.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ResolvingMethods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("unresolvedMethod()").withDescription("Method unresolvedMethod() is not found in class ResolvingMethods"))
                .has(anInfo().withSeverity(ERROR).withText("unresolvedMethod(#arg1)").withDescription("Method unresolvedMethod(?) is not found in class ResolvingMethods"))
                .has(anInfo().withSeverity(ERROR).withText("unresolvedMethod(#arg1, #arg2, #arg3)").withDescription("Method unresolvedMethod(?, ?, ?) is not found in class ResolvingMethods"));
    }

    public void testDoesNotErrorOutResolvedMethods() {

        copyTestFixtureToConcordionProject("ResolvingMethods.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ResolvingMethods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("resolvedMethod()"))
                .hasNo(anInfo().withSeverity(ERROR).withText("resolvedMethodWithArg(#arg1)"))
                .hasNo(anInfo().withSeverity(ERROR).withText("resolvedMethodWithArgs(#arg1, #arg2, #arg3)"));
    }
}