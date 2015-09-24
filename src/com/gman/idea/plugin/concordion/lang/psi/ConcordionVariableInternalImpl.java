package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.ConcordionVariableInformation.forVariable;

public abstract class ConcordionVariableInternalImpl extends AbstractConcordionPsiElement implements ConcordionVariableInternal {

    public ConcordionVariableInternalImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    protected PsiType determineType() {
        return forVariable(this).determineType();
    }

    @Override
    public boolean isResolvable() {
        return forVariable(this).isDeclared();
    }
}
