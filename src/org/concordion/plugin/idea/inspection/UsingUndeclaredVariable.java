package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.patterns.ConcordionPatterns.*;

public class UsingUndeclaredVariable extends LocalInspectionTool {

    private static final ConcordionElementPattern.Capture<ConcordionVariable> UNDECLARED_VARIABLE =
            concordionElement(ConcordionVariable.class).withResolved(false);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConcordionInspectionVisitor<>(
                UNDECLARED_VARIABLE,
                holder,
                ConcordionBundle.message("concordion.inspection.using_undeclared_variable"),
                null
        );
    }
}
