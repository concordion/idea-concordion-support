package com.gman.idea.plugin.concordion;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;

public final class ConcordionPatterns extends PlatformPatterns {

    private ConcordionPatterns() {
    }

    public static ConcordionElementPattern.Capture<PsiElement> concordionElement() {
        return new ConcordionElementPattern.Capture<>(PsiElement.class);
    }

    public static <T extends PsiElement> ConcordionElementPattern.Capture<T> concordionElement(Class<T> aClass) {
        return new ConcordionElementPattern.Capture<>(aClass);
    }
}
