package org.concordion.plugin.idea.autocomplete;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionDictionaryValuesCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteMatchStrategyWithPredefinedStrategies() {

        copyTestFixtureToConcordionProject("MatchStrategy.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MatchStrategy.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings()).containsAll(asList("Default", "BestMatch", "KeyMatch"));
    }

    public void testCompleteMatchingRoleWithKeyRole() {

        copyTestFixtureToConcordionProject("MatchingRole.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MatchingRole.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings()).contains("key");
    }
    
    public void testCompleteStatus() {
        copyTestFixtureToConcordionProject("Status.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Status.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings()).containsAll(asList("ExpectedToPass", "ExpectedToFail", "Unimplemented"));
    }
}
