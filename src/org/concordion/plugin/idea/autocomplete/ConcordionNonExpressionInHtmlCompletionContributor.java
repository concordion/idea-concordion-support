package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionNonExpressionInHtmlCompletionContributor extends CompletionContributor {

    public ConcordionNonExpressionInHtmlCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand("matchStrategy", "match-strategy"),
                fixedValues("Default", "BestMatch", "KeyMatch")
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand("matchingRole", "matching-Role"),
                fixedValues("key")
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand("status"),
                fixedValues("Unimplemented", "ExpectedToFail", "ExpectedToPass")
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttributeValue.class).withConcordionCommand("screenshot"),
                fixedValues("linked")
        );
    }

    @NotNull
    private static ConcordionFixedAttributeValuesProvider fixedValues(@NotNull String... values) {
        return new ConcordionFixedAttributeValuesProvider(
                stream(values).map(LookupElementBuilder::create).collect(toList())
        );
    }

    private static final class ConcordionFixedAttributeValuesProvider extends CompletionProvider<CompletionParameters> {

        @NotNull
        private final Iterable<LookupElement> values;

        public ConcordionFixedAttributeValuesProvider(@NotNull Iterable<LookupElement> values) {
            this.values = values;
        }

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(values);
        }
    }
}
