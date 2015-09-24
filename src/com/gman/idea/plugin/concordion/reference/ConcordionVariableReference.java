package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariable;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.ConcordionVariableInformation.forVariable;

public class ConcordionVariableReference extends AbstractConcordionReference<ConcordionVariable> {

    public ConcordionVariableReference(@NotNull ConcordionVariable owner) {
        super(owner);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return forVariable(owner).findDeclaration();
    }
}
