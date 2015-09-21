package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class AnnotateUnresolvedMembersTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testShouldMarkUnresolvedConcordionFields() {

        copyJavaRunnerToConcordionProject("ConcordionUnresolvedField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionUnresolvedField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }

    public void testShouldMarkUnresolvedConcordionMethods() {

        copyJavaRunnerToConcordionProject("ConcordionUnresolvedMethod.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionUnresolvedMethod.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }
}
