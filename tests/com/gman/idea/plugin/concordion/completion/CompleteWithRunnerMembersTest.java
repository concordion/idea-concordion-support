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

    public void testShouldCompleteConcordionExpressionWithRunnerProperties() {

        copyJavaRunnerToConcordionProject("Fields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Fields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicProperty")
                .doesNotContain("privateProperty")
                .doesNotContain("staticProperty");
    }

    public void testShouldCompleteConcordionExpressionWithRunnerMethods() throws Exception {

        copyJavaRunnerToConcordionProject("Methods.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Methods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod");
    }

    public void testShouldCompleteWithNestedMembersOfField() {

        copyJavaRunnerToConcordionProject("NestedMembersOfField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testShouldCompleteWithNestedMembersOfMethods() {

        copyJavaRunnerToConcordionProject("NestedMembersOfMethod.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfMethod.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testShouldCompleteWithNestedMembersOfVariables() {

        copyJavaRunnerToConcordionProject("NestedMembersOfVariable.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NestedMembersOfVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }
}
