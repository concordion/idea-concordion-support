package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.settings.ConcordionSettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionCommands.*;
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

        private ConcordionCommandsCaseType caseType = ConcordionCommandsCaseType.BOTH;

        public ConcordionEmbeddedCommandsCompletionProvider() {
            ConcordionSettings.getInstance().addListener(this);
        }

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(forCommands(embeddedCommands(caseType)));
        }

        @Override
        public void settingsChanged(@NotNull ConcordionSettingsState newSettings) {
            caseType = newSettings.getCommandsCaseType();
        }
    }

    @NotNull
    private static Iterable<LookupElement> forCommands(Collection<String> commands) {
        return commands.stream()
                .map(c -> LookupElementBuilder.create(c + '=').withIcon(ConcordionIcons.ICON))
                .collect(toList());
    }
}
