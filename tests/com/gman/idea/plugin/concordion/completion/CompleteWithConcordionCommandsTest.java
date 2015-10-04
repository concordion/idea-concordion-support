package com.gman.idea.plugin.concordion.completion;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.gman.idea.plugin.concordion.autocomplete.ConcordionCommandsCompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithConcordionCommandsTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testShouldCompleteConcordionCommandsInHtmlTags() {

        copyJavaRunnerToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).containsAll(commandsWithSchemaPrefix("c", ConcordionCommandsCompletionContributor.ALL_COMMANDS));
    }

    private List<String> commandsWithSchemaPrefix(String schemaPrefix, List<String> commands) {
        return commands.stream().map(c -> schemaPrefix + ':' + c).collect(toList());
    }
}
