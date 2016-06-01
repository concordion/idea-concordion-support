package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public interface ConcordionFixtureMemberFactory {

    @NotNull
    PsiField createField(@NotNull MemberCreationParameters parameters);

    @NotNull
    PsiMethod createMethod(@NotNull MemberCreationParameters parameters);
}
