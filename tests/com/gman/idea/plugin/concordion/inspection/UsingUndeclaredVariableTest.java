package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static com.gman.idea.plugin.concordion.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class UsingUndeclaredVariableTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(UsingUndeclaredVariable.class);
    }

    public void testWarnUndeclaredVariableUsage() {
        copyJavaRunnerToConcordionProject("ResolvingVariables.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingVariables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("#undefined").withDescription("Using undeclared variable"));
    }

    public void testWarnUsingBeforeDeclaring() {
        copyJavaRunnerToConcordionProject("ResolvingVariables.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingVariables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("#definedToLate").withDescription("Using undeclared variable"));
    }

    public void testDoNotWarnUsageOfDeclaredVariable() {
        copyJavaRunnerToConcordionProject("ResolvingVariables.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingVariables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("#definedBeforehand").withDescription("Using undeclared variable"));
    }

    public void testDoNotWarnUsageOfNestedDeclaration() {
        copyJavaRunnerToConcordionProject("ResolvingVariables.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingVariables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("#definedInside").withDescription("Using undeclared variable"));
    }

    public void testDoNotWarnUsageOfReservedFariableWithoutDeclaration() {
        copyJavaRunnerToConcordionProject("ResolvingVariables.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingVariables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("#TEXT").withDescription("Using undeclared variable"));
    }

    //TODO warn overwriting reserved variables
}