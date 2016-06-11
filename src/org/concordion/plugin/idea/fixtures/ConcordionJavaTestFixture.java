package org.concordion.plugin.idea.fixtures;

import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import org.concordion.plugin.idea.action.quickfix.factories.JavaFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class ConcordionJavaTestFixture extends AbstractConcordionTestFixture<PsiJavaCodeReferenceElement> {

    public ConcordionJavaTestFixture() {
        super(JavaLanguage.INSTANCE, "java", PsiJavaCodeReferenceElement.class, new JavaFixtureMemberFactory());
    }

    @Nullable
    @Override
    protected String qualifiedReference(@NotNull PsiJavaCodeReferenceElement codeReference) {
        return codeReference.getCanonicalText();
    }
}
