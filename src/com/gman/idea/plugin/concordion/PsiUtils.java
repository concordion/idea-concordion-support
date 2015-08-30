package com.gman.idea.plugin.concordion;

import com.intellij.psi.PsiElement;

public class PsiUtils {

    public static <T> T findParent(PsiElement element, Class<T> parentType) {
        PsiElement current = element;
        while (!parentType.isAssignableFrom(current.getClass())) {
            current = current.getParent();
        }
        return (T) current;
    }
}
