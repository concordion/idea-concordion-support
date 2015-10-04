package com.gman.idea.plugin.concordion.lang.psi;

import com.gman.idea.plugin.concordion.ConcordionNavigationService;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionInjectionUtils.*;

public abstract class AbstractConcordionMember extends AbstractConcordionPsiElement implements ConcordionMember {

    //TODO find a good way to cache (no outdated date, ok with renaming) using SmartPsiElementPointer?
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
            PsiFile htmlRunner = getTopLevelFile(this);
            return ConcordionNavigationService.getInstance(getProject()).correspondingJavaRunner(htmlRunner);
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
