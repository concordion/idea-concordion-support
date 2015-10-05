package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.ConcordionPsiUtils.findMethodInClass;

public abstract class ConcordionMethodInternalImpl extends AbstractConcordionMember implements ConcordionMethodInternal {

    public ConcordionMethodInternalImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isResolvable() {
        return getContainingMember() != null;
    }

    @Nullable
    @Override
    protected PsiMethod determineContainingMember() {
        PsiClass containingClass = getContainingClass();
        if (containingClass == null) {
            return null;
        }
        return findMethodInClass(containingClass, getName(), getParametersCount());
    }

    @Nullable
    @Override
    protected PsiType determineType() {
        PsiMethod containingMember = (PsiMethod) getContainingMember();
        return containingMember != null ? containingMember.getReturnType() : null;
    }

    @Override
    public int getParametersCount() {
        return getNode()
                .findChildByType(ConcordionTypes.ARGUMENTS)
                .getChildren(TokenSet.create(ConcordionTypes.OGNL_EXPRESSION_START)).length;
    }
}
