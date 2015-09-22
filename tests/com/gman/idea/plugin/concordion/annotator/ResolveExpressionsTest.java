package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class ResolveExpressionsTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testShouldResolveOrMarkUnresolvedFields() {

        copyJavaRunnerToConcordionProject("ResolvingFields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }

    public void testShouldResolveOrMarkUnresolvedMethods() {

        copyJavaRunnerToConcordionProject("ResolvingMethod.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingMethod.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }
}
