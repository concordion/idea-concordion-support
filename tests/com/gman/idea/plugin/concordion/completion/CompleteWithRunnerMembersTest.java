package com.gman.idea.plugin.concordion.completion;

import com.gman.idea.plugin.concordion.ConcordionLightCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import static org.assertj.core.api.Assertions.assertThat;

public class CompleteWithRunnerMembersTest extends ConcordionLightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testShouldCompleteConcordionExpressionWithRunnerProperties() {

        copyJavaRunnerToConcordionProject("ConcordionFields.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicProperty")
                .doesNotContain("privateProperty")
                .doesNotContain("staticProperty");
    }

    public void testShouldCompleteConcordionExpressionWithRunnerMethods() throws Exception {

        copyJavaRunnerToConcordionProject("ConcordionMethods.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionMethods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.complete(CompletionType.BASIC, 1);

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod");
    }
}
