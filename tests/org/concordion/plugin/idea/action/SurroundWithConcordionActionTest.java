package org.concordion.plugin.idea.action;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.EditorTestUtil;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class SurroundWithConcordionActionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testSurroundHtmlTextWithConcordion() throws InterruptedException {

        copyTestFixtureToConcordionProject("SurroundWithConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SurroundWithConcordion.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        EditorTestUtil.executeAction(myFixture.getEditor(), "surroundWithConcordionAction");

        myFixture.checkResultByFile("/resources/com/test/SurroundWithConcordion.html", "after/SurroundWithConcordion.html", false);
    }
}