package org.concordion.plugin.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static java.lang.Character.toUpperCase;

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
        return firstNotNullIfPresent(
                findMethodInClass(containingClass, correspondingGetterName(name), 0),
                findFieldInClass(containingClass, name)
        );
    }

    @Nullable
    @Override
    protected PsiType determineType() {
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

    private String correspondingGetterName(@NotNull String name) {
        return  "get" + toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
