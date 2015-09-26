package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.action.quickfix.CreateMethodFromConcordionUsage;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class UnresolvedMethodAnnotator extends UnresolvedConcordionAnnotator<ConcordionMethod> {

    public UnresolvedMethodAnnotator() {
        super(ConcordionMethod.class);
    }

    @Override
    protected void reportUnresolved(
            @NotNull ConcordionMethod concordionPsiElement,
            @NotNull AnnotationHolder holder,
            @NotNull PsiFile htmlSpec,
            @NotNull PsiClass javaRunner
    ) {
        holder
                .createErrorAnnotation(concordionPsiElement.getNode(), "Method not found")
                .registerFix(new CreateMethodFromConcordionUsage(javaRunner, concordionPsiElement));
    }
}
