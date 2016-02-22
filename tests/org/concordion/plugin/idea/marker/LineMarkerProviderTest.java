package org.concordion.plugin.idea.marker;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.GuttersAssert.assertThat;

public class LineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/marker";
    }

    public void testSecAndFixtureHaveSameName() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec1.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec1.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testFixtureMayHaveOptionalTestSuffix() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec2Test.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec2.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testSpecShouldNotHaveTestSuffix() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec3Test.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec3Test.html");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testFixtureMayHaveOptionalFixtureSuffix() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec4Fixture.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec4.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testSpecShouldNotHaveFixtureSuffix() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec5Fixture.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec5Fixture.html");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testNoMarkerInHtmlIfNoConcordionPresent() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("NoConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoConcordion.html");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testNoMarkerIfNoConcordionPresentForMarkDown() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("NoConcordion.java");
        VirtualFile mdSpec = copySpecToConcordionProject("NoConcordion.md");

        assertHasNoGutters(testFixture, mdSpec);
    }

    public void testNoConcordionRunnerAnnotation() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("NoRunnerAnnotation.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoRunnerAnnotation.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testNoConcordionNamespace() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("NoNamespace.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoNamespace.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testUseMarkdownSpec() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Markdown.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Markdown.md");

        assertHasGutters(testFixture, mdSpec);
    }

    public void testUseMarkdownSpecWithLongExtension() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Markdown.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Markdown.markdown");

        assertHasGutters(testFixture, mdSpec);
    }

    private void assertHasGutters(VirtualFile fixture, VirtualFile spec) {
        myFixture.configureFromExistingVirtualFile(fixture);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(spec);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }

    private void assertHasNoGutters(VirtualFile fixture, VirtualFile spec) {
        myFixture.configureFromExistingVirtualFile(fixture);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();

        myFixture.configureFromExistingVirtualFile(spec);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();
    }
}
