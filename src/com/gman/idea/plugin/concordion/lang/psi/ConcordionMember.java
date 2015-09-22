package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import org.jetbrains.annotations.Nullable;

public interface ConcordionMember extends ConcordionPsiElement {

    @Nullable
    PsiClass getContainingClass();

    @Nullable
    PsiMember getContainingMember();
}
