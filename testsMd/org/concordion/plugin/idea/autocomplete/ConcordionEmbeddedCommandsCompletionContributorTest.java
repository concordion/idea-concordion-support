package org.concordion.plugin.idea.autocomplete;

import com.google.common.collect.ImmutableList;
import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.settings.ConcordionCommandsCaseType.*;

public class ConcordionEmbeddedCommandsCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    private static final Iterable<String> CAMEL_CASE_COMMANDS = ImmutableList.of(
            "c:assertEquals=",
            "c:assertTrue=",
            "c:assertFalse=",
            "c:verifyRows=",
            "c:matchStrategy=",
            "c:matchingRole="
    );

    private static final Iterable<String> SPINAL_CASE_COMMANDS = ImmutableList.of(
            "c:assert-equals=",
            "c:assert-true=",
            "c:assert-false=",
            "c:verify-rows=",
            "c:match-strategy=",
            "c:matching-role="
    );

    private static final Iterable<String> SINGLE_WORD_COMMANDS = ImmutableList.of(
            "c:execute=",
            "c:set=",
            "c:echo=",
            "c:run=",
            "c:example=",
            "c:status=",
            "?="
    );

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionCommandsInMarkdownLinksWithBothCases() {

        useCommandsCase(BOTH);

        copyTestFixtureToConcordionProject("EmbeddedCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("EmbeddedCommands.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(SINGLE_WORD_COMMANDS)
                .containsAll(CAMEL_CASE_COMMANDS)
                .containsAll(SPINAL_CASE_COMMANDS);
    }

    public void testCompleteConcordionCommandsInMarkdownLinksWithCamelCaseOnly() {

        useCommandsCase(CAMEL_CASE);

        copyTestFixtureToConcordionProject("EmbeddedCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("EmbeddedCommands.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(SINGLE_WORD_COMMANDS)
                .containsAll(CAMEL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(SPINAL_CASE_COMMANDS);
    }

    public void testCompleteConcordionCommandsInMarkdownLinksWithSpinalCaseOnly() {

        useCommandsCase(SPINAL_CASE);

        copyTestFixtureToConcordionProject("EmbeddedCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("EmbeddedCommands.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(SINGLE_WORD_COMMANDS)
                .containsAll(SPINAL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(CAMEL_CASE_COMMANDS);
    }

    public void testDoNotCompleteWithCommandsIfOnePresent() {

        copyTestFixtureToConcordionProject("CommandsAfterCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CommandsAfterCommands.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .doesNotContainAnyElementsOf(SINGLE_WORD_COMMANDS)
                .doesNotContainAnyElementsOf(SPINAL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(CAMEL_CASE_COMMANDS);
    }
}