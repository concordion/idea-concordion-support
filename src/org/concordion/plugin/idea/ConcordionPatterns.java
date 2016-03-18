package org.concordion.plugin.idea;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public final class ConcordionPatterns extends PlatformPatterns {

    private ConcordionPatterns() {
    }

    public static ConcordionElementPattern.Capture<PsiElement> concordionElement() {
        return new ConcordionElementPattern.Capture<>(PsiElement.class);
    }

    public static ConcordionElementPattern.Capture<PsiElement> concordionElement(IElementType type) {
        return new ConcordionElementPattern.Capture<>(PsiElement.class).withElementType(type);
    }

    public static <T extends PsiElement> ConcordionElementPattern.Capture<T> concordionElement(Class<T> aClass) {
        return new ConcordionElementPattern.Capture<>(aClass);
    }
}
