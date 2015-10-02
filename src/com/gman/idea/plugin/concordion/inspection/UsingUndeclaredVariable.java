package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionExpressionElementPattern;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariable;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ConcordionExpressionPatterns.concordionElement;

public class UsingUndeclaredVariable extends LocalInspectionTool {

    private static final ConcordionExpressionElementPattern.Capture<ConcordionVariable> UNDECLARED_VARIABLE =
            concordionElement(ConcordionVariable.class).withResolved(false);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);

                if (UNDECLARED_VARIABLE.accepts(element)) {
                    holder.registerProblem(element, "Using undeclared variable");
                }
            }
        };
    }
}
