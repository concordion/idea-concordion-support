package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import org.concordion.plugin.idea.action.quickfix.factories.ConcordionFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.Namespaces.CONCORDION_EXTENSIONS;

public abstract class AbstractConcordionTestFixture<T extends PsiElement> implements ConcordionTestFixture {

    @NotNull private final Set<String> extensions;
    @NotNull private final Class<T> codeReferenceType;
    @NotNull private final ConcordionFixtureMemberFactory memberFactory;

    protected AbstractConcordionTestFixture(
            @NotNull String extension,
            @NotNull Class<T> codeReferenceType,
            @NotNull ConcordionFixtureMemberFactory memberFactory
    ) {
        this.extensions = ImmutableSet.of(extension);
        this.codeReferenceType = codeReferenceType;
        this.memberFactory = memberFactory;
    }

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return extensions;
    }

    @Override
    public boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        PsiAnnotation runWithAnnotation = findAnnotationInClassHierarchy(testFixture, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }
        T runner = findChildOfType(findAnnotationParameters(runWithAnnotation), codeReferenceType);
        return runner != null && CONCORDION_RUNNER.equals(qualifiedReference(runner));
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
        return findChildrenOfType(findAnnotationParameters(extensionsAnnotation), codeReferenceType).stream()
                .map(this::qualifiedReference)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    @Nullable
    @Override
    public String extensionNamespace(@NotNull PsiClass testFixture) {
        PsiAnnotation optionsAnnotation = findAnnotationInClassHierarchy(testFixture, CONCORDION_OPTIONS_ANNOTATION);
        if (optionsAnnotation == null) {
            return null;
        }

        List<String> literals = findChildrenOfType(findAnnotationParameters(optionsAnnotation), PsiLiteral.class).stream()
                .map(PsiLiteral::getValue)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(toList());

        int namespaceIndex = literals.indexOf(CONCORDION_EXTENSIONS.namespace);
        int prefixIndex = namespaceIndex - 1;
        return prefixIndex >= 0 && prefixIndex < literals.size() ? literals.get(prefixIndex) : null;
    }

    @NotNull
    @Override
    public ConcordionFixtureMemberFactory memberFactory(@NotNull PsiClass testFixture) {
        return memberFactory;
    }

    @Nullable
    protected abstract String qualifiedReference(@NotNull T codeReference);

    @Nullable
    protected PsiElement findAnnotationParameters(@NotNull PsiAnnotation annotation) {
        return annotation.getParameterList();
    }

    @Nullable
    protected PsiAnnotation findAnnotationInClassHierarchy(@NotNull PsiClass psiClass, @NotNull String qualifiedName) {
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
