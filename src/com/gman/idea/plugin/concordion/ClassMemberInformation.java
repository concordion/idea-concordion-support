package com.gman.idea.plugin.concordion;

import com.intellij.codeInsight.completion.JavaLookupElementBuilder;
import com.intellij.codeInsight.completion.util.MethodParenthesesHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

public class ClassMemberInformation {

    @NotNull private final List<PsiField> fields;
    @NotNull private final List<PsiMethod> methods;

    public static final ClassMemberInformation EMPTY = new ClassMemberInformation(emptyList(), emptyList());


    public static ClassMemberInformation fromPsiClass(@Nullable PsiClass psiClass) {
        if (psiClass == null) {
            return EMPTY;
        }
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

        filterOverriddenMembers(fields).stream()
                .filter(ConcordionPsiUtils::concordionVisibleField)
                .map(ClassMemberInformation::toFieldLookup)
                .collect(toCollection(() -> lookups));

        filterOverriddenMembers(methods).stream()
                .filter(ConcordionPsiUtils::concordionVisibleMethod)
                .map(ClassMemberInformation::toMethodLookup)
                .collect(toCollection(() -> lookups));

        return lookups;
    }

    @NotNull
    private <T extends PsiMember> Collection<T> filterOverriddenMembers(@NotNull Collection<T> members) {
        return members.stream().collect(toMap(
                ClassMemberInformation::distinctKey,
                Function.identity(),
                (u, v) -> u,
                HashMap::new
        )).values();
    }

    @Nullable
    private static String distinctKey(@NotNull PsiMember member) {
        ItemPresentation presentation = member.getPresentation();
        return presentation != null ? presentation.getPresentableText() : "";
    }

    @NotNull
    private static LookupElementBuilder toFieldLookup(@NotNull PsiField psiField) {
        return JavaLookupElementBuilder
                .forField(psiField)
                .withTypeText(psiField.getType().getPresentableText());
    }

    @NotNull
    private static LookupElementBuilder toMethodLookup(@NotNull PsiMethod psiMethod) {
        return JavaLookupElementBuilder
                .forMethod(psiMethod, PsiSubstitutor.UNKNOWN)
                .withInsertHandler(new MethodParenthesesHandler(psiMethod, false));
    }
}
