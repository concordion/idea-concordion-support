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

    public void testCompleteConcordionExpressionWithRunnerProperties() {

        copyJavaRunnerToConcordionProject("Fields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Fields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicProperty")
                .doesNotContain("privateProperty")
                .doesNotContain("staticProperty");
    }

    public void testCompleteConcordionExpressionWithRunnerMethods() {

        copyJavaRunnerToConcordionProject("Methods.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Methods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod");
    }

    public void testCompleteWithNestedMembersOfField() {

        copyJavaRunnerToConcordionProject("NestedMembersOfField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testCompleteWithNestedMembersOfMethods() {

        copyJavaRunnerToConcordionProject("NestedMembersOfMethod.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfMethod.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testCompleteWithNestedMembersOfVariables() {

        copyJavaRunnerToConcordionProject("NestedMembersOfVariable.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    /**
     * Does not resolve jdk types and qualified names in tests
     */
    public void ignoredTestCompleteWithMembersOfVerifyRowsLoopVariable() {
        //type of variable produced by verifyRows command is determined by generic parameter of the iterable
        copyJavaRunnerToConcordionProject("NestedMembersOfVerifyRowsLoopVariable.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfVerifyRowsLoopVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings()).
                contains("method");
    }
}
