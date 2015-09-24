package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionMember;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionMemberReference extends AbstractConcordionReference<ConcordionMember> {

    public ConcordionMemberReference(@NotNull ConcordionMember owner) {
        super(owner);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return owner.getContainingMember();
    }
}
