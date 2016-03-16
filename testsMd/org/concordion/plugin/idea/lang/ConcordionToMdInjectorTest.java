package org.concordion.plugin.idea.lang;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.concordion.plugin.idea.HighlightingAssert.assertThat;

public class ConcordionToMdInjectorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/injection";
    }

    public void testInjectLangInMdSpecWithTestFixture() {

        copyTestFixtureToConcordionProject("Paired.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Paired.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        assertThat(myFixture.doHighlighting())
                .hasInjectedFragments("?=field", 2)
                .hasInjectedFragments("?=method()", 2);
    }

    public void testNotInjectLangInMdSpecWithoutTestFixture() {

        VirtualFile mdSpec = copySpecToConcordionProject("Unpaired.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

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
