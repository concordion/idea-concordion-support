package org.concordion.plugin.idea.marker;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class HtmlSpecLineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/marker";
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

    public void testMarksHtml() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.html");

        assertHasGutters(testFixture, htmlSpec);
    }

    public void testMarksXhtml() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.xhtml");

        assertHasGutters(testFixture, htmlSpec);
    }
}
