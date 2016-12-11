package org.concordion.plugin.idea.lang;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.assertThat;

public class ConcordionToHtmlInjectorTest extends ConcordionCodeInsightFixtureTestCase {

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

    public void testNotInjectLangInHtmlSpecWithoutTestFixture() {

        VirtualFile htmlSpec = copySpecToConcordionProject("Unpaired.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNoInjectedFragments();
    }
}
