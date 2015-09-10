package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.OgnlChainResolver;
import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariable;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionExpressionCompletionContributor extends CompletionContributor {

    public ConcordionExpressionCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.IDENTIFIER)
                        .andNot(psiElement(ConcordionTypes.IDENTIFIER).withParent(ConcordionVariable.class))
                        .withLanguage(ConcordionLanguage.INSTANCE),
                new ConcordionExpressionProvider()
        );
    }

    private static final class ConcordionExpressionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            PsiFile htmlSpec = unpackSpecFromLanguageInjection(parameters.getOriginalFile());
            PsiClass psiClass = correspondingJavaRunner(htmlSpec);

            if (htmlSpec == null || psiClass == null) {
                return;
            }

            PsiMember[] psiMembers = OgnlChainResolver.create(psiClass).resolveMembers(parameters.getPosition());

            result.addAllElements(fromMembers(psiMembers));
        }

        private Iterable<LookupElement> fromMembers(PsiMember[] psiMembers) {
            return stream(psiMembers)
                    .map(PsiMember::getName)
                    .map(LookupElementBuilder::create)
                    .collect(toList());
        }
    }
}
