package org.concordion.plugin.idea.lang.psi;

import org.concordion.plugin.idea.variables.ConcordionVariableUsage;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.variables.ConcordionVariableUsageSearcher.findDeclaration;

public abstract class ConcordionVariableInternalImpl extends AbstractConcordionPsiElement implements ConcordionVariableInternal {

    public ConcordionVariableInternalImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    protected PsiType containingType() {
        ConcordionVariableUsage declaration = findDeclaration(this);
        return declaration != null ? declaration.determineType() : null;
    }

    @Override
    public boolean isResolvable() {
        return findDeclaration(this) != null;
    }
}
