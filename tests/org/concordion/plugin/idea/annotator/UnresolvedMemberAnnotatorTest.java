package org.concordion.plugin.idea.annotator;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.HighlightingAssert;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class UnresolvedMemberAnnotatorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testErrorOutUnresolvedFields() {

        copyJavaRunnerToConcordionProject("ResolvingFields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("unresolvedProperty").withDescription("Member not found"));
    }

    public void testDoesNotErrorOutResolvedFields() {

        copyJavaRunnerToConcordionProject("ResolvingFields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("resolvedProperty").withDescription("Member not found"));
    }

    public void testErrorOutUnresolvedMethods() {

        copyJavaRunnerToConcordionProject("ResolvingMethods.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingMethods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        HighlightingAssert.Info prototype = anInfo().withSeverity(ERROR).withDescription("Member not found");

        assertThat(myFixture.doHighlighting())
                .has(prototype.copy().withText("unresolvedMethod()"))
                .has(prototype.copy().withText("unresolvedMethod(#arg1)"))
                .has(prototype.copy().withText("unresolvedMethod(#arg1, #arg2, #arg3)"));
    }

    public void testDoesNotErrorOutResolvedMethods() {

        copyJavaRunnerToConcordionProject("ResolvingMethods.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingMethods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        HighlightingAssert.Info prototype = anInfo().withSeverity(ERROR).withDescription("Member not found");

        assertThat(myFixture.doHighlighting())
                .hasNo(prototype.copy().withText("resolvedMethod()"))
                .hasNo(prototype.copy().withText("resolvedMethodWithArg(#arg1)"))
                .hasNo(prototype.copy().withText("resolvedMethodWithArgs(#arg1, #arg2, #arg3)"));
    }

    public void testDoesNotErrorOutArraysLengthProperty() {

        copyJavaRunnerToConcordionProject("LengthOfArray.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("LengthOfArray.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Member not found").withText("length"));
    }
}
