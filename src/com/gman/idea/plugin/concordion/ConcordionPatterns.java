package com.gman.idea.plugin.concordion;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;

public class ConcordionPatterns extends PlatformPatterns {

    public static ConcordionElementPattern<PsiElement> concordionElement() {
        return new ConcordionElementPattern<>(PsiElement.class);
    }

    public static <T extends PsiElement> ConcordionElementPattern<T> concordionElement(Class<T> aClass) {
        return new ConcordionElementPattern<>(aClass);
    }
}
