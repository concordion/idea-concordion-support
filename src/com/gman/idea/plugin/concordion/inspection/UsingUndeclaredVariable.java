package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionExpressionElementPattern;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariable;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ConcordionExpressionPatterns.*;

public class UsingUndeclaredVariable extends LocalInspectionTool {

    private static final ConcordionExpressionElementPattern.Capture<ConcordionVariable> UNDECLARED_VARIABLE =
            concordionExpressionElement(ConcordionVariable.class).withResolved(false);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConcordionInspectionVisitor<>(
                UNDECLARED_VARIABLE,
                holder,
                "Using undeclared variable"
        );
    }
}
