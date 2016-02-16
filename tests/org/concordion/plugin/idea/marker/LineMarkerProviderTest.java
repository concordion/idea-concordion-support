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
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec1.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec1.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testFixtureMayHaveOptionalTestSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec2Test.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec2.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testSpecShouldNotHaveTestSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec3Test.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec3Test.html");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testFixtureMayHaveOptionalFixtureSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec4Fixture.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec4.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testSpecShouldNotHaveFixtureSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec5Fixture.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec5Fixture.html");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testNoMarkerInHtmlIfNoConcordionPresent() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoConcordion.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoConcordion.html");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testNoMarkerIfNoConcordionPresentForMarkDown() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoConcordion.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoConcordion.md");

        assertHasNoGutters(testFixture, htmlSpec);
    }

    public void testNoConcordionRunnerAnnotation() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoRunnerAnnotation.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoRunnerAnnotation.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testNoConcordionNamespace() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoNamespace.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoNamespace.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void ignoredTestCanUseMarkdownSpec() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Markdown.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Markdown.html");

        assertHasGutters(testFixture, htmlSpec);
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
