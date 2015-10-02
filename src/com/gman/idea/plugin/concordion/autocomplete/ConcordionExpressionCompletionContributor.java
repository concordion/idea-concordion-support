package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMember;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes;
import com.intellij.codeInsight.completion.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ClassMemberInformation.fromPsiClass;
import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ConcordionExpressionCompletionContributor extends CompletionContributor {

    public ConcordionExpressionCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.IDENTIFIER)
                        .withParent(ConcordionMember.class)
                        .withLanguage(ConcordionLanguage.INSTANCE),
                new ConcordionExpressionProvider()
        );
    }

    private static final class ConcordionExpressionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            ConcordionMember concordionMember = (ConcordionMember) parameters.getPosition().getParent();

            result.addAllElements(
                    fromPsiClass(concordionMember.getContainingClass()).createAutoCompleteInformation()
            );
        }

    }
}
