package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionStart;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.search.ProjectScope.getAllScope;
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
                getAllScope(project),
                JavaPsiFacade.getInstance(project),
                runnerClass
        );
    }

    @Nullable
    public PsiMember resolveReference(@NotNull PsiElement methodOrField) {
        return resolveReferenceInternal(1, methodOrField);
    }

    @NotNull
    public PsiMember[] resolveMembers(@NotNull PsiElement identifier) {
        PsiClass psiClass = resolveMemberOwnerClass(1, identifier.getParent());
        if (psiClass != null) {
            return findMembers(psiClass);
        }
        return PsiMember.EMPTY_ARRAY;
    }

    @Nullable
    private PsiClass resolveMemberOwnerClass (int stackDepth, @NotNull PsiElement methodOrField) {
        if (stackDepth > MAX_DEPTH_TO_RESOLVE) {
            return null;
        }

        if (methodOrField.getParent() instanceof ConcordionOgnlExpressionStart) {
            return runnerClass;
        } else {
            PsiMember psiMember = resolveReferenceInternal(stackDepth + 1, parentExpressionMethodOrField(methodOrField));
            String qualifiedName = qualifiedClassNameFromMember(psiMember);
            if (qualifiedName != null) {
                return findClass(qualifiedName);
            }
            return null;
        }
    }

    @NotNull
    private PsiElement parentExpressionMethodOrField(@NotNull PsiElement methodOrField) {
        //TODO fix for not ognl expressions
        return methodOrField.getParent().getParent().getFirstChild();
    }

    @Nullable
    private PsiMember resolveReferenceInternal(int stackDepth, @NotNull PsiElement methodOrField) {

        PsiClass ownerClass = resolveMemberOwnerClass(stackDepth, methodOrField);
        if (ownerClass != null) {
            return findMemberFromClassByExpression(ownerClass, methodOrField);
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
    private PsiMember findMemberFromClassByExpression(@NotNull PsiClass owner, @Nullable PsiElement methodOrField) {
        if (methodOrField instanceof ConcordionField) {
            return findField(owner, (ConcordionField) methodOrField);
        } else if (methodOrField instanceof ConcordionMethod) {
            return findMethod(owner, (ConcordionMethod) methodOrField);
        } else {
            return null;
        }
    }

    @Nullable
    private PsiMethod findMethod(@NotNull PsiClass clazz, @NotNull ConcordionMethod method) {
        String name = method.getMethodName();
        int paramsCount = method.getMethodParametersCount();
        return stream(clazz.getAllMethods())
                .filter(m -> m.getName().equals(name) && m.getParameterList().getParametersCount() == paramsCount)
                .findFirst().orElse(null);
    }

    @Nullable
    private PsiField findField(@NotNull PsiClass clazz, @NotNull ConcordionField field) {
        String name = field.getFieldName();
        return stream(clazz.getAllFields())
                .filter(f -> f.getName().equals(name))
                .findFirst().orElse(null);
    }

    @NotNull
    private PsiMember[] findMembers(@NotNull PsiClass clazz) {
        PsiField[] allFields = clazz.getAllFields();
        PsiMethod[] allMethods = clazz.getAllMethods();

        PsiMember[] allMembers = new PsiMember[allFields.length + allMethods.length];

        System.arraycopy(allFields, 0, allMembers, 0, allFields.length);
        System.arraycopy(allMethods, 0, allMembers, allFields.length, allMethods.length);

        return allMembers;
    }
}
