package com.gman.idea.plugin.concordion;


import com.intellij.codeInsight.completion.JavaLookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.psi.PsiModifier.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;

public class ClassMemberInformation {

    @NotNull private final List<PsiField> fields;
    @NotNull private final List<PsiMethod> methods;

    public static final ClassMemberInformation EMPTY = new ClassMemberInformation(emptyList(), emptyList());

    public static ClassMemberInformation fromPsiClass(@NotNull PsiClass psiClass) {
        return new ClassMemberInformation(
                asList(psiClass.getAllFields()),
                asList(psiClass.getAllMethods())
        );
    }

    private ClassMemberInformation(@NotNull List<PsiField> fields, @NotNull List<PsiMethod> methods) {
        this.fields = fields;
        this.methods = methods;
    }

    public Iterable<LookupElement> createAutoCompleteInformation() {
        List<LookupElement> lookups = new ArrayList<>();

        fields.stream()
                .filter(ClassMemberInformation::concordionVisibleFields)
                .map(ClassMemberInformation::toFieldLookup)
                .collect(toCollection(() -> lookups));

        methods.stream()
                .filter(ClassMemberInformation::concordionVisibleMethods)
                .map(ClassMemberInformation::toMethodLookup)
                .collect(toCollection(() -> lookups));

        return lookups;
    }

    private static LookupElementBuilder toFieldLookup(PsiField psiField) {
        return JavaLookupElementBuilder.forField(psiField).withTypeText(psiField.getType().getPresentableText());
    }

    private static LookupElementBuilder toMethodLookup(PsiMethod psiMethod) {
        return JavaLookupElementBuilder.forMethod(psiMethod, PsiSubstitutor.UNKNOWN);
    }

    private static boolean concordionVisibleFields(PsiField psiField) {
        return psiField.getModifierList().hasModifierProperty(PUBLIC)
                && !psiField.getModifierList().hasModifierProperty(STATIC);
    }

    private static boolean concordionVisibleMethods(PsiMethod psiMethod) {
        return psiMethod.getModifierList().hasModifierProperty(PUBLIC);//Yes, static methods are accepted, fields are not
    }

}
