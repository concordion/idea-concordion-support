package com.gman.idea.plugin.concordion.completion;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.VirtualFile;

import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithRunnerMembersTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testShouldCompleteConcordionExpressionWithRunnerProperties() {

        copyJavaRunnerToConcordionProject("ConcordionFields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicProperty")
                .doesNotContain("privateProperty")
                .doesNotContain("staticProperty");
    }

    public void testShouldCompleteConcordionExpressionWithRunnerMethods() throws Exception {

        copyJavaRunnerToConcordionProject("ConcordionMethods.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionMethods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod");
    }
}
