package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class ResolveExpressionsTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testDummy() {}

    public void ignoreTestShouldResolveOrMarkUnresolvedFields() {

        copyJavaRunnerToConcordionProject("ResolvingFields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }

    public void ignoreTestShouldResolveOrMarkUnresolvedMethods() {

        copyJavaRunnerToConcordionProject("ResolvingMethod.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ResolvingMethod.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.checkHighlighting(true, true, true);
    }
}
