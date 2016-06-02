package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public interface ConcordionFixtureMemberFactory {

    /**
     * This may return getter
     */
    @NotNull
    PsiMember createField(@NotNull MemberCreationParameters parameters);

    @NotNull
    PsiMethod createMethod(@NotNull MemberCreationParameters parameters);
}
