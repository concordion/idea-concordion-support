package org.concordion.plugin.idea.inspection;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionInspectionVisitor<T> extends PsiElementVisitor {

    @NotNull private final ElementPattern<T> problemPattern;
    @NotNull private final ProblemsHolder problemsHolder;
    @NotNull private final String problemDescription;
    @NotNull private final LocalQuickFix[] quickFices;

    public ConcordionInspectionVisitor(
            @NotNull ElementPattern<T> problemPattern,
            @NotNull ProblemsHolder problemsHolder,
            @NotNull String problemDescription,
            @Nullable LocalQuickFix quickFix
    ) {
        this.problemPattern = problemPattern;
        this.problemsHolder = problemsHolder;
        this.problemDescription = problemDescription;
        this.quickFices = quickFix != null ? new LocalQuickFix[]{quickFix} : new LocalQuickFix[]{};
    }

    @Override
    public void visitElement(PsiElement element) {
        super.visitElement(element);

        if (problematic(element)) {
            problemsHolder.registerProblem(element, problemDescription, quickFices);
        }
    }

    protected boolean problematic(PsiElement element) {
        return problemPattern.accepts(element);
    }
}
