package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.lang.psi.ConcordionIterateExpression;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.concordion.plugin.idea.lang.psi.ConcordionSetExpression;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.concordion.internal.SimpleEvaluator;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionCommand.SET;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class FullOgnlRequired extends LocalInspectionTool {

    private static final ConcordionElementPattern.Capture<ConcordionOgnlExpressionStart> COMPLEX_EVALUATION =
            concordionElement(ConcordionOgnlExpressionStart.class)
                    .withParent(PsiFile.class)
                    .withTextNotMatching(SimpleEvaluator.EVALUATION_PATTERNS);

    private static final ConcordionElementPattern.Capture<ConcordionSetExpression> COMPLEX_SET =
            concordionElement(ConcordionSetExpression.class)
                    .withTextNotMatching(SimpleEvaluator.SET_VARIABLE_PATTERNS);

    private static final ConcordionElementPattern.Capture<ConcordionOgnlExpressionStart> COMPLEX_SET_VIA_COMMAND =
            concordionElement(ConcordionOgnlExpressionStart.class)
                    .withParent(PsiFile.class)
                    .withCommandText(SET.text())
                    .withTextNotMatching(SimpleEvaluator.SET_VARIABLE_PATTERNS);

    private static final ConcordionElementPattern.Capture<ConcordionOgnlExpressionStart> COMPLEX_ITERATE =
            concordionElement(ConcordionOgnlExpressionStart.class)
                    .withParent(ConcordionIterateExpression.class)
                    .withTextNotMatching(SimpleEvaluator.EVALUATION_PATTERNS);

    private static final ConcordionElementPattern.Capture<PsiElement> TOO_COMPLEX_CONCORDION_EXPRESSION =
            concordionElement()
                    .withFoundTestFixture()
                    .withFullOgnl(false)
                    .andOr(COMPLEX_EVALUATION, COMPLEX_SET, COMPLEX_SET_VIA_COMMAND, COMPLEX_ITERATE);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConcordionInspectionVisitor<>(
                TOO_COMPLEX_CONCORDION_EXPRESSION,
                holder,
                "Too complex expression"
        );
    }
}
