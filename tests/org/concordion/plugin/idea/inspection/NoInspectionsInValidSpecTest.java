package org.concordion.plugin.idea.inspection;

import com.intellij.codeInspection.htmlInspections.HtmlUnknownAttributeInspection;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class NoInspectionsInValidSpecTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(HtmlUnknownAttributeInspection.class);
    }

    public void testNoWarningsInDefaultHtmlSpec() {
        copyTestFixtureToConcordionProject("NoInspections.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoInspections.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting(HighlightSeverity.WEAK_WARNING)).isEmpty();
    }

    public void testNoWarningsInDefaultXhtmlSpec() {
        copyTestFixtureToConcordionProject("NoInspections.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoInspections.xhtml");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting(HighlightSeverity.WEAK_WARNING)).isEmpty();
    }
}
