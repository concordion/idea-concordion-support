package org.concordion.plugin.idea.action.intention;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class AddMissingTestFixtureIntentionActionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreatesMissingFixture() {

        //copy something to test sources to make sure it exists
        copyTestFixtureToConcordionProject("blank.txt");
        VirtualFile spec = copySpecToConcordionProject("AddMissingFixture.html");

        myFixture.configureFromExistingVirtualFile(spec);

        executeIntention("Add missing Concordion test fixture");

        myFixture.checkResultByFile("/src/com/test/AddMissingFixture.java", "after/AddMissingFixture.java", false);
    }
}
