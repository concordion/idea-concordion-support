package org.concordion.plugin.idea.autocomplete;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.settings.ConcordionCommandsCaseType.*;

public class ConcordionHtmlCommandsCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    private static final Iterable<String> EXTENSION_COMMANDS_WITH_EXT_PREFIX = ImmutableList.of(
            "ext:screenshot",
            "ext:embed",
            "ext:executeOnlyIf"
    );

    private static final Iterable<String> CAMEL_CASE_COMMANDS = ImmutableList.of(
            "c:assertEquals",
            "c:assertTrue",
            "c:assertFalse",
            "c:verifyRows",
            "c:matchStrategy",
            "c:matchingRole"
    );

    private static final Iterable<String> SPINAL_CASE_COMMANDS = ImmutableList.of(
            "c:assert-equals",
            "c:assert-true",
            "c:assert-false",
            "c:verify-rows",
            "c:match-strategy",
            "c:matching-role"
    );

    private static final Iterable<String> SINGLE_WORD_COMMANDS = ImmutableList.of(
            "c:execute",
            "c:set",
            "c:echo",
            "c:run",
            "c:example",
            "c:status"
    );

    private static final Iterable<String> EMBEDDED_SPECIFIC_COMMANDS = ImmutableList.of(
            "?="
    );

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionCommandsInHtmlTagsWithBothCases() {

        useCommandsCase(BOTH);

        copyTestFixtureToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(SINGLE_WORD_COMMANDS)
                .containsAll(CAMEL_CASE_COMMANDS)
                .containsAll(SPINAL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(EMBEDDED_SPECIFIC_COMMANDS)
                .doesNotContainAnyElementsOf(EXTENSION_COMMANDS_WITH_EXT_PREFIX);

        completeWithConcordionCommand("c:execute");

        myFixture.checkResultByFile("after/Commands.html");
    }

    public void testCompleteConcordionCommandsInHtmlTagsWithCamelCaseOnly() {

        useCommandsCase(CAMEL_CASE);

        copyTestFixtureToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(SINGLE_WORD_COMMANDS)
                .containsAll(CAMEL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(SPINAL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(EMBEDDED_SPECIFIC_COMMANDS)
                .doesNotContainAnyElementsOf(EXTENSION_COMMANDS_WITH_EXT_PREFIX);
    }

    public void testCompleteConcordionCommandsInHtmlTagsWithSpinalCaseOnly() {

        useCommandsCase(SPINAL_CASE);

        copyTestFixtureToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(SINGLE_WORD_COMMANDS)
                .containsAll(SPINAL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(CAMEL_CASE_COMMANDS)
                .doesNotContainAnyElementsOf(EMBEDDED_SPECIFIC_COMMANDS)
                .doesNotContainAnyElementsOf(EXTENSION_COMMANDS_WITH_EXT_PREFIX);
    }

    public void testDoNotCompleteWithEmbeddedConcordionCommandsInHtmlTags() {

        copyTestFixtureToConcordionProject("EmbeddedCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("EmbeddedCommands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        List<String> lookupElementStrings = myFixture.getLookupElementStrings();

        assertThat(lookupElementStrings)
                .doesNotContainAnyElementsOf(EMBEDDED_SPECIFIC_COMMANDS);
    }

    public void testCompleteExtensionsCommandsInHtmlTags() {

        copyTestFixtureToConcordionProject("ExtensionsCommands.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ExtensionsCommands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(EXTENSION_COMMANDS_WITH_EXT_PREFIX);
    }

    public void testCompleteExtensionsCommandsWithGroovyInHtmlTags() {

        copyTestFixtureToConcordionProject("ExtensionsCommands.groovy");
        VirtualFile htmlSpec = copySpecToConcordionProject("ExtensionsCommands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(EXTENSION_COMMANDS_WITH_EXT_PREFIX);
    }


    public void testCompleteExtensionsCommandsWithScalaInHtmlTags() {

        copyTestFixtureToConcordionProject("ExtensionsCommands.scala");
        VirtualFile htmlSpec = copySpecToConcordionProject("ExtensionsCommands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(EXTENSION_COMMANDS_WITH_EXT_PREFIX);
    }

    public void testDoesNotDuplicateConcordionPrefixForStartedHalfTypedCommand() {

        copyTestFixtureToConcordionProject("CommandStarted.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CommandStarted.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        completeWithConcordionCommand("c:execute");

        myFixture.checkResultByFile("after/CommandStarted.html");
    }

    public void testInsertConcordionSchemaIfAbsentWhileCompletion() {

        copyTestFixtureToConcordionProject("CommandsNoSchema.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CommandsNoSchema.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        completeWithConcordionCommand("c:execute");

        myFixture.checkResultByFile("after/CommandsNoSchema.html");
    }

    public void testInsertConcordionExtensionSchemaIfAbsentWhileCompletion() {

        copyTestFixtureToConcordionProject("ExtensionsCommandsNoSchema.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ExtensionsCommandsNoSchema.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        completeWithConcordionCommand("ext:executeOnlyIf");

        myFixture.checkResultByFile("after/ExtensionsCommandsNoSchema.html");
    }

    private void completeWithConcordionCommand(String command) {
        LookupImpl lookup = (LookupImpl) LookupManager.getActiveLookup(myFixture.getEditor());
        for (LookupElement element : lookup.getItems()) {
            if (element.getLookupString().equals(command)) {
                lookup.setCurrentItem(element);
                lookup.finishLookup('\t');
                return;
            }
        }
        fail(command + " is not found in completion");
    }

    @Override
    protected void runTest() throws Throwable {
        new WriteCommandAction(getProject()) {
            @Override
            protected void run(@NotNull Result result) throws Throwable {
                ConcordionHtmlCommandsCompletionContributorTest.super.runTest();
            }
        }.execute();
    }
}
