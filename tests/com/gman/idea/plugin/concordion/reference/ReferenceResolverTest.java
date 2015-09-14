package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.gman.idea.plugin.concordion.OgnlChainResolver;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceResolverTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/reference";
    }

    public void testShouldResolveFieldReference() {

        copyJavaRunnerToConcordionProject("ConcordionFieldReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionFieldReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiField javaField = resolveReferences(concordionField);

        assertThat(javaField.getName()).isEqualTo("propertyToResolve");
    }

    public void testShouldResolveMethodReference() {

        copyJavaRunnerToConcordionProject("ConcordionMethodReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionMethodReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod concordionField = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionField);

        assertThat(method.getName()).isEqualTo("methodToResolve");
    }

    public void testShouldResolveChains() {

        copyJavaRunnerToConcordionProject("ConcordionChainReference.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("ConcordionChainReference.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionMethod chainNext = elementUnderCaret();
        PsiMethod chainNextMethod = resolveReferences(chainNext);

        PsiElement chainStart = OgnlChainResolver.parentExpressionMethodOrField(chainNext);
        PsiField chainStartField = resolveReferences(chainStart);

        assertThat(chainStartField.getName()).isEqualTo("chainStart");
        assertThat(chainNextMethod.getName()).isEqualTo("chainNext");
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
