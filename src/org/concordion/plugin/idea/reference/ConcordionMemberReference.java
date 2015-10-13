package org.concordion.plugin.idea.reference;

import org.concordion.plugin.idea.lang.psi.ConcordionMember;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionMemberReference extends AbstractConcordionReference<ConcordionMember> {

    public ConcordionMemberReference(@NotNull ConcordionMember owner) {
        super(owner, createRange(owner));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return owner.getContainingMember();
    }

    private static TextRange createRange(@NotNull ConcordionMember owner) {
        return owner.getName() != null
                ? new TextRange(0, owner.getName().length())
                : new TextRange(0, owner.getTextLength());
    }
}
