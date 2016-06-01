package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScStableCodeReferenceElement;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScArgumentExprList;

import java.util.List;
import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.Namespaces.CONCORDION_EXTENSIONS;

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
        PsiAnnotation extensionsAnnotation = findAnnotationInClassHierarchy(testFixture, CONCORDION_EXTENSIONS_ANNOTATION);
        if (extensionsAnnotation == null) {
            return ImmutableSet.of();
        }
        ScArgumentExprList annotationArguments = findChildOfType(extensionsAnnotation, ScArgumentExprList.class);
        return findChildrenOfType(annotationArguments, ScStableCodeReferenceElement.class).stream()
                .map(ScStableCodeReferenceElement::getCanonicalText)
                .collect(toSet());
    }

    @Nullable
    @Override
    public String extensionNamespace(@NotNull PsiClass testFixture) {
        PsiAnnotation options = findAnnotationInClassHierarchy(testFixture, CONCORDION_OPTIONS_ANNOTATION);
        if (options == null) {
            return null;
        }


        ScArgumentExprList annotationArguments = findChildOfType(options, ScArgumentExprList.class);
        List<String> literals = findChildrenOfType(annotationArguments, PsiLiteral.class).stream()
                .map(PsiLiteral::getValue)
                .map(Object::toString)
                .collect(toList());

        int namespaceIndex = literals.indexOf(CONCORDION_EXTENSIONS.namespace);
        int prefixIndex = namespaceIndex - 1;
        return prefixIndex >= 0 && prefixIndex < literals.size() ? literals.get(prefixIndex) : null;
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
