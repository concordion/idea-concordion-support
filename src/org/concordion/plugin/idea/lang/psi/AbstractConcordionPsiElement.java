package org.concordion.plugin.idea.lang.psi;

import org.concordion.plugin.idea.lang.ConcordionElementFactory;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public abstract class AbstractConcordionPsiElement extends ASTWrapperPsiElement implements ConcordionPsiElement {

    //TODO find a good way to cache (no outdated, ok with renaming)
    protected PsiType type;

    public AbstractConcordionPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        ASTNode identifierNode = getNode().findChildByType(ConcordionTypes.IDENTIFIER);
        return identifierNode != null ? identifierNode.getText() : null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        ASTNode identifier = getNode().findChildByType(ConcordionTypes.IDENTIFIER);
        if (identifier != null) {
            getNode().replaceChild(identifier, ConcordionElementFactory.createIdentifier(getProject(), name).getNode());
        }
        return this;
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }

    @Override
    public PsiType getType() {
        return determineType();
    }

    @Nullable
    protected abstract PsiType determineType();

    @Nullable
    protected ConcordionPsiElement parentConcordionExpression() {
        return parentConcordionExpressionOf(this);
    }
}
