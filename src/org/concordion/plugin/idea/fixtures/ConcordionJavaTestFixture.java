package org.concordion.plugin.idea.fixtures;

import com.intellij.psi.*;
import org.concordion.plugin.idea.action.quickfix.factories.JavaFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionJavaTestFixture extends AbstractConcordionTestFixture<PsiJavaCodeReferenceElement> {

    public ConcordionJavaTestFixture() {
        super("java", PsiJavaCodeReferenceElement.class, new JavaFixtureMemberFactory());
    }

    @Nullable
    @Override
    protected String qualifiedReference(@NotNull PsiJavaCodeReferenceElement codeReference) {
        return codeReference.getCanonicalText();
    }
}
