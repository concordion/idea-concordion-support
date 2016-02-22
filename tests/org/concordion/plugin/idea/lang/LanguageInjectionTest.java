package org.concordion.plugin.idea.lang;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.assertThat;

public class LanguageInjectionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/injection";
    }

    public void testInjectLangInHtmlSpecWithTestFixture() {

        copyTestFixtureToConcordionProject("Paired.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Paired.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragment("field")
                .hasInjectedFragment("method()");
    }

    public void testInjectLangInXhtmlSpecWithTestFixture() {

        copyTestFixtureToConcordionProject("Paired.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Paired.xhtml");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragment("field")
                .hasInjectedFragment("method()");
    }

    public void testInjectLangInMdSpecWithTestFixture() {

        copyTestFixtureToConcordionProject("Paired.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Paired.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragments("field", 2)
                .hasInjectedFragments("method()", 2);
    }

    public void testNotInjectLangInHtmlSpecWithoutTestFixture() {

        VirtualFile htmlSpec = copySpecToConcordionProject("Unpaired.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }

    public void testNotInjectLangInMdSpecWithoutTestFixture() {

        VirtualFile mdSpec = copySpecToConcordionProject("Unpaired.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }

    public void testDoesNotInjectLangInNotExpressionTags() {

        copyTestFixtureToConcordionProject("NoExpressionInjection.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoExpressionInjection.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }

    //TODO fix
    public void ignoredTestDoesNotInjectInRegularLinksWithTitle() {

        copyTestFixtureToConcordionProject("RegularLinks.java");
        VirtualFile mdSpec = copySpecToConcordionProject("RegularLinks.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }
}
