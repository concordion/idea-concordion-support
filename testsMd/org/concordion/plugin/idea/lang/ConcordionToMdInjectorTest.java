package org.concordion.plugin.idea.lang;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.concordion.plugin.idea.HighlightingAssert.assertThat;

public class ConcordionToMdInjectorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/injection";
    }

    public void testInjectLangSpecWithTestFixture() {

        copyTestFixtureToConcordionProject("Paired.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Paired.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragments("?=field", 1)
                .hasInjectedFragments("?=method()", 1);
    }

    public void ignoreTestInjectLangSpecWithTestFixtureUsingSingleQuotes() {

        copyTestFixtureToConcordionProject("PairedSingleQuotes.java");
        VirtualFile mdSpec = copySpecToConcordionProject("PairedSingleQuotes.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragments("?=field", 1)
                .hasInjectedFragments("?=method()", 1);
    }

    public void testNotInjectLangSpecWithoutTestFixture() {

        VirtualFile mdSpec = copySpecToConcordionProject("Unpaired.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }

    public void testDoesNotInjectInRegularLinksWithTitle() {

        copyTestFixtureToConcordionProject("RegularLinks.java");
        VirtualFile mdSpec = copySpecToConcordionProject("RegularLinks.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }
}
