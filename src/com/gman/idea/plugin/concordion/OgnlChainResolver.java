package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionStart;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.search.ProjectScope.getProjectScope;
import static java.util.Arrays.stream;

public class OgnlChainResolver {

    public static final int MAX_DEPTH_TO_RESOLVE = 5;

    private GlobalSearchScope scope;
    private JavaPsiFacade javaPsiFacade;

    private PsiClass runnerClass;

    private OgnlChainResolver(GlobalSearchScope scope, JavaPsiFacade javaPsiFacade, PsiClass runnerClass) {
        this.scope = scope;
        this.javaPsiFacade = javaPsiFacade;
        this.runnerClass = runnerClass;
    }

    @NotNull
    public static OgnlChainResolver create(@NotNull PsiClass runnerClass) {
        Project project = runnerClass.getProject();

        return new OgnlChainResolver(
                getProjectScope(project),
                JavaPsiFacade.getInstance(project),
                runnerClass
        );
    }

    @Nullable
    public PsiMember resolveReference(@NotNull PsiElement element) {
        return resolveReferenceInternal(1, element);
    }

    @Nullable
    public PsiClass resolveClassForCompletion(@NotNull PsiElement element) {
        return resolveMemberOwnerClass(1, element);
    }

    @Nullable
    private PsiClass resolveMemberOwnerClass (int stackDepth, @NotNull PsiElement element) {
        if (stackDepth > MAX_DEPTH_TO_RESOLVE) {
            return null;
        }

        if (element.getParent() instanceof ConcordionOgnlExpressionStart) {
            return runnerClass;
        } else {
            PsiMember psiMember = resolveReferenceInternal(stackDepth + 1, element.getParent().getParent().getFirstChild());
            String qualifiedName = qualifiedClassNameFromMember(psiMember);
            if (qualifiedName != null) {
                return findClass(qualifiedName);
            }
            return null;
        }
    }

    @Nullable
    private PsiMember resolveReferenceInternal(int stackDepth, @NotNull PsiElement element) {

        PsiClass ownerClass = resolveMemberOwnerClass(stackDepth, element);
        if (ownerClass != null) {
            return findMemberFromClassByExpression(ownerClass, element);
        }
        return null;
    }

    @Nullable
    private PsiClass findClass(@NotNull String qualifiedName) {
        return javaPsiFacade.findClass(qualifiedName, scope);
    }

    @Nullable
    private String qualifiedClassNameFromMember(@Nullable PsiMember psiMember) {
        if (psiMember instanceof PsiField) {
            return  ((PsiField) psiMember).getType().getCanonicalText();
        } else if (psiMember instanceof PsiMethod) {
            return ((PsiMethod) psiMember).getReturnType().getCanonicalText();
        } else {
            return null;
        }
    }

    @Nullable
    private PsiMember findMemberFromClassByExpression(@NotNull PsiClass owner, @Nullable PsiElement expression) {
        if (expression instanceof ConcordionField) {
            return findField(owner, (ConcordionField) expression);
        } else if (expression instanceof ConcordionMethod) {
            return findMethod(owner, (ConcordionMethod) expression);
        } else {
            return null;
        }
    }

    @Nullable
    private static PsiMethod findMethod(@NotNull PsiClass clazz, @NotNull ConcordionMethod method) {
        String name = method.getMethodName();
        int paramsCount = method.getMethodParametersCount();
        return stream(clazz.getAllMethods())
                .filter(m -> m.getName().equals(name) && m.getParameterList().getParametersCount() == paramsCount)
                .findFirst().orElse(null);
    }

    @Nullable
    private static PsiField findField(@NotNull PsiClass clazz, @NotNull ConcordionField field) {
        String name = field.getFieldName();
        return stream(clazz.getAllFields())
                .filter(f -> f.getName().equals(name))
                .findFirst().orElse(null);
    }
}
