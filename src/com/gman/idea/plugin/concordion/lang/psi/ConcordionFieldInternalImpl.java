package com.gman.idea.plugin.concordion.lang.psi;

import com.gman.idea.plugin.concordion.ConcordionMemberRestrictions;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.search.GlobalSearchScope.allScope;
import static java.util.Arrays.stream;

public abstract class ConcordionFieldInternalImpl extends AbstractConcordionPsiElement implements ConcordionFieldInternal {

    private static final String MAP_TYPE = java.util.Map.class.getName();

    public ConcordionFieldInternalImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isResolvable() {
        return determineContainingMember() != null || isKeyInMap();
    }

    @Override
    public boolean isKeyInMap() {
        if (determineContainingMember() != null) {
            return false;
        }
        PsiClass psiClass = determineContainingClass();
        if (psiClass == null) {
            return false;
        }
        Project project = getProject();
        GlobalSearchScope resolveScope = allScope(project);

        PsiType mapType = PsiType.getTypeByName(MAP_TYPE, project, resolveScope);
        PsiType containingType = PsiType.getTypeByName(psiClass.getQualifiedName(), project, resolveScope);

        return mapType.isAssignableFrom(containingType);
    }

    @Nullable
    @Override
    protected PsiField determineContainingMember() {
        PsiClass containingClass = determineContainingClass();
        if (containingClass == null) {
            return null;
        }
        String name = getName();
        return stream(containingClass.getAllFields())
                .filter(f -> f.getName().equals(name))
                .filter(ConcordionMemberRestrictions::concordionVisibleField)
                .findFirst().orElse(null);
    }

    @Nullable
    @Override
    protected PsiType determineType() {
        PsiField containingMember = determineContainingMember();
        return containingMember != null ? containingMember.getType() : null;
    }
}
