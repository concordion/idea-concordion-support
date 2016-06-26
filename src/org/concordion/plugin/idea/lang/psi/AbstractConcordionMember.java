package org.concordion.plugin.idea.lang.psi;

import com.intellij.psi.*;
import org.concordion.plugin.idea.*;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiUtil.resolveClassInType;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;

public abstract class AbstractConcordionMember extends AbstractConcordionPsiElement implements ConcordionMember {

    public AbstractConcordionMember(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiClass getContainingClass() {
        return determineContainingClass();
    }

    @Nullable
    @Override
    public PsiMember getContainingMember() {
        return determineContainingMember();
    }

    @Nullable
    private PsiClass determineContainingClass() {
        if (getParent() instanceof ConcordionOgnlExpressionStart) {
            return ConcordionNavigationService.getInstance(getProject()).correspondingTestFixture(getTopLevelFile(this));
        } else {
            ConcordionPsiElement parent = getConcordionParent();
            if (parent == null) {
                return null;
            }
            return resolveClassInType(parent.getType());
        }
    }

    @Nullable
    protected abstract PsiMember determineContainingMember();
}
