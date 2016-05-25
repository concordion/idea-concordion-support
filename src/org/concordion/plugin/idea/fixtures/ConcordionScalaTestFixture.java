package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScStableCodeReferenceElement;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScArgumentExprList;

import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class ConcordionScalaTestFixture implements ConcordionTestFixture {

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return ImmutableSet.of("scala");
    }

    @Override
    public boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        PsiAnnotation runWithAnnotation = findAnnotationInClassHierarchy(testFixture, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }

        ScArgumentExprList annotationArguments = findChildOfType(runWithAnnotation, ScArgumentExprList.class);
        ScStableCodeReferenceElement runner = findChildOfType(annotationArguments, ScStableCodeReferenceElement.class);

        return runner != null && CONCORDION_RUNNER.equals(runner.getCanonicalText());
    }

    @Override
    public boolean fullOgnlEnabled(@NotNull PsiClass testFixture) {
        return false;
    }

    @NotNull
    @Override
    public Set<String> configuredExtensions(@NotNull PsiClass testFixture) {
        return ImmutableSet.of();
    }

    @Nullable
    @Override
    public String extensionNamespace(@NotNull PsiClass testFixture) {
        return null;
    }

    @NotNull
    @Override
    public JVMElementFactory elementFactory(@NotNull PsiClass testFixture) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    private static PsiAnnotation findAnnotationInClassHierarchy(@NotNull PsiClass psiClass, @NotNull String qualifiedName) {
        for (PsiClass current = psiClass; current != null; current = current.getSuperClass()) {
            PsiModifierList modifiers = current.getModifierList();
            if (modifiers == null) {
                continue;
            }
            PsiAnnotation annotation = modifiers.findAnnotation(qualifiedName);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}
