package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.Concordion;
import com.gman.idea.plugin.concordion.PsiUtils;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionGutterRenderer.rendererForRunnerClass;

public class ConcordionJavaRunnerAnnotator implements Annotator {

    private static final String CONCORDION_CONFIGURED_MESSAGE = "In concordion runner class";
    private static final String MISSING_HTML_SPEC_MESSAGE = "Missing html spec";
    private static final String HTML_SPEC_IS_NOT_ANNOTATED_MESSAGE = "Missing concordion schema in html spec";

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof PsiAnnotation)) {
            return;
        }
        PsiAnnotation annotation = (PsiAnnotation) element;
        if (!Concordion.JUNIT_RUN_WITH_ANNOTATION.equals(annotation.getQualifiedName())
                || !runWithAnnotationUsesConcordionRunner(annotation)) {
            return;
        }

        PsiClass runnerClass = PsiUtils.findParent(annotation, PsiClass.class);

        PsiFile htmlSpec = correspondingHtmlSpec(runnerClass);
        if (htmlSpec == null) {
            Annotation errorAnnotation = holder.createErrorAnnotation(element, MISSING_HTML_SPEC_MESSAGE);
            errorAnnotation.setTooltip(MISSING_HTML_SPEC_MESSAGE);
            //TODO provide quick fix to the user
        } else if (!isConcordionHtmlSpec(htmlSpec)) {
            Annotation errorAnnotation = holder.createErrorAnnotation(element, HTML_SPEC_IS_NOT_ANNOTATED_MESSAGE);
            errorAnnotation.setTooltip(HTML_SPEC_IS_NOT_ANNOTATED_MESSAGE);
            //TODO provide quick fix to the user
        } else {
            Annotation infoAnnotation = holder.createInfoAnnotation(element.getTextRange(), CONCORDION_CONFIGURED_MESSAGE);
            infoAnnotation.setTooltip(CONCORDION_CONFIGURED_MESSAGE);
            infoAnnotation.setGutterIconRenderer(rendererForRunnerClass(runnerClass, htmlSpec));
        }
    }
}
