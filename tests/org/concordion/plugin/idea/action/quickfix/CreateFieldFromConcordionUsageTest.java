package org.concordion.plugin.idea.action.quickfix;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class CreateFieldFromConcordionUsageTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreateFieldFromUsage() throws InterruptedException {

        copyTestFixtureToConcordionProject("CreateFieldFromUsage.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CreateFieldFromUsage.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        executeIntention("Create field from usage");

        myFixture.checkResultByFile("/src/com/test/CreateFieldFromUsage.java", "after/CreateFieldFromUsage.java", false);
    }
}