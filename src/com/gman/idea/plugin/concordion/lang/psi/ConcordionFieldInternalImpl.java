package com.gman.idea.plugin.concordion.lang.psi;

import com.gman.idea.plugin.concordion.ConcordionMemberRestrictions;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.Nullable;

import static java.util.Arrays.stream;

public abstract class ConcordionFieldInternalImpl extends AbstractConcordionPsiElement implements ConcordionFieldInternal {

    public ConcordionFieldInternalImpl(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    protected PsiField determineContainingMember() {
        PsiClass containingClass = determineContainingClass();
        if (containingClass == null) {
            return null;
        }
        String name = getName();
        return stream(containingClass.getAllFields())
                .filter(f -> f.getName().equals(name))
                .filter(ConcordionMemberRestrictions::concordionVisibleField)
                .findFirst().orElse(null);
    }

    @Nullable
    @Override
    protected PsiType determineType() {
        PsiField containingMember = determineContainingMember();
        return containingMember != null ? containingMember.getType() : null;
    }
}
