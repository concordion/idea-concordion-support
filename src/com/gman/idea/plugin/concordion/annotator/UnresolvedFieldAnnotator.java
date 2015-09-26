package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.action.quickfix.CreateFieldFromConcordionUsage;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class UnresolvedFieldAnnotator extends UnresolvedConcordionAnnotator<ConcordionField> {

    public UnresolvedFieldAnnotator() {
        super(ConcordionField.class);
    }

    @Override
    protected void reportUnresolved(
            @NotNull ConcordionField concordionPsiElement,
            @NotNull AnnotationHolder holder,
            @NotNull PsiFile htmlSpec,
            @NotNull PsiClass javaRunner
    ) {
        holder
                .createErrorAnnotation(concordionPsiElement.getNode(), "Field not found")
                .registerFix(new CreateFieldFromConcordionUsage(javaRunner, concordionPsiElement));
    }
}
