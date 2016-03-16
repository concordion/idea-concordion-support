package org.concordion.plugin.idea.autocomplete;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.ConcordionCommands;

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionEmbeddedCommandsCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionCommandsInMarkdownLinks() {

        copyTestFixtureToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Commands.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(ConcordionCommands.EMBEDDED_COMMANDS.stream().map(c -> c + '=').collect(toList()));
    }
}