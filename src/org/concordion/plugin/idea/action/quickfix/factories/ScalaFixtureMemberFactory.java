package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class ScalaFixtureMemberFactory implements ConcordionFixtureMemberFactory {

    @NotNull
    @Override
    public PsiField createField(@NotNull MemberCreationParameters parameters) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public PsiMethod createMethod(@NotNull MemberCreationParameters parameters) {
        throw new UnsupportedOperationException();
    }
}
