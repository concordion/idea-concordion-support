package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

import java.util.List;
import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.Namespaces.CONCORDION_EXTENSIONS;

public class ConcordionGroovyTestFixture implements ConcordionTestFixture {

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return ImmutableSet.of("groovy");
    }

    @Override
    public boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        PsiAnnotation runWithAnnotation = findAnnotationInClassHierarchy(testFixture, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }
        GrReferenceExpression runner = findChildOfType(runWithAnnotation.getParameterList(), GrReferenceExpression.class);
        return CONCORDION_RUNNER.equals(qualifiedType(runner));
    }

    @Override
    public boolean fullOgnlEnabled(@NotNull PsiClass testFixture) {
        return findAnnotationInClassHierarchy(testFixture, CONCORDION_FULL_OGNL_ANNOTATION) != null;
    }

    @NotNull
    @Override
    public Set<String> configuredExtensions(@NotNull PsiClass testFixture) {
        PsiAnnotation extensionsAnnotation = findAnnotationInClassHierarchy(testFixture, CONCORDION_EXTENSIONS_ANNOTATION);
        if (extensionsAnnotation == null) {
            return ImmutableSet.of();
        }
        return findChildrenOfType(extensionsAnnotation.getParameterList(), GrReferenceExpression.class).stream()
                .map(ConcordionGroovyTestFixture::qualifiedType)
                .collect(toSet());
    }

    @Nullable
    @Override
    public String extensionNamespace(@NotNull PsiClass testFixture) {
        PsiAnnotation options = findAnnotationInClassHierarchy(testFixture, CONCORDION_OPTIONS_ANNOTATION);
        if (options == null) {
            return null;
        }

        List<String> literals = findChildrenOfType(options.getParameterList(), PsiLiteralExpression.class).stream()
                .map(PsiLiteralExpression::getValue)
                .map(Object::toString)
                .collect(toList());

        int namespaceIndex = literals.indexOf(CONCORDION_EXTENSIONS.namespace);
        int prefixIndex = namespaceIndex - 1;
        return prefixIndex >= 0 && prefixIndex < literals.size() ? literals.get(prefixIndex) : null;
    }

    @NotNull
    @Override
    public JVMElementFactory elementFactory(@NotNull PsiClass testFixture) {
        return GroovyPsiElementFactory.getInstance(testFixture.getProject());
    }

    @Nullable
    private static PsiAnnotation findAnnotationInClassHierarchy(@NotNull PsiClass psiClass, @NotNull String qualifiedName) {
        for (PsiClass current = psiClass; current != null ; current = current.getSuperClass()) {
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

    @Nullable
    private static String qualifiedType(@Nullable GrReferenceExpression expression) {
        return expression != null && expression.getQualifier() != null && expression.getQualifier().getNominalType() != null
                ? expression.getQualifier().getNominalType().getCanonicalText() : null;
    }
}
