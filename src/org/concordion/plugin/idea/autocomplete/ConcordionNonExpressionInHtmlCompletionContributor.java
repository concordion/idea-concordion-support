package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.specifications.ConcordionHtmlSpecification;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.concordion.plugin.idea.ConcordionCommand.commands;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;
import static java.util.stream.Collectors.toList;

public class ConcordionNonExpressionInHtmlCompletionContributor extends CompletionContributor {

    public ConcordionNonExpressionInHtmlCompletionContributor() {

        commands()
                .filter(command -> command.fitsSpecType(ConcordionHtmlSpecification.INSTANCE))
                .filter(ConcordionCommand::nonExpression)
                .forEach(
                        command -> extend(
                                CompletionType.BASIC,
                                defaultInjectionPattern().withCommandText(command.text()),
                                fixedValues(command.nonExpressionValues())
                        )
                );
    }

    @NotNull
    private static ConcordionElementPattern.Capture<PsiElement> defaultInjectionPattern() {
        return concordionElement()
                .withConfiguredSpecOfType(ConcordionHtmlSpecification.INSTANCE)
                .withFoundTestFixture()
                .withParent(XmlAttributeValue.class)
                .withConcordionXmlAttribute();
    }

    @NotNull
    private static ConcordionFixedAttributeValuesProvider fixedValues(@NotNull List<String> values) {
        return new ConcordionFixedAttributeValuesProvider(
                values.stream().map(LookupElementBuilder::create).collect(toList())
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
