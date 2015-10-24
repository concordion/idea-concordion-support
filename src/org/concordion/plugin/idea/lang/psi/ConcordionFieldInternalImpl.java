package org.concordion.plugin.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static com.intellij.psi.search.GlobalSearchScope.allScope;
import static java.lang.Character.toUpperCase;

public abstract class ConcordionFieldInternalImpl extends AbstractConcordionMember implements ConcordionFieldInternal {

    private static final String MAP_TYPE = java.util.Map.class.getName();

    public ConcordionFieldInternalImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isResolvable() {
        return getContainingMember() != null || isKeyInMap() || isArrayLength();
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
        Project project = getProject();
        GlobalSearchScope resolveScope = allScope(project);

        PsiType mapType = PsiType.getTypeByName(MAP_TYPE, project, resolveScope);
        PsiType containingType = PsiType.getTypeByName(psiClass.getQualifiedName(), project, resolveScope);

        return mapType.isAssignableFrom(containingType);
    }

    @Override
    public boolean isArrayLength() {
        ConcordionPsiElement concordionParent = getConcordionParent();
        return concordionParent != null
                && concordionParent.isArray()
                && "length".equals(getName());
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
        }
        //TODO return PsiType.NULL if parent is a map
        return null;
    }

    private String correspondingGetterName(@NotNull String name) {
        return  "get" + toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
