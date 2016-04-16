package org.concordion.plugin.idea.action.quickfix;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class CreateMethodFromConcordionUsageInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreateMethodFromUsage() throws InterruptedException {

        copyTestFixtureToConcordionProject("CreateMethodFromUsage.java");
        VirtualFile mdSpec = copySpecToConcordionProject("CreateMethodFromUsage.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        executeIntention("Create method from usage");

        myFixture.checkResultByFile("/src/com/test/CreateMethodFromUsage.java", "after/CreateMethodFromUsage.java", false);
    }
}