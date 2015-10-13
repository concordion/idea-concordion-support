package org.concordion.plugin.idea;

import org.concordion.plugin.idea.lang.psi.ConcordionPsiElement;

public final class ConcordionExpressionPatterns {

    private ConcordionExpressionPatterns() {
    }

    public static ConcordionExpressionElementPattern.Capture<ConcordionPsiElement> concordionExpressionElement() {
        return new ConcordionExpressionElementPattern.Capture<>(ConcordionPsiElement.class);
    }

    public static <T extends ConcordionPsiElement> ConcordionExpressionElementPattern.Capture<T> concordionExpressionElement(Class<T> aClass) {
        return new ConcordionExpressionElementPattern.Capture<>(aClass);
    }
}
