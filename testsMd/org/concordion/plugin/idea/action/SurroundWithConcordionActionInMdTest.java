package org.concordion.plugin.idea.action;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.EditorTestUtil;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class SurroundWithConcordionActionInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testSurroundMdTextWithConcordion() throws InterruptedException {

        copyTestFixtureToConcordionProject("SurroundWithConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SurroundWithConcordion.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        EditorTestUtil.executeAction(myFixture.getEditor(), "surroundWithConcordionAction");

        myFixture.checkResultByFile("/resources/com/test/SurroundWithConcordion.md", "after/SurroundWithConcordion.md", false);
    }
}