package com.gman.idea.plugin.concordion.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ConcordionPatternConditions.MATCHING_ROLE_ATTRIBUTE;
import static com.gman.idea.plugin.concordion.ConcordionPatternConditions.MATCH_STRATEGY_ATTRIBUTE;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionNonExpressionCompletionContributor extends CompletionContributor {

    public ConcordionNonExpressionCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement().withParent(XmlAttributeValue.class).with(MATCH_STRATEGY_ATTRIBUTE),
                new ConcordionMatchStrategyProvider()
        );
        extend(
                CompletionType.BASIC,
                psiElement().withParent(XmlAttributeValue.class).with(MATCHING_ROLE_ATTRIBUTE),
                new ConcordionMatchingRoleProvider()
        );
    }

    private static final class ConcordionMatchStrategyProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(fromStrings("Default", "BestMatch", "KeyMatch"));
            //TODO contribute by classes extending RowsMatchStrategy
        }
    }

    private static final class ConcordionMatchingRoleProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(fromStrings("key"));
        }
    }

    private static Iterable<LookupElement> fromStrings(String... strings) {
        return stream(strings).map(LookupElementBuilder::create).collect(toList());
    }
}
