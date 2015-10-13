package org.concordion.plugin.idea.completion;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.VirtualFile;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithSpecialValuesTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteMatchStrategyWithPredefinedStrategies() {

        copyJavaRunnerToConcordionProject("MatchStrategy.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("MatchStrategy.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).containsAll(asList("Default", "BestMatch", "KeyMatch"));
    }

    public void testCompleteMatchingRoleWithKeyRole() {

        copyJavaRunnerToConcordionProject("MatchingRole.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("MatchingRole.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).contains("key");
    }
    
    public void testCompleteStatus() {
        copyJavaRunnerToConcordionProject("Status.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Status.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).containsAll(asList("ExpectedToPass", "ExpectedToFail", "Unimplemented"));
    }
}
