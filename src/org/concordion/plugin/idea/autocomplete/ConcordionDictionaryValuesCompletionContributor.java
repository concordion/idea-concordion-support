package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionMember;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.jetbrains.annotations.NotNull;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionContextKeys.CONCORDION_COMMAND;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ConcordionDictionaryValuesCompletionContributor extends CompletionContributor {

    public ConcordionDictionaryValuesCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().andOr(
                        concordionElement(ConcordionTypes.IDENTIFIER).withParent(ConcordionMember.class),
                        concordionElement(ConcordionTypes.DICTIONARY)
                ).withCommand(ConcordionCommand::dictionary),
                new ConcordionDictionaryValuesCompletionProvider()
        );
    }

    private static final class ConcordionDictionaryValuesCompletionProvider extends CompletionProvider<CompletionParameters> {

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(
                    dictionaryCompletionsForCommand(context.get(CONCORDION_COMMAND))
            );
        }

        private Iterable<LookupElement> dictionaryCompletionsForCommand(ConcordionCommand command) {
            return command.dictionaryValues().stream()
                    .map(v -> LookupElementBuilder.create(v).withIcon(ConcordionIcons.ICON))
                    .collect(toList());
        }
    }
}
