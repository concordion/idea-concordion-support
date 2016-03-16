package org.concordion.plugin.idea.autocomplete;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.ConcordionCommands.EMBEDDED_COMMANDS;

public class ConcordionEmbeddedCommandsCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    private static final Collection<String> MD_LOOK_UPS = EMBEDDED_COMMANDS.stream().map(c -> c + '=').collect(toList());

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
                .containsAll(MD_LOOK_UPS);
    }

    public void testDoNotCompleteWithCommandsIfOnePresent() {

        copyTestFixtureToConcordionProject("CommandsAfterCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CommandsAfterCommands.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .doesNotContainAnyElementsOf(MD_LOOK_UPS);
    }
}