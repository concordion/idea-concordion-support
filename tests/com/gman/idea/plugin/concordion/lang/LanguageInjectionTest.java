package com.gman.idea.plugin.concordion.lang;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static com.gman.idea.plugin.concordion.HighlightingAssert.assertThat;

public class LanguageInjectionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/injection";
    }

    public void testInjectLangInHtmlSpecWithTestFixture() {

        copyJavaRunnerToConcordionProject("Paired.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Paired.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragment("field")
                .hasInjectedFragment("method()");
    }

    public void testNotInjectLangInHtmlSpecWithoutTestFixture() {

        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Unpaired.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }

    public void testDoesNotInjectLangInNotExpressionTags() {

        copyJavaRunnerToConcordionProject("NoExpressionInjection.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoExpressionInjection.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }
}
