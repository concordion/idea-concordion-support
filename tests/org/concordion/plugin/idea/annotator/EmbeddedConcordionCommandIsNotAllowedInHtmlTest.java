package org.concordion.plugin.idea.annotator;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static org.concordion.plugin.idea.HighlightingAssert.anInfo;
import static org.concordion.plugin.idea.HighlightingAssert.assertThat;

public class EmbeddedConcordionCommandIsNotAllowedInHtmlTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testErrorOutWhenEmbeddedCommandUsedInHtmlSpec() {

        copyTestFixtureToConcordionProject("IllegalEmbeddedCommand.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("IllegalEmbeddedCommand.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("?=").withDescription("Unexpected concordion command"))
                .has(anInfo().withSeverity(ERROR).withText("c:assertEquals=").withDescription("Unexpected concordion command"));
    }
}