package org.concordion.plugin.idea.action.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

public class CreateFieldFromConcordionUsageTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreateFieldFromUsage() throws InterruptedException {

        copyTestFixtureToConcordionProject("CreateFieldFromUsage.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CreateFieldFromUsage.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        IntentionAction fix = myFixture.findSingleIntention("Create field from usage");
        assertTrue(fix.isAvailable(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile()));

        new WriteCommandAction(myFixture.getProject()){
            @Override
            protected void run(@NotNull final Result result) throws Throwable {
                fix.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile());
            }
        }.execute();

        myFixture.checkResultByFile("/src/com/test/CreateFieldFromUsage.java", "after/CreateFieldFromUsage.java", false);
    }
}