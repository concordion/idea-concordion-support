package org.concordion.plugin.idea.autocomplete;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.settings.ConcordionSettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionCommand.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.*;
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
                concordionElement(ConcordionTypes.IDENTIFIER).withSpecOfType(MD).withStartOfInjection(),
                new ConcordionEmbeddedCommandsCompletionProvider()
        );
    }

    private static final class ConcordionEmbeddedCommandsCompletionProvider extends CompletionProvider<CompletionParameters> implements ConcordionSettingsListener {

        private List<LookupElement> commands = ImmutableList.of();

        public ConcordionEmbeddedCommandsCompletionProvider() {
            ConcordionSettings.getInstance().addListener(this);
        }

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(commands);
        }

        @Override
        public void settingsChanged(@NotNull ConcordionSettingsState newSettings) {
            commands = commands()
                    .filter(command -> command.fitsForCaseType(newSettings.getCommandsCaseType()))
                    .filter(command -> command.fitsSpecType(MD))
                    .filter(ConcordionCommand::buildIn)
                    .map(command -> command.prefixedText("c"))
                    .map(c -> LookupElementBuilder.create(c + '=').withIcon(ConcordionIcons.ICON))
                    .collect(toList());
        }
    }
}
