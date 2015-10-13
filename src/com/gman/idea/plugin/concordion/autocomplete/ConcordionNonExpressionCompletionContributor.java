package com.gman.idea.plugin.concordion.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ConcordionPatterns.concordionElement;
import static com.gman.idea.plugin.concordion.ConcordionPsiUtils.setOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionNonExpressionCompletionContributor extends CompletionContributor {

    public ConcordionNonExpressionCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand(setOf("matchStrategy", "match-strategy")),
                new ConcordionMatchStrategyProvider()
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand(setOf("matchingRole", "matching-Role")),
                new ConcordionMatchingRoleProvider()
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand(setOf("status")),
                new ConcordionStatusProvider()
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

    private static final class ConcordionStatusProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(fromStrings("Unimplemented", "ExpectedToFail", "ExpectedToPass"));
        }
    }

    private static Iterable<LookupElement> fromStrings(String... strings) {
        return stream(strings).map(LookupElementBuilder::create).collect(toList());
    }
}
