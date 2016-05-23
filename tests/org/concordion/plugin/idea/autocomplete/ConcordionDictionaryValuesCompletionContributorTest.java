package org.concordion.plugin.idea.autocomplete;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

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

        assertThat(myFixture.getLookupElementStrings())
                .contains("Default", "BestMatch", "KeyMatch")
                .doesNotContain("shouldNotBeInDictionaryCompletion");
    }

    public void testCompleteMatchingRoleWithKeyRole() {

        copyTestFixtureToConcordionProject("MatchingRole.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MatchingRole.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("key")
                .doesNotContain("shouldNotBeInDictionaryCompletion");
    }
    
    public void testCompleteStatus() {
        copyTestFixtureToConcordionProject("Status.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Status.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("ExpectedToPass", "ExpectedToFail", "Unimplemented")
                .doesNotContain("shouldNotBeInDictionaryCompletion");
    }
}
