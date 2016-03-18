package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionCommands.EMBEDDED_COMMANDS;
import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionSpecType.MD;

public class ConcordionEmbeddedCommandsCompletionContributor extends CompletionContributor {

    public ConcordionEmbeddedCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement(ConcordionTypes.COMMAND).withSpecOfType(MD),
                new ConcordionEmbeddedCommandsCompletionProvider()
        );
        extend(
                CompletionType.BASIC,
                concordionElement(ConcordionTypes.IDENTIFIER).withSpecOfType(MD).with(new PatternCondition<PsiElement>("startOfInjection") {
                    @Override
                    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                        return element.getTextOffset() == 0;
                    }
                }),
                new ConcordionEmbeddedCommandsCompletionProvider()
        );
    }

    private static final class ConcordionEmbeddedCommandsCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(forCommands(EMBEDDED_COMMANDS));
        }
    }

    @NotNull
    private static Iterable<LookupElement> forCommands(Collection<String> commands) {
        return commands.stream()
                .map(c -> LookupElementBuilder.create(c + '=').withIcon(ConcordionIcons.ICON))
                .collect(toList());
    }
}
