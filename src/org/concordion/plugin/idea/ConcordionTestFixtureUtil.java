package org.concordion.plugin.idea;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiModifierList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ConcordionTestFixtureUtil {

    private static final String CONCORDION_FULL_OGNL = "org.concordion.api.FullOGNL";
    private static final String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";
    private static final String CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";
    private static final String CONCORDION_EXTENSIONS_ANNOTATION = "org.concordion.api.extension.Extensions";

    public static boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        PsiAnnotation runWithAnnotation = findAnnotationInClassHierarchy(testFixture, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }
        PsiJavaCodeReferenceElement runner = findChildOfType(runWithAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class);
        return runner != null && CONCORDION_RUNNER.equals(runner.getQualifiedName());
    }

    public static boolean fullOgnlEnabled(@NotNull PsiClass testFixture) {
        return findAnnotationInClassHierarchy(testFixture, CONCORDION_FULL_OGNL) != null;
    }

    @NotNull
    public static Collection<String> configuredExtensions(@NotNull PsiClass testFixture) {
        PsiAnnotation extensionsAnnotation = findAnnotationInClassHierarchy(testFixture, CONCORDION_EXTENSIONS_ANNOTATION);
        if (extensionsAnnotation == null) {
            return emptyList();
        }
        return findChildrenOfType(extensionsAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class).stream()
                .map(PsiJavaCodeReferenceElement::getQualifiedName)
                .collect(toList());
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
