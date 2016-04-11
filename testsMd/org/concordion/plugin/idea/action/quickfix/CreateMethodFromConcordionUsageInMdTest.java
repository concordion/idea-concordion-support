package org.concordion.plugin.idea.action.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
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

        IntentionAction fix = myFixture.findSingleIntention("Create method from usage");
        assertTrue(fix.isAvailable(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile()));

        writeAction(() -> {
            fix.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile());
            return null;
        });

        myFixture.checkResultByFile("/src/com/test/CreateMethodFromUsage.java", "after/CreateMethodFromUsage.java", false);
    }
}