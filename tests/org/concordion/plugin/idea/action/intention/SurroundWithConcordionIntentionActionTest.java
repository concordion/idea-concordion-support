package org.concordion.plugin.idea.action.intention;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class SurroundWithConcordionIntentionActionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testSurroundSelectedTextInHtmlWithConcordion() {

        copyTestFixtureToConcordionProject("SurroundWithConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SurroundWithConcordion.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        executeIntention("Surround with Concordion expression");

        myFixture.checkResultByFile("/resources/com/test/SurroundWithConcordion.html", "after/SurroundWithConcordion.html", false);
    }

    public void testSurroundWordUnderCaretInHtmlWithConcordion() {

        copyTestFixtureToConcordionProject("SurroundWordWithConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SurroundWordWithConcordion.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        executeIntention("Surround with Concordion expression");

        myFixture.checkResultByFile("/resources/com/test/SurroundWordWithConcordion.html", "after/SurroundWordWithConcordion.html", false);
    }
}