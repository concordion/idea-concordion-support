package org.concordion.plugin.idea.lang.psi;

import com.intellij.psi.*;
import org.concordion.plugin.idea.*;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;

public abstract class AbstractConcordionMember extends AbstractConcordionPsiElement implements ConcordionMember {

    public AbstractConcordionMember(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiClass getContainingClass() {
        PsiClassType classType = getContainingClassType();
        return classType != null ? classType.resolve() : null;
    }

    @Nullable
    @Override
    public PsiClassType getContainingClassType() {
        return determineContainingClassType();
    }

    @Nullable
    @Override
    public PsiMember getContainingMember() {
        return determineContainingMember();
    }

    @Nullable
    private PsiClassType determineContainingClassType() {
        if (getParent() instanceof ConcordionOgnlExpressionStart) {
            PsiClass fixture = ConcordionNavigationService.getInstance(getProject()).correspondingTestFixture(getTopLevelFile(this));
            if (fixture == null) {
                return null;
            }
            return JavaPsiFacade.getInstance(getProject()).getElementFactory().createType(fixture);
        } else {
            ConcordionPsiElement parent = getConcordionParent();
            if (parent == null) {
                return null;
            }
            PsiType type = parent.getType();
            if (!(type instanceof PsiClassType)) {
                return null;
            }
            return (PsiClassType) type;
        }
    }

    @Nullable
    protected abstract PsiMember determineContainingMember();

    @Nullable
    protected PsiType resolveGenericType(@Nullable PsiType original) {
        PsiClassType containingClassType = getContainingClassType();
        if (containingClassType == null || original == null) {
            return null;
        }
        return containingClassType.resolveGenerics().getSubstitutor().substitute(original);
    }
}
