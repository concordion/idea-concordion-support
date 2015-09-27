package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionNext;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionStart;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionPsiElement;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

import static java.util.Arrays.stream;

public final class ConcordionPsiUtils {

    private ConcordionPsiUtils() {
    }

    @Nullable
    public static PsiType typeOfExpression(@NotNull ConcordionOgnlExpressionStart start) {
        if (start.getOgnlExpressionNext() != null) {
            return typeOfChain(start.getOgnlExpressionNext());
        } else {
            ConcordionPsiElement typedElement = firstNotNull(start.getMethod(), start.getField(), start.getVariable());
            if (typedElement != null) {
                return typedElement.getType();
            }
            if (start.getLiteral() != null) {
                return PsiType.NULL;
            }
            return null;
        }
    }

    @Nullable
    private static PsiType typeOfChain(@NotNull ConcordionOgnlExpressionNext next) {
        Iterator<ConcordionOgnlExpressionNext> following = next.getOgnlExpressionNextList().iterator();
        if (following.hasNext()) {
            return typeOfChain(following.next());
        } else {
            ConcordionPsiElement typedElement = firstNotNull(next.getMethod(), next.getField());
            if (typedElement != null) {
                return typedElement.getType();
            }
            return null;
        }
    }

    @Nullable
    private static ConcordionPsiElement firstNotNull(@NotNull ConcordionPsiElement... elements) {
        return stream(elements).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
