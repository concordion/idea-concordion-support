package com.gman.idea.plugin.concordion.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ConcordionInspectionVisitor<T> extends PsiElementVisitor {

    @NotNull private final ElementPattern<T> problemPattern;
    @NotNull private final ProblemsHolder problemsHolder;
    @NotNull private final String problemDescription;

    public ConcordionInspectionVisitor(
            @NotNull ElementPattern<T> problemPattern,
            @NotNull ProblemsHolder problemsHolder,
            @NotNull String problemDescription
    ) {
        this.problemPattern = problemPattern;
        this.problemsHolder = problemsHolder;
        this.problemDescription = problemDescription;
    }

    @Override
    public void visitElement(PsiElement element) {
        super.visitElement(element);

        if (problemPattern.accepts(element)) {
            problemsHolder.registerProblem(element, problemDescription);
        }
    }
}
