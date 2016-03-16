package org.concordion.plugin.idea.marker;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.GuttersAssert.assertThat;

public class HtmlSpecLineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

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

    public void testUseXhtml() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Xhtml.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Xhtml.xhtml");

        assertHasGutters(testFixture, htmlSpec);
    }
}
