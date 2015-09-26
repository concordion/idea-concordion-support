package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariable;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class UnresolvedVariableAnnotator extends UnresolvedConcordionAnnotator<ConcordionVariable> {

    public UnresolvedVariableAnnotator() {
        super(ConcordionVariable.class);
    }

    @Override
    protected void reportUnresolved(
            @NotNull ConcordionVariable concordionPsiElement,
            @NotNull AnnotationHolder holder,
            @NotNull PsiFile htmlSpec,
            @NotNull PsiClass javaRunner
    ) {
        holder.createWarningAnnotation(concordionPsiElement.getNode(), "Variable is not declared in this scope. null will be used instead");
    }
}
