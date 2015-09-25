package com.gman.idea.plugin.concordion;

import com.intellij.codeInsight.completion.JavaLookupElementBuilder;
import com.intellij.codeInsight.completion.util.MethodParenthesesHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;

public class ClassMemberInformation {

    @NotNull private final List<PsiField> fields;
    @NotNull private final List<PsiMethod> methods;

    public static final ClassMemberInformation EMPTY = new ClassMemberInformation(emptyList(), emptyList());


    public static ClassMemberInformation fromPsiClass(@Nullable PsiClass psiClass) {
        if (psiClass == null) {
            return EMPTY;
        }
//        .getPresentation().getPresentableText()
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
                .filter(ConcordionMemberRestrictions::concordionVisibleField)
                .map(ClassMemberInformation::toFieldLookup)
                .collect(toCollection(() -> lookups));

        methods.stream()
                .filter(ConcordionMemberRestrictions::concordionVisibleMethod)
                .map(ClassMemberInformation::toMethodLookup)
                .collect(toCollection(() -> lookups));

        return lookups;
    }

    private static LookupElementBuilder toFieldLookup(PsiField psiField) {
        return JavaLookupElementBuilder
                .forField(psiField)
                .withTypeText(psiField.getType().getPresentableText());
    }

    private static LookupElementBuilder toMethodLookup(PsiMethod psiMethod) {
        return JavaLookupElementBuilder
                .forMethod(psiMethod, PsiSubstitutor.UNKNOWN)
                .withInsertHandler(new MethodParenthesesHandler(psiMethod, false));
    }
}
