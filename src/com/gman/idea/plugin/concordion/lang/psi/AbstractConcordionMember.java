package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.Concordion.correspondingJavaRunner;
import static com.gman.idea.plugin.concordion.Concordion.unpackSpecFromLanguageInjection;

public abstract class AbstractConcordionMember extends AbstractConcordionPsiElement implements ConcordionMember {

    //TODO find a good way to cache (no outdated date, ok with renaming)
    protected PsiClass containingClass;
    protected PsiMember containingMember;

    public AbstractConcordionMember(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiClass getContainingClass() {
        return determineContainingClass();
    }

    @Override
    public PsiMember getContainingMember() {
        return determineContainingMember();
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
}
