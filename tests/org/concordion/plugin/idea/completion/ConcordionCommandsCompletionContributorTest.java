package org.concordion.plugin.idea.completion;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionCommandsCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    private static final Iterable<String> DEFAULT_COMMANDS_WITH_C_PREFIX = ImmutableList.of(
            "c:assertEquals", "c:assert-equals",
            "c:assertTrue", "c:assert-true",
            "c:assertFalse", "c:assert-false",
            "c:execute",
            "c:set",
            "c:echo",
            "c:verifyRows", "c:verify-rows",
            "c:matchStrategy", "c:match-strategy",
            "c:matchingRole", "c:matching-role",
            "c:run",
            "c:example",
            "c:status"
    );

    private static final Iterable<String> EXTENSION_COMMANDS_WITH_EXT_PREFIX = ImmutableList.of(
            "ext:screenshot",
            "ext:embed",
            "ext:executeOnlyIf"
    );

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionCommandsInHtmlTags() {

        copyJavaRunnerToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(DEFAULT_COMMANDS_WITH_C_PREFIX)
                .doesNotContainAnyElementsOf(EXTENSION_COMMANDS_WITH_EXT_PREFIX);

        completeWithExecuteCommand();

        myFixture.checkResultByFile("CommandsCompleted.html");
    }

    public void testCompleteExtensionsCommandsInHtmlTags() {

        copyJavaRunnerToConcordionProject("ExtensionsCommands.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ExtensionsCommands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .containsAll(DEFAULT_COMMANDS_WITH_C_PREFIX)
                .containsAll(EXTENSION_COMMANDS_WITH_EXT_PREFIX);
    }

    public void testDoesNotDuplicateConcordionPrefixForStartedHalfTypedCommand() {

        copyJavaRunnerToConcordionProject("CommandStarted.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("CommandStarted.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        completeWithExecuteCommand();

        myFixture.checkResultByFile("CommandStartedCompleted.html");
    }

    private void completeWithExecuteCommand() {
        LookupImpl lookup = (LookupImpl) LookupManager.getActiveLookup(myFixture.getEditor());
        for (LookupElement element : lookup.getItems()) {
            if (element.getLookupString().contains("execute")) {
                lookup.setCurrentItem(element);
                lookup.finishLookup('\t');
                return;
            }
        }
        fail("Expected to exit before with command completion");
    }

    @Override
    protected void runTest() throws Throwable {
        new WriteCommandAction(getProject()) {
            @Override
            protected void run(@NotNull Result result) throws Throwable {
                ConcordionCommandsCompletionContributorTest.super.runTest();
            }
        }.execute();
    }
}
