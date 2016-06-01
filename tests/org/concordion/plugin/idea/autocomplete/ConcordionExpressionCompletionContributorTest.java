package org.concordion.plugin.idea.autocomplete;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionExpressionCompletionContributorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/completion";
    }

    public void testCompleteConcordionExpressionWithFixtureProperties() {

        copyTestFixtureToConcordionProject("Fields.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Fields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicProperty")
                .doesNotContain("privateProperty")
                .doesNotContain("protectedProperty")
                .doesNotContain("packagePrivateProperty")
                .doesNotContain("staticProperty");
    }

    public void testCompleteConcordionExpressionWithGroovyFixtureProperties() {

        copyTestFixtureToConcordionProject("Fields.groovy");
        VirtualFile htmlSpec = copySpecToConcordionProject("Fields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        //groovy converts fields into accessors
        assertThat(myFixture.getLookupElementStrings())
                .doesNotContain("privateByDefaultProperty").contains("getPrivateByDefaultProperty").contains("setPrivateByDefaultProperty")
                .doesNotContain("privateProperty").doesNotContain("getPrivateProperty").doesNotContain("setPrivateProperty")
                .doesNotContain("protectedProperty").doesNotContain("getProtectedProperty").doesNotContain("setProtectedProperty")
                .contains("publicProperty").doesNotContain("getPublicProperty").doesNotContain("setPublicProperty")
                .doesNotContain("staticProperty").doesNotContain("getStaticProperty").doesNotContain("setStaticProperty");
    }

    public void testCompleteConcordionExpressionWithFixtureMethods() {

        copyTestFixtureToConcordionProject("Methods.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Methods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod")
                .doesNotContain("protectedMethod")
                .doesNotContain("packagePrivateMethod");
    }

    public void testCompleteConcordionExpressionWithGroovyFixtureMethods() {

        copyTestFixtureToConcordionProject("Methods.groovy");
        VirtualFile htmlSpec = copySpecToConcordionProject("Methods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("publicByDefaultMethod")
                .contains("staticMethod")
                .doesNotContain("privateMethod")
                .doesNotContain("protectedMethod");
    }

    public void testCompleteConcordionExpressionWithScalaFixtureMethods() {

        copyTestFixtureToConcordionProject("Methods.scala");
        VirtualFile htmlSpec = copySpecToConcordionProject("Methods.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();
        assertThat(myFixture.getLookupElementStrings())
                .contains("publicMethod")
                .contains("protectedMethod")//is generated to java public method so that concordion can see it
                .contains("staticMethod")
                .doesNotContain("privateMethod");
    }

    public void testCompleteWithVariablesOfCurrentScope() {

        copyTestFixtureToConcordionProject("Variables.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Variables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("#before")
                .contains("#nested")
                .doesNotContain("#after");
    }

    public void testCompleteWithVariablesOfCurrentScopeIfHashCharAlreadyPresent() {

        copyTestFixtureToConcordionProject("VariablesAfterHashChar.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("VariablesAfterHashChar.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("#before")
                .contains("#nested")
                .doesNotContain("#after");
    }

    public void testCompleteWithVariablesInTheStartOfExpression() {

        copyTestFixtureToConcordionProject("VariablesOnTheStartOfExpression.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("VariablesOnTheStartOfExpression.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("#before")
                .doesNotContain("#after");
    }

    public void testDoesNotCompleteChainWithVariables() {

        copyTestFixtureToConcordionProject("ChainFromVariable.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ChainFromVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .doesNotContain("#before")
                .doesNotContain("#nested")
                .doesNotContain("#after");
    }

    public void testCompleteWithNestedMembersOfField() {

        copyTestFixtureToConcordionProject("NestedMembersOfField.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NestedMembersOfField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testCompleteWithNestedMembersOfMethods() {

        copyTestFixtureToConcordionProject("NestedMembersOfMethod.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NestedMembersOfMethod.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testCompleteWithNestedMembersOfVariables() {

        copyTestFixtureToConcordionProject("NestedMembersOfVariable.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NestedMembersOfVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("field")
                .contains("method");
    }

    public void testCompleteWithLengthPropertyForArrays() {

        copyTestFixtureToConcordionProject("LengthOfArray.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("LengthOfArray.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings())
                .contains("length", "clone")
                .doesNotContain("field", "method");//this should not complete wih methods of array elements
    }

    /**
     * Does not resolve jdk types and qualified names in tests
     */
    public void ignoredTestCompleteWithMembersOfVerifyRowsLoopVariable() {
        //type of variable produced by verifyRows command is determined by generic parameter of the iterable
        copyTestFixtureToConcordionProject("NestedMembersOfVerifyRowsLoopVariable.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NestedMembersOfVerifyRowsLoopVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings()).
                contains("method");
    }

    public void testNoDuplicatedMembersInCompletion() {
        //PsiClass#getFields() and PsiClass#getMethods() return declared field and member in class and each of its parents.
        copyTestFixtureToConcordionProject("NoDuplicatedMemebrsInCompletion.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("NoDuplicatedMemebrsInCompletion.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.completeBasic();

        assertThat(myFixture.getLookupElementStrings()).
                containsOnlyOnce("method").
                containsOnlyOnce("field");
    }
}
