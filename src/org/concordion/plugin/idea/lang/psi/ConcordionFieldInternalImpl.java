package org.concordion.plugin.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

import static java.util.Collections.emptyList;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public abstract class ConcordionFieldInternalImpl extends AbstractConcordionMember implements ConcordionFieldInternal {

    public ConcordionFieldInternalImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isResolvable() {
        return getContainingMember() != null || isKeyInMap();
    }

    @Override
    public boolean isKeyInMap() {
        if (getContainingMember() != null) {
            return false;
        }
        PsiClass psiClass = getContainingClass();
        if (psiClass == null) {
            return false;
        }

        PsiType containingType = findType(psiClass.getQualifiedName(), getProject());

        return isMap(containingType, getProject());
    }

    @Nullable
    @Override
    protected PsiMember determineContainingMember() {
        PsiClass containingClass = getContainingClass();
        if (containingClass == null) {
            return null;
        }
        String name = getName();
        if (name == null) {
            return null;
        }
        return firstNotNull(
                () -> findMethodInClass(containingClass, getterFor(name), emptyList()),
                () -> findFieldInClass(containingClass, name)
        );
    }

    @Nullable
    @Override
    protected PsiType containingType() {
        PsiMember containingMember = getContainingMember();
        if (containingMember instanceof PsiField) {
            return ((PsiField) containingMember).getType();
        } else if (containingMember instanceof PsiMethod) {
            return ((PsiMethod) containingMember).getReturnType();
        } else if (isKeyInMap()) {
            return mapValueParameterType(findType(getContainingClass().getQualifiedName(), getProject()));
        }
        return null;
    }
}
