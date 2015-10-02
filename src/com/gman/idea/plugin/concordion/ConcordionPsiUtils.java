package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionNext;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionOgnlExpressionStart;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionPsiElement;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static com.intellij.psi.PsiModifier.STATIC;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singleton;

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
                //PsiType.NULL means resolved, but can be dynamically typed to Integer/Double/String
                return PsiType.NULL;
            }
            return null;
        }
    }


    public static final String ITERABLE = java.lang.Iterable.class.getCanonicalName();

    @Nullable
    public static PsiType listParameterType(@NotNull PsiType listPsiType) {
        return stream(listPsiType.getSuperTypes())
                .filter(st -> st.getCanonicalText().startsWith(ITERABLE))
                .map(st -> ((PsiClassType) st).getParameters()[0])
                .findFirst().orElse(null);
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

    @NotNull
    public static <T> Set<T> setOf(@NotNull T... elements) {
        //should I use google guava just for this?
        if (elements.length == 1) {
            return singleton(elements[0]);
        }
        return new HashSet<>(asList(elements));
    }
}
