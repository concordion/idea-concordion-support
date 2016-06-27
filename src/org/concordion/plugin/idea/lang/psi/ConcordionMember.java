package org.concordion.plugin.idea.lang.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMember;
import org.jetbrains.annotations.Nullable;

public interface ConcordionMember extends ConcordionPsiElement {

    @Nullable
    PsiClass getContainingClass();

    @Nullable
    PsiClassType getContainingClassType();

    @Nullable
    PsiMember getContainingMember();
}
