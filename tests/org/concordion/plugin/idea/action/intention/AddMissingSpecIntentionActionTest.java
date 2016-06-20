package org.concordion.plugin.idea.action.intention;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class AddMissingSpecIntentionActionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreatesMissingSpec() {

        //copy something to test resources to make sure it exists
        copySpecToConcordionProject("blank.txt");
        VirtualFile fixture = copyTestFixtureToConcordionProject("AddMissingSpec.java");

        myFixture.configureFromExistingVirtualFile(fixture);

        executeIntention("Add missing Concordion spec");

        //Due to unit test mode simplified name is used. Actual name is asserted in title of html file
        myFixture.checkResultByFile("/resources/com/test/HTML.html", "after/AddMissingSpec.html", false);
    }
}
