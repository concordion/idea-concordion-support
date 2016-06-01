package org.concordion.plugin.idea.fixtures;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionJavaTestFixture extends AbstractConcordionTestFixture<PsiJavaCodeReferenceElement> {

    public ConcordionJavaTestFixture() {
        super("java", PsiJavaCodeReferenceElement.class);
    }

    @Nullable
    @Override
    protected String qualifiedReference(@NotNull PsiJavaCodeReferenceElement codeReference) {
        return codeReference.getCanonicalText();
    }

    @NotNull
    @Override
    public JVMElementFactory elementFactory(@NotNull PsiClass testFixture) {
        return JavaPsiFacade.getElementFactory(testFixture.getProject());
    }
}
