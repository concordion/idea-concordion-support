package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.psi.*;

public interface ConcordionPsiElement extends PsiNamedElement {

    PsiClass getContainingClass();

    PsiMember getContainingMember();

    PsiType getType();
}
