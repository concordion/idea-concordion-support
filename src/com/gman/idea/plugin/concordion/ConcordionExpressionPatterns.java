package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionPsiElement;

public final class ConcordionExpressionPatterns {

    private ConcordionExpressionPatterns() {
    }

    public static ConcordionExpressionElementPattern.Capture<ConcordionPsiElement> concordionElement() {
        return new ConcordionExpressionElementPattern.Capture<>(ConcordionPsiElement.class);
    }

    public static <T extends ConcordionPsiElement> ConcordionExpressionElementPattern.Capture<T> concordionElement(Class<T> aClass) {
        return new ConcordionExpressionElementPattern.Capture<>(aClass);
    }
}
