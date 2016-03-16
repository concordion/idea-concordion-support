package org.concordion.plugin.idea.reference;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.lang.psi.ConcordionField;
import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceResolverInHtmlInjectionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/reference";
    }

    public void testResolveField() {

        copyTestFixtureToConcordionProject("FieldReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("FieldReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiField javaField = resolveReferences(concordionField);

        assertThat(javaField).isNotNull();
        assertThat(javaField.getName()).isEqualTo("propertyToResolve");
    }


    public void testResolveInheritedField() {

        copyTestFixtureToConcordionProject("Parent.java");
        copyTestFixtureToConcordionProject("InheritedFieldReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("InheritedFieldReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiField javaField = resolveReferences(concordionField);

        assertThat(javaField).isNotNull();
        assertThat(javaField.getName()).isEqualTo("inheritedField");
    }

    public void testResolveGetterAsAFieldIfPresent() {

        copyTestFixtureToConcordionProject("GetterAsAField.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("GetterAsAField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiMethod javaGetter = resolveReferences(concordionField);

        assertThat(javaGetter).isNotNull();
        assertThat(javaGetter.getName()).isEqualTo("getPropertyToResolve");
    }

    public void testResolveMethod() {

        copyTestFixtureToConcordionProject("MethodReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MethodReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("methodToResolve");
    }


    public void testResolveMethodWithArguments() {

        copyTestFixtureToConcordionProject("MethodWithArgumentsReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MethodWithArgumentsReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("methodToResolve");
        assertThat(method.getParameterList().getParameters()).hasSize(6);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(6);
    }

    public void testResolveMethodWithVarArgs() {

        copyTestFixtureToConcordionProject("MethodWithVarArg.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MethodWithVarArg.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("sum");
        assertThat(method.getParameterList().getParameters()).hasSize(1);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(3);
    }

    public void testResolveMethodWithVarArgsWithExplicitArray() {

        copyTestFixtureToConcordionProject("MethodWithVarArgArray.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MethodWithVarArgArray.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("sum");
        assertThat(method.getParameterList().getParameters()).hasSize(1);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(1);
    }

    public void testResolveOverloadedMethodsWithDifferentArgumentsNumber() {

        copyTestFixtureToConcordionProject("OverloadedMethodArgumentsNumber.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("OverloadedMethodArgumentsNumber.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod javaMethod = resolveReferences(concordionMethod);

        assertThat(javaMethod).isNotNull();
        assertThat(javaMethod.getName()).isEqualTo("methodToResolve");
        assertThat(javaMethod.getParameterList().getParameters()).hasSize(2);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(2);
    }

    public void testResolveOverloadedMethodsWithDifferentArgumentsType() {

        copyTestFixtureToConcordionProject("OverloadedMethodArgumentsType.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("OverloadedMethodArgumentsType.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod javaMethod = resolveReferences(concordionMethod);

        assertThat(javaMethod).isNotNull();
        assertThat(javaMethod.getName()).isEqualTo("methodToResolve");
        assertThat(javaMethod.getParameterList().getParameters()).hasSize(3);

        assertThat(methodParameterType(javaMethod, 0)).isEqualTo("com.test.OverloadedMethodArgumentsType.B");
        assertThat(methodParameterType(javaMethod, 1)).isEqualTo("int");
        assertThat(methodParameterType(javaMethod, 2)).isEqualTo("com.test.OverloadedMethodArgumentsType.A");

        assertThat(concordionMethod.getParametersCount()).isEqualTo(3);
    }

    public void testResolveOverloadedMethodsWithDifferentArgumentsType2() {
        copyTestFixtureToConcordionProject("OverloadedMethodArgumentsNumber2.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("OverloadedMethodArgumentsNumber2.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod javaMethod = resolveReferences(concordionMethod);

        assertThat(javaMethod).isNotNull();
        assertThat(javaMethod.getName()).isEqualTo("methodToResolve");
        assertThat(javaMethod.getParameterList().getParameters()).hasSize(2);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(2);
    }

    public void testResolveInheritedMethodReferences() {

        copyTestFixtureToConcordionProject("Parent.java");
        copyTestFixtureToConcordionProject("InheritedMethodReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("InheritedMethodReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod javaMethod = resolveReferences(concordionMethod);

        assertThat(javaMethod).isNotNull();
        assertThat(javaMethod.getName()).isEqualTo("inheritedMethod");
    }

    public void testResolveChains() {

        copyTestFixtureToConcordionProject("ChainReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ChainReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod chainNext = elementUnderCaret();
        PsiMethod chainNextMethod = resolveReferences(chainNext);

        PsiElement chainStart = chainNext.getConcordionParent();
        PsiField chainStartField = resolveReferences(chainStart);

        assertThat(chainStartField).isNotNull();
        assertThat(chainStartField.getName()).isEqualTo("chainStart");
        assertThat(chainNextMethod).isNotNull();
        assertThat(chainNextMethod.getName()).isEqualTo("chainNext");
    }

    public void testResolveChainsWithArrays() {

        copyTestFixtureToConcordionProject("ChainReferenceWithArray.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ChainReferenceWithArray.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod chainNext = elementUnderCaret();
        PsiMethod chainNextMethod = resolveReferences(chainNext);

        PsiElement chainStart = chainNext.getConcordionParent();
        PsiField chainStartField = resolveReferences(chainStart);

        assertThat(chainStartField).isNotNull();
        assertThat(chainStartField.getName()).isEqualTo("chainStart");
        assertThat(chainNextMethod).isNotNull();
        assertThat(chainNextMethod.getName()).isEqualTo("chainNext");
    }

    public void testResolveVariable() {

        copyTestFixtureToConcordionProject("VariableReference.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("VariableReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testResolveVariableReferenceFromNestedScope() {

        copyTestFixtureToConcordionProject("VariableReferenceFromNestedScope.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("VariableReferenceFromNestedScope.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("nested");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testResolveVariableFromExecuteCommand() {

        copyTestFixtureToConcordionProject("VariableDefinedInExecute.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("VariableDefinedInExecute.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testResolveVariableFromVerifyRowsCommand() {

        copyTestFixtureToConcordionProject("VariableDefinedInVerifyRows.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("VariableDefinedInVerifyRows.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testResolveReservedVariable() {

        copyTestFixtureToConcordionProject("ReservedVariable.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ReservedVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("TEXT");
        assertThat(declaration).isEqualTo(variable);
    }

    public void testResolveLastAssignmentInCaseOfVariableReuse() {
        // resolve inner var set as it executed last before method run
        copyTestFixtureToConcordionProject("ReuseVariable.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ReuseVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");

        //In this case variable declared on inner level after it is actually used on outer
        assertThat(InjectedLanguageUtil.findInjectionHost(declaration).getTextOffset())
                .isGreaterThan(InjectedLanguageUtil.findInjectionHost(variable).getTextOffset());
    }

    public void testVariableDeclarationIsResolvedOnlyByFullNameMatch() {
        //first simple implementation resolved #thevar or #variable as declaration for #var :D
        copyTestFixtureToConcordionProject("DoNotResolveVariableDeclarationByPartialNameMatch.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("DoNotResolveVariableDeclarationByPartialNameMatch.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNull();
    }

    private String methodParameterType(PsiMethod method, int parameter) {
        return method.getParameterList().getParameters()[parameter].getType().getCanonicalText();
    }
}
