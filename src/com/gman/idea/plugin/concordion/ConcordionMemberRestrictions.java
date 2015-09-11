package com.gman.idea.plugin.concordion;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static com.intellij.psi.PsiModifier.STATIC;

public final class ConcordionMemberRestrictions {

    private ConcordionMemberRestrictions() {
    }

    public static boolean concordionVisibleField(PsiField psiField) {
        return psiField.getModifierList().hasModifierProperty(PUBLIC)
                && !psiField.getModifierList().hasModifierProperty(STATIC);
    }

    public static boolean concordionVisibleMethod(PsiMethod psiMethod) {
        return psiMethod.getModifierList().hasModifierProperty(PUBLIC)
                && !psiMethod.isConstructor();//Yes, static methods are accepted, static fields are not
    }
}
