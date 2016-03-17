package org.concordion.plugin.idea.autocomplete;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionExpressionCompletionContributorInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionExpressionWithFixtureProperties() {

        copyTestFixtureToConcordionProject("Fields.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Fields.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicProperty")
                .doesNotContain("privateProperty")
                .doesNotContain("staticProperty");
    }

    public void testCompleteConcordionExpressionWithFixtureMethods() {

        copyTestFixtureToConcordionProject("Methods.java");
        VirtualFile mdSpec = copySpecToConcordionProject("Methods.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod");
    }

    public void testCompleteWithVariablesOfCurrentScope() {

        copyTestFixtureToConcordionProject("Variables.java");
        VirtualFile md = copySpecToConcordionProject("Variables.md");

        myFixture.configureFromExistingVirtualFile(md);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("#before");
        //May go forward for references table in the end of the file and find #after
    }

    //TODO fix
    public void ignoredTestCompleteWithVariablesOfCurrentScopeIfHashCharAlreadyPresent() {

        copyTestFixtureToConcordionProject("VariablesAfterHashChar.java");
        VirtualFile mdSpec = copySpecToConcordionProject("VariablesAfterHashChar.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("#before")
                .doesNotContain("#after");
    }

    //TODO fix
    public void ignoredCompleteWithVariablesInTheStartOfExpression() {

        copyTestFixtureToConcordionProject("VariablesOnTheStartOfExpression.java");
        VirtualFile mdSpec = copySpecToConcordionProject("VariablesOnTheStartOfExpression.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("#before")
                .doesNotContain("#after");
    }
}
