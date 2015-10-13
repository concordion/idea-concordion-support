package org.concordion.plugin.idea.reference;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.lang.psi.AbstractConcordionPsiElement;
import org.concordion.plugin.idea.lang.psi.ConcordionField;
import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceResolverTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/reference";
    }

    public void testShouldResolveFieldReference() {

        copyJavaRunnerToConcordionProject("FieldReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("FieldReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiField javaField = resolveReferences(concordionField);

        assertThat(javaField).isNotNull();
        assertThat(javaField.getName()).isEqualTo("propertyToResolve");
    }

    public void testShouldResolveInheritedFieldReference() {

        copyJavaRunnerToConcordionProject("Parent.java");
        copyJavaRunnerToConcordionProject("InheritedFieldReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("InheritedFieldReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiField javaField = resolveReferences(concordionField);

        assertThat(javaField).isNotNull();
        assertThat(javaField.getName()).isEqualTo("inheritedField");
    }

    public void testShouldResolveGetterAsAFieldIfPresent() {

        copyJavaRunnerToConcordionProject("GetterAsAField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("GetterAsAField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiMethod javaGetter = resolveReferences(concordionField);

        assertThat(javaGetter).isNotNull();
        assertThat(javaGetter.getName()).isEqualTo("getPropertyToResolve");
    }

    public void testShouldResolveMethodReference() {

        copyJavaRunnerToConcordionProject("MethodReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("MethodReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("methodToResolve");
    }

    public void testShouldResolveMethodWithArgumentsReference() {

        copyJavaRunnerToConcordionProject("MethodWithArgumentsReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("MethodWithArgumentsReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("methodToResolve");
        assertThat(method.getParameterList().getParameters()).hasSize(6);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(6);
    }

    public void testShouldResolveOverloadedMethodReference() {

        copyJavaRunnerToConcordionProject("OverloadedMethodReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("OverloadedMethodReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("methodToResolve");
        assertThat(method.getParameterList().getParameters()).hasSize(2);
        assertThat(concordionMethod.getParametersCount()).isEqualTo(2);

        PsiClass containingClass = method.getContainingClass();
        for (PsiMethod psiMethod : containingClass.getMethods()) {
            if (psiMethod.getParameterList().getParametersCount() != 2) {
                assertThat(psiMethod.getReferences()).hasSize(0);
            }
        }
    }

    public void testShouldResolveInheritedMethodReferences() {

        copyJavaRunnerToConcordionProject("Parent.java");
        copyJavaRunnerToConcordionProject("InheritedMethodReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("InheritedMethodReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod javaMethod = resolveReferences(concordionMethod);

        assertThat(javaMethod).isNotNull();
        assertThat(javaMethod.getName()).isEqualTo("inheritedMethod");
    }

    public void testShouldResolveChains() {

        copyJavaRunnerToConcordionProject("ChainReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ChainReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod chainNext = elementUnderCaret();
        PsiMethod chainNextMethod = resolveReferences(chainNext);

        PsiElement chainStart = AbstractConcordionPsiElement.parentConcordionExpressionOf(chainNext);
        PsiField chainStartField = resolveReferences(chainStart);

        assertThat(chainStartField).isNotNull();
        assertThat(chainStartField.getName()).isEqualTo("chainStart");
        assertThat(chainNextMethod).isNotNull();
        assertThat(chainNextMethod.getName()).isEqualTo("chainNext");
    }

    public void testShouldResolveVariableReference() {

        copyJavaRunnerToConcordionProject("VariableReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("VariableReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testShouldResolveVariableReferenceFromNestedScope() {

        copyJavaRunnerToConcordionProject("VariableReferenceFromNestedScope.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("VariableReferenceFromNestedScope.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("nested");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testShouldResolveVariableFromExecuteCommand() {

        copyJavaRunnerToConcordionProject("VariableDefinedInExecute.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("VariableDefinedInExecute.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testShouldResolveVariableFromVerifyRowsCommand() {

        copyJavaRunnerToConcordionProject("VariableDefinedInVerifyRows.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("VariableDefinedInVerifyRows.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }

    public void testShouldResolveReservedVariable() {

        copyJavaRunnerToConcordionProject("ReservedVariable.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ReservedVariable.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("TEXT");
        assertThat(declaration).isEqualTo(variable);
    }

    public void testShouldResolveLastAssignmentInCaseOfVariableReuse() {
        //Should resolve inner var set as it executed last before method run
        copyJavaRunnerToConcordionProject("ReuseVariable.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ReuseVariable.html");

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
        copyJavaRunnerToConcordionProject("DoNotResolveVariableDeclarationByPartialNameMatch.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("DoNotResolveVariableDeclarationByPartialNameMatch.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNull();
    }

    private <T extends PsiElement> T elementUnderCaret() {
        return (T) myFixture.getFile().findElementAt(myFixture.getCaretOffset() - 1).getParent();
    }

    private <T extends PsiElement> T resolveReferences(PsiElement e) {
        PsiReference[] references = e.getReferences();
        assertThat(references).hasSize(1);
        return (T) references[0].resolve();
    }

}
