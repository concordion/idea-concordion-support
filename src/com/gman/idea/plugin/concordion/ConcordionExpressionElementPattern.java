package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionPsiElement;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class ConcordionExpressionElementPattern<T extends ConcordionPsiElement, Self extends ConcordionExpressionElementPattern<T, Self>> extends ConcordionElementPattern<T, Self> {

    public ConcordionExpressionElementPattern(Class<T> aClass) {
        super(aClass);
    }

    public Self withResolved(boolean resolved) {
        return with(new PatternCondition<T>("withResolved") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                return t.isResolvable() == resolved;
            }
        });
    }

    public static class Capture<T extends ConcordionPsiElement> extends ConcordionExpressionElementPattern<T, Capture<T>> {

        protected Capture(final Class<T> aClass) {
            super(aClass);
        }
    }
}
