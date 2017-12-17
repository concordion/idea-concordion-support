package org.concordion.plugin.idea.marker;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.concordion.plugin.idea.GuttersAssert.assertThat;

public class MdExampleLineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/marker";
    }

    public void testDetectExamplesAsFieldsForMd() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Example.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("FirstExample");
    }

    public void testDetectsOneExamplePerInjection() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Example.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("First Example");
    }

    public void testDetectExamplesAsFieldsWithoutEmbeddedCommandForMd() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Example.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("SecondExample");
    }

    public void testDetectsOneExamplePerInjectionWithoutEmbedded() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Example.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("Second Example");
    }

    public void testDetectExamplesForMd() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Example.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("1-example");
    }

    public void testDetectExamplesWithoutEmbeddedCommandForMd() {
        copyTestFixtureToConcordionProject("Example.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Example.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionExampleGutter("2-example");
    }
}