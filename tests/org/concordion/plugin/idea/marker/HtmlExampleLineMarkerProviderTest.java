package org.concordion.plugin.idea.marker;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.concordion.plugin.idea.GuttersAssert.assertThat;

public class HtmlExampleLineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/marker";
    }

    public void testDetectExamplesAsFields() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Example.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("FirstExample");
    }

    public void testDetectExamples() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Example.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("1-example");
    }

    public void testDetectsOneExamplePerInjection() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Example.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("First Example");
    }
}