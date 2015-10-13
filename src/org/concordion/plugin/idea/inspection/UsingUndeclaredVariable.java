package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.ConcordionExpressionElementPattern;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionExpressionPatterns.*;

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
