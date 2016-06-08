package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.JavaLookupElementBuilder;
import com.intellij.codeInsight.completion.util.MethodParenthesesHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.*;
import org.concordion.plugin.idea.ConcordionPsiUtils;
import org.concordion.plugin.idea.variables.ConcordionVariableUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.nullToEmpty;
import static org.concordion.plugin.idea.variables.ConcordionVariableUsageSearcher.findAllDeclarationsFrom;

public class CompleteInformation {

    private static final CompleteInformation EMPTY = new CompleteInformation(emptyList());

    @NotNull
    public static CompleteInformation fromMembersOf(@Nullable PsiClass psiClass) {
        if (psiClass == null) {
            return EMPTY;
        }

        List<LookupElement> lookups = new ArrayList<>();

        stream(psiClass.getAllFields())
                .filter(onlyUniqueSignature())
                .filter(ConcordionPsiUtils::concordionVisibleField)
                .map(CompleteInformation::toFieldLookup)
                .collect(toCollection(() -> lookups));

        stream(psiClass.getAllMethods())
                .filter(onlyUniqueSignature())
                .filter(ConcordionPsiUtils::concordionVisibleMethod)
                .map(CompleteInformation::toMethodLookup)
                .collect(toCollection(() -> lookups));

        return new CompleteInformation(lookups);
    }

    @NotNull
    public static CompleteInformation fromVariablesOf(@NotNull PsiFile injection) {

        return new CompleteInformation(
                findAllDeclarationsFrom(injection).stream()
                        .map(CompleteInformation::toVariableLookup)
                        .collect(toList())
        );
    }

    @NotNull
    private final Iterable<LookupElement> completions;

    private CompleteInformation(@NotNull Iterable<LookupElement> completions) {
        this.completions = completions;
    }

    @NotNull
    public Iterable<LookupElement> createAutoCompleteInformation() {
        return completions;
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

    @NotNull
    private static LookupElementBuilder toVariableLookup(@NotNull ConcordionVariableUsage usage) {
        return LookupElementBuilder
                .create(nullToEmpty(usage.getName()))
                .withIcon(AllIcons.Nodes.Variable)
                .withTypeText(variableTypeText(usage.determineType()));
    }

    @NotNull
    private static String variableTypeText(@Nullable PsiType psiType) {
        return psiType == null ? "?" : psiType.getPresentableText();
    }

    @NotNull
    private static <T extends PsiMember> Predicate<T> onlyUniqueSignature() {
        Set<String> seenSignatures = new HashSet<>();
        return  member -> seenSignatures.add(signature(member));
    }

    @Nullable
    private static String signature(@NotNull PsiMember member) {
        ItemPresentation presentation = member.getPresentation();
        return presentation != null ? presentation.getPresentableText() : "";
    }
}
