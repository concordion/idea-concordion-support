package org.concordion.plugin.idea.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.autocomplete.ConcordionCommandsCompletionContributor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionCommandsCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionCommandsInHtmlTags() {

        copyJavaRunnerToConcordionProject("Commands.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Commands.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings()).containsAll(commandsWithSchemaPrefix("c", ConcordionCommandsCompletionContributor.ALL_COMMANDS));

        completeWithExecuteCommand();

        myFixture.checkResultByFile("CommandsCompleted.html");
    }

    public void testDoesNotDuplicateConcordionPrefixForStartedHalfTypedCommand() {

        copyJavaRunnerToConcordionProject("CommandStarted.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("CommandStarted.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        completeWithExecuteCommand();

        myFixture.checkResultByFile("CommandStartedCompleted.html");
    }

    private List<String> commandsWithSchemaPrefix(String schemaPrefix, List<String> commands) {
        return commands.stream().map(c -> schemaPrefix + ':' + c).collect(toList());
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
