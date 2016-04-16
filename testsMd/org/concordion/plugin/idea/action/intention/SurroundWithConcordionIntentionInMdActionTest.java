package org.concordion.plugin.idea.action.intention;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class SurroundWithConcordionIntentionInMdActionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testSurroundSelectedTextInMdWithConcordion() {

        copyTestFixtureToConcordionProject("SurroundWithConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SurroundWithConcordion.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        executeIntention("Surround with Concordion expression");

        myFixture.checkResultByFile("/resources/com/test/SurroundWithConcordion.md", "after/SurroundWithConcordion.md", false);
    }

    public void testSurroundWordUnderCaretInMdWithConcordion() {

        copyTestFixtureToConcordionProject("SurroundWordWithConcordion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("SurroundWordWithConcordion.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        executeIntention("Surround with Concordion expression");

        myFixture.checkResultByFile("/resources/com/test/SurroundWordWithConcordion.md", "after/SurroundWordWithConcordion.md", false);
    }
}