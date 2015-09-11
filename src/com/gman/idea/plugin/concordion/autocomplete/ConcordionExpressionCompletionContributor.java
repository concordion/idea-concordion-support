package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.OgnlChainResolver;
import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionExpressionCompletionContributor extends CompletionContributor {

    public ConcordionExpressionCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.IDENTIFIER)
                        .withParent(or(psiElement(ConcordionTypes.FIELD), psiElement(ConcordionTypes.METHOD)))
                        .withLanguage(ConcordionLanguage.INSTANCE),
                new ConcordionExpressionProvider()
        );
    }

    private static final class ConcordionExpressionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            PsiFile htmlSpec = unpackSpecFromLanguageInjection(parameters.getOriginalFile());
            PsiClass javaRunner = correspondingJavaRunner(htmlSpec);

            if (htmlSpec == null || javaRunner == null) {
                return;
            }

            result.addAllElements(
                    OgnlChainResolver
                            .create(javaRunner)
                            .resolveMembers(parameters.getPosition())
                            .createAutoCompleteInformation()
            );
        }

    }
}
