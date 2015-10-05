package com.gman.idea.plugin.concordion.lang.psi;

import com.gman.idea.plugin.concordion.ConcordionNavigationService;
import com.gman.idea.plugin.concordion.ConcordionPsiUtils;
import com.gman.idea.plugin.concordion.PsiElementCached;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.ConcordionInjectionUtils.*;

public abstract class AbstractConcordionMember extends AbstractConcordionPsiElement implements ConcordionMember {

    protected PsiElementCached<PsiClass> containingClass = new PsiElementCached<>(PsiClass::getQualifiedName);
    protected PsiElementCached<PsiMember> containingMember = new PsiElementCached<>(ConcordionPsiUtils::memberIdentity);

    public AbstractConcordionMember(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiClass getContainingClass() {
        return containingClass.getOrCompute(this::determineContainingClass);
    }

    @Override
    public PsiMember getContainingMember() {
        return containingMember.getOrCompute(this::determineContainingMember);
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
