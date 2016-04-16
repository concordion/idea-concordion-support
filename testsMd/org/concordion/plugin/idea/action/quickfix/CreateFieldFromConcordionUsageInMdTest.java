package org.concordion.plugin.idea.action.quickfix;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class CreateFieldFromConcordionUsageInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreateFieldFromUsage() throws InterruptedException {

        copyTestFixtureToConcordionProject("CreateFieldFromUsage.java");
        VirtualFile mdSpec = copySpecToConcordionProject("CreateFieldFromUsage.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        executeIntention("Create field from usage");

        myFixture.checkResultByFile("/src/com/test/CreateFieldFromUsage.java", "after/CreateFieldFromUsage.java", false);
    }
}