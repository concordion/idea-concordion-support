package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.Namespaces.CONCORDION_EXTENSIONS;

public class ConcordionTestFixtureUtil {

    private static final String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";
    private static final String CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";
    private static final String CONCORDION_FULL_OGNL_ANNOTATION = "org.concordion.api.FullOGNL";
    private static final String CONCORDION_EXTENSIONS_ANNOTATION = "org.concordion.api.extension.Extensions";
    private static final String CONCORDION_OPTIONS_ANNOTATION = "org.concordion.api.option.ConcordionOptions";

    public static boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        PsiAnnotation runWithAnnotation = findAnnotationInClassHierarchy(testFixture, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }
        PsiJavaCodeReferenceElement runner = findChildOfType(runWithAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class);
        return runner != null && CONCORDION_RUNNER.equals(runner.getQualifiedName());
    }

    public static boolean fullOgnlEnabled(@NotNull PsiClass testFixture) {
        return findAnnotationInClassHierarchy(testFixture, CONCORDION_FULL_OGNL_ANNOTATION) != null;
    }

    @NotNull
    public static Set<String> configuredExtensions(@NotNull PsiClass testFixture) {
        PsiAnnotation extensionsAnnotation = findAnnotationInClassHierarchy(testFixture, CONCORDION_EXTENSIONS_ANNOTATION);
        if (extensionsAnnotation == null) {
            return ImmutableSet.of();
        }
        return findChildrenOfType(extensionsAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class).stream()
                .map(PsiJavaCodeReferenceElement::getQualifiedName)
                .collect(toSet());
    }

    @Nullable
    public static String extensionNamespace(@NotNull PsiClass testFixture) {
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
}
