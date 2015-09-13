package com.gman.idea.plugin.concordion.completion;

import com.gman.idea.plugin.concordion.ConcordionLightCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithConcordionCommandsTest extends ConcordionLightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion/concordionCommands";
    }

    public void testShouldCompleteConcordionCommandsInHtnlTags() {

        copyJavaRunnerToConcordionProject("ConcordionSpec.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionSpec.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        myFixture.complete(CompletionType.BASIC, 1);

        List<String> options = myFixture.getLookupElementStrings();
        assertThat(options).containsAll(Arrays.asList(
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
