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
}
