package com.gman.idea.plugin.concordion.completion;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.VirtualFile;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithSpecialValuesTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testShouldCompleteMatchStrategyWithPredefinedStrategies() {

        copyJavaRunnerToConcordionProject("MatchStrategy.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("MatchStrategy.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).containsAll(asList("Default", "BestMatch", "KeyMatch"));
    }

    public void testShouldCompleteMatchingRoleWithKeyRole() {

        copyJavaRunnerToConcordionProject("MatchingRole.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("MatchingRole.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).contains("key");
    }
}
