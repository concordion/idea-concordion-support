package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiType;

public interface ConcordionPsiElement extends PsiNameIdentifierOwner {

    String getName();

    PsiClass getContainingClass();

    PsiMember getContainingMember();

    PsiType getType();
}
