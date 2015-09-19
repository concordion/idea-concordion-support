package com.gman.idea.plugin.concordion.lang.psi;

import com.gman.idea.plugin.concordion.ConcordionMemberRestrictions;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;

import static java.util.Arrays.stream;

public abstract class ConcordionMethodInternalImpl extends AbstractConcordionPsiElement implements ConcordionMethodInternal {

    public ConcordionMethodInternalImpl(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    protected PsiMethod determineContainingMember() {
        PsiClass containingClass = determineContainingClass();
        if (containingClass == null) {
            return null;
        }
        String name = getName();
        int paramsCount = getParametersCount();
        return stream(containingClass.getAllMethods())
                .filter(m -> m.getName().equals(name) && m.getParameterList().getParametersCount() == paramsCount)
                .filter(ConcordionMemberRestrictions::concordionVisibleMethod)
                .findFirst().orElse(null);
    }

    @Nullable
    @Override
    protected PsiType determineType() {
        PsiMethod containingMember = determineContainingMember();
        return containingMember != null ? containingMember.getReturnType() : null;
    }

    @Override
    public int getParametersCount() {
        return getNode()
                .findChildByType(ConcordionTypes.ARGUMENTS)
                .getChildren(TokenSet.create(ConcordionTypes.OGNL_EXPRESSION_START)).length;
    }
}
