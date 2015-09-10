package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class UnresolvedPropertyAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof ConcordionField && element.getReferences().length == 0) {
            reportUnresolvedField((ConcordionField) element, holder);
        } else if (element instanceof ConcordionMethod && element.getReferences().length == 0) {
            reportUnresolvedMethod((ConcordionMethod) element, holder);
        }
    }

    private void reportUnresolvedField(@NotNull ConcordionField field, @NotNull AnnotationHolder holder) {
        holder.createErrorAnnotation(field.getNode(), "Field not found");
    }

    private void reportUnresolvedMethod(@NotNull ConcordionMethod method, @NotNull AnnotationHolder holder) {
        holder.createErrorAnnotation(method.getNode(), "Method not found");
    }
}
