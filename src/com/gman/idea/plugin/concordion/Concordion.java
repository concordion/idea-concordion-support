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

@Deprecated
public final class Concordion {

    private static final String JUNIT_RUN_WITH_ANNOTATION = RunWith.class.getName();
    private static final String BASIC_CONCORDION_RUNNER = ConcordionRunner.class.getName();


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
