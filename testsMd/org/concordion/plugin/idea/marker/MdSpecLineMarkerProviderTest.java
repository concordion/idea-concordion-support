package org.concordion.plugin.idea.marker;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class MdSpecLineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/marker";
    }

    public void testNoConcordionRunnerAnnotationForMd() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("NoConcordion.java");
        VirtualFile mdSpec = copySpecToConcordionProject("NoConcordion.md");

        assertHasGutters(testFixture, mdSpec);
    }

    public void testMarksMd() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Spec.md");

        assertHasGutters(testFixture, mdSpec);
    }

    public void testMarksMarkdown() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Spec.markdown");

        assertHasGutters(testFixture, mdSpec);
    }
}
