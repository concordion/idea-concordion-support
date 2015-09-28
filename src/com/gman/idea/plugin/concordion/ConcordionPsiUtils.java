package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionNext;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionStart;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionPsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static com.intellij.psi.PsiModifier.STATIC;
import static java.util.Arrays.stream;

public final class ConcordionPsiUtils {

    private ConcordionPsiUtils() {
    }

    @Nullable
    public static PsiType typeOfExpression(@NotNull ConcordionOgnlExpressionStart start) {
        if (start.getOgnlExpressionNext() != null) {
            return typeOfChain(start.getOgnlExpressionNext());
        } else {
            ConcordionPsiElement typedElement = firstNotNullIfPresent(start.getMethod(), start.getField(), start.getVariable());
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
            ConcordionPsiElement typedElement = firstNotNullIfPresent(next.getMethod(), next.getField());
            if (typedElement != null) {
                return typedElement.getType();
            }
            return null;
        }
    }

    @Nullable
    public static PsiMethod findMethodInClass(PsiClass containingClass, @Nullable String name, int paramsCount) {
        return stream(containingClass.getAllMethods())
                .filter(m -> m.getName().equals(name) && m.getParameterList().getParametersCount() == paramsCount)
                .filter(ConcordionPsiUtils::concordionVisibleMethod)
                .findFirst().orElse(null);
    }

    @Nullable
    public static PsiField findFieldInClass(@NotNull PsiClass containingClass, @Nullable String name) {
        return stream(containingClass.getAllFields())
                .filter(f -> f.getName().equals(name))
                .filter(ConcordionPsiUtils::concordionVisibleField)
                .findFirst().orElse(null);
    }

    public static boolean concordionVisibleField(@NotNull PsiField psiField) {
        return psiField.getModifierList().hasModifierProperty(PUBLIC)
                && !psiField.getModifierList().hasModifierProperty(STATIC);
    }

    public static boolean concordionVisibleMethod(@NotNull PsiMethod psiMethod) {
        return psiMethod.getModifierList().hasModifierProperty(PUBLIC)
                && !psiMethod.isConstructor();//Yes, static methods are accepted, static fields are not
    }

    @Nullable
    public static <T> T firstNotNullIfPresent(@NotNull T... elements) {
        return stream(elements).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
