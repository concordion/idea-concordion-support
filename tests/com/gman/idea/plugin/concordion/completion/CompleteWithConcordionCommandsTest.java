package com.gman.idea.plugin.concordion.completion;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithConcordionCommandsTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testShouldCompleteConcordionCommandsInHtnlTags() {

        copyJavaRunnerToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).containsAll(Arrays.asList(
                "c:assertEquals", "c:assert-equals",
                "c:assertTrue", "c:assert-true",
                "c:assertFalse", "c:assert-false",
                "c:echo",
                "c:execute",
                "c:run",
                "c:set",
                "c:verifyRows", "c:verify-rows",
                "c:example"
        ));
    }
}
