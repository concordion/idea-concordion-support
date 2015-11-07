package org.concordion.plugin.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public abstract class ConcordionMethodInternalImpl extends AbstractConcordionMember implements ConcordionMethodInternal {

    public ConcordionMethodInternalImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isResolvable() {
        return getContainingMember() != null;
    }

    @Nullable
    @Override
    protected PsiMethod determineContainingMember() {
        PsiClass containingClass = getContainingClass();
        if (containingClass == null) {
            return null;
        }
        List<ConcordionOgnlExpressionStart> arguments = findNotNullChildByClass(ConcordionArguments.class).getOgnlExpressionStartList();
        return findMethodInClass(containingClass, getName(), typeOfExpressions(arguments));
    }

    @Nullable
    @Override
    protected PsiType determineType() {
        PsiMethod containingMember = (PsiMethod) getContainingMember();
        return containingMember != null ? containingMember.getReturnType() : null;
    }

    @Override
    public int getParametersCount() {
        return findNotNullChildByClass(ConcordionArguments.class).getOgnlExpressionStartList().size();
    }
}
