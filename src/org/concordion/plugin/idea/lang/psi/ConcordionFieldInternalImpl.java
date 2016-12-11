package org.concordion.plugin.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

import static java.util.Collections.emptyList;
import static org.concordion.plugin.idea.ConcordionCommand.EXAMPLE;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public abstract class ConcordionFieldInternalImpl extends AbstractConcordionMember implements ConcordionFieldInternal {

    public ConcordionFieldInternalImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isResolvable() {
        return getContainingMember() != null || isKeyInMap() || isExampleName();
    }

    @Override
    public boolean isKeyInMap() {
        if (getContainingMember() != null) {
            return false;
        }
        PsiClassType type = getContainingClassType();
        return type != null && isMap(type, getProject());
    }

    @Override
    public boolean isExampleName() {
        return commandOf(this).orElse(EXAMPLE).equals(EXAMPLE);
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
            return resolveGenericType(((PsiField) containingMember).getType());
        } else if (containingMember instanceof PsiMethod) {
            return resolveGenericType(((PsiMethod) containingMember).getReturnType());
        } else if (isKeyInMap()) {
            return mapValueParameterType(getContainingClassType(), getProject());
        }
        return null;
    }
}
