package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.Concordion.*;

public abstract class AbstractConcordionPsiElement extends ASTWrapperPsiElement implements ConcordionPsiElement {

    //TODO find a way to cache
    protected PsiClass containingClass;
    protected PsiMember containingMember;
    protected PsiType type;

    public AbstractConcordionPsiElement(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        ASTNode identifierNode = identifierNode();
        return identifierNode != null ? identifierNode.getPsi() : null;
    }

    @Override
    public String getName() {
        ASTNode identifierNode = identifierNode();
        return identifierNode != null ? identifierNode.getText() : null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;//TODO implement for renaming
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }

    @Override
    public PsiClass getContainingClass() {
        return determineContainingClass();
    }

    @Override
    public PsiMember getContainingMember() {
        return determineContainingMember();
    }

    @Override
    public PsiType getType() {
        return determineType();
    }

    @Nullable
    protected PsiClass determineContainingClass() {
        if (getParent() instanceof ConcordionOgnlExpressionStart) {
            PsiFile htmlRunner = unpackSpecFromLanguageInjection(getContainingFile());
            return correspondingJavaRunner(htmlRunner);
        } else {
            ConcordionPsiElement parent = parentConcordionExpression();
            if (parent == null) {
                return null;
            }
            return PsiUtil.resolveClassInType(parent.getType());
        }
    }

    @Nullable
    protected abstract PsiMember determineContainingMember();

    @Nullable
    protected abstract PsiType determineType();

    @Nullable
    private ConcordionPsiElement parentConcordionExpression() {
        return parentConcordionExpressionOf(this);
    }

    @Nullable
    private ASTNode identifierNode() {
        return getNode().findChildByType(ConcordionTypes.IDENTIFIER);
    }

    public static ConcordionPsiElement parentConcordionExpressionOf(ConcordionPsiElement current) {
        //Parent may not be present for some malformed chains
        if (current.getParent() == null || current.getParent().getParent() == null) {
            return null;
        }
        PsiElement parent = current.getParent().getParent().getFirstChild();
        if (!(parent instanceof ConcordionPsiElement)) {
            return null;
        }
        return (ConcordionPsiElement) parent;
    }
}
