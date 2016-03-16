package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.lang.ConcordionFile;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionCommands.EMBEDDED_COMMANDS;
import static org.concordion.plugin.idea.ConcordionElementPattern.PARENT_OF_THE_PARENT_OF_THE_PARENT;

public class ConcordionEmbeddedCommandsCompletionContributor extends CompletionContributor {

    public ConcordionEmbeddedCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.COMMAND),
                new ConcordionEmbeddedCommandsCompletionProvider()
        );
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.IDENTIFIER).withSuperParent(PARENT_OF_THE_PARENT_OF_THE_PARENT, ConcordionFile.class),
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
