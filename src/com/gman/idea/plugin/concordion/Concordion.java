package com.gman.idea.plugin.concordion;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.xml.XmlTag;
import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public final class Concordion {

    private static final String CONCORDION_NAMESPACE = "http://www.concordion.org/2007/concordion";

    public static boolean isConcordionHtmlSpec(@NotNull PsiFile psiFile) {
        return concordionSchemaPrefixOf(psiFile) != null;
    }

    public static boolean isConcordionNamespace(@Nullable String namespace) {
        return CONCORDION_NAMESPACE.equalsIgnoreCase(namespace);
    }

    @Nullable
    public static String concordionSchemaPrefixOf(@NotNull PsiFile psiFile) {
        if (!HtmlFileType.INSTANCE.equals(psiFile.getFileType())) {
            return null;
        }
        XmlTag rootTag = ((HtmlFileImpl) psiFile).getRootTag();
        if (rootTag == null) {
            return null;
        }
        for (Map.Entry<String, String> declaration : rootTag.getLocalNamespaceDeclarations().entrySet()) {
            if (CONCORDION_NAMESPACE.equalsIgnoreCase(declaration.getValue())) {
                return declaration.getKey();
            }
        }
        return null;
    }

    private static final String JUNIT_RUN_WITH_ANNOTATION = RunWith.class.getName();
    private static final String BASIC_CONCORDION_RUNNER = ConcordionRunner.class.getName();
    private static final String USING_FULL_OGNL = FullOGNL.class.getName();

    public static boolean isUsingFullOgnl(@NotNull PsiClass runnerClass) {
        PsiModifierList modifierList = runnerClass.getModifierList();
        return modifierList != null && modifierList.findAnnotation(USING_FULL_OGNL) != null;
    }

    public static boolean isClassAnnotatedWithConcordionRunner(@NotNull PsiClass runnerClass) {
        PsiAnnotation runner = findJunitRunWithAnnotation(runnerClass);
        return runner != null && isRunWithAnnotationUsesConcordionRunner(runner);
    }

    public static PsiAnnotation findJunitRunWithAnnotation(@NotNull PsiClass runnerClass) {
        PsiModifierList modifierList = runnerClass.getModifierList();
        return modifierList != null ? modifierList.findAnnotation(JUNIT_RUN_WITH_ANNOTATION) : null;
    }

    public static boolean isJunitRunWithAnnotation(@NotNull PsiAnnotation runWithAnnotation) {
        return JUNIT_RUN_WITH_ANNOTATION.equals(runWithAnnotation.getQualifiedName());
    }

    public static boolean isRunWithAnnotationUsesConcordionRunner(@NotNull PsiAnnotation runWithAnnotation) {
        PsiJavaCodeReferenceElement runnerReference = findChildOfType(runWithAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class);
        return runnerReference != null && BASIC_CONCORDION_RUNNER.equals(runnerReference.getQualifiedName());
    }

    private Concordion() {
    }
}
