package org.concordion.plugin.idea.action.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
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

        IntentionAction fix = myFixture.findSingleIntention("Create field from usage");
        assertTrue(fix.isAvailable(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile()));

        writeAction(() -> {
            fix.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile());
            return null;
        });

        myFixture.checkResultByFile("/src/com/test/CreateFieldFromUsage.java", "after/CreateFieldFromUsage.java", false);
    }
}