package org.concordion.plugin.idea.autocomplete;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionDictionaryValuesCompletionContributorInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteMatchStrategyWithPredefinedStrategies() {

        copyTestFixtureToConcordionProject("MatchStrategy.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MatchStrategy.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("Default", "BestMatch", "KeyMatch")
                .doesNotContain("shouldNotBeInDictionaryCompletion");
    }

    public void testCompleteStatus() {
        copyTestFixtureToConcordionProject("Status.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Status.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("ExpectedToPass", "ExpectedToFail", "Unimplemented")
                .doesNotContain("shouldNotBeInDictionaryCompletion");
    }
}
